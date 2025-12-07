package auebprogramming;

public class BudgetStructureManager {

    /**
     * Προσθέτει μια νέα γραμμή στον πίνακα δεδομένων.
     * Επιστρέφει τον ΝΕΟ πίνακα (μεγαλύτερο κατά 1).
     */
    public static String[][] addRow(String[][] originalData, String[] newRowData) {
        // Δημιουργία μεγαλύτερου πίνακα
        String[][] newData = new String[originalData.length + 1][originalData[0].length];
        
        // Αντιγραφή παλιών δεδομένων
        for (int i = 0; i < originalData.length; i++) {
            newData[i] = originalData[i];
        }
        
        // Προσθήκη νέας γραμμής στο τέλος
        newData[newData.length - 1] = newRowData;
        
        return newData;
    }

    /**
     * Διαγράφει μια γραμμή βάσει κωδικού.
     * Επιστρέφει τον ΝΕΟ πίνακα (μικρότερο κατά 1).
     */
    public static String[][] deleteRow(String[][] originalData, String code, int codeCol) {
        int indexToDelete = -1;
        
        // Βρίσκουμε ποια γραμμή να σβήσουμε
        for (int i = 1; i < originalData.length; i++) {
            if (originalData[i][codeCol].equals(code)) {
                indexToDelete = i;
                break;
            }
        }
        
        if (indexToDelete == -1) return originalData; // Δεν βρέθηκε, επιστρέφουμε τον ίδιο

        // Δημιουργία μικρότερου πίνακα
        String[][] newData = new String[originalData.length - 1][originalData[0].length];
        
        int k = 0;
        for (int i = 0; i < originalData.length; i++) {
            if (i == indexToDelete) continue; // Skip τη γραμμή που σβήνουμε
            newData[k++] = originalData[i];
        }
        
        return newData;
    }
}