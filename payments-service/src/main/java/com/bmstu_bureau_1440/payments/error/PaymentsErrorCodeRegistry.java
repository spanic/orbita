package com.bmstu_bureau_1440.payments.error;

import org.springframework.http.HttpStatus;

import com.bmstu_bureau_1440.shared.error.ErrorCodesRegistry;

public final class PaymentsErrorCodeRegistry extends ErrorCodesRegistry {

    static {
        register("ACCOUNT_ALREADY_EXISTS", HttpStatus.CONFLICT, "Account already exists for this user");
    }

}
