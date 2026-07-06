package com.bmstu_bureau_1440.orders.dto;

import java.time.Instant;

import com.bmstu_bureau_1440.orders.model.SensorType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskingPayload(
        @NotBlank String aoi,
        @NotNull Instant timeWindowStart,
        @NotNull Instant timeWindowEnd,
        @NotNull SensorType sensorType) implements OrderPayload {
}
