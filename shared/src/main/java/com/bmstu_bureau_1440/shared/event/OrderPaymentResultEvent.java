package com.bmstu_bureau_1440.shared.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderPaymentResultEvent(
        UUID eventId,
        UUID orderId,
        String userId,
        BigDecimal amount,
        PaymentResultOutcome outcome,
        PaymentFailureReason failureReason,
        BigDecimal newBalance,
        Instant occurredAt) {
}
