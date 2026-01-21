package com.market.bank.service;

import com.market.bank.domain.Loan;
import com.market.bank.repository.LoanRepository;
import com.market.bank.util.LoanStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    LoanRepository repository;

    @InjectMocks
    LoanService loanService;

    @Test
    void shouldApproveLoan() {
        Loan loan = new Loan();
        loan.setStatus(LoanStatus.PENDING);

        when(repository.findById(1L)).thenReturn(Optional.of(loan));

        Loan result = loanService.decideLoan(1L, true);

        assertEquals(LoanStatus.APPROVED, result.getStatus());
    }
}

