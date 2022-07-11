package com.codedifferently.tankofamerica.domain.transaction.services;

import com.codedifferently.tankofamerica.domain.account.exceptions.AccountNotFoundException;
import com.codedifferently.tankofamerica.domain.account.models.Account;
import com.codedifferently.tankofamerica.domain.account.services.AccountService;
import com.codedifferently.tankofamerica.domain.transaction.exceptions.OverdraftException;
import com.codedifferently.tankofamerica.domain.transaction.exceptions.TransactionNotFoundException;
import com.codedifferently.tankofamerica.domain.transaction.models.Transaction;
import com.codedifferently.tankofamerica.domain.transaction.repos.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepo transactionRepo;
    private AccountService accountService;

    @Autowired
    public TransactionServiceImpl(TransactionRepo transactionRepo, AccountService accountService) {
        this.transactionRepo = transactionRepo;
        this.accountService = accountService;
    }

    @Override
    public Transaction create(String accountId, Transaction transaction) throws AccountNotFoundException, OverdraftException {
        try {
            Double amount = transaction.getAmount();
            Account account = transaction.getAccount();
            account.updateBalance(amount);
            account = accountService.update(account);
            transaction.setAccount(account);
            return transactionRepo.save(transaction);
        } catch (OverdraftException e) {
            throw new OverdraftException("Not enough funds!");
        }
    }

    @Override
    public Transaction getById(Long id) throws TransactionNotFoundException {
        Optional<Transaction> optional = transactionRepo.findById(id);
        if (optional.isEmpty()) {
            throw new TransactionNotFoundException(String.format("Transaction with id: %s not found", id));
        }
        return optional.get();
    }

    @Override
    public String getAllFromAccount(String accountId) throws AccountNotFoundException {
        Account account = accountService.getById(accountId);
        List<Transaction> transactions = transactionRepo.findByAccount(account);
        StringBuilder builder = new StringBuilder();
        for (Transaction transaction : transactions) {
            builder.append(transaction).append("\n");
        }
        return builder.toString().trim();
    }

    @Override
    public String getAllWithdrawals(String accountId) throws AccountNotFoundException {
        Account account = accountService.getById(accountId);
        List<Transaction> transactions = transactionRepo.findByAccount(account);
        StringBuilder builder = new StringBuilder();
        for (Transaction transaction : transactions) {
            Double amount = transaction.getAmount();
            if (amount < 0) {
                builder.append(transaction).append("\n");
            }
        }
        return (builder.toString()).trim();
    }

    @Override
    public String getAllDeposits(String accountId) throws AccountNotFoundException {
        Account account = accountService.getById(accountId);
        List<Transaction> transactions = transactionRepo.findByAccount(account);
        StringBuilder builder = new StringBuilder();
        for (Transaction transaction : transactions) {
            Double amount = transaction.getAmount();
            if (amount > 0) {
                builder.append(transaction).append("\n");
            }
        }
        return (builder.toString()).trim();
    }

    @Override
    public String getFirstTransaction(String accountId) throws AccountNotFoundException {
        Account account = accountService.getById(accountId);
        List<Transaction> transactions = transactionRepo.findByAccount(account);
        if (!transactions.isEmpty()) {
            return transactions.get(0).toString();
        }
        return "You currently have no transactions!";
    }

    @Override
    public String getMostRecentTransaction(String accountId) throws AccountNotFoundException {
        Account account = accountService.getById(accountId);
        List<Transaction> transactions = transactionRepo.findByAccount(account);
        if (!transactions.isEmpty()) {
            return transactions.get(transactions.size() - 1).toString();
        }
        return "You currently have no transactions!";
    }

    public TransactionRepo getTransactionRepo() {
        return transactionRepo;
    }

    public void setTransactionRepo(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
