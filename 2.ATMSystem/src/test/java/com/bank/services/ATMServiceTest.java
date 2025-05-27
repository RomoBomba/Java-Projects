package com.bank.services;

import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidOperationException;
import com.bank.model.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ATMServiceTest {

    @Test
    void testCreateAccount() {
        AccountService accountService = new AccountService();
        TransactionService transactionService = new TransactionService();
        ATMService atmService = new ATMService(accountService, transactionService);

        Account account = atmService.createAccount("12345", 1000);
        assertNotNull(account, "Account should be created successfully.");
        assertEquals(1000, account.getBalance(), "Initial balance should be set correctly.");
    }

    @Test
    void testDeposit() throws InvalidOperationException {
        AccountService accountService = new AccountService();
        TransactionService transactionService = new TransactionService();
        ATMService atmService = new ATMService(accountService, transactionService);

        Account account = atmService.createAccount("12345", 1000);
        atmService.deposit(account, 500);
        assertEquals(1500, account.getBalance(), "Balance should be updated after deposit.");
    }

    @Test
    void testWithdraw() throws InsufficientFundsException, InvalidOperationException {
        AccountService accountService = new AccountService();
        TransactionService transactionService = new TransactionService();
        ATMService atmService = new ATMService(accountService, transactionService);

        Account account = atmService.createAccount("12345", 1000);
        atmService.withdraw(account, 500);
        assertEquals(500, account.getBalance(), "Balance should be updated after withdrawal.");
    }

    @Test
    void testWithdrawInsufficientFunds() {
        AccountService accountService = new AccountService();
        TransactionService transactionService = new TransactionService();
        ATMService atmService = new ATMService(accountService, transactionService);

        Account account = atmService.createAccount("12345", 1000);
        Exception exception = assertThrows(InsufficientFundsException.class, () -> {
            atmService.withdraw(account, 1500);
        });
        assertEquals("Insufficient funds for withdrawal", exception.getMessage());
    }

    @Test
    void testInvalidOperation() {
        AccountService accountService = new AccountService();
        TransactionService transactionService = new TransactionService();
        ATMService atmService = new ATMService(accountService, transactionService);

        Account account = atmService.createAccount("12345", 1000);
        Exception exception = assertThrows(InvalidOperationException.class, () -> {
            atmService.deposit(account, -100);
        });
        assertEquals("Amount must be greater than 0", exception.getMessage());
    }
}
