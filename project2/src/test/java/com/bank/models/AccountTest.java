package com.bank.models;

import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    @Test
    void testDeposit() {
        Account account = new Account("12345", 1000);
        account.deposit(500);
        assertEquals(1500, account.getBalance(), "Balance should be updated after deposit.");
    }

    @Test
    void testWithdraw() throws InsufficientFundsException {
        Account account = new Account("12345", 1000);
        account.withdraw(500);
        assertEquals(500, account.getBalance(), "Balance should be updated after withdrawal.");
    }

    @Test
    void testWithdrawInsufficientFunds() {
        Account account = new Account("12345", 1000);
        Exception exception = assertThrows(InsufficientFundsException.class, () -> {
            account.withdraw(1500);
        });
        assertEquals("Insufficient funds for withdrawal", exception.getMessage());
    }

    @Test
    void testTransactionHistory() throws InsufficientFundsException {
        Account account = new Account("12345", 1000);
        account.deposit(500);
        account.withdraw(200);
        assertEquals(2, account.getTransactionHistory().size(), "There should be two transactions in history.");
    }
}
