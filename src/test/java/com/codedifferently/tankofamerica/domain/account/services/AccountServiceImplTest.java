package com.codedifferently.tankofamerica.domain.account.services;


import com.codedifferently.tankofamerica.domain.account.exceptions.AccountNotFoundException;
import com.codedifferently.tankofamerica.domain.account.models.Account;
import com.codedifferently.tankofamerica.domain.account.repos.AccountRepo;
import com.codedifferently.tankofamerica.domain.user.exceptions.UserNotFoundException;
import com.codedifferently.tankofamerica.domain.user.models.User;
import com.codedifferently.tankofamerica.domain.user.repos.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT + ".enabled=false"
})
@ExtendWith(SpringExtension.class)
public class AccountServiceImplTest {
    @Autowired
    private AccountService accountService;
    @MockBean
    private AccountRepo accountRepo;
    private Account account;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("McKenna", "O'Hara", "email", "password");
        user.setId(1L);
        account = new Account("travel", user);
        account.setId(UUID.fromString("aafca6f6-84e1-4ec7-b5b6-9d1f9b8e68cd"));
    }

    @Test
    public void createTest01() throws UserNotFoundException {
        User user = new User("McKenna", "O'Hara", "email", "password");
        user.setId(1L);
        Account account = new Account("travel", user);
        account.setId(UUID.fromString("aafca6f6-84e1-4ec7-b5b6-9d1f9b8e68cd"));
        BDDMockito.doReturn(account).when(accountRepo).save(account);
        Account actual = accountService.create(1L, account);
        Assertions.assertEquals(account, actual);
    }

    @Test
    public void getByIdTest01() throws AccountNotFoundException {
        UUID id = account.getId();
        BDDMockito.doReturn(Optional.of(account)).when(accountRepo).findById(id);
        Account actual = accountService.getById("aafca6f6-84e1-4ec7-b5b6-9d1f9b8e68cd");
        Assertions.assertEquals(account, actual);
    }

    @Test
    public void getByIdTest02() {
        UUID id = account.getId();
        BDDMockito.doReturn(Optional.empty()).when(accountRepo).findById(id);
        Assertions.assertThrows(AccountNotFoundException.class, ()->{
            accountService.getById("aafca6f6-84e1-4ec7-b5b6-9d1f9b8e68cd");
        });
    }

    @Test
    public void getByIdTest03() {
        UUID id = account.getId();
        BDDMockito.doReturn(Optional.empty()).when(accountRepo).findById(id);
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            accountService.getById("4ec7-b5b6-9d1f9b8e68cd");
        });
    }


    /*
    @Test
    public void getAllFromUserTest01() throws UserNotFoundException {
        Iterable<Account> accounts = new ArrayList<>();
        BDDMockito.doReturn(account).when(accountRepo).save(account);
        BDDMockito.doReturn(accounts).when(accountRepo).findByOwner(user);
        String expected = "Account for McKenna named travel with id aafca6f6-84e1-4ec7-b5b6-9d1f9b8e68cd and balance $0.00";
        String actual = accountService.getAllFromUser(1L);
        Assertions.assertEquals(expected, actual);
    } */

    @Test
    public void updateTest01() {
        account.setBalance(100.00);
        BDDMockito.doReturn(account).when(accountRepo).save(account);
        Account actual = accountService.update(account);
        Assertions.assertEquals(account, actual);
    }
}
