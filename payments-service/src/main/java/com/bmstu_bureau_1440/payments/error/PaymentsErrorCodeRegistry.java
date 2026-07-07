package com.bmstu_bureau_1440.payments.error;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.shared.error.ErrorCode;
import com.bmstu_bureau_1440.shared.error.ErrorCodesRegistry;

@Component
public final class PaymentsErrorCodeRegistry extends ErrorCodesRegistry {

    public static final ErrorCode ACCOUNT_ALREADY_EXISTS = new ErrorCode(
            HttpStatus.CONFLICT,
            "Account already exists for this user",
            "ACCOUNT_ALREADY_EXISTS");

}
