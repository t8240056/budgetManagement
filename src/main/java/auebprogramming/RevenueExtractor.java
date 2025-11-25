package auebprogramming;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RevenueExtractor {

    /**
     * Read file and extract revenues between start and stop markers,
     * merging broken lines until an actual amount is found.
     */
    public static void extractRevenues(String filePath) {
        boolean insideRevenueSection = false;
        StringBuilder currentEntry = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {

                // Start marker
                if (line.contains("Κωνσταντίνος Χατζηδάκης")) {
                    insideRevenueSection = true;
                    continue;
                }

                // Stop marker
                if (line.contains("Σύνολο: 4.190.000.000")) {
                    insideRevenueSection = false;
                }

                if (!insideRevenueSection) continue;

                // Clean line
                line = line.trim();
                if (line.isEmpty()) continue;

                // If line starts with a digit, it's part of an entry
                if (line.matches("^[0-9].*")) {
                    currentEntry.append(" ").append(line);

                    // Try to detect actual amount
                    String[] parts = currentEntry.toString().trim().split(" ");
                    String last = parts[parts.length - 1];

                    if (isAmount(last)) {
                        // Join everything except last as name
                        StringBuilder name = new StringBuilder();
                        for (int i = 0; i < parts.length - 1; i++) {
                            name.append(parts[i]).append(" ");
                        }

                        System.out.println("Έσοδο: " + name.toString().trim());
                        System.out.println("Ποσό: " + last);
                        System.out.println("-----------------------");

                        currentEntry.setLength(0);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Determine if a token is a valid numeric amount.
     */
    private static boolean isAmount(String token) {
        return token.matches("^[0-9]{1,3}");
    }

    public void printRevenues() {
        extractRevenues("output.txt");
    }
}
