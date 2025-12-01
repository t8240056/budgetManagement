package auebprogramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to load a CSV file from the application's classpath (resources)
 * and convert it to a String 2D array.
 * Note: Assumes CSV data does not contain escaped commas or double quotes.
 */
public final class CsvToArray {

    /** Private constructor to prevent instantiation. */
    private CsvToArray() {
        // Utility class
    }

    /**
     * Loads a CSV file from the classpath into a two-dimensional String array.
     * @param filename The name of the CSV file (e.g., "data.csv").
     * @return A String[][] array containing the CSV data, or an empty array on error.
     */
    public static String[][] loadCsvToArray(final String filename) {
        final List<String[]> dataList = new ArrayList<>();

        // Χρησιμοποιούμε getResourceAsStream για να διαβάσουμε από το Classpath (π.χ. src/main/resources)
        try (InputStream input = CsvToArray.class.getClassLoader()
            .getResourceAsStream(filename)) {
            if (input == null) {
                System.err.println("Σφάλμα: Το αρχείο δεν βρέθηκε στο classpath (" + filename + ")");
                return new String[0][0];
            }

            // Χρησιμοποιούμε StandardCharsets.UTF_8 για σωστή ανάγνωση ελληνικών
            final BufferedReader br = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8));
            String line;

            while ((line = br.readLine()) != null) {
                // Χωρισμός με κόμμα.
                final String[] rawParts = line.split(",");
                final String[] trimmedParts = new String[rawParts.length];
                
                // *** ΝΕΑ ΤΡΟΠΟΠΟΙΗΣΗ ***: Καθαρισμός κάθε στοιχείου από περιττά κενά.
                for (int i = 0; i < rawParts.length; i++) {
                    trimmedParts[i] = rawParts[i].trim();
                }
                
                dataList.add(trimmedParts);
            }

            // Μετατροπή της λίστας σε δισδιάστατο πίνακα String[][]
            return dataList.toArray(new String[0][]);

        } catch (IOException e) {
            System.err.println("Σφάλμα κατά τη φόρτωση του αρχείου: " + e.getMessage());
            return new String[0][0];
        }
    }
}
