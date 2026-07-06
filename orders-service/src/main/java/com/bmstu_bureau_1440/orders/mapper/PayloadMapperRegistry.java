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

    private final Map<Class<? extends OrderPayload>, Function<? extends OrderPayload, Order>> mappers;

    public PayloadMapperRegistry(List<Function<? extends OrderPayload, Order>> mappers) {
        this.mappers = mappers.stream().collect(Collectors.toUnmodifiableMap(
                PayloadMapperRegistry::resolvePayloadType,
                Function.identity()));
    }

    @SuppressWarnings("unchecked")
    public Order map(OrderPayload payload) {
        Function<OrderPayload, Order> mapper = (Function<OrderPayload, Order>) mappers.get(payload.getClass());

        if (mapper == null) {
            throw new IllegalStateException(String.format(
                    "No PayloadMapper registered for payload type: %s",
                    payload.getClass().getSimpleName()));
        }

        return mapper.apply(payload);
    }

    private static Class<? extends OrderPayload> resolvePayloadType(Function<? extends OrderPayload, Order> mapper) {
        Class<?> payloadType = ResolvableType.forClass(Function.class, mapper.getClass())
                .getGeneric(0)
                .resolve();

        if (payloadType == null) {
            throw new IllegalStateException(String.format(
                    "Could not resolve payload type for mapper: %s - ensure it implements Function<PayloadType, Order> directly",
                    mapper.getClass().getName()));
        }

        return payloadType.asSubclass(OrderPayload.class);
    }

}
