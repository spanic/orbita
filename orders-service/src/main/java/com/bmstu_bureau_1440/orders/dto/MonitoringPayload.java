package com.bmstu_bureau_1440.orders.dto;

import com.bmstu_bureau_1440.orders.model.MonitoringOrder;
import com.bmstu_bureau_1440.orders.model.Order;
import jakarta.validation.constraints.NotBlank;

public record MonitoringPayload(@NotBlank String aoi, @NotBlank String cadence) implements OrderPayload {

    @Override
    public Order toEntity() {
        return new MonitoringOrder(aoi, cadence);
    }

}
