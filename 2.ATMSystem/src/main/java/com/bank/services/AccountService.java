package com.bank.services;

import com.bank.model.Account;

/**
 * Сервис для работы с аккаунтами.
 */
public class AccountService {

    /**
     * Создает новый аккаунт с указанным номером и начальным балансом.
     *
     * @param accountId идентификатор аккаунта.
     * @param InitialBalance начальный баланс аккаунта.
     * @return созданный аккаунт.
     */
    public Account createAccount(String accountId, double InitialBalance) {
        return new Account(accountId, InitialBalance);
    }

    /**
     * Получает баланс указанного аккаунта.
     *
     * @param account аккаунт, для которого нужно получить баланс.
     * @return баланс аккаунта.
     */
    public double getBalance(Account account) {
        return account.getBalance();
    }
}
