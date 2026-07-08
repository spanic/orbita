package com.bmstu_bureau_1440.shared.event;

import java.time.Instant;
import java.util.UUID;

public record OrderPaymentFailedEvent(
        UUID eventId,
        UUID orderId,
        String userId,
        PaymentFailureReason reason,
        Instant occurredAt) {
}
