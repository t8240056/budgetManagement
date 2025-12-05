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
        
        System.out.println("Εφαρμογή Κρατικού Προϋπολογισμού - ΔΟΚΙΜΗ EXCEPTIONS");
        
        // 1. Δοκιμή: getArticle2Data() - Εμφάνιση του Άρθρου 2
        String[][] article2Data = analyzer.getArticle2Data();
        printTable(article2Data, "ΑΠΟΤΕΛΕΣΜΑ: ΣΥΝΟΠΤΙΚΑ (Άρθρο 2)", 0);
        
        // 2. Είσοδος Κωδικού Φορέα
        System.out.print("\n>>> Εισάγετε κωδικό για ανάλυση (π.χ. 1032, 9999, abc): ");
        
        String input = scanner.nextLine(); // Παίρνουμε όλη τη γραμμή ως String
        
        try {
            // 3. Έλεγχος & Εκτέλεση
            int code;
            
            // Έλεγχος για άδεια εισαγωγή
            if (input == null || input.trim().isEmpty()) {
                throw new IllegalArgumentException("Παρακαλώ εισάγετε έναν κωδικό φορέα.");
            }
            
            // Μετατροπή σε int (εδώ πιάνεται το NumberFormatException)
            code = Integer.parseInt(input.trim()); 
            
            // Η κλήση της δικής σου μεθόδου. Εδώ μπορεί να πεταχτεί η IllegalArgumentException 
            // αν ο κωδικός (π.χ. 5000) δεν βρεθεί στο Άρθρο 2.
            String[][] detailedData = analyzer.getDetailedBudget(code);

            // 4. Εμφάνιση αποτελέσματος (αν η κλήση πετύχει)
            printTable(detailedData, "ΑΠΟΤΕΛΕΣΜΑ: ΑΝΑΛΥΣΗ ΦΟΡΕΑ " + code, 3);
            
        } catch (NumberFormatException e) {
            // Πιάνουμε το λάθος αν ο χρήστης έβαλε "abc"
            System.out.println("\n[ΣΦΑΛΜΑ ΕΙΣΟΔΟΥ] Μη έγκυρη εισαγωγή: Παρακαλώ εισάγετε μόνο αριθμούς.");
        } catch (IllegalArgumentException e) {
            // Πιάνουμε το λάθος που πέταξε η BudgetAnalyzer (π.χ. 5000)
            System.out.println("\n[ΣΦΑΛΜΑ ΛΟΓΙΚΗΣ] " + e.getMessage());
        }
        
        scanner.close();
    }
}