package com.bank.services;

import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    @Test
    void testDeposit() {
        TransactionService transactionService = new TransactionService();
        Account account = new Account("12345", 1000);
        transactionService.deposit(account, 500);
        assertEquals(1500, account.getBalance(), "Balance should be updated after deposit.");
    }

    @Test
    void testWithdraw() throws InsufficientFundsException {
        TransactionService transactionService = new TransactionService();
        Account account = new Account("12345", 1000);
        transactionService.withdraw(account, 500);
        assertEquals(500, account.getBalance(), "Balance should be updated after withdrawal.");
    }

    @Test
    void testWithdrawInsufficientFunds() {
        TransactionService transactionService = new TransactionService();
        Account account = new Account("12345", 1000);
        Exception exception = assertThrows(InsufficientFundsException.class, () -> {
            transactionService.withdraw(account, 1500);
        });
        assertEquals("Insufficient funds for withdrawal", exception.getMessage());
    }
}
