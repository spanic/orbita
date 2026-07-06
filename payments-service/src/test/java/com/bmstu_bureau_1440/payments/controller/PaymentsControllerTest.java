package com.bmstu_bureau_1440.payments.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.bmstu_bureau_1440.payments.error.AccountAlreadyExistsException;
import com.bmstu_bureau_1440.payments.service.AccountService;

@WebMvcTest(PaymentsController.class)
class PaymentsControllerTest {

    @Autowired
    MockMvcTester mvcTester;

    @MockitoBean
    AccountService accountService;

    @Test
    void createPayment_returnsConflict_whenAccountAlreadyExistsForUserId() {
        when(accountService.createAccount(any())).thenThrow(new AccountAlreadyExistsException());

        assertThat(mvcTester.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user_id\":\"user-1\"}")))
                .hasStatus(HttpStatus.CONFLICT);
    }

}
