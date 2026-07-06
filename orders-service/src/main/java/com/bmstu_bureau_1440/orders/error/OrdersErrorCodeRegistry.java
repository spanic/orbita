package com.bmstu_bureau_1440.orders.error;

import org.springframework.http.HttpStatus;

import com.bmstu_bureau_1440.shared.error.ErrorCodesRegistry;

public final class OrdersErrorCodeRegistry extends ErrorCodesRegistry {

    static {
        register("UNKNOWN_PRODUCT_TYPE", HttpStatus.BAD_REQUEST, "Unknown product type");
        register("ORDER_NOT_FOUND", HttpStatus.NOT_FOUND, "Order not found");
    }

}
