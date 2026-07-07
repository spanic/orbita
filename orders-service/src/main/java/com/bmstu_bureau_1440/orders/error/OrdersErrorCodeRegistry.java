package com.bmstu_bureau_1440.orders.error;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.shared.error.ErrorCode;
import com.bmstu_bureau_1440.shared.error.ErrorCodesRegistry;

@Component
public final class OrdersErrorCodeRegistry extends ErrorCodesRegistry {

    public static final ErrorCode INVALID_PAYLOAD = new ErrorCode(
            HttpStatus.BAD_REQUEST,
            "Invalid payload",
            "INVALID_PAYLOAD");

    public static final ErrorCode UNKNOWN_PRODUCT_TYPE = new ErrorCode(
            HttpStatus.BAD_REQUEST,
            "Unknown product type",
            "UNKNOWN_PRODUCT_TYPE");

    public static final ErrorCode ORDER_NOT_FOUND = new ErrorCode(
            HttpStatus.NOT_FOUND,
            "Order not found",
            "ORDER_NOT_FOUND");

}
