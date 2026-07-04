package com.bmstu_bureau_1440.orders.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArchivePayload(@NotBlank String aoi, @NotNull LocalDate captureDate) implements OrderPayload {
}
