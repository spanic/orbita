package com.bmstu_bureau_1440.orders.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(@NotNull String type, @NotNull @Valid OrderPayload payload) {
}
