package auebprogramming;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class ExpenseDisplay {

    static final class ExpenseCategory {
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

    public static void main(String[] args) {
        // ✅ Load output.txt from src/main/resources
        List<String> lines = readResourceFile("output.txt");
        if (lines == null) return;

        List<ExpenseCategory> generalExpenses = new ArrayList<>();
        boolean readingGeneral = false;

        for (String line : lines) {

            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("ΕΞΟΔΑ")) {
                readingGeneral = true;
                continue;
            }

            if (readingGeneral) {

                if (line.startsWith("Σύνολο")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        long total = parseAmount(parts[1].trim());
                        generalExpenses.add(new ExpenseCategory("", "Σύνολο", total));
                    }
                    readingGeneral = false;
                    continue;
                }

                ExpenseCategory cat = parseExpenseCategory(line);
                if (cat != null) generalExpenses.add(cat);
            }
        }

        System.out.println("==== ΕΞΟΔΑ ΚΡΑΤΙΚΟΥ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ====");
        for (ExpenseCategory c : generalExpenses) {
            System.out.println(c);
        }
    }

    // ✅ NEW METHOD — reads from resources/
    private static List<String> readResourceFile(String filename) {
        try {
            InputStream inputStream = ExpenseDisplay.class
                .getClassLoader()
                .getResourceAsStream(filename);

            if (inputStream == null) {
                System.err.println("Resource not found: " + filename);
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader.lines().toList();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ExpenseCategory parseExpenseCategory(String line) {
        line = line.trim();
        if (line.isEmpty()) return null;

        java.util.regex.Pattern pattern =
            java.util.regex.Pattern.compile("^(\\d+)\\s+(.*?)\\s+([\\d.,]+)$");
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

    private static long parseAmount(String str) {
        try {
            str = str.replace(".", "").replace(",", "");
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
