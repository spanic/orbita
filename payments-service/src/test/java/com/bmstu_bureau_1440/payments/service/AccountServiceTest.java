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

import com.bmstu_bureau_1440.payments.dto.CreateAccountRequest;
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
        CreateAccountRequest request = Instancio.of(CreateAccountRequest.class)
                .set(Select.field(CreateAccountRequest::userId), "fixed-user-id")
                .create();
        Account account = Instancio.create(ACCOUNT_MODEL);

        when(createAccountRequestMapper.apply(request)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);

        assertThat(accountService.createAccount(request)).isEqualTo(account);
    }

    @Test
    void createAccount_throwsAccountAlreadyExists_whenUserIdAlreadyHasAnAccount() {
        CreateAccountRequest request = Instancio.of(CreateAccountRequest.class)
                .set(Select.field(CreateAccountRequest::userId), "fixed-user-id")
                .create();
        Account account = Instancio.create(ACCOUNT_MODEL);

        when(createAccountRequestMapper.apply(request)).thenReturn(account);
        when(accountRepository.save(account))
                .thenThrow(new DataIntegrityViolationException("duplicate user_id"));

        assertThatThrownBy(() -> accountService.createAccount(request))
                .isInstanceOf(AccountAlreadyExistsException.class);
    }

    @Test
    void topUpAccount_returnsAccountWithIncreasedBalance_whenAccountExists() {
        Account account = Instancio.of(Account.class)
                .set(Select.field(Account::getId), UUID.randomUUID())
                .set(Select.field(Account::getBalance), BigDecimal.TEN)
                .create();
        TopUpAccountRequest request = new TopUpAccountRequest(account.getId().toString(), BigDecimal.ONE);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.topUpAccount(request);

        assertThat(result.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(11));
    }

    @Test
    void topUpAccount_throwsAccountNotFound_whenAccountDoesNotExist() {
        UUID accountId = UUID.randomUUID();
        TopUpAccountRequest request = new TopUpAccountRequest(accountId.toString(), BigDecimal.ONE);

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.topUpAccount(request))
                .isInstanceOf(AccountNotFoundException.class);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void topUpAccount_propagatesOptimisticLockingFailure_whenAccountWasConcurrentlyModified() {
        Account account = Instancio.of(Account.class)
                .set(Select.field(Account::getId), UUID.randomUUID())
                .create();
        TopUpAccountRequest request = new TopUpAccountRequest(account.getId().toString(), BigDecimal.ONE);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenThrow(new OptimisticLockingFailureException("stale version"));

        assertThatThrownBy(() -> accountService.topUpAccount(request))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

}
