package com.bmstu_bureau_1440.orders.dto;

import com.bmstu_bureau_1440.orders.model.ArchiveOrder;
import com.bmstu_bureau_1440.orders.model.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ArchivePayload(@NotBlank String aoi, @NotNull LocalDate captureDate) implements OrderPayload {

    @Override
    public Order toEntity() {
        return new ArchiveOrder(aoi, captureDate);
    }

}
