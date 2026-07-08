package com.bmstu_bureau_1440.payments.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bmstu_bureau_1440.shared.event.PaymentFailureReason;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "payment_transactions", uniqueConstraints = @UniqueConstraint(columnNames = "order_id"))
@EntityListeners(AuditingEntityListener.class)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NonNull
    @Column(name = "order_id", nullable = false, updatable = false)
    private UUID orderId;

    @NonNull
    @Column(nullable = false, updatable = false)
    private String userId;

    @NonNull
    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private PaymentOutcome outcome;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private PaymentFailureReason failureReason;

    @Column(updatable = false)
    private BigDecimal balanceAfter;

    private PaymentTransaction(UUID orderId, String userId, BigDecimal amount, PaymentOutcome outcome,
            PaymentFailureReason failureReason, BigDecimal balanceAfter) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.outcome = outcome;
        this.failureReason = failureReason;
        this.balanceAfter = balanceAfter;
    }

    public static PaymentTransaction paid(UUID orderId, String userId, BigDecimal amount, BigDecimal balanceAfter) {
        return new PaymentTransaction(orderId, userId, amount, PaymentOutcome.PAID, null, balanceAfter);
    }

    public static PaymentTransaction failed(UUID orderId, String userId, BigDecimal amount,
            PaymentFailureReason reason) {
        return new PaymentTransaction(orderId, userId, amount, PaymentOutcome.FAILED, reason, null);
    }

}
