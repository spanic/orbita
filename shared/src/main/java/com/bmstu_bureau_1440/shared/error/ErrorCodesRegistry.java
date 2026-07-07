package com.bmstu_bureau_1440.shared.error;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorCodesRegistry {

    public static final ErrorCode INVALID_REQUEST = new ErrorCode(
            HttpStatus.BAD_REQUEST,
            "Invalid request",
            "INVALID_REQUEST");

    public static final ErrorCode INTERNAL_ERROR = new ErrorCode(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred",
            "INTERNAL_ERROR");

}
