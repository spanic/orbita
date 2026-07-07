package com.bmstu_bureau_1440.payments.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.payments.model.Account;

@Component
public class CreateAccountRequestMapper implements Function<String, Account> {

    @Override
    public Account apply(String userId) {
        return new Account(userId);
    }

}
