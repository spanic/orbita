package com.bmstu_bureau_1440.orders.dto;

import com.bmstu_bureau_1440.orders.model.OrderTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
        @NotNull String type,
        @NotNull @Valid @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXTERNAL_PROPERTY) @JsonSubTypes({
                @JsonSubTypes.Type(value = ArchivePayload.class, name = OrderTypes.ARCHIVE),
                @JsonSubTypes.Type(value = TaskingPayload.class, name = OrderTypes.TASKING),
                @JsonSubTypes.Type(value = MonitoringPayload.class, name = OrderTypes.MONITORING)
        }) OrderPayload payload) {
}
