package com.bmstu_bureau_1440.payments.repository;

import static com.bmstu_bureau_1440.payments.AccountTestsFixtures.ACCOUNT_MODEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.bmstu_bureau_1440.payments.TestContainersConfiguration;
import com.bmstu_bureau_1440.payments.model.Account;

@SpringBootTest
@Import(TestContainersConfiguration.class)
@ExtendWith(InstancioExtension.class)
class AccountsRepositoryIntegrationTest {

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void cleanUp() {
        accountRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void save_assignsGeneratedIdAndCreatedAt() {
        Account account = accountRepository.save(Instancio.create(ACCOUNT_MODEL));

        assertThat(account.getId()).isNotNull();
        assertThat(account.getCreatedAt()).isNotNull();
    }

    @Test
    void findById_returnsMatchingAccount() {
        Account account = accountRepository.save(Instancio.create(ACCOUNT_MODEL));

        Optional<Account> found = accountRepository.findById(account.getId());

        assertThat(found.get()).isEqualTo(account);
    }

    @Test
    void findAll_returnsAllPersistedAccounts() {
        List<Account> accounts = accountRepository.saveAll(Instancio.ofList(ACCOUNT_MODEL).size(3).create());

        List<Account> found = accountRepository.findAll();

        assertThat(found)
                .hasSameSizeAs(accounts)
                .containsExactlyElementsOf(accounts);
    }

    @Test
    void save_throwsDataIntegrityViolation_whenUserIdAlreadyHasAnAccount() {
        Account account = accountRepository.saveAndFlush(Instancio.create(ACCOUNT_MODEL));
        Account duplicate = new Account(account.getUserId());

        assertThatThrownBy(() -> accountRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_throwsOptimisticLockingFailure_whenAccountWasConcurrentlyModified() {
        Account account = accountRepository.saveAndFlush(Instancio.create(ACCOUNT_MODEL));

        Account firstRead = accountRepository.findById(account.getId()).orElseThrow();
        Account secondRead = accountRepository.findById(account.getId()).orElseThrow();

        firstRead.topUp(BigDecimal.TEN);
        accountRepository.saveAndFlush(firstRead);

        secondRead.topUp(BigDecimal.ONE);
        assertThatThrownBy(() -> accountRepository.saveAndFlush(secondRead))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

}
