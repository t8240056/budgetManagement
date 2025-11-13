import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;

public class CVSmanagement {
    public static String[][] loadCsvToArray(String filename) {
    List<String[]> dataList = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        String line;
        
        while ((line = br.readLine()) != null) {
            

            // Διαχωρισμός των πεδίων με βάση το κόμμα
            String[] parts = line.split(",");

            dataList.add(parts);
        }
        br.close();

        // Μετατροπή της λίστας σε δισδιάστατο πίνακα String[][]
        String[][] dataArray = new String[dataList.size()][];
        return dataList.toArray(dataArray);

    } catch (IOException e) {
        System.out.println("Σφάλμα κατά τη φόρτωση του αρχείου: " + e.getMessage());
        return null;
    }
}
}
