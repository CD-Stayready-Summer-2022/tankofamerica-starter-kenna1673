package com.codedifferently.tankofamerica.domain.account.controllers;

import com.codedifferently.tankofamerica.domain.account.models.Account;
import com.codedifferently.tankofamerica.domain.account.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ShellMethod("Create a new account")
    public Account createNewAccount(@ShellOption({"-F", "--firstname"}) String firstName,
                                    @ShellOption({"-L", "--lastname"}) String lastName,
                                    @ShellOption({"-B", "--balance"}) Double balance) {

        Account account = new Account(firstName, lastName, balance);
        account = accountService.create(account);
        return account;
    }

    @ShellMethod("Get all users")
    public String getAllAccounts() {
        return accountService.getAllAccounts();
    }



}
