import java.util.*;

// Base class for common account features
abstract class Account {
    protected String customerName;
    protected int accountNumber;
    protected double balance;

    public Account(String customerName, int accountNumber, double initialDeposit) {
        this.customerName = customerName;
        this.accountNumber = accountNumber;
        this.balance = initialDeposit;
    }

    public abstract void withdraw(double amount);
    public abstract void calculateInterest();

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: Rs." + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void showDetails() {
        System.out.println("Customer Name: " + customerName);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Balance: Rs." + balance);
    }

    public double getBalance() {
        return balance;
    }
}

// Savings account with interest and limited withdrawal
class SavingsAccount extends Account {
    private final double interestRate = 0.04; // 4% annual
    private final double withdrawalLimit = 1000.00;

    public SavingsAccount(String customerName, int accountNumber, double initialDeposit) {
        super(customerName, accountNumber, initialDeposit);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.");
        } else if (amount > withdrawalLimit) {
            System.out.println("Withdrawal failed: exceeds limit of Rs." + withdrawalLimit);
        } else if (amount > balance) {
            System.out.println("Withdrawal failed: insufficient funds.");
        } else {
            balance -= amount;
            System.out.println("Withdrew: Rs." + amount);
        }
    }

    @Override
    public void calculateInterest() {
        double interest = balance * interestRate;
        balance += interest;
        System.out.println("Interest of Rs." + interest + " added to savings account.");
    }
}

// Current account with overdraft facility
class CurrentAccount extends Account {
    private final double overdraftLimit = 500.00;

    public CurrentAccount(String customerName, int accountNumber, double initialDeposit) {
        super(customerName, accountNumber, initialDeposit);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.");
        } else if (balance + overdraftLimit >= amount) {
            balance -= amount;
            System.out.println("Withdrew: Rs." + amount);
        } else {
            System.out.println("Withdrawal failed: exceeds overdraft limit.");
        }
    }

    @Override
    public void calculateInterest() {
        System.out.println("No interest applied to current account.");
    }
}

// Main class to simulate the system
public class BankingSystem {
    private static final Map<Integer, Account> accounts = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static int nextAccountNumber = 1001;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Banking System Menu ===");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Calculate Interest");
            System.out.println("5. View Account Details");
            System.out.println("6. Exit");
            System.out.print("Select option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer
                switch (choice) {
                    case 1 -> createAccount();
                    case 2 -> depositToAccount();
                    case 3 -> withdrawFromAccount();
                    case 4 -> applyInterest();
                    case 5 -> viewDetails();
                    case 6 -> {
                        System.out.println("Thank you for using the banking system.");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid numeric input.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void createAccount() {
        try {
            System.out.print("Enter customer name: ");
            String name = scanner.nextLine();

            System.out.print("Enter account type (1: Savings, 2: Current): ");
            int type = scanner.nextInt();

            System.out.print("Enter initial deposit: ");
            double deposit = scanner.nextDouble();

            if (deposit <= 0) {
                System.out.println("Initial deposit must be greater than zero.");
                return;
            }

            Account account;
            int accountNumber = nextAccountNumber++;
            if (type == 1) {
                account = new SavingsAccount(name, accountNumber, deposit);
            } else if (type == 2) {
                account = new CurrentAccount(name, accountNumber, deposit);
            } else {
                System.out.println("Invalid account type.");
                return;
            }

            accounts.put(accountNumber, account);
            System.out.println("Account created successfully. Account Number: " + accountNumber);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numeric values for type and deposit.");
            scanner.nextLine(); // Clear invalid input
        }
    }

    private static void depositToAccount() {
        Account account = getAccount();
        if (account != null) {
            try {
                System.out.print("Enter amount to deposit: ");
                double amount = scanner.nextDouble();
                account.deposit(amount);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.nextLine();
            }
        }
    }

    private static void withdrawFromAccount() {
        Account account = getAccount();
        if (account != null) {
            try {
                System.out.print("Enter amount to withdraw: ");
                double amount = scanner.nextDouble();
                account.withdraw(amount);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.nextLine();
            }
        }
    }

    private static void applyInterest() {
        Account account = getAccount();
        if (account != null) {
            account.calculateInterest();
        }
    }

    private static void viewDetails() {
        Account account = getAccount();
        if (account != null) {
            account.showDetails();
        }
    }

    private static Account getAccount() {
        try {
            System.out.print("Enter account number: ");
            int accNo = scanner.nextInt();
            Account account = accounts.get(accNo);
            if (account == null) {
                System.out.println("Account not found.");
            }
            return account;
        } catch (InputMismatchException e) {
            System.out.println("Invalid account number. Please enter a valid number.");
            scanner.nextLine(); // Clear invalid input
            return null;
        }
    }
}
