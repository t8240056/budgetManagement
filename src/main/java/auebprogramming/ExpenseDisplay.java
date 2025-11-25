package auebprogramming;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ExpenseDisplay reads CSV files containing state budget expenses
 * and displays them categorized by category and by ministry/agency.
 * 
 * Currently only supports the year 2025.
 */
public final class ExpenseDisplay {

    // ---------------------------
    // INNER CLASSES
    // ---------------------------

    /** Represents an expense category */
    static final class ExpenseCategory {
        String code;
        String description;
        long amount;

        ExpenseCategory(String code, String description, long amount) {
            this.code = code;
            this.description = description;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return code + " - " + description + ": " + String.format("%,d €", amount);
        }
    }

    /** Represents a ministry/agency expense */
    static final class MinistryExpense {
        String code;
        String ministry;
        long regularBudget;
        long investmentBudget;
        long total;

        MinistryExpense(String code, String ministry, long regularBudget, long investmentBudget, long total) {
            this.code = code;
            this.ministry = ministry;
            this.regularBudget = regularBudget;
            this.investmentBudget = investmentBudget;
            this.total = total;
        }

        @Override
        public String toString() {
            return code + " - " + ministry +
                    " | Regular: " + String.format("%,d €", regularBudget) +
                    " | Investment: " + String.format("%,d €", investmentBudget) +
                    " | Total: " + String.format("%,d €", total);
        }
    }

    // ---------------------------
    // MAIN METHOD
    // ---------------------------
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Which year do you want to see?");
        int year = scanner.nextInt();
        scanner.close();

        if (year != 2025) {
            System.out.println("Only the year 2025 is currently available.");
            return;
        }

        // Direct path to CSV files inside java folder
        String basePath = "src/main/java/auebprogramming/resources/";
        String categoriesFile = basePath + "expense_categories_2025.csv";
        String ministriesFile = basePath + "expense_ministries_2025.csv";

        // Read CSV files
        List<ExpenseCategory> categories = readCategoriesCSV(categoriesFile);
        List<MinistryExpense> ministries = readMinistriesCSV(ministriesFile);

        // Display expenses by category
        System.out.println("\n==== EXPENSES BY CATEGORY ====");
        categories.forEach(System.out::println);

        // Display expenses by ministry/agency
        System.out.println("\n==== EXPENSES BY MINISTRY/AGENCY ====");
        ministries.forEach(System.out::println);
    }

    // ---------------------------
    // CSV READERS
    // ---------------------------

    /**
     * Reads expense categories CSV and returns a list of ExpenseCategory objects.
     */
    private static List<ExpenseCategory> readCategoriesCSV(String path) {
        List<ExpenseCategory> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length < 4) continue;

                String code = parts[1].trim();
                String description = parts[2].trim();
                long amount = Long.parseLong(parts[3].trim());

                list.add(new ExpenseCategory(code, description, amount));
            }

        } catch (Exception e) {
            System.err.println("Error reading categories CSV: " + path);
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Reads ministries/agencies CSV and returns a list of MinistryExpense objects.
     */
    private static List<MinistryExpense> readMinistriesCSV(String path) {
        List<MinistryExpense> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length < 5) continue;

                String code = parts[0].trim();
                String ministry = parts[1].trim();
                long regularBudget = Long.parseLong(parts[2].trim());
                long investmentBudget = Long.parseLong(parts[3].trim());
                long total = Long.parseLong(parts[4].trim());

                list.add(new MinistryExpense(code, ministry, regularBudget, investmentBudget, total));
            }

        } catch (Exception e) {
            System.err.println("Error reading ministries CSV: " + path);
            e.printStackTrace();
        }

        return list;
    }
}
