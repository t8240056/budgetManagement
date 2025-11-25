package auebprogramming;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Final class because it is not intended to be inherited
public final class ExpenseDisplay {

    // Final static inner class that will not be inherited
    static final class ExpenseCategory {
        String code;
        String name;
        long amount;

        // Constructor: Creates an expense category object
        ExpenseCategory(String code, String name, long amount) {
            this.code = code;
            this.name = name;
            this.amount = amount;
        }

        // toString: Returns formatted string representation of a category
        @Override
        public String toString() {
            return code + " " + name + ": " + String.format("%,d", amount);
        }
    }

    // main: Entry point of the program that reads file and prints parsed expenses
    public static void main(String[] args) {
        List<String> lines = readFile("../../../target/classes/output.txt");
        if (lines == null) return;

        List<ExpenseCategory> generalExpenses = new ArrayList<>();

        boolean readingGeneral = false;

        for (String line : lines) {

            line = line.trim();
            if (line.isEmpty()) continue;

            // Detect the start of expenses section
            if (line.startsWith("ΕΞΟΔΑ")) {
                readingGeneral = true;
                continue;
            }

            if (readingGeneral) {

                // Handle line with total
                if (line.startsWith("Σύνολο")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        long total = parseAmount(parts[1].trim());
                        generalExpenses.add(new ExpenseCategory("Σύνολο", "", total));
                    }
                    readingGeneral = false;
                    continue;
                }

                // Try to parse an expense category
                ExpenseCategory cat = parseExpenseCategory(line);
                if (cat != null) generalExpenses.add(cat);
            }
        }

        // Display only expenses
        System.out.println("==== ΕΞΟΔΑ ΚΡΑΤΙΚΟΥ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ====");
        for (ExpenseCategory c : generalExpenses) {
            System.out.println(c);
        }
    }

    // readFile: Reads all lines of a text file and returns them as a list
    private static List<String> readFile(String path) {
        try {
            return java.nio.file.Files.readAllLines(java.nio.file.Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // parseExpenseCategory: Parses a line into an ExpenseCategory object
    private static ExpenseCategory parseExpenseCategory(String line) {
        line = line.trim();
        if (line.isEmpty()) return null;

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^(\\d+)\\s+(.*?)\\s+([\\d.,]+)$");
        java.util.regex.Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            String code = matcher.group(1);
            String name = matcher.group(2);
            String amountStr = matcher.group(3);
            long amount = parseAmount(amountStr);
            return new ExpenseCategory(code, name, amount);
        }

        return null;
    }

    // parseAmount: Converts formatted number text to a long value
    private static long parseAmount(String str) {
        try {
            str = str.replace(".", "").replace(",", "");
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
