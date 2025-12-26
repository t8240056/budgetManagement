package auebprogramming;

import java.util.Scanner;

/**
 * Main class for testing the Budget application functionality.
 * Demonstrates exception handling and data retrieval.
 */
public final class BudgetMainTest {

    /**
     * Private constructor to prevent instantiation.
     * Utility classes should not have a public or default constructor.
     */
    private BudgetMainTest() {
        // Empty constructor
    }

    /**
     * Βοηθητική μέθοδος για να εκτυπώνουμε τους πίνακες που επιστρέφει
     * ο BudgetAnalyzer.
     *
     * @param data     Ο πίνακας String[][] προς εκτύπωση.
     * @param title    Ο τίτλος του πίνακα.
     * @param startRow Η γραμμή από την οποία ξεκινά η εκτύπωση.
     */
    private static void printTable(final String[][] data,
                                   final String title,
                                   final int startRow) {
        if (data == null || data.length <= startRow) {
            System.out.println("\n--- " + title
                    + " (ΔΕΝ ΒΡΕΘΗΚΑΝ ΔΕΔΟΜΕΝΑ) ---");
            return;
        }

        System.out.println("\n--- " + title + " ---");

        // Εκτύπωση Κεφαλίδας (Η πρώτη γραμμή του πίνακα)
        System.out.print("| ");
        for (final String cell : data[startRow]) {
            System.out.printf("%-20s | ", cell.replace("\"", ""));
        }
        System.out.println();

        // Εκτύπωση Δεδομένων (Από την επόμενη γραμμή)
        for (int i = startRow + 1; i < data.length; i++) {
            System.out.print("| ");
            for (final String cell : data[i]) {
                System.out.printf("%-20s | ", cell.replace("\"", ""));
            }
            System.out.println();
        }
        System.out.println("----------------------------------------"
                + "-------------------------------------------------");
    }

    /**
     * Main entry point for the test application.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {

        final BudgetAnalyzer analyzer = new BudgetAnalyzer();
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Εφαρμογή Κρατικού Προϋπολογισμού - "
                + "ΔΟΚΙΜΗ EXCEPTIONS");

        // 1. Δοκιμή: getArticle2Data() - Εμφάνιση του Άρθρου 2
        final String[][] article2Data = analyzer.getArticle2Data();
        printTable(article2Data, "ΑΠΟΤΕΛΕΣΜΑ: ΣΥΝΟΠΤΙΚΑ (Άρθρο 2)", 0);

        // 2. Είσοδος Κωδικού Φορέα
        System.out.print("\n>>> Εισάγετε κωδικό για ανάλυση "
                + "(π.χ. 1032, 9999, abc): ");

        final String input = scanner.nextLine();

        try {
            // 3. Έλεγχος & Εκτέλεση
            final int code;

            // Έλεγχος για άδεια εισαγωγή
            if (input == null || input.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Παρακαλώ εισάγετε έναν κωδικό φορέα.");
            }

            // Μετατροπή σε int (εδώ πιάνεται το NumberFormatException)
            code = Integer.parseInt(input.trim());

            // Η κλήση της μεθόδου.
            final String[][] detailedData = analyzer.getDetailedBudget(code);

            // 4. Εμφάνιση αποτελέσματος (αν η κλήση πετύχει)
            printTable(detailedData, "ΑΠΟΤΕΛΕΣΜΑ: ΑΝΑΛΥΣΗ ΦΟΡΕΑ " + code, 3);

        } catch (final NumberFormatException e) {
            // Πιάνουμε το λάθος αν ο χρήστης έβαλε "abc"
            System.out.println("\n[ΣΦΑΛΜΑ ΕΙΣΟΔΟΥ] Μη έγκυρη εισαγωγή: "
                    + "Παρακαλώ εισάγετε μόνο αριθμούς.");
        } catch (final IllegalArgumentException e) {
            // Πιάνουμε το λάθος που πέταξε η BudgetAnalyzer
            System.out.println("\n[ΣΦΑΛΜΑ ΛΟΓΙΚΗΣ] " + e.getMessage());
        }

        scanner.close();
    }
}
