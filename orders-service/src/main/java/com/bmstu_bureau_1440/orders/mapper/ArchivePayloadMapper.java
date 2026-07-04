package com.bmstu_bureau_1440.orders.mapper;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.orders.dto.ArchivePayload;
import com.bmstu_bureau_1440.orders.model.ArchiveOrder;

@Component
public class ArchivePayloadMapper implements PayloadMapper<ArchivePayload> {

    @Override
    public ArchiveOrder map(ArchivePayload payload) {
        return new ArchiveOrder(payload.aoi(), payload.captureDate());
    }

}
