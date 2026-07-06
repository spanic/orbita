package com.bmstu_bureau_1440.payments.dto;

import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(@NotNull String userId) {
}
