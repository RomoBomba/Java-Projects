package com.bank.exception;

/**
 * Исключение, выбрасываемое при выполнении недопустимой операции.
 */
public class InvalidOperationException extends Exception {

    /**
     * Конструктор исключения.
     *
     * @param message сообщение об ошибке.
     */
    public InvalidOperationException(String message) {
        super(message);
    }
}
