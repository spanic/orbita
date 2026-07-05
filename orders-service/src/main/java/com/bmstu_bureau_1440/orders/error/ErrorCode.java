package com.bmstu_bureau_1440.orders.error;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_PAYLOAD(Codes.INVALID_PAYLOAD, HttpStatus.BAD_REQUEST, "Invalid payload"),
    INVALID_REQUEST(Codes.INVALID_REQUEST, HttpStatus.BAD_REQUEST, "Invalid request"),
    UNKNOWN_PRODUCT_TYPE(Codes.UNKNOWN_PRODUCT_TYPE, HttpStatus.BAD_REQUEST, "Unknown product type"),
    VALIDATION_FAILED(Codes.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, "Request validation failed"),
    INTERNAL_ERROR(Codes.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");

    @Getter
    private final String code;
    @Getter
    private final HttpStatus status;
    @Getter
    private final String message;

    public static Optional<ErrorCode> byCode(String code) {
        return Arrays.stream(values()).filter(value -> value.code.equals(code)).findFirst();
    }

    public static final class Codes {

        public static final String INVALID_PAYLOAD = "INVALID_PAYLOAD";
        public static final String INVALID_REQUEST = "INVALID_REQUEST";
        public static final String UNKNOWN_PRODUCT_TYPE = "UNKNOWN_PRODUCT_TYPE";
        public static final String VALIDATION_FAILED = "VALIDATION_FAILED";
        public static final String INTERNAL_ERROR = "INTERNAL_ERROR";

        private Codes() {
        }

    }

}
