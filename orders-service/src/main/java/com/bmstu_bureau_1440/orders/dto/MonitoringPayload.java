package com.bmstu_bureau_1440.orders.dto;

import jakarta.validation.constraints.NotBlank;

public record MonitoringPayload(@NotBlank String aoi, @NotBlank String cadence) implements OrderPayload {
}
