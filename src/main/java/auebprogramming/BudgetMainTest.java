package auebprogramming;

import java.util.Scanner;

public class BudgetMainTest {
    
    /**
     * Βοηθητική μέθοδος για να εκτυπώνουμε τους πίνακες που επιστρέφει ο BudgetAnalyzer.
     * @param data Ο πίνακας String[][] προς εκτύπωση.
     */
    private static void printTable(String[][] data, String title, int startRow) {
        if (data == null || data.length <= startRow) {
            System.out.println("\n--- " + title + " (ΔΕΝ ΒΡΕΘΗΚΑΝ ΔΕΔΟΜΕΝΑ) ---");
            return;
        }

        System.out.println("\n--- " + title + " ---");
        
        // Εκτύπωση Κεφαλίδας (Η πρώτη γραμμή του πίνακα)
        System.out.print("| ");
        for (String cell : data[startRow]) {
            System.out.printf("%-20s | ", cell.replace("\"", ""));
        }
        System.out.println();
        
        // Εκτύπωση Δεδομένων (Από την επόμενη γραμμή)
        for (int i = startRow + 1; i < data.length; i++) {
            System.out.print("| ");
            for (String cell : data[i]) {
                System.out.printf("%-20s | ", cell.replace("\"", ""));
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }

    public static void main(String[] args) {
        
        BudgetAnalyzer analyzer = new BudgetAnalyzer();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Εφαρμογή Κρατικού Προϋπολογισμού - ΔΟΚΙΜΗ GUI LOGIC");
        
        // 1. Δοκιμή: getArticle2Data() - Εμφάνιση του Άρθρου 2
        String[][] article2Data = analyzer.getArticle2Data();
        // Το Άρθρο 2 έχει την κεφαλίδα στην index 0
        printTable(article2Data, "ΑΠΟΤΕΛΕΣΜΑ: ΣΥΝΟΠΤΙΚΑ (Άρθρο 2)", 0);
        
        // 2. Είσοδος Κωδικού Φορέα
        System.out.print("\n>>> Εισάγετε κωδικό για αναλυτική προβολή (π.χ. 1032, 4101): ");
        
        if (scanner.hasNextInt()) {
            int code = scanner.nextInt();
            
            // 3. Δοκιμή: getDetailedBudget(code) - Φόρτωση Ανάλυσης
            String[][] detailedData = analyzer.getDetailedBudget(code);

            // Το Αναλυτικό CSV έχει την κεφαλίδα στην index 3
            printTable(detailedData, "ΑΠΟΤΕΛΕΣΜΑ: ΑΝΑΛΥΣΗ ΦΟΡΕΑ " + code, 3);
        } else {
            System.out.println("Άκυρη είσοδος.");
        }
        
        scanner.close();
    }
}