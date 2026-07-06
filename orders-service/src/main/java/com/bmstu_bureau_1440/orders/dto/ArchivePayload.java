package com.bmstu_bureau_1440.orders.dto;

import java.time.LocalDate;

import com.bmstu_bureau_1440.orders.model.SensorType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArchivePayload(
        @NotBlank String aoi,
        @NotNull LocalDate captureDate,
        @NotNull SensorType sensorType) implements OrderPayload {
}
