package com.bmstu_bureau_1440.orders.dto;

import com.bmstu_bureau_1440.orders.model.Cadence;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MonitoringPayload(
        @NotBlank String aoi,
        @NotNull Cadence cadence,
        @NotNull @Positive @Min(1) Integer durationDays) implements OrderPayload {
}
