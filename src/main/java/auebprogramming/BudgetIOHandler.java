package auebprogramming;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Η "Γέφυρα" που ενώνει την Ομάδα Β (Αρχεία/Πίνακες) με την Ομάδα Α (Αντικείμενα).
 */
public class BudgetIOHandler {

    // Βήμα 1: Από Αρχείο -> Ομάδα Β (Πίνακας) -> Ομάδα Α (Repository)
    public static void loadDataFromFile(String filename, BudgetRepository repository) {
        // Εδώ θα μπορούσες να χρησιμοποιήσεις κώδικα της Ομάδας Β για να διαβάσεις το αρχείο σε String[][]
        // Αλλά ας δούμε την άμεση μετατροπή για απλότητα:
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true; // Για να πηδήξουμε την επικεφαλίδα
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) { isFirstLine = false; continue; }

                String[] parts = line.split(","); // Χωρίζουμε με κόμμα
                
                // ΠΑΙΡΝΟΥΜΕ ΤΑ RAW DATA (Στυλ Ομάδας Β)
                String code = parts[0].trim();
                String desc = parts[1].trim();
                String amountStr = parts[2].trim();

                // ΜΕΤΑΤΡΟΠΗ ΣΕ ΟΜΑΔΑ Α (Αντικείμενα)
                BigDecimal amount = new BigDecimal(amountStr);
                BudgetChangesEntry entry = new BudgetChangesEntry(code, desc, amount);

                // Αποθήκευση στο Repository
                repository.save(entry);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Βήμα 2: Από Ομάδα Α (Repository) -> Ομάδα Β (Πίνακας) -> Αρχείο
    public static void saveDataToFile(String filename, BudgetRepository repository) {
        List<BudgetChangesEntry> entries = repository.findAll();

        // Μετατροπή της λίστας (Ομάδα Α) σε πίνακα (Ομάδα Β) για εγγραφή
        // Εδώ ουσιαστικά φτιάχνουμε το String[][] που ήθελε η Ομάδα Β
        String[][] dataToWrite = new String[entries.size() + 1][3]; // +1 για τίτλους
        
        // 1. Επικεφαλίδες
        dataToWrite[0][0] = "CODE";
        dataToWrite[0][1] = "DESCRIPTION";
        dataToWrite[0][2] = "AMOUNT";

        // 2. Γέμισμα του πίνακα με τα δεδομένα της Ομάδας Α
        for (int i = 0; i < entries.size(); i++) {
            BudgetChangesEntry entry = entries.get(i);
            dataToWrite[i + 1][0] = entry.getCode();
            dataToWrite[i + 1][1] = entry.getDescription();
            // Μετατροπή του BigDecimal σε απλό String
            dataToWrite[i + 1][2] = entry.getAmount().toString(); 
        }

        // 3. Εγγραφή στο αρχείο
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String[] row : dataToWrite) {
                writer.println(String.join(",", row));
            }
            System.out.println("Επιτυχής αποθήκευση στο " + filename);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }
}
