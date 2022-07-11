package com.codedifferently.tankofamerica.domain.transaction.services;

import com.codedifferently.tankofamerica.domain.account.exceptions.AccountNotFoundException;
import com.codedifferently.tankofamerica.domain.transaction.exceptions.OverdraftException;
import com.codedifferently.tankofamerica.domain.transaction.exceptions.TransactionNotFoundException;
import com.codedifferently.tankofamerica.domain.transaction.models.Transaction;

import java.util.UUID;

public interface TransactionService {
    Transaction create(String accountId, Transaction transaction) throws AccountNotFoundException, OverdraftException;
    Transaction getById(Long id) throws TransactionNotFoundException;
    String getAllFromAccount(String accountId) throws AccountNotFoundException;
    String getAllWithdrawals(String accountId) throws AccountNotFoundException;
    String getAllDeposits(String accountId) throws AccountNotFoundException;
    String getFirstTransaction(String accountId) throws AccountNotFoundException;
    String getMostRecentTransaction(String accountId) throws AccountNotFoundException;
}
