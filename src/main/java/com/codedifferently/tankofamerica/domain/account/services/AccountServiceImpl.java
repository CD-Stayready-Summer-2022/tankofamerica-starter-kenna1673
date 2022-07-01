package com.codedifferently.tankofamerica.domain.account.services;

import com.codedifferently.tankofamerica.domain.account.models.Account;
import com.codedifferently.tankofamerica.domain.account.repos.AccountRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private AccountRepo accountRepo;

    @Autowired
    public AccountServiceImpl(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    public Account create(Account account) {
        return accountRepo.save(account);
    }

    public String getAllAccounts() {
        StringBuilder builder = new StringBuilder();
        Iterable<Account> accounts = accountRepo.findAll();
        for (Account account : accounts) {
            builder.append(account.toString() + "\n");
        }
        return builder.toString().trim();
    }
}
