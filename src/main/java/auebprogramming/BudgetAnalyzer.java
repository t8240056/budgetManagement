package auebprogramming;

/**
 * Διαχειρίζεται τη φόρτωση, αναζήτηση και επιστροφή δεδομένων του κρατικού
 * προϋπολογισμού για χρήση σε Γραφικό Περιβάλλον (GUI).
 */
public final class BudgetAnalyzer {

    /** Τα δεδομένα του Άρθρου 2. */
    private final String[][] article2Data;

    /** Ο επιλεγμένος κωδικός φορέα. */
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
     *
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
     *
     * @param code Ο τετραψήφιος κωδικός του φορέα που δίνει το GUI.
     * @return Δισδιάστατο πίνακα String[][] με τα αναλυτικά δεδομένα.
     * @throws IllegalArgumentException Αν ο κωδικός δεν βρεθεί ή
     * το αρχείο είναι άδειο.
     */
    public String[][] getDetailedBudget(final int code)
            throws IllegalArgumentException {
        this.selectedEntityCode = code;

        // 1. Ελέγχουμε αν ο κωδικός είναι έγκυρος
        if (isCodeValid(code)) {

            // 2. Λογική Αντιστοίχισης: Ο κωδικός γίνεται όνομα αρχείου
            final String filename = code + ".csv";

            final String[][] detailedData = CsvToArray
                    .loadCsvToArray(filename);

            // 3. Ελέγχουμε αν το αρχείο βρέθηκε (όχι κενό)
            if (detailedData == null || detailedData.length < 5) {
                // Αν βρεθεί ο κωδικός αλλά όχι το αρχείο, πετάμε εξαίρεση
                throw new IllegalArgumentException(
                        "Το αναλυτικό αρχείο για τον κωδικό "
                        + code + " δεν βρέθηκε ή είναι άδειο.");
            }

            return detailedData;
        } else {
            // 4. Αν ο κωδικός δεν βρεθεί στο Άρθρο 2, πετάμε εξαίρεση
            throw new IllegalArgumentException("Ο κωδικός φορέα " + code
                    + " δεν αντιστοιχεί σε κανέναν φορέα του Άρθρου 2.");
        }
    }

    /**
     * Ελέγχει αν ο δοθείς κωδικός φορέα υπάρχει στον πίνακα του Άρθρου 2.
     *
     * @param code Ο κωδικός φορέα προς έλεγχο.
     * @return true αν βρεθεί ο κωδικός, false διαφορετικά.
     */
    private boolean isCodeValid(final int code) {
        final String codeString = String.valueOf(code);
        // Ψάχνουμε από τη γραμμή 1 (μετά την κεφαλίδα)
        for (int i = 1; i < article2Data.length; i++) {
            final String[] row = article2Data[i];
            if (row.length > 0 && row[0].trim().equals(codeString)) {
                return true;
            }
        }
        return false;
    }
}
