package auebprogramming;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CsvFilter {

    public static void main(String[] args) {
        // Ονόματα αρχείων
        String inputFileName = "src/main/resources/output2025.csv";
        String outputFileName = "src/main/resources/filtered_output2025.csv";
        // Το οριοθετικό (delimiter) του CSV
        String delimiter = ",";

        try {
            filterCsv(inputFileName, outputFileName, delimiter);
            System.out.println("Επιτυχής δημιουργία του αρχείου: " + outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Παρουσιάστηκε σφάλμα κατά την επεξεργασία του αρχείου.");
        }
    }

    /**
     * Φιλτράρει το αρχείο CSV, εξαιρώντας γραμμές όπου ο κωδικός (πρώτη στήλη) 
     * είναι αριθμός με περισσότερα από 4 ψηφία.
     */
    private static void filterCsv(String inputPath, String outputPath, String delimiter) throws IOException {
        
        // Χρήση try-with-resources για αυτόματο κλείσιμο των πόρων
        try (BufferedReader br = new BufferedReader(new FileReader(inputPath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {

            String line;
            boolean isHeader = true;

            // Ανάγνωση του αρχείου γραμμή-γραμμή
            while ((line = br.readLine()) != null) {
                // Η πρώτη γραμμή είναι η κεφαλίδα (header)
                if (isHeader) {
                    bw.write(line);
                    bw.newLine();
                    isHeader = false;
                    continue; // Προχωράμε στην επόμενη γραμμή
                }

                // Διαχωρισμός της γραμμής σε στήλες
                // ΠΡΟΣΟΧΗ: Αυτή η απλή μέθοδος split() ενδέχεται να έχει προβλήματα
                // με τιμές που περιέχουν το οριοθετικό (π.χ. μέσα σε εισαγωγικά "").
                // Για πιο σύνθετα CSV, συνιστάται η χρήση εξωτερικής βιβλιοθήκης (π.χ. OpenCSV).
                String[] columns = line.split(delimiter, 2); // Split μόνο στην πρώτη κόμμα

                // Ελέγχουμε αν υπάρχει τουλάχιστον μια στήλη (ο κωδικός)
                if (columns.length > 0) {
                    String codeCandidate = columns[0].trim().replace("\"", "");
                    
                    // Αφαιρούμε την τελεία (π.χ. '1.' -> '1')
                    if (codeCandidate.endsWith(".")) {
                        codeCandidate = codeCandidate.substring(0, codeCandidate.length() - 1);
                    }

                    // Ελέγχουμε αν ο κωδικός είναι αριθμός
                    try {
                        // Αφαιρούμε τυχόν κόμματα/τελείες ως χιλιάδες/δεκαδικά διαχωριστικά
                        String numericCode = codeCandidate.replaceAll("[,\\.]", "");
                        
                        // Ελέγχουμε αν η γραμμή πρέπει να διατηρηθεί
                        // Το κριτήριο είναι: ο αριθμός των ψηφίων (μήκος συμβολοσειράς) πρέπει να είναι <= 4
                        // Ή αν δεν είναι αριθμός (π.χ. "ΕΣΟΔΑ Ευρώ") - αν και το αρχείο φαίνεται να έχει αριθμούς
                        if (numericCode.length() <= 4) {
                            bw.write(line);
                            bw.newLine();
                        }
                        // else: η γραμμή διαγράφεται (δεν γράφεται στο νέο αρχείο)

                    } catch (NumberFormatException e) {
                        // Εάν δεν είναι αριθμός (π.χ. κεφαλίδα ή περιγραφή όπως "1.") - το διατηρούμε
                        // Αφού έχουμε ήδη χειριστεί την κεφαλίδα, εδώ μπαίνουν οι "ψευδο-κωδικοί"
                        bw.write(line);
                        bw.newLine();
                    }
                }
            }
        }
    }
}
