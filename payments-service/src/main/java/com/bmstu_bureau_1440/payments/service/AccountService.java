package com.bmstu_bureau_1440.payments.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.bmstu_bureau_1440.payments.dto.CreateAccountRequest;
import com.bmstu_bureau_1440.payments.error.AccountAlreadyExistsException;
import com.bmstu_bureau_1440.payments.mapper.CreateAccountRequestMapper;
import com.bmstu_bureau_1440.payments.model.Account;
import com.bmstu_bureau_1440.payments.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final CreateAccountRequestMapper createAccountRequestMapper;

    private final AccountRepository accountRepository;

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account createAccount(CreateAccountRequest request) {
        try {
            return accountRepository.save(createAccountRequestMapper.apply(request));
        } catch (DataIntegrityViolationException e) {
            throw new AccountAlreadyExistsException();
        }
    }

}
