package auebprogramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class  CsvToArray {
    public static String[][] loadCsvToArray(String filename) {
    List<String[]> dataList = new ArrayList<>();
    
    try (InputStream input = CsvToArray.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                System.out.println("Σφάλμα: Το αρχείο δεν βρέθηκε (" + filename + ")");
                return new String[0][0];
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                dataList.add(parts);
            }

            // Μετατροπή της λίστας σε δισδιάστατο πίνακα String[][]
            return dataList.toArray(new String[0][]);

        } catch (IOException e) {
            System.out.println("Σφάλμα κατά τη φόρτωση του αρχείου: " + e.getMessage());
            return new String[0][0];
        }
    }
}
