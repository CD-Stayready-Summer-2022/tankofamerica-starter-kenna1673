package com.codedifferently.tankofamerica.domain.account.services;

import com.codedifferently.tankofamerica.domain.account.models.Account;

public interface AccountService {
    Account create(Account account);
    String getAllAccounts();
}
