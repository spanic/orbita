package com.bmstu_bureau_1440.payments;

import static org.instancio.Select.all;
import static org.instancio.Select.field;

import org.instancio.Instancio;
import org.instancio.Model;

import com.bmstu_bureau_1440.payments.model.Account;

public final class AccountTestsFixtures {

    public static final Model<Account> ACCOUNT_MODEL = Instancio.of(Account.class)
            .ignore(all(field(Account::getId), field(Account::getCreatedAt)))
            .toModel();

}
