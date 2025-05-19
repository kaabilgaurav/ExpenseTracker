import java.io.*;
import java.util.*;

public class ExpenseTracker {
    static double income = 0.0;
    static ArrayList<Expense> expenses = new ArrayList<>();
    static final String FILE_NAME = "expenses.txt";

    public static void main(String[] args) {
        loadFromFile();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Enter Monthly Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Summary Report");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> enterIncome(sc);
                case 2 -> addExpense(sc);
                case 3 -> showSummary();
                case 4 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 4);

        saveToFile();
        sc.close();
    }

    static void enterIncome(Scanner sc) {
        System.out.print("Enter monthly income: ₹");
        income = sc.nextDouble();
        System.out.println("Income recorded.");
    }

    static void addExpense(Scanner sc) {
        sc.nextLine(); // consume newline
        System.out.print("Enter expense category: ");
        String category = sc.nextLine();
        System.out.print("Enter amount: ₹");
        double amount = sc.nextDouble();

        expenses.add(new Expense(category, amount));
        System.out.println("Expense added.");
    }

    static void showSummary() {
        double totalExpenses = 0.0;
        HashMap<String, Double> categoryMap = new HashMap<>();

        for (Expense e : expenses) {
            totalExpenses += e.getAmount();
            categoryMap.put(e.getCategory(),
                categoryMap.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
        }

        System.out.println("\n--- Summary Report ---");
        System.out.printf("Total Income: ₹%.2f\n", income);
        System.out.printf("Total Expenses: ₹%.2f\n", totalExpenses);
        System.out.printf("Net Savings: ₹%.2f\n", income - totalExpenses);
        System.out.println("\nExpenses by Category:");
        for (String cat : categoryMap.keySet()) {
            System.out.printf("- %s: ₹%.2f\n", cat, categoryMap.get(cat));
        }
    }

    @SuppressWarnings("unchecked")
    static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            income = in.readDouble();
            expenses = (ArrayList<Expense>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading saved data.");
        }
    }

    static void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeDouble(income);
            out.writeObject(expenses);
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }
}
