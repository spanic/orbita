package com.bmstu_bureau_1440.orders.dto;

import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.model.SensorType;
import com.bmstu_bureau_1440.orders.model.TaskingOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskingPayload(
        @NotBlank String aoi,
        @NotNull LocalDateTime timeWindowStart,
        @NotNull LocalDateTime timeWindowEnd,
        @NotNull SensorType sensorType) implements OrderPayload {

    @Override
    public Order toEntity() {
        return new TaskingOrder(aoi, timeWindowStart, timeWindowEnd, sensorType);
    }

}
