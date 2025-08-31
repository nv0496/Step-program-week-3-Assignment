/**
 * Assignment1_YourName.java
 * Personal Finance Manager System
 * Demonstrates static vs instance members, encapsulation, and object usage.
 */

import java.util.Scanner;

/**
 * Class representing a Personal Finance Account.
 */
class PersonalAccount {
    private String accountHolderName;
    private String accountNumber;
    private double currentBalance;
    private double totalIncome;
    private double totalExpenses;

    private static int totalAccounts = 0;
    private static String bankName = "Default Bank";

    /**
     * Constructor to create a new PersonalAccount.
     * @param accountHolderName name of the account holder
     * @param initialDeposit initial deposit amount
     */
    public PersonalAccount(String accountHolderName, double initialDeposit) {
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative.");
        }
        this.accountHolderName = accountHolderName;
        this.accountNumber = generateAccountNumber();
        this.currentBalance = initialDeposit;
        this.totalIncome = initialDeposit;
        this.totalExpenses = 0;
        totalAccounts++;
    }

    /**
     * Add income to the account.
     * @param amount income amount
     * @param description description of income source
     */
    public void addIncome(double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Income must be positive.");
        }
        currentBalance += amount;
        totalIncome += amount;
        System.out.println(description + " added: " + amount);
    }

    /**
     * Add expense to the account.
     * @param amount expense amount
     * @param description description of expense
     */
    public void addExpense(double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Expense must be positive.");
        }
        if (amount > currentBalance) {
            throw new IllegalArgumentException("Insufficient funds for this expense.");
        }
        currentBalance -= amount;
        totalExpenses += amount;
        System.out.println(description + " spent: " + amount);
    }

    /**
     * Calculate savings (income - expenses).
     * @return total savings
     */
    public double calculateSavings() {
        return totalIncome - totalExpenses;
    }

    /**
     * Display account summary.
     */
    public void displayAccountSummary() {
        System.out.println("------------------------------------------------");
        System.out.println("Bank: " + bankName);
        System.out.println("Account Holder: " + accountHolderName);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Current Balance: " + currentBalance);
        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Expenses: " + totalExpenses);
        System.out.println("Savings: " + calculateSavings());
        System.out.println("------------------------------------------------");
    }

    /**
     * Set bank name (static).
     * @param name new bank name
     */
    public static void setBankName(String name) {
        bankName = name;
    }

    /**
     * Get total accounts created.
     * @return total accounts
     */
    public static int getTotalAccounts() {
        return totalAccounts;
    }

    /**
     * Generate unique account number.
     * @return account number string
     */
    private static String generateAccountNumber() {
        return "ACC" + String.format("%03d", totalAccounts + 1);
    }
}

/**
 * Main class for testing the Personal Finance Manager.
 */
public class Assignment1_YourName {
    public static void main(String[] args) {
        try {
            // Set bank name (shared for all accounts)
            PersonalAccount.setBankName("Global Finance Bank");

            // Create accounts
            PersonalAccount acc1 = new PersonalAccount("Alice", 1000);
            PersonalAccount acc2 = new PersonalAccount("Bob", 500);
            PersonalAccount acc3 = new PersonalAccount("Charlie", 2000);

            // Perform transactions
            acc1.addIncome(500, "Salary");
            acc1.addExpense(200, "Groceries");
            acc2.addIncome(300, "Freelance Work");
            acc2.addExpense(100, "Electricity Bill");
            acc3.addExpense(500, "Rent");
            acc3.addIncome(1000, "Bonus");

            // Display summaries
            acc1.displayAccountSummary();
            acc2.displayAccountSummary();
            acc3.displayAccountSummary();

            // Show total accounts created
            System.out.println("Total Accounts Created: " + PersonalAccount.getTotalAccounts());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
