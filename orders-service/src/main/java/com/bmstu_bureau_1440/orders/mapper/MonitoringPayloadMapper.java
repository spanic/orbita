package com.bmstu_bureau_1440.orders.mapper;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.orders.config.PricingProperties;
import com.bmstu_bureau_1440.orders.dto.MonitoringPayload;
import com.bmstu_bureau_1440.orders.model.MonitoringOrder;
import com.bmstu_bureau_1440.orders.model.Order;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MonitoringPayloadMapper implements BiFunction<String, MonitoringPayload, Order> {

    private final PricingProperties pricingProperties;

    @Override
    public Order apply(String userId, MonitoringPayload payload) {
        int intervalDays = payload.cadence().getIntervalDays();
        int observations = (payload.durationDays() + intervalDays - 1) / intervalDays;
        BigDecimal price = pricingProperties.unitPrice().multiply(BigDecimal.valueOf(observations));

        return new MonitoringOrder(userId, payload.aoi(), price, payload.cadence(), payload.durationDays());
    }

}
