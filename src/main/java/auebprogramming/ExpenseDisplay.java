package auebprogramming;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class ExpenseDisplay {

    // === INNER CLASSES ===
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

    static final class AgencyExpense {
        String code;
        String agencyName;
        long regularBudget;
        long investmentBudget;
        long total;

        AgencyExpense(String code, String agencyName, long regularBudget, long investmentBudget, long total) {
            this.code = code;
            this.agencyName = agencyName;
            this.regularBudget = regularBudget;
            this.investmentBudget = investmentBudget;
            this.total = total;
        }

        @Override
        public String toString() {
            return code + " - " + agencyName +
                    " | Τακτικός: " + String.format("%,d €", regularBudget) +
                    " | ΠΔΕ: " + String.format("%,d €", investmentBudget) +
                    " | Σύνολο: " + String.format("%,d €", total);
        }
    }

    // === MAIN ===
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(" Which year do you want to see ?");
        int year = scanner.nextInt();
        scanner.close();

        String basePath = "src/main/java/auebprogramming/resources/";

        String categoriesPath = basePath + "categories.csv";
        String agenciesPath = basePath + "agencies.csv";

        List<ExpenseCategory> categories = readCategoriesCSV(categoriesPath);
        List<AgencyExpense> agencies = readAgenciesCSV(agenciesPath);

        System.out.println("\n==== ΕΞΟΔΑ ΑΝΑ ΚΑΤΗΓΟΡΙΑ ====");
        categories.forEach(System.out::println);

        System.out.println("\n==== ΕΞΟΔΑ ΑΝΑ ΦΟΡΕΑ ====");
        agencies.forEach(System.out::println);
    }

    // === CSV PARSING ===

    private static List<ExpenseCategory> readCategoriesCSV(String path) {
        List<ExpenseCategory> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                // Σπάει σωστά το CSV, ακόμη κι αν έχει ελληνικά
                String[] parts = line.split(",", 4);

                String category = parts[0];
                String code = parts[1];
                String description = parts[2];
                long amount = Long.parseLong(parts[3]);

                list.add(new ExpenseCategory(category, code, description, amount));
            }

        } catch (IOException e) {
            System.err.println("Error reading categories CSV.");
            e.printStackTrace();
        }

        return list;
    }

    private static List<AgencyExpense> readAgenciesCSV(String path) {
        List<AgencyExpense> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {

                String[] p = line.split(",", 5);

                String code = p[0];
                String agency = p[1];
                long regular = Long.parseLong(p[2].trim());
                long investment = Long.parseLong(p[3].trim());
                long total = Long.parseLong(p[4].trim());

                list.add(new AgencyExpense(code, agency, regular, investment, total));
            }

        } catch (IOException e) {
            System.err.println("Error reading agencies CSV.");
            e.printStackTrace();
        }

        return list;
    }
}
