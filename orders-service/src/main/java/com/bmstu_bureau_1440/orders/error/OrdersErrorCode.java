package com.bmstu_bureau_1440.orders.error;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.bmstu_bureau_1440.shared.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrdersErrorCode implements ErrorCode {

    UNKNOWN_PRODUCT_TYPE(HttpStatus.BAD_REQUEST, "Unknown product type"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order not found");

    private final HttpStatus status;
    private final String message;

    private static final Map<String, OrdersErrorCode> ERROR_CODE_BY_NAME_MAP = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(Enum::name, Function.identity()));

    public static Optional<OrdersErrorCode> byCode(String code) {
        return Optional.ofNullable(ERROR_CODE_BY_NAME_MAP.get(code));
    }

}
