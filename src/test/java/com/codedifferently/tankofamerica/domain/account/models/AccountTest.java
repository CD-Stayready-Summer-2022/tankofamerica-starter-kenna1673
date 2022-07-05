package com.codedifferently.tankofamerica.domain.account.models;

import com.codedifferently.tankofamerica.domain.transaction.exceptions.OverdraftException;
import com.codedifferently.tankofamerica.domain.user.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.UUID;

public class AccountTest {

    private User user;
    private Account account;

    @BeforeEach
    public void setUp() {
        user = new User("McKenna", "O'Hara", "email", "password");
        user.setId(1L);
        account = new Account("travel", user);
        account.setId(UUID.fromString("aafca6f6-84e1-4ec7-b5b6-9d1f9b8e68cd"));
    }

    @Test
    @DisplayName("Add positive amount to balance")
    public void updateBalanceTest01() throws OverdraftException {
        account.updateBalance(100.00);
        Double expected = 100.00;
        Double actual = account.getBalance();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Add negative amount to balance")
    public void updateBalanceTest02() {
        Assertions.assertThrows(OverdraftException.class, ()->{
            account.updateBalance(-100.00);
        });
    }
}
