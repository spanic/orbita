package com.bmstu_bureau_1440.payments.controller;

import static com.bmstu_bureau_1440.payments.AccountTestsFixtures.ACCOUNT_MODEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.math.BigDecimal;

import org.instancio.Instancio;
import org.instancio.Select;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.bmstu_bureau_1440.payments.dto.CreateAccountRequest;
import com.bmstu_bureau_1440.payments.dto.TopUpAccountRequest;
import com.bmstu_bureau_1440.payments.error.AccountAlreadyExistsException;
import com.bmstu_bureau_1440.payments.error.AccountNotFoundException;
import com.bmstu_bureau_1440.payments.model.Account;
import com.bmstu_bureau_1440.payments.service.AccountService;

import tools.jackson.databind.ObjectMapper;

@ExtendWith(InstancioExtension.class)
@WebMvcTest(PaymentsController.class)
class PaymentsControllerTest {

    @Autowired
    MockMvcTester mvcTester;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AccountService accountService;

    @Test
    void createPayment_returnsConflict_whenAccountAlreadyExistsForUserId() throws Exception {
        CreateAccountRequest request = Instancio.create(CreateAccountRequest.class);
        when(accountService.createAccount(any())).thenThrow(new AccountAlreadyExistsException());

        assertThat(mvcTester.perform(post(PaymentsApi.BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))))
                .hasStatus(HttpStatus.CONFLICT);
    }

    @Test
    void topUpAccount_returnsAccountAsJson_whenAccountExists() throws Exception {
        TopUpAccountRequest request = Instancio.create(TopUpAccountRequest.class);
        Account account = Instancio.create(ACCOUNT_MODEL);

        when(accountService.topUpAccount(any())).thenReturn(account);

        assertThat(mvcTester.perform(post(PaymentsApi.BASE_PATH + PaymentsApi.TOP_UP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))))
                .hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson()
                .convertTo(Account.class)
                .isEqualTo(account);
    }

    @Test
    void topUpAccount_returnsNotFound_whenAccountDoesNotExist() throws Exception {
        TopUpAccountRequest request = Instancio.create(TopUpAccountRequest.class);

        when(accountService.topUpAccount(any())).thenThrow(new AccountNotFoundException());

        assertThat(mvcTester.perform(post(PaymentsApi.BASE_PATH + PaymentsApi.TOP_UP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))))
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void topUpAccount_returnsConflict_whenAccountWasConcurrentlyModified() throws Exception {
        TopUpAccountRequest request = Instancio.create(TopUpAccountRequest.class);

        when(accountService.topUpAccount(any())).thenThrow(new OptimisticLockingFailureException("stale version"));

        assertThat(mvcTester.perform(post(PaymentsApi.BASE_PATH + PaymentsApi.TOP_UP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))))
                .hasStatus(HttpStatus.CONFLICT);
    }

    @Test
    void topUpAccount_returnsBadRequest_whenAmountIsNotPositive() throws Exception {
        TopUpAccountRequest request = Instancio.of(TopUpAccountRequest.class)
                .set(Select.field(TopUpAccountRequest::amount), BigDecimal.ZERO)
                .create();

        assertThat(mvcTester.perform(post(PaymentsApi.BASE_PATH + PaymentsApi.TOP_UP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .extractingPath("$.error_code")
                .isEqualTo("INVALID_AMOUNT");
    }

}
