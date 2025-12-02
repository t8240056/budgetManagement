package auebprogramming;

import java.util.Scanner;

/**
 * Η κλάση BudgetAnalyzer διαχειρίζεται τη φόρτωση, αναζήτηση και εμφάνιση 
 * των δεδομένων του κρατικού προϋπολογισμού από τα CSV αρχεία.
 * Σχεδιάστηκε ώστε να επιτρέπει την μελλοντική τροποποίηση των δεδομένων.
 */
public class BudgetAnalyzer {

    // Πίνακας για τα δεδομένα του Άρθρου 2 (budget_ministries.csv)
    private String[][] article2Data; 
    
    // Πίνακας για τα αναλυτικά δεδομένα του επιλεγμένου φορέα (π.χ. 1032.csv)
    private String[][] detailedBudget;
    
    // Κρατάμε τον κωδικό που ζήτησε ο χρήστης
    private int selectedEntityCode;

    /**
     * Constructor της κλάσης. Φορτώνει αυτόματα τα συνοπτικά δεδομένα
     * του Άρθρου 2 στη μνήμη, μέσω της CsvToArray.
     */
    public BudgetAnalyzer() {
        // Καλούμε την CsvToArray για να φορτώσει το Άρθρο 2
        this.article2Data = CsvToArray.loadCsvToArray("budget_ministries.csv");
        
        if (article2Data.length < 2) {
            System.out.println("Προειδοποίηση: Το αρχείο budget_ministries.csv δεν φορτώθηκε σωστά.");
        } else {
            System.out.println("Επιτυχής φόρτωση συνοπτικών δεδομένων Άρθρου 2.");
        }
    }

    /**
     * Εμφανίζει τον πίνακα του Άρθρου 2 (budget_ministries.csv)
     * με τους κωδικούς φορέων, ονομασίες και συνολικά ποσά.
     * Εμφανίζει αυτούσιο το περιεχόμενο του φορτωμένου πίνακα.
     */
    public void displayArticle2() {
        if (article2Data.length < 2) {
            System.out.println("Δεν υπάρχουν δεδομένα για εμφάνιση.");
            return;
        }
        
        System.out.println("\n----------------- ΑΡΘΡΟ 2: ΣΥΝΟΛΙΚΑ ΕΞΟΔΑ ΦΟΡΕΩΝ -----------------");
        
        // Εκτύπωση της κεφαλίδας (γραμμή 0)
        String[] header = article2Data[0];
        String formatString = "%-10s | %-45s | %-20s | %-20s\n";
        System.out.printf(formatString, header[0], header[1], header[2], header[3]); 

        System.out.println("---------------------------------------------------------------------------------------------------------");
        
        // Εκτύπωση των δεδομένων (από γραμμή 1)
        for (int i = 1; i < article2Data.length; i++) {
            String[] row = article2Data[i];
            if (row.length >= 4) {
                 // Εκτυπώνουμε τα στοιχεία των 4 πρώτων στηλών
                 System.out.printf(formatString, row[0], row[1], row[2], row[3]);
            }
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }

    /**
     * Δέχεται τον κωδικό φορέα, ελέγχει την εγκυρότητά του,
     * φορτώνει το αντίστοιχο αναλυτικό CSV και το εμφανίζει.
     * @param code Ο τετραψήφιος κωδικός του φορέα που εισάγει ο χρήστης.
     */
    public void processEntityCode(int code) {
        this.selectedEntityCode = code;
        
        // 1. Ελέγχουμε αν ο κωδικός είναι έγκυρος
        if (isCodeValid(code)) {
            
            System.out.println("\n--- Φόρτωση αναλυτικών δεδομένων για τον κωδικό " + code + " ---");
            
            // 2. Η Λογική Αντιστοίχισης: Ο κωδικός γίνεται όνομα αρχείου (π.χ. 1032.csv)
            String filename = code + ".csv"; 
            
            // 3. Φόρτωσε το αναλυτικό CSV
            this.detailedBudget = CsvToArray.loadCsvToArray(filename);
            
            // 4. Εμφάνισε το αναλυτικό CSV
            displayDetailedBudget();
        } else {
            System.out.println("Σφάλμα: Ο κωδικός φορέα " + code + " δεν βρέθηκε στο Άρθρο 2.");
        }
    }

    /**
     * Ελέγχει αν ο δοθείς κωδικός φορέα υπάρχει στον πίνακα του Άρθρου 2.
     * @param code Ο κωδικός φορέα προς έλεγχο.
     * @return true αν βρεθεί ο κωδικός, false διαφορετικά.
     */
    private boolean isCodeValid(int code) {
        String codeString = String.valueOf(code);
        // Ψάχνουμε από τη γραμμή 1 (μετά την κεφαλίδα)
        for (int i = 1; i < article2Data.length; i++) {
            String[] row = article2Data[i];
            // Ελέγχουμε αν ο κωδικός της γραμμής είναι ίσος με τον κωδικό
            if (row.length > 0 && row[0].trim().equals(codeString)) {
                return true; // Ο κωδικός βρέθηκε!
            }
        }
        return false; // Ο κωδικός δεν βρέθηκε
    }
    
    /**
     * Εμφανίζει τον πίνακα με τον Αναλυτικό Τακτικό Προϋπολογισμό 
     * του επιλεγμένου φορέα.
     */
    public void displayDetailedBudget() {
        // Ελέγχουμε αν έχει φορτωθεί και αν έχει αρκετές γραμμές (τουλάχιστον 5)
        if (detailedBudget.length < 5) {
            System.out.println("Δεν βρέθηκαν αναλυτικά δεδομένα για τον φορέα " 
                               + selectedEntityCode + ". Ελέγξτε αν το " 
                               + selectedEntityCode + ".csv υπάρχει.");
            return;
        }
        
        System.out.println("\n--------------------- ΑΝΑΛΥΤΙΚΟΣ ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ ---------------------");
        // Εκτύπωση των 3 γραμμών metadata (π.χ. Όνομα Φορέα, Έτος, Κωδικός)
        // Χρησιμοποιούμε replace για να αφαιρέσουμε τα εισαγωγικά ("")
        System.out.println(detailedBudget[0][0].replace("\"", ""));
        System.out.println(detailedBudget[1][0].replace("\"", ""));
        System.out.println(detailedBudget[2][0].replace("\"", ""));
        
        System.out.println("--------------------------------------------------------------------------");
        
        // Εκτύπωση της κεφαλίδας (γραμμή 3)
        String[] header = detailedBudget[3];
        String formatHeader = "%-15s | %-50s | %-20s\n";
        System.out.printf(formatHeader, 
                          header[0].replace("\"", ""), 
                          header[1].replace("\"", ""), 
                          header[2].replace("\"", ""));

        System.out.println("--------------------------------------------------------------------------");

        // Εκτύπωση των δεδομένων (από γραμμή 4)
        String formatData = "%-15s | %-50s | %-20s\n";
        for (int i = 4; i < detailedBudget.length; i++) {
            String[] row = detailedBudget[i];
            if (row.length >= 3) {
                 System.out.printf(formatData, 
                                   row[0], 
                                   row[1].replace("\"", ""), 
                                   row[2]);
            }
        }
        System.out.println("--------------------------------------------------------------------------");
    }
}
