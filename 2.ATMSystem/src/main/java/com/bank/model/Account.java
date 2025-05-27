package com.bank.model;

import com.bank.exception.InsufficientFundsException;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий банковский аккаунт.
 */
public class Account {
    private String AccountID;
    private double balance;
    private List<Transaction> transactionHistory;

    /**
     * Конструктор аккаунта.
     *
     * @param AccountID уникальный идентификатор аккаунта.
     * @param balance начальный баланс аккаунта.
     */
    public Account(String AccountID, double balance) {
        this.AccountID = AccountID;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    /**
     * Получить уникальный идентификатор аккаунта.
     *
     * @return идентификатор аккаунта.
     */
    public String getAccountID() {
        return AccountID;
    }

    /**
     * Получить баланс аккаунта.
     *
     * @return баланс аккаунта.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Пополнить баланс аккаунта.
     *
     * @param amount сумма для депозита.
     */
    public void deposit(double amount) {
        this.balance += amount;
        transactionHistory.add(new Transaction("Deposit", amount));
    }

    /**
     * Снять деньги с баланса аккаунта.
     *
     * @param amount сумма для снятия.
     * @throws InsufficientFundsException если на счету недостаточно средств.
     */
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > this.balance) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }
        this.balance -= amount;
        transactionHistory.add(new Transaction("Withdrawal", amount));
    }

    /**
     * Получить историю транзакций аккаунта.
     *
     * @return список транзакций.
     */
    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}
