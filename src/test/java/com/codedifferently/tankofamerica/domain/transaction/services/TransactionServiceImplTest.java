package com.codedifferently.tankofamerica.domain.transaction.services;

import com.codedifferently.tankofamerica.domain.account.exceptions.AccountNotFoundException;
import com.codedifferently.tankofamerica.domain.account.models.Account;
import com.codedifferently.tankofamerica.domain.transaction.exceptions.OverdraftException;
import com.codedifferently.tankofamerica.domain.transaction.exceptions.TransactionNotFoundException;
import com.codedifferently.tankofamerica.domain.transaction.models.Transaction;
import com.codedifferently.tankofamerica.domain.transaction.repos.TransactionRepo;
import com.codedifferently.tankofamerica.domain.user.models.User;
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
public class TransactionServiceImplTest {
    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepo transactionRepo;

    private Account account;
    private User user;
    private Transaction transaction;
    private Transaction transaction2;

    @BeforeEach
    public void setUp() {
        user = new User("McKenna", "O'Hara", "email", "password");
        user.setId(1L);
        account = new Account("travel", user);
        account.setId(UUID.fromString("aafca6f6-84e1-4ec7-b5b6-9d1f9b8e68cd"));
        transaction = new Transaction(100.00, account);
        transaction2 = new Transaction(-100.00, account);
        transaction.setId(1L);
        transaction2.setId(2L);
    }

    @Test
    public void createTest01() throws AccountNotFoundException, OverdraftException {
        String id = "aafca6f6-84e1-4ec7-b5b6-9d1f9b8e68cd";
        BDDMockito.doReturn(transaction).when(transactionRepo).save(transaction);
        Transaction actual = transactionService.create(id, transaction);
        Assertions.assertEquals(100, transaction.getAccount().getBalance());
    }

    @Test
    public void createTest02() throws AccountNotFoundException {
        String id = "aafca6f6-84e1-4ec7-b5b6-9d1f9b8e68cd";
        Assertions.assertThrows(OverdraftException.class, ()->{
            transactionService.create(id, transaction2);
        });
    }

    @Test
    public void getByIdTest01() throws TransactionNotFoundException {
        Long id = transaction.getId();
        BDDMockito.doReturn(Optional.of(transaction)).when(transactionRepo).findById(id);
        Transaction actual = transactionService.getById(1L);
        Assertions.assertEquals(transaction, actual);
    }

    @Test
    public void getByIdTest02() {
        Long id = transaction.getId();
        BDDMockito.doReturn(Optional.empty()).when(transactionRepo).findById(id);
        Assertions.assertThrows(TransactionNotFoundException.class, ()->{
            transactionService.getById(1L);
        });
    }
    
    @Test
    public void getAllFromAccountTest01() {
        // Given

        // WHen
        List<Transaction> transactions = new ArrayList<>();
        BDDMockito.doReturn(transactions).when(transactionRepo).findByAccount(account);
        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
        String expected = String.format("Amount: %.2f Account id: %s. Balance is: %.2f", 100.00, account.getId().toString(), account.getBalance());
        // Then
    } 


}
