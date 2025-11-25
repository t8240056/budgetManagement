package auebprogramming;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ExpenseDisplay reads CSV files containing state budget expenses
 * and displays them categorized by category and by agency.
 * 
 * NOTE: Currently works only for the year 2025.
 */
public final class ExpenseDisplay {

    // ---------------------------
    // INNER CLASSES
    // ---------------------------

    /** Represents an expense category */
    static final class ExpenseCategory {
        String category;
        String code;
        String description;
        long amount;

        ExpenseCategory(String category, String code, String description, long amount) {
            this.category = category;
            this.code = code;
            this.description = description;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return code + " - " + description + ": " + String.format("%,d €", amount);
        }
    }

    /** Represents an agency expense */
    static final class AgencyExpense {
        String code;
        String agency;
        long regular;
        long investment;
        long total;

        AgencyExpense(String code, String agency, long regular, long investment, long total) {
            this.code = code;
            this.agency = agency;
            this.regular = regular;
            this.investment = investment;
            this.total = total;
        }

        @Override
        public String toString() {
            return code + " - " + agency +
                    " | Regular: " + String.format("%,d €", regular) +
                    " | Investment: " + String.format("%,d €", investment) +
                    " | Total: " + String.format("%,d €", total);
        }
    }

    // ---------------------------
    // MAIN
    // ---------------------------
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Which year do you want to see?");
        int year = scanner.nextInt();
        scanner.close();

        // Only 2025 is available for now
        if (year != 2025) {
            System.out.println("Only the year 2025 is available at the moment.");
            return;
        }

        // Direct path to CSV files inside java folder
        String basePath = "src/main/java/auebprogramming/resources/";
        String categoriesFile = basePath + "categories2025.csv";
        String agenciesFile = basePath + "agencies2025.csv";

        // Read CSV files
        List<ExpenseCategory> categories = readCategoriesCSV(categoriesFile);
        List<AgencyExpense> agencies = readAgenciesCSV(agenciesFile);

        // Display results
        System.out.println("\n==== EXPENSES BY CATEGORY ====");
        categories.forEach(System.out::println);

        System.out.println("\n==== EXPENSES BY AGENCY ====");
        agencies.forEach(System.out::println);
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
            br.readLine(); // skip header line

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length < 4) continue;

                String category = parts[0].trim();
                String code = parts[1].trim();
                String description = parts[2].trim();
                long amount = Long.parseLong(parts[3].trim());

                list.add(new ExpenseCategory(category, code, description, amount));
            }

        } catch (Exception e) {
            System.err.println("Error reading categories CSV: " + path);
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Reads agency expenses CSV and returns a list of AgencyExpense objects.
     */
    private static List<AgencyExpense> readAgenciesCSV(String path) {
        List<AgencyExpense> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // skip header line

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length < 5) continue;

                String code = parts[0].trim();
                String agency = parts[1].trim();
                long regular = Long.parseLong(parts[2].trim());
                long investment = Long.parseLong(parts[3].trim());
                long total = Long.parseLong(parts[4].trim());

                list.add(new AgencyExpense(code, agency, regular, investment, total));
            }

        } catch (Exception e) {
            System.err.println("Error reading agencies CSV: " + path);
            e.printStackTrace();
        }

        return list;
    }
}
