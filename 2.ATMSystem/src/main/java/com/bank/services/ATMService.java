package com.bank.services;

import com.bank.model.Account;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidOperationException;

/**
 * Сервис для управления операциями банкомата.
 */
public class ATMService {
    private final AccountService accountService;
    private final TransactionService transactionService;

    /**
     * Конструктор для инициализации сервисов.
     *
     * @param accountService сервис для работы с аккаунтами.
     * @param transactionService сервис для работы с транзакциями.
     */
    public ATMService(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    /**
     * Создает новый аккаунт с указанным номером и начальным балансом.
     *
     * @param accountId идентификатор аккаунта.
     * @param InitialBalance начальный баланс аккаунта.
     * @return созданный аккаунт.
     */
    public Account createAccount(String accountId, double InitialBalance) {
        return accountService.createAccount(accountId, InitialBalance);
    }

    /**
     * Вносит деньги на указанный аккаунт.
     *
     * @param account аккаунт, на который нужно внести деньги.
     * @param amount сумма депозита.
     * @throws InvalidOperationException если сумма депозита неверна.
     */
    public void deposit(Account account, double amount) throws InvalidOperationException {
        if (amount <= 0) {
            throw new InvalidOperationException("Amount must be greater than 0");
        }
        transactionService.deposit(account, amount);
    }

    /**
     * Снимает деньги с указанного аккаунта.
     *
     * @param account аккаунт, с которого нужно снять деньги.
     * @param amount сумма для снятия.
     * @throws InsufficientFundsException если на аккаунте недостаточно средств.
     * @throws InvalidOperationException если сумма снятия неверна.
     */
    public void withdraw(Account account, double amount) throws InsufficientFundsException, InvalidOperationException {
        if (amount <= 0) {
            throw new InvalidOperationException("Amount must be greater than 0");
        }
        transactionService.withdraw(account, amount);
    }

    /**
     * Проверяет баланс указанного аккаунта.
     *
     * @param account аккаунт, баланс которого нужно проверить.
     * @return баланс аккаунта.
     */
    public double checkBalance(Account account) {
        return accountService.getBalance(account);
    }

    /**
     * Печатает историю транзакций для указанного аккаунта.
     *
     * @param account аккаунт, для которого нужно вывести историю.
     */
    public void printTransactionHistory(Account account) {
        transactionService.printTransactionHistory(account);
    }
}
