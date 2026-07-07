package com.bmstu_bureau_1440.payments.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bmstu_bureau_1440.payments.dto.TopUpAccountRequest;
import com.bmstu_bureau_1440.payments.model.Account;
import com.bmstu_bureau_1440.payments.service.AccountService;
import com.bmstu_bureau_1440.shared.web.UserIdHeaderInterceptor;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(PaymentsApi.BASE_PATH)
@RequiredArgsConstructor
public class PaymentsController {

    private final AccountService accountService;

    @GetMapping
    public Account getAccount(@RequestAttribute(UserIdHeaderInterceptor.USER_ID_ATTRIBUTE) String userId) {
        return accountService.findByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createPayment(@RequestAttribute(UserIdHeaderInterceptor.USER_ID_ATTRIBUTE) String userId) {
        return accountService.createAccount(userId);
    }

    @PostMapping(PaymentsApi.TOP_UP_PATH)
    public Account topUpAccount(
            @RequestAttribute(UserIdHeaderInterceptor.USER_ID_ATTRIBUTE) String userId,
            @RequestBody @Validated TopUpAccountRequest request) {
        return accountService.topUpAccount(userId, request);
    }

}
