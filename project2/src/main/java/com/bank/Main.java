package com.bank;

import java.util.Scanner;
import com.bank.services.AccountService;
import com.bank.services.TransactionService;
import com.bank.services.ATMService;
import com.bank.model.Account;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidOperationException;

/**
 * Главный класс для запуска ATM-системы.
 */
public class Main {

    /**
     * Основной метод для запуска ATM-системы.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        AccountService accountService = new AccountService();
        TransactionService transactionService = new TransactionService();
        ATMService atmService = new ATMService(accountService, transactionService);

        System.out.println("Enter your account number: ");
        String accountNumber = sc.nextLine();
        Account account = atmService.createAccount(accountNumber, 0);
        System.out.println("An account with the number " + accountNumber + " has been created.");

        while(true) {
            System.out.println("\nSelect an operation: "
                    + "\n1. Check Balance"
                    + "\n2. Deposit"
                    + "\n3. Withdraw"
                    + "\n4. Operation history"
                    + "\n5. Exit");

            int choice = sc.nextInt();

            try {
                switch(choice) {
                    case 1:
                        System.out.println("Balance: " + atmService.checkBalance(account));
                        break;
                    case 2:
                        System.out.println("Enter the deposit amount:");
                        double depositAmount = sc.nextDouble();
                        atmService.deposit(account, depositAmount);
                        System.out.println("Your account was credited to: " + depositAmount);
                        break;
                    case 3:
                        System.out.println("Enter the withdrawal amount:");
                        double withdrawAmount = sc.nextDouble();
                        atmService.withdraw(account, withdrawAmount);
                        System.out.println("Removed: " + withdrawAmount);
                        break;
                    case 4:
                        atmService.printTransactionHistory(account);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (InsufficientFundsException | InvalidOperationException e) {
                System.out.println("Wrong: " + e.getMessage());
            }
        }
    }
}
