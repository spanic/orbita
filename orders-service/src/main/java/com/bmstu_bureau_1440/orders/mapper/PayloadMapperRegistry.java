package com.bmstu_bureau_1440.orders.mapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.orders.dto.OrderPayload;
import com.bmstu_bureau_1440.orders.model.Order;

@Component
public class PayloadMapperRegistry {

    private final Map<Class<? extends OrderPayload>, PayloadMapper<? extends OrderPayload>> mappers;

    public PayloadMapperRegistry(List<PayloadMapper<? extends OrderPayload>> mappers) {
        this.mappers = mappers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        PayloadMapperRegistry::resolvePayloadType,
                        Function.identity()));
    }

    @SuppressWarnings("unchecked")
    public Order map(OrderPayload payload) {
        PayloadMapper<? extends OrderPayload> mapper = mappers.get(payload.getClass());

        if (mapper == null) {
            throw new IllegalStateException(String.format(
                    "No PayloadMapper registered for payload type: %s",
                    payload.getClass().getSimpleName()));
        }

        return ((PayloadMapper<OrderPayload>) mapper).map(payload);
    }

    private static Class<? extends OrderPayload> resolvePayloadType(PayloadMapper<?> mapper) {
        Class<?> payloadType = ResolvableType.forClass(PayloadMapper.class, mapper.getClass())
                .getGeneric(0)
                .resolve();

        if (payloadType == null) {
            throw new IllegalStateException(String.format(
                    "Could not resolve payload type for mapper: %s - ensure it implements PayloadMapper interface properly",
                    mapper.getClass().getName()));
        }

        return payloadType.asSubclass(OrderPayload.class);
    }

}
