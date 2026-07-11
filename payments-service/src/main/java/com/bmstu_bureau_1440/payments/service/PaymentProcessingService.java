package com.bmstu_bureau_1440.payments.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bmstu_bureau_1440.payments.model.Account;
import com.bmstu_bureau_1440.payments.model.PaymentTransaction;
import com.bmstu_bureau_1440.payments.repository.AccountRepository;
import com.bmstu_bureau_1440.payments.repository.PaymentTransactionRepository;
import com.bmstu_bureau_1440.shared.event.OrderPaymentRequestedEvent;
import com.bmstu_bureau_1440.shared.event.OrderPaymentResultEvent;
import com.bmstu_bureau_1440.shared.event.PaymentFailureReason;
import com.bmstu_bureau_1440.shared.event.PaymentResultOutcome;
import com.bmstu_bureau_1440.shared.event.PaymentTopics;
import com.bmstu_bureau_1440.shared.outbox.OutboxEventWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessingService {

    private final AccountRepository accountRepository;

    private final PaymentTransactionRepository paymentTransactionRepository;

    private final OutboxEventWriter outboxEventWriter;

    @Transactional
    public void processOrderPaymentRequest(OrderPaymentRequestedEvent event) {
        Optional<PaymentTransaction> existingPaymentTransaction = paymentTransactionRepository
                .findByOrderId(event.orderId());

        if (existingPaymentTransaction.isPresent()) {
            log.info("Duplicate OrderPaymentRequested for order {}, already processed as {}",
                    event.orderId(), existingPaymentTransaction.get().getOutcome());
            enqueueSuccess(existingPaymentTransaction.get());
            return;
        }

        Optional<Account> accountOptional = accountRepository.findByUserId(event.userId());

        if (accountOptional.isEmpty()) {
            recordAndEnqueueFailure(event, PaymentFailureReason.ACCOUNT_NOT_FOUND);
            return;
        }

        Account account = accountOptional.get();

        if (account.getBalance().compareTo(event.amount()) < 0) {
            recordAndEnqueueFailure(event, PaymentFailureReason.INSUFFICIENT_BALANCE);
            return;
        }

        account.withdraw(event.amount());

        recordAndEnqueueSuccess(event, account);
    }

    private void recordAndEnqueueSuccess(OrderPaymentRequestedEvent event, Account account) {
        PaymentTransaction transaction = paymentTransactionRepository.save(
                PaymentTransaction.paid(event.orderId(), event.userId(), event.amount(), account.getBalance()));

        enqueueSuccess(transaction);
    }

    private void enqueueSuccess(PaymentTransaction transaction) {
        outboxEventWriter.enqueue(
                transaction.getOrderId(),
                "ORDER_PAYMENT_COMPLETED",
                PaymentTopics.ORDER_PAYMENT_RESULT,
                new OrderPaymentResultEvent(
                        UUID.randomUUID(),
                        transaction.getOrderId(),
                        transaction.getUserId(),
                        transaction.getAmount(),
                        PaymentResultOutcome.COMPLETED,
                        null,
                        transaction.getBalanceAfter(),
                        Instant.now()));
    }

    private void recordAndEnqueueFailure(OrderPaymentRequestedEvent event, PaymentFailureReason reason) {
        PaymentTransaction transaction = paymentTransactionRepository.save(
                PaymentTransaction.failed(event.orderId(), event.userId(), event.amount(), reason));

        outboxEventWriter.enqueue(
                transaction.getOrderId(),
                "ORDER_PAYMENT_FAILED",
                PaymentTopics.ORDER_PAYMENT_RESULT,
                new OrderPaymentResultEvent(
                        UUID.randomUUID(),
                        transaction.getOrderId(),
                        transaction.getUserId(),
                        transaction.getAmount(),
                        PaymentResultOutcome.FAILED,
                        transaction.getFailureReason(),
                        null,
                        Instant.now()));
    }

}
