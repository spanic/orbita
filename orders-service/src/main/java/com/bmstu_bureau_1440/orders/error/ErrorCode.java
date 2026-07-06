package com.bmstu_bureau_1440.orders.error;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_PAYLOAD(HttpStatus.BAD_REQUEST, "Invalid payload"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request"),
    UNKNOWN_PRODUCT_TYPE(HttpStatus.BAD_REQUEST, "Unknown product type"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Request validation failed"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order not found");

    private final HttpStatus status;
    private final String message;

    private static final Map<String, ErrorCode> BY_NAME =
            Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(Enum::name, Function.identity()));

    public static Optional<ErrorCode> byCode(String code) {
        return Optional.ofNullable(BY_NAME.get(code));
    }

}
