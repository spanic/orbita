package com.bmstu_bureau_1440.shared.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaymentTopics {

    public static final String ORDER_PAYMENT_REQUESTED = "order-payment-requested";

    public static final String ORDER_PAYMENT_RESULT = "order-payment-result";

}
