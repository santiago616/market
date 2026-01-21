package com.market.bank.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record LoanRequest(
        @NotNull BigDecimal amount,
        @NotNull Integer termInMonths
) {}