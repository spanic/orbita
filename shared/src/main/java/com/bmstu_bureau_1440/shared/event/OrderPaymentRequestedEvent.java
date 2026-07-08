package com.bmstu_bureau_1440.shared.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderPaymentRequestedEvent(
        UUID eventId,
        UUID orderId,
        String userId,
        BigDecimal amount,
        Instant occurredAt) {
}
