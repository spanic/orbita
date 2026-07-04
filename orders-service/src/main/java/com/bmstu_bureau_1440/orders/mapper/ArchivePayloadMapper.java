package com.bmstu_bureau_1440.orders.mapper;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.orders.config.PricingProperties;
import com.bmstu_bureau_1440.orders.dto.ArchivePayload;
import com.bmstu_bureau_1440.orders.model.ArchiveOrder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArchivePayloadMapper implements PayloadMapper<ArchivePayload> {

    private final PricingProperties pricingProperties;

    @Override
    public ArchiveOrder map(ArchivePayload payload) {
        return new ArchiveOrder(payload.aoi(), pricingProperties.unitPrice(), payload.captureDate());
    }

}
