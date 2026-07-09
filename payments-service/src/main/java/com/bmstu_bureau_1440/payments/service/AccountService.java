package com.bmstu_bureau_1440.payments.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bmstu_bureau_1440.payments.dto.AccountBalanceResponse;
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

    @Transactional
    public Account createAccount(String userId) {
        try {
            return accountRepository.saveAndFlush(createAccountRequestMapper.apply(userId));
        } catch (DataIntegrityViolationException e) {
            throw new AccountAlreadyExistsException();
        }
    }

    @Transactional
    public AccountBalanceResponse topUpAccount(String userId, TopUpAccountRequest request) {
        Account account = accountRepository.findByUserId(userId).orElseThrow(AccountNotFoundException::new);
        account.topUp(request.amount());
        Account updatedAccount = accountRepository.save(account);
        return new AccountBalanceResponse(updatedAccount.getBalance());
    }

    public AccountBalanceResponse getAccountBalance(String userId) {
        Account account = accountRepository.findByUserId(userId).orElseThrow(AccountNotFoundException::new);
        return new AccountBalanceResponse(account.getBalance());
    }

}
