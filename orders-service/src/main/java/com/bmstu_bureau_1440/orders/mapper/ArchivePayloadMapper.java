package com.bmstu_bureau_1440.orders.mapper;

import java.util.function.BiFunction;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.orders.config.PricingProperties;
import com.bmstu_bureau_1440.orders.dto.ArchivePayload;
import com.bmstu_bureau_1440.orders.model.ArchiveOrder;
import com.bmstu_bureau_1440.orders.model.Order;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArchivePayloadMapper implements BiFunction<String, ArchivePayload, Order> {

    private final PricingProperties pricingProperties;

    @Override
    public Order apply(String userId, ArchivePayload payload) {
        return new ArchiveOrder(
                userId,
                payload.aoi(),
                pricingProperties.unitPrice(),
                payload.captureDate(),
                payload.sensorType());
    }

}
