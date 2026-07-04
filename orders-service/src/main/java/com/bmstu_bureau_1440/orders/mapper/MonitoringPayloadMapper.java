package com.bmstu_bureau_1440.orders.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.orders.config.PricingProperties;
import com.bmstu_bureau_1440.orders.dto.MonitoringPayload;
import com.bmstu_bureau_1440.orders.model.MonitoringOrder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MonitoringPayloadMapper implements PayloadMapper<MonitoringPayload> {

    private final PricingProperties pricingProperties;

    @Override
    public MonitoringOrder map(MonitoringPayload payload) {
        int intervalDays = payload.cadence().getIntervalDays();
        int observations = (payload.durationDays() + intervalDays - 1) / intervalDays;
        BigDecimal price = pricingProperties.unitPrice().multiply(BigDecimal.valueOf(observations));

        return new MonitoringOrder(payload.aoi(), price, payload.cadence(), payload.durationDays());
    }

}
