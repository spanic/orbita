package com.bmstu_bureau_1440.payments.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmstu_bureau_1440.payments.model.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByUserId(String userId);

}
