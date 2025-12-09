package auebprogramming;

/**
 * Διαχειρίζεται τη φόρτωση, αναζήτηση και επιστροφή δεδομένων του κρατικού 
 * προϋπολογισμού για χρήση σε Γραφικό Περιβάλλον (GUI).
 */
public class BudgetAnalyzer {

    private String[][] article2Data; 
    private int selectedEntityCode;

    /**
     * Constructor. Φορτώνει αυτόματα τα συνοπτικά δεδομένα του Άρθρου 2.
     */
    public BudgetAnalyzer() {
        this.article2Data = CsvToArray.loadCsvToArray("budget_ministries.csv");
    }

    /**
     * Επιστρέφει τα συνοπτικά δεδομένα του Άρθρου 2 (μαζί με την κεφαλίδα) 
     * για εμφάνιση σε JTable.
     * @return Δισδιάστατο πίνακα String[][] με τα στοιχεία του Άρθρου 2.
     */
    public String[][] getArticle2Data() {
        if (article2Data.length < 2) {
            return new String[0][0];
        }
        return article2Data; 
    }

    /**
     * Ελέγχει την εγκυρότητα του κωδικού και επιστρέφει τα αναλυτικά δεδομένα 
     * του Τακτικού Προϋπολογισμού για GUI.
     * @param code Ο τετραψήφιος κωδικός του φορέα που δίνει το GUI.
     * @return Δισδιάστατο πίνακα String[][] με τα αναλυτικά δεδομένα, ή null αν ο κωδικός δεν βρεθεί.
     */
    public String[][] getDetailedBudget(int code) {
        this.selectedEntityCode = code;
        
        if (isCodeValid(code)) {
            
            // Λογική Αντιστοίχισης: Ο κωδικός γίνεται όνομα αρχείου (π.χ. 1032.csv)
            String filename = code + ".csv"; 
            
            String[][] detailedData = CsvToArray.loadCsvToArray(filename);
            
            // Εάν το αρχείο δεν βρεθεί (π.χ. < 5 γραμμές), επιστρέφουμε null
            if (detailedData.length < 5) {
                return null;
            }

            return detailedData;
        } else {
            return null; // Ο κωδικός δεν βρέθηκε
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
            if (row.length > 0 && row[0].trim().equals(codeString)) {
                return true;
            }
        }
        return false;
    }
}
