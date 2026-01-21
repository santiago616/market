package com.market.bank.service;

import com.market.bank.domain.Loan;
import com.market.bank.exception.LoanNotFoundException;
import com.market.bank.util.LoanStatus;
import com.market.bank.repository.LoanRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository repository;

    public LoanService(LoanRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(cacheNames = "loans-by-user", key = "#username")
    public Loan requestLoan(BigDecimal amount, Integer term, String username) {
        Loan loan = new Loan();
        loan.setAmount(amount);
        loan.setTermInMonths(term);
        loan.setStatus(LoanStatus.PENDING);
        loan.setUsername(username);
        return repository.save(loan);
    }

    @CacheEvict(cacheNames = "loans-by-user", allEntries = true)
    @Transactional
    public Loan decideLoan(Long loanId, boolean approved) {
        Loan loan = repository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        loan.setStatus(approved ? LoanStatus.APPROVED : LoanStatus.REJECTED);
        return loan;
    }

    @Cacheable("loans-by-user")
    public List<Loan> getLoansByUser(String username) {
        return repository.findByUsername(username);
    }
}
