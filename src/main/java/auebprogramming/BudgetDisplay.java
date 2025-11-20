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
    // Αφαιρούμε περιττά κενά
    line = line.trim();
    if (line.isEmpty()) return null;

    // Regex: πρώτα 1-4 ψηφία για κωδικό, μετά όνομα (ό,τι μένει), στο τέλος ποσό
    // Ποσό: μόνο ψηφία και κόμματα/τελείες
    // Παράδειγμα γραμμής: "21 Παροχές σε εργαζομένους 14.889.199.000"
    // Pattern: (\d+)\s+(.*)\s+([\d\.,]+)
    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^(\\d+)\\s+(.*?)\\s+([\\d.,]+)$");
    java.util.regex.Matcher matcher = pattern.matcher(line);
    if (matcher.find()) {
        String code = matcher.group(1);
        String name = matcher.group(2);
        String amountStr = matcher.group(3);
        long amount = parseAmount(amountStr);
        return new ExpenseCategory(code, name, amount);
    }

    return null; // αν δεν ταιριάζει, επιστρέφουμε null
}

private static long parseAmount(String str) {
    try {
        // Αφαιρούμε τελείες και κόμματα
        str = str.replace(".", "").replace(",", "");
        return Long.parseLong(str);
    } catch (NumberFormatException e) {
        return 0;
    }
}

}
