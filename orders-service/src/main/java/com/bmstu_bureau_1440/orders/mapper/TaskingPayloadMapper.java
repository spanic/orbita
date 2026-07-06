package com.bmstu_bureau_1440.orders.mapper;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.orders.config.PricingProperties;
import com.bmstu_bureau_1440.orders.dto.TaskingPayload;
import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.model.TaskingOrder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskingPayloadMapper implements Function<TaskingPayload, Order> {

    private final PricingProperties pricingProperties;

    @Override
    public Order apply(TaskingPayload payload) {
        long windowHours = Duration.between(payload.timeWindowStart(), payload.timeWindowEnd()).toHours();
        BigDecimal price = pricingProperties.unitPrice().multiply(BigDecimal.valueOf(windowHours));

        return new TaskingOrder(
                payload.aoi(),
                price,
                payload.timeWindowStart(),
                payload.timeWindowEnd(),
                payload.sensorType());
    }

}
