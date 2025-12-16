package auebprogramming;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

/**
 * Η "Γέφυρα" που ενώνει την Ομάδα Β (Αρχεία/Πίνακες) με την Ομάδα Α
 * (Αντικείμενα).
 * Utility class για ανάγνωση και εγγραφή αρχείων.
 */
public final class BudgetIOHandler {

    /**
     * Private constructor to hide the implicit public one.
     * Utility classes should not have a public or default constructor.
     */
    private BudgetIOHandler() {
        // Empty constructor
    }

    /**
     * Βήμα 1: Από Αρχείο -> Ομάδα Β (Πίνακας) -> Ομάδα Α (Repository).
     *
     * @param filename   Το όνομα του αρχείου προς ανάγνωση.
     * @param repository Το repository για αποθήκευση των αντικειμένων.
     */
    public static void loadDataFromFile(final String filename,
                                        final BudgetRepository repository) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true; // Για να πηδήξουμε την επικεφαλίδα

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                final String[] parts = line.split(","); // Χωρίζουμε με κόμμα

                // ΠΑΙΡΝΟΥΜΕ ΤΑ RAW DATA (Στυλ Ομάδας Β)
                final String code = parts[0].trim();
                final String desc = parts[1].trim();
                final String amountStr = parts[2].trim();

                // ΜΕΤΑΤΡΟΠΗ ΣΕ ΟΜΑΔΑ Α (Αντικείμενα)
                final BigDecimal amount = new BigDecimal(amountStr);
                final BudgetChangesEntry entry = new BudgetChangesEntry(
                        code, desc, amount);

                // Αποθήκευση στο Repository
                repository.save(entry);
            }
        } catch (final IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Βήμα 2: Από Ομάδα Α (Repository) -> Ομάδα Β (Πίνακας) -> Αρχείο.
     *
     * @param filename   Το όνομα του αρχείου προς εγγραφή.
     * @param repository Το repository που περιέχει τα δεδομένα.
     */
    public static void saveDataToFile(final String filename,
                                      final BudgetRepository repository) {
        final List<BudgetChangesEntry> entries = repository.findAll();

        // Μετατροπή της λίστας (Ομάδα Α) σε πίνακα (Ομάδα Β) για εγγραφή
        // +1 για τίτλους
        final String[][] dataToWrite = new String[entries.size() + 1][3];

        // 1. Επικεφαλίδες
        dataToWrite[0][0] = "CODE";
        dataToWrite[0][1] = "DESCRIPTION";
        dataToWrite[0][2] = "AMOUNT";

        // 2. Γέμισμα του πίνακα με τα δεδομένα της Ομάδας Α
        for (int i = 0; i < entries.size(); i++) {
            final BudgetChangesEntry entry = entries.get(i);
            dataToWrite[i + 1][0] = entry.getCode();
            dataToWrite[i + 1][1] = entry.getDescription();
            // Μετατροπή του BigDecimal σε απλό String
            dataToWrite[i + 1][2] = entry.getAmount().toString();
        }

        // 3. Εγγραφή στο αρχείο
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (final String[] row : dataToWrite) {
                writer.println(String.join(",", row));
            }
            System.out.println("Επιτυχής αποθήκευση στο " + filename);
        } catch (final IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }
}
