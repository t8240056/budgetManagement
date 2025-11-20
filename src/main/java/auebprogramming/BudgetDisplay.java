package auebprogramming;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    // Χωρίζουμε την γραμμή με βάση το ":" (π.χ. "21 Παροχές: 34,741,365,000")
    String[] mainParts = line.split(":");
    if (mainParts.length < 2) return null;

    String left = mainParts[0].trim();      // "21 Παροχές"
    String amountStr = mainParts[1].trim(); // "34,741,365,000"

    // Καθαρίζουμε το ποσό από τελείες και κόμματα
    amountStr = amountStr.replace(".", "").replace(",", "");

    long amount = parseAmount(amountStr);

    // Χωρίζουμε το αριστερό κομμάτι σε 2 μέρη: κωδικός + όνομα
    String[] leftParts = left.split("\\s+", 2);
    if (leftParts.length < 2) return null;

    String code = leftParts[0];      // 21
    String name = leftParts[1];      // Παροχές

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
