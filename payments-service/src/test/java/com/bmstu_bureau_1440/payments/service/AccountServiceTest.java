package com.bmstu_bureau_1440.payments.service;

import static com.bmstu_bureau_1440.payments.AccountTestsFixtures.ACCOUNT_MODEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.instancio.Instancio;
import org.instancio.Select;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.bmstu_bureau_1440.payments.dto.AccountBalanceResponse;
import com.bmstu_bureau_1440.payments.dto.TopUpAccountRequest;
import com.bmstu_bureau_1440.payments.error.AccountAlreadyExistsException;
import com.bmstu_bureau_1440.payments.error.AccountNotFoundException;
import com.bmstu_bureau_1440.payments.mapper.CreateAccountRequestMapper;
import com.bmstu_bureau_1440.payments.model.Account;
import com.bmstu_bureau_1440.payments.repository.AccountRepository;

@ExtendWith({ MockitoExtension.class, InstancioExtension.class })
class AccountServiceTest {

    @Mock
    CreateAccountRequestMapper createAccountRequestMapper;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountService accountService;

    @Test
    void createAccount_returnsSavedAccount_whenUserIdIsNew() {
        String userId = "test-user-id";
        Account account = Instancio.create(ACCOUNT_MODEL);

        when(createAccountRequestMapper.apply(userId)).thenReturn(account);
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        assertThat(accountService.createAccount(userId)).isEqualTo(account);
    }

    @Test
    void createAccount_throwsAccountAlreadyExists_whenUserIdAlreadyHasAnAccount() {
        String userId = "test-user-id";
        Account account = Instancio.create(ACCOUNT_MODEL);

        when(createAccountRequestMapper.apply(userId)).thenReturn(account);
        when(accountRepository.saveAndFlush(account))
                .thenThrow(new DataIntegrityViolationException("duplicate user_id"));

        assertThatThrownBy(() -> accountService.createAccount(userId))
                .isInstanceOf(AccountAlreadyExistsException.class);
    }

    @Test
    void topUpAccount_returnsAccountWithIncreasedBalance_whenAccountExists() {
        Account account = Instancio.of(Account.class)
                .set(Select.field(Account::getId), UUID.randomUUID())
                .set(Select.field(Account::getBalance), BigDecimal.TEN)
                .create();
        TopUpAccountRequest request = new TopUpAccountRequest(BigDecimal.ONE);

        when(accountRepository.findByUserId(account.getUserId())).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        AccountBalanceResponse result = accountService.topUpAccount(account.getUserId(), request);

        assertThat(result.balance()).isEqualByComparingTo(BigDecimal.valueOf(11));
    }

    @Test
    void topUpAccount_throwsAccountNotFound_whenAccountDoesNotExist() {
        String userId = "missing-user-id";
        TopUpAccountRequest request = new TopUpAccountRequest(BigDecimal.ONE);

        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.topUpAccount(userId, request))
                .isInstanceOf(AccountNotFoundException.class);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void topUpAccount_propagatesOptimisticLockingFailure_whenAccountWasConcurrentlyModified() {
        Account account = Instancio.of(Account.class)
                .set(Select.field(Account::getId), UUID.randomUUID())
                .create();
        TopUpAccountRequest request = new TopUpAccountRequest(BigDecimal.ONE);

        when(accountRepository.findByUserId(account.getUserId())).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenThrow(new OptimisticLockingFailureException("stale version"));

        assertThatThrownBy(() -> accountService.topUpAccount(account.getUserId(), request))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

}
