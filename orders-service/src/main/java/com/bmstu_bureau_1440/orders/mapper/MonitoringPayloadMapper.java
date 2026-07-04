package com.bmstu_bureau_1440.orders.mapper;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.orders.dto.MonitoringPayload;
import com.bmstu_bureau_1440.orders.model.MonitoringOrder;

@Component
public class MonitoringPayloadMapper implements PayloadMapper<MonitoringPayload> {

    @Override
    public MonitoringOrder map(MonitoringPayload payload) {
        return new MonitoringOrder(payload.aoi(), payload.cadence());
    }
}
