package com.bank.services;

import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.model.Transaction;

/**
 * Сервис для работы с транзакциями.
 */
public class TransactionService {

    /**
     * Вносит деньги на счет.
     *
     * @param account аккаунт, на который нужно внести деньги.
     * @param amount сумма для депозита.
     */
    public void deposit(Account account, double amount) {
        account.deposit(amount);
    }

    /**
     * Снимает деньги с аккаунта.
     *
     * @param account аккаунт, с которого нужно снять деньги.
     * @param amount сумма для снятия.
     * @throws InsufficientFundsException если на аккаунте недостаточно средств.
     */
    public void withdraw(Account account, double amount) throws InsufficientFundsException {
        account.withdraw(amount);
    }

    /**
     * Печатает историю транзакций для аккаунта.
     *
     * @param account аккаунт, для которого нужно вывести историю транзакций.
     */
    public void printTransactionHistory(Account account) {
        for (Transaction transaction : account.getTransactionHistory()) {
            System.out.println(transaction.getType() + ": " + transaction.getAmount() + " at " + transaction.getTimestmp());
        }
    }
}
