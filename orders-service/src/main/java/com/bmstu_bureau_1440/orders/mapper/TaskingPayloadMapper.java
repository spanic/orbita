package com.bmstu_bureau_1440.orders.mapper;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.orders.dto.TaskingPayload;
import com.bmstu_bureau_1440.orders.model.TaskingOrder;

@Component
public class TaskingPayloadMapper implements PayloadMapper<TaskingPayload> {

    @Override
    public TaskingOrder map(TaskingPayload payload) {
        return new TaskingOrder(
                payload.aoi(),
                payload.timeWindowStart(),
                payload.timeWindowEnd(),
                payload.sensorType());
    }

}
