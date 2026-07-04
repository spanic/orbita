package com.bmstu_bureau_1440.orders.dto;

import java.time.LocalDateTime;

import com.bmstu_bureau_1440.orders.model.SensorType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskingPayload(
        @NotBlank String aoi,
        @NotNull LocalDateTime timeWindowStart,
        @NotNull LocalDateTime timeWindowEnd,
        @NotNull SensorType sensorType) implements OrderPayload {
}
