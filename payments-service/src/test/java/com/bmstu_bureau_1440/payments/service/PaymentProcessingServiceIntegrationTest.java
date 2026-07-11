package com.bmstu_bureau_1440.payments.service;

import static com.bmstu_bureau_1440.payments.AccountTestsFixtures.ACCOUNT_MODEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.math.BigDecimal;
import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.bmstu_bureau_1440.payments.TestContainersConfiguration;
import com.bmstu_bureau_1440.payments.model.Account;
import com.bmstu_bureau_1440.payments.model.PaymentOutcome;
import com.bmstu_bureau_1440.payments.model.PaymentTransaction;
import com.bmstu_bureau_1440.payments.repository.AccountRepository;
import com.bmstu_bureau_1440.payments.repository.PaymentTransactionRepository;
import com.bmstu_bureau_1440.shared.event.OrderPaymentRequestedEvent;
import com.bmstu_bureau_1440.shared.event.PaymentFailureReason;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class PaymentProcessingServiceIntegrationTest {

    @Autowired
    PaymentProcessingService paymentProcessingService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PaymentTransactionRepository paymentTransactionRepository;

    @BeforeEach
    void cleanUp() {
        paymentTransactionRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void process_withdrawsBalanceAndRecordsPaidTransaction_whenBalanceIsSufficient() {
        Account account = accountRepository.save(accountWithBalance(BigDecimal.valueOf(100)));
        OrderPaymentRequestedEvent event = requestFor(account.getUserId(), BigDecimal.valueOf(40));

        paymentProcessingService.processOrderPaymentRequest(event);

        assertThat(accountRepository.findByUserId(account.getUserId()).orElseThrow().getBalance())
                .isEqualByComparingTo(BigDecimal.valueOf(60));

        PaymentTransaction transaction = paymentTransactionRepository.findByOrderId(event.orderId()).orElseThrow();
        assertThat(transaction.getOutcome()).isEqualTo(PaymentOutcome.PAID);
        assertThat(transaction.getBalanceAfter()).isEqualByComparingTo(BigDecimal.valueOf(60));
    }

    @Test
    void process_recordsFailedTransactionWithoutWithdrawal_whenBalanceIsInsufficient() {
        Account account = accountRepository.save(accountWithBalance(BigDecimal.valueOf(10)));
        OrderPaymentRequestedEvent event = requestFor(account.getUserId(), BigDecimal.valueOf(40));

        paymentProcessingService.processOrderPaymentRequest(event);

        assertThat(accountRepository.findByUserId(account.getUserId()).orElseThrow().getBalance())
                .isEqualByComparingTo(BigDecimal.valueOf(10));

        PaymentTransaction transaction = paymentTransactionRepository.findByOrderId(event.orderId()).orElseThrow();
        assertThat(transaction.getOutcome()).isEqualTo(PaymentOutcome.FAILED);
        assertThat(transaction.getFailureReason()).isEqualTo(PaymentFailureReason.INSUFFICIENT_BALANCE);
    }

    @Test
    void process_recordsFailedTransaction_whenAccountDoesNotExist() {
        OrderPaymentRequestedEvent event = requestFor("no-such-user", BigDecimal.valueOf(40));

        paymentProcessingService.processOrderPaymentRequest(event);

        PaymentTransaction transaction = paymentTransactionRepository.findByOrderId(event.orderId()).orElseThrow();
        assertThat(transaction.getOutcome()).isEqualTo(PaymentOutcome.FAILED);
        assertThat(transaction.getFailureReason()).isEqualTo(PaymentFailureReason.ACCOUNT_NOT_FOUND);
    }

    @Test
    void process_isEffectivelyExactlyOnce_whenRedeliveredForTheSameOrderId() {
        Account account = accountRepository.save(accountWithBalance(BigDecimal.valueOf(100)));
        OrderPaymentRequestedEvent event = requestFor(account.getUserId(), BigDecimal.valueOf(40));

        paymentProcessingService.processOrderPaymentRequest(event);
        paymentProcessingService.processOrderPaymentRequest(event);

        assertThat(accountRepository.findByUserId(account.getUserId()).orElseThrow().getBalance())
                .isEqualByComparingTo(BigDecimal.valueOf(60));

        List<PaymentTransaction> transactions = paymentTransactionRepository.findAll();
        assertThat(transactions).filteredOn(tx -> tx.getOrderId().equals(event.orderId())).hasSize(1);
    }

    private static Account accountWithBalance(BigDecimal balance) {
        return Instancio.of(ACCOUNT_MODEL)
                .set(field(Account::getBalance), balance)
                .create();
    }

    private static OrderPaymentRequestedEvent requestFor(String userId, BigDecimal amount) {
        return Instancio.of(OrderPaymentRequestedEvent.class)
                .set(field(OrderPaymentRequestedEvent::userId), userId)
                .set(field(OrderPaymentRequestedEvent::amount), amount)
                .create();
    }

}
