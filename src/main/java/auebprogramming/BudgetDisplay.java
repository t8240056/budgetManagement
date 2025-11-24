package auebprogramming;

import java.io.*;
import java.util.*;

public class BudgetDisplay {

    static class ExpenseCategory {
        String code;
        String name;
        long amount;

        ExpenseCategory(String code, String name, long amount) {
            this.code = code;
            this.name = name;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return code + " " + name + ": " + String.format("%,d", amount);
        }
    }

    static class Ministry {
        String name;
        long total;
        List<ExpenseCategory> categories = new ArrayList<>();

        Ministry(String name) {
            this.name = name;
        }

        void addCategory(ExpenseCategory c) {
            categories.add(c);
        }

        void setTotal(long total) {
            this.total = total;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("==== ").append(name).append(" ====\n");
            for (ExpenseCategory c : categories) {
                sb.append(c).append("\n");
            }
            sb.append("Σύνολο: ").append(String.format("%,d", total)).append("\n");
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        List<String> lines = readFile("src/main/java/auebprogramming/resources/output.txt");

        if (lines == null) return;

        List<ExpenseCategory> generalExpenses = new ArrayList<>();
        List<Ministry> ministries = new ArrayList<>();

        Ministry currentMinistry = null;
        boolean readingGeneral = false;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Γενικά έξοδα
            if (line.startsWith("ΕΞΟΔΑ")) {
                readingGeneral = true;
                continue;
            }

            if (readingGeneral) {
                if (line.startsWith("Σύνολο")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        long total = parseAmount(parts[1].trim());
                        generalExpenses.add(new ExpenseCategory("Σύνολο", "Σύνολο", total));
                    }
                    readingGeneral = false;
                } else {
                    // Κωδικός + Όνομα + Ποσό
                    ExpenseCategory cat = parseExpenseCategory(line);
                    if (cat != null) generalExpenses.add(cat);
                }
            }

            // Υπουργεία
            if (line.startsWith("ΥΠΟΥΡΓΕΙΟ")) {
                currentMinistry = new Ministry(line);
                ministries.add(currentMinistry);
                continue;
            }

            if (currentMinistry != null) {
                if (line.startsWith("Σύνολο")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        currentMinistry.setTotal(parseAmount(parts[1].trim()));
                    }
                } else {
                    ExpenseCategory cat = parseExpenseCategory(line);
                    if (cat != null) currentMinistry.addCategory(cat);
                }
            }
        }

        // Εμφάνιση
        System.out.println("==== ΓΕΝΙΚΑ ΕΞΟΔΑ ====");
        for (ExpenseCategory c : generalExpenses) {
            System.out.println(c);
        }
        System.out.println();

        for (Ministry m : ministries) {
            System.out.println(m);
        }
    }

    private static List<String> readFile(String path) {
        try {
            return java.nio.file.Files.readAllLines(java.nio.file.Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ExpenseCategory parseExpenseCategory(String line) {
        String[] parts = line.split("\\s+", 3);
        if (parts.length < 3) return null;
        String code = parts[0];
        String name = parts[1];
        String amountStr = parts[2].replace(".", "").replace(",", "");
        long amount = parseAmount(amountStr);
        return new ExpenseCategory(code, name, amount);
    }

    private static long parseAmount(String str) {
        try {
            str = str.replace(".", "").replace(",", "");
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
