package com.bmstu_bureau_1440.payments.error;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.shared.error.ErrorCode;
import com.bmstu_bureau_1440.shared.error.ErrorCodesRegistry;

@Component
public final class PaymentsErrorCodeRegistry extends ErrorCodesRegistry {

    public static final ErrorCode VALIDATION_FAILED = new ErrorCode(
            HttpStatus.BAD_REQUEST,
            "Request validation failed",
            "VALIDATION_FAILED");

    public static final ErrorCode ACCOUNT_ALREADY_EXISTS = new ErrorCode(
            HttpStatus.CONFLICT,
            "Account already exists for this user",
            "ACCOUNT_ALREADY_EXISTS");

    public static final ErrorCode ACCOUNT_NOT_FOUND = new ErrorCode(
            HttpStatus.NOT_FOUND,
            "Account not found",
            "ACCOUNT_NOT_FOUND");

    public static final ErrorCode ACCOUNT_UPDATE_CONFLICT = new ErrorCode(
            HttpStatus.CONFLICT,
            "Account was concurrently modified, please retry",
            "ACCOUNT_UPDATE_CONFLICT");

    public static final ErrorCode INVALID_AMOUNT = new ErrorCode(
            HttpStatus.BAD_REQUEST,
            "Amount must be positive",
            "INVALID_AMOUNT");

}
