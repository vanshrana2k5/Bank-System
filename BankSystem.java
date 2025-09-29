import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

// Utility for money formatting
class MoneyUtil {
    private static final DecimalFormat df = new DecimalFormat("₹#,##0.00");
    public static String format(double amount) {
        return df.format(amount);
    }
}

// Transaction model
class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Date date;
    private final String type;
    private final double amount;
    private final double balanceAfter;

    public Transaction(String type, double amount, double balanceAfter) {
        this.date = new Date();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    @Override
    public String toString() {
        return String.format("[%tF %<tT] %-12s %10s | Balance: %s",
                date, type, MoneyUtil.format(amount), MoneyUtil.format(balanceAfter));
    }
}

// Abstract Account
abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    protected final String accountNumber;
    protected final String holderName;
    protected double balance;
    protected final List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, String holderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = initialBalance;
        transactions.add(new Transaction("Account Open", initialBalance, balance));
    }

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive!");
        balance += amount;
        transactions.add(new Transaction("Deposit", amount, balance));
    }

    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdraw must be positive!");
        if (amount > balance) throw new IllegalArgumentException("Insufficient balance!");
        balance -= amount;
        transactions.add(new Transaction("Withdraw", amount, balance));
    }

    public void printTransactionHistory() {
        System.out.println("\n📜 Transaction History - " + holderName + " (" + accountNumber + ")");
        System.out.println("--------------------------------------------------------------");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    public abstract void applyMonthlyInterest();

    @Override
    public String toString() {
        return String.format("%-10s | %-15s | Balance: %s", accountNumber, holderName, MoneyUtil.format(balance));
    }
}

// SavingsAccount with monthly interest
class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    private final double interestRate;

    public SavingsAccount(String accNo, String holder, double balance, double interestRate) {
        super(accNo, holder, balance);
        this.interestRate = interestRate;
    }

    @Override
    public void applyMonthlyInterest() {
        double interest = balance * (interestRate / 100);
        balance += interest;
        transactions.add(new Transaction("Interest", interest, balance));
    }
}

// CurrentAccount with overdraft feature
class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;
    private final double overdraftLimit;

    public CurrentAccount(String accNo, String holder, double balance, double overdraftLimit) {
        super(accNo, holder, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdraw must be positive!");
        if (amount > balance + overdraftLimit)
            throw new IllegalArgumentException("Overdraft limit exceeded!");
        balance -= amount;
        transactions.add(new Transaction("Withdraw", amount, balance));
    }

    @Override
    public void applyMonthlyInterest() {
        // No interest on current accounts
    }
}

// Bank class - manages accounts + persistence
class Bank {
    private Map<String, Account> accounts = new HashMap<>();
    private static final String DATA_FILE = "bank_data.ser";
    private static int accountCounter = 1000;

    // Load saved accounts
    @SuppressWarnings("unchecked")
    public Bank() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            accounts = (HashMap<String, Account>) ois.readObject();
            System.out.println("✅ Bank data loaded.");
        } catch (Exception e) {
            System.out.println("⚠ No previous data found. Starting fresh.");
        }
    }

    // Save accounts to file
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(accounts);
            System.out.println("💾 Data saved successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }

    private String generateAccountNumber() {
        return "ACC" + (++accountCounter);
    }

    public void createSavingsAccount(String name, double balance, double interestRate) {
        String accNo = generateAccountNumber();
        accounts.put(accNo, new SavingsAccount(accNo, name, balance, interestRate));
        System.out.println("✅ Savings Account Created: " + accNo);
    }

    public void createCurrentAccount(String name, double balance, double overdraft) {
        String accNo = generateAccountNumber();
        accounts.put(accNo, new CurrentAccount(accNo, name, balance, overdraft));
        System.out.println("✅ Current Account Created: " + accNo);
    }

    public Account getAccount(String accNo) { return accounts.get(accNo); }

    public void showAllAccounts() {
        System.out.println("\n🏦 Bank Accounts:");
        System.out.println("--------------------------------------------------------------");
        for (Account acc : accounts.values()) {
            System.out.println(acc);
        }
    }

    public void applyInterestToAllSavings() {
        for (Account acc : accounts.values()) {
            acc.applyMonthlyInterest();
        }
        System.out.println("✅ Interest applied to all savings accounts.");
    }
    public boolean deleteAccount(String accNo) {
        Account removed = accounts.remove(accNo);
        if (removed != null) {
            System.out.println("🗑 Account " + accNo + " (" + removed.getHolderName() + ") deleted successfully.");
            return true;
        } else {
            System.out.println("❌ Account not found!");
            return false;
        }
    }

}

// Main CLI System
public class BankSystem{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();

        try {
            while (true) {
                System.out.println("\n================= BANK MENU =================");
                System.out.println("1. Open New Account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Check Balance");
                System.out.println("5. Transaction History");
                System.out.println("6. Apply Interest to Savings");
                System.out.println("7. Show All Accounts");
                System.out.println("8. Delete an Account");

                System.out.println("0. Exit");
                System.out.println("============================================");
                System.out.print("👉 Enter choice: ");

                int choice = sc.nextInt();
                sc.nextLine();


                try {
                    switch (choice) {
                        case 1 -> {
                            System.out.print("How many accounts do you want to create? ");
                            int n = sc.nextInt();
                            sc.nextLine(); // consume newline

                            for (int i = 1; i <= n; i++) {
                                System.out.println("\n👤 Enter details for User " + i + ":");
                                System.out.print("Holder Name: ");
                                String name = sc.nextLine();
                                System.out.print("Initial Balance: ");
                                double bal = sc.nextDouble();
                                sc.nextLine();

                                System.out.print("Type (S for Savings / C for Current): ");
                                String type = sc.nextLine().trim().toUpperCase();

                                if (type.equals("S")) {
                                    System.out.print("Interest Rate (%): ");
                                    double rate = sc.nextDouble();
                                    sc.nextLine();
                                    bank.createSavingsAccount(name, bal, rate);
                                } else if (type.equals("C")) {
                                    System.out.print("Overdraft Limit: ");
                                    double od = sc.nextDouble();
                                    sc.nextLine();
                                    bank.createCurrentAccount(name, bal, od);
                                } else {
                                    System.out.println("❌ Invalid account type! Skipping this user.");
                                }
                            }
                            System.out.println("✅ All accounts created successfully.");
                        }


                            case 2 -> {
                            System.out.print("Account Number: ");
                            String accNo = sc.nextLine();
                            Account acc = bank.getAccount(accNo);
                            if (acc != null) {
                                System.out.print("Deposit Amount: ");
                                double amt = sc.nextDouble();
                                acc.deposit(amt);
                                System.out.println("✅ Deposit successful. New Balance: " + MoneyUtil.format(acc.getBalance()));
                            } else System.out.println("❌ Account not found!");
                        }
                        case 3 -> {
                            System.out.print("Account Number: ");
                            String accNo = sc.nextLine();
                            Account acc = bank.getAccount(accNo);
                            if (acc != null) {
                                System.out.print("Withdraw Amount: ");
                                double amt = sc.nextDouble();
                                acc.withdraw(amt);
                                System.out.println("✅ Withdrawal successful. New Balance: " + MoneyUtil.format(acc.getBalance()));
                            } else System.out.println("❌ Account not found!");
                        }
                        case 4 -> {
                            System.out.print("Account Number: ");
                            String accNo = sc.nextLine();
                            Account acc = bank.getAccount(accNo);
                            if (acc != null)
                                System.out.println("💰 Balance: " + MoneyUtil.format(acc.getBalance()));
                            else System.out.println("❌ Account not found!");
                        }
                        case 5 -> {
                            System.out.print("Account Number: ");
                            String accNo = sc.nextLine();
                            Account acc = bank.getAccount(accNo);
                            if (acc != null) acc.printTransactionHistory();
                            else System.out.println("❌ Account not found!");
                        }
                        case 6 -> bank.applyInterestToAllSavings();
                        case 7 -> bank.showAllAccounts();
                        case 8 -> {
                            System.out.print("Enter Account Number to delete: ");
                            String accNo = sc.nextLine();
                            bank.deleteAccount(accNo);
                        }

                        case 0 -> {
                            bank.saveData();
                            System.out.println("👋 Goodbye! Thank you for using our bank.");
                            return;
                        }
                        default -> System.out.println("❌ Invalid choice!");
                    }
                } catch (Exception e) {
                    System.out.println("⚠ Error: " + e.getMessage());
                }
            }
        } finally {
            bank.saveData();
            sc.close();
        }
    }
}
