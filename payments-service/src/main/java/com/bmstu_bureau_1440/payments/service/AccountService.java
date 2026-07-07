package com.bmstu_bureau_1440.payments.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.bmstu_bureau_1440.payments.dto.TopUpAccountRequest;
import com.bmstu_bureau_1440.payments.error.AccountAlreadyExistsException;
import com.bmstu_bureau_1440.payments.error.AccountNotFoundException;
import com.bmstu_bureau_1440.payments.mapper.CreateAccountRequestMapper;
import com.bmstu_bureau_1440.payments.model.Account;
import com.bmstu_bureau_1440.payments.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final CreateAccountRequestMapper createAccountRequestMapper;

    private final AccountRepository accountRepository;

    public Account findByUserId(String userId) {
        return accountRepository.findByUserId(userId).orElseThrow(AccountNotFoundException::new);
    }

    public Account createAccount(String userId) {
        try {
            return accountRepository.save(createAccountRequestMapper.apply(userId));
        } catch (DataIntegrityViolationException e) {
            throw new AccountAlreadyExistsException();
        }
    }

    public Account topUpAccount(String userId, TopUpAccountRequest request) {
        Account account = accountRepository.findByUserId(userId).orElseThrow(AccountNotFoundException::new);

        account.topUp(request.amount());

        return accountRepository.save(account);
    }

}
