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
     * 
     * @param filename The name of the CSV file (e.g., "data.csv").
     * @return A String[][] array containing the CSV data, or an empty array on
     *         error.
     */
    public static String[][] loadCsvToArray(final String filename) {
        final List<String[]> dataList = new ArrayList<>();

        // Use getResourceAsStream to read from the Classpath (e.g., src/main/resources)
        try (InputStream input = CsvToArray.class.getClassLoader()
                .getResourceAsStream(filename)) {
            if (input == null) {
                System.err.println("Error: File not found in classpath (" + filename + ")");
                return new String[0][0];
            }

            // Use StandardCharsets.UTF_8 for correct reading of Greek characters
            final BufferedReader br = new BufferedReader(
                    new InputStreamReader(input, StandardCharsets.UTF_8));
            String line;

            while ((line = br.readLine()) != null) {
                // Splitting by comma, based on your restrictions.
                final String[] rawParts = line.split(",");
                final String[] trimmedParts = new String[rawParts.length];

                // Trimming each element from surrounding whitespace.
                for (int i = 0; i < rawParts.length; i++) {
                    trimmedParts[i] = rawParts[i].trim();
                }

                dataList.add(trimmedParts);
            }

            // Convert the list to a two-dimensional String[][] array
            return dataList.toArray(new String[0][]);

        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
            return new String[0][0];
        }
    }
}
