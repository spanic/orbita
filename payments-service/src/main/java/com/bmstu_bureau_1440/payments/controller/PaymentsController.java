package com.bmstu_bureau_1440.payments.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bmstu_bureau_1440.payments.dto.CreateAccountRequest;
import com.bmstu_bureau_1440.payments.model.Account;
import com.bmstu_bureau_1440.payments.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(PaymentsApi.BASE_PATH)
@RequiredArgsConstructor
public class PaymentsController {

    private final AccountService accountService;

    @GetMapping
    public List<Account> getAccounts() {
        return accountService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createPayment(@RequestBody @Validated CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

}
