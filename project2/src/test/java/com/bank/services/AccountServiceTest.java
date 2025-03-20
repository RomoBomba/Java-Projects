package com.bank.services;

import com.bank.model.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    @Test
    void testCreateAccount() {
        AccountService accountService = new AccountService();
        Account account = accountService.createAccount("12345", 1000);
        assertNotNull(account, "Account should be created successfully.");
        assertEquals("12345", account.getAccountID(), "Account ID should be correct.");
        assertEquals(1000, account.getBalance(), "Initial balance should be set correctly.");
    }
}
