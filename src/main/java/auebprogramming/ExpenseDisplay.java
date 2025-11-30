package auebprogramming;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * ExpenseDisplay reads CSV files containing state budget expenses
 * and provides methods to get expenses by category and by ministry/agency.
 */
public final class ExpenseDisplay {

    // ---------------------------
    // INNER CLASSES
    // ---------------------------

    /** Represents an expense category */
    public static final class ExpenseCategory {
        public String code;
        public String description;
        public long amount;

        public ExpenseCategory(String code, String description, long amount) {
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
    public static final class MinistryExpense {
        public String code;
        public String ministry;
        public long regularBudget;
        public long investmentBudget;
        public long total;

        public MinistryExpense(String code, String ministry, long regularBudget, long investmentBudget, long total) {
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
    // PUBLIC METHODS
    // ---------------------------

    /**
     * Reads expense categories CSV and returns a list of ExpenseCategory objects.
     */
    public List<ExpenseCategory> readCategoriesCSV(String path) {
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
    public List<MinistryExpense> readMinistriesCSV(String path) {
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
