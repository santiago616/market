package com.market.bank.rest;

import com.market.bank.domain.Loan;
import com.market.bank.dto.LoanDecisionRequest;
import com.market.bank.dto.LoanRequest;
import com.market.bank.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> requestLoan(
            @Valid @RequestBody LoanRequest request,
            Authentication authentication) {

        Loan loan = loanService.requestLoan(
                request.amount(),
                request.termInMonths(),
                authentication.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @GetMapping
    public List<Loan> myLoans(Authentication authentication) {
        return loanService.getLoansByUser(authentication.getName());
    }

    @PostMapping("/{id}/decision")
    @PreAuthorize("hasRole('ADMIN')")
    public Loan decideLoan(
            @PathVariable Long id,
            @RequestBody LoanDecisionRequest request) {

        return loanService.decideLoan(id, request.approved());
    }
}

