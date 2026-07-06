package com.bmstu_bureau_1440.shared.error;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;

public class ErrorCodesRegistry {

    private static final Map<String, ErrorCode> REGISTRY = new HashMap<>();

    static {
        register("INVALID_PAYLOAD", HttpStatus.BAD_REQUEST, "Invalid payload");
        register("INVALID_REQUEST", HttpStatus.BAD_REQUEST, "Invalid request");
        register("VALIDATION_FAILED", HttpStatus.BAD_REQUEST, "Request validation failed");
        register("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    public static Optional<ErrorCode> byCode(String code) {
        return Optional.ofNullable(REGISTRY.get(code));
    }

    protected static void register(String name, HttpStatus status, String message) {
        REGISTRY.put(name, new ErrorCode(name, status, message));
    }

}
