package com.bank.exception;

/**
 * Исключение, выбрасываемое при недостаточности средств на счете.
 */
public class InsufficientFundsException extends Exception {

    /**
     * Конструктор исключения.
     *
     * @param message сообщение об ошибке.
     */
    public InsufficientFundsException(String message) {
        super(message);
    }
}
