package auebprogramming;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDisplay {

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

    public static void main(String[] args) {
        List<String> lines = readFile("../../../output.txt");
        if (lines == null) return;

        List<ExpenseCategory> generalExpenses = new ArrayList<>();

        boolean readingGeneral = false;

        for (String line : lines) {

            line = line.trim();
            if (line.isEmpty()) continue;

            // Ξεκινάμε να διαβάζουμε τα "ΕΞΟΔΑ"
            if (line.startsWith("ΕΞΟΔΑ")) {
                readingGeneral = true;
                continue;
            }

            if (readingGeneral) {

                // Στο PDF εμφανίζεται γραμμή σύνολο
                if (line.startsWith("Σύνολο")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        long total = parseAmount(parts[1].trim());
                        generalExpenses.add(new ExpenseCategory("Σύνολο", "Σύνολο", total));
                    }
                    readingGeneral = false; // τελειώνει η ενότητα
                    continue;
                }

                // Προσπάθεια να κάνουμε parse μία κατηγορία εξόδων
                ExpenseCategory cat = parseExpenseCategory(line);
                if (cat != null) generalExpenses.add(cat);
            }
        }

        // ==== ΕΜΦΑΝΙΣΗ ΜΟΝΟ ΕΞΟΔΩΝ ====
        System.out.println("==== ΕΞΟΔΑ ΚΡΑΤΙΚΟΥ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ====");
        for (ExpenseCategory c : generalExpenses) {
            System.out.println(c);
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
        try {
            // Μορφή:   Κωδικός    Όνομα    Ποσό
            String[] parts = line.split("\\s+", 3);
            if (parts.length < 3) return null;

            String code = parts[0];
            String name = parts[1];
            String amountStr = parts[2];

            long amount = parseAmount(amountStr);
            return new ExpenseCategory(code, name, amount);

        } catch (Exception e) {
            return null;
        }
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
