package com.bmstu_bureau_1440.payments.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TopUpAccountRequest(@NotNull @Positive BigDecimal amount) {
}
