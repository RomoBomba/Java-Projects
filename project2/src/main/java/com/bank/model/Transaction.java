package com.bank.model;

import java.time.LocalDateTime;

/**
 * Класс, представляющий транзакцию.
 */
public class Transaction {
    private String type;
    private double amount;
    private LocalDateTime timestmp;

    /**
     * Конструктор транзакции.
     *
     * @param type тип транзакции (например, Deposit или Withdrawal).
     * @param amount сумма транзакции.
     */
    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.timestmp = LocalDateTime.now();
    }

    /**
     * Получить тип транзакции.
     *
     * @return тип транзакции.
     */
    public String getType() {
        return type;
    }

    /**
     * Получить сумму транзакции.
     *
     * @return сумма транзакции.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Получить временную метку транзакции.
     *
     * @return временная метка.
     */
    public LocalDateTime getTimestmp() {
        return timestmp;
    }
}
