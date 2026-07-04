package com.bmstu_bureau_1440.orders.dto;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(@NotNull String type, @NotNull @Validated OrderPayload payload) {
}
