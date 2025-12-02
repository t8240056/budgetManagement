package auebprogramming;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * BudgetDataProcessor - Processes Greek budget data from text file to extract
 * revenue data
 */
public class BudgetRevenueReader {

    /*
     * // Main method - entry point of the application
     *
     * public static void main(String[] args) {
     * String inputFile = "output" + "2025" + ".txt";
     * String outputCsv = "revenues.csv";
     *
     * try {
     * // Read all lines from the input file
     * List<String> lines = readFile(inputFile);
     *
     * // Extract revenue data from the file
     * List<RevenueRecord> revenues = extractRevenueData(lines);
     *
     * // Save revenue data to CSV file
     * saveToCsv(revenues, outputCsv);
     *
     * // Display revenue data
     * displayRevenueData(revenues);
     *
     * System.out.println("\nData successfully processed and saved to " +
     * outputCsv);
     *
     * } catch (IOException e) {
     * System.err.println("Error processing file: " + e.getMessage());
     * e.printStackTrace();
     * }
     * }
     */
    /**
     * Reads all lines from the specified file
     */
    private static List<String> readFile(String filename) throws IOException {
        return Files.readAllLines(Paths.get(filename));
    }

    /**
     * Extracts revenue data from the file lines
     * Looks for the ΕΣΟΔΑ section and processes revenue records
     */
    private static List<RevenueRecord> extractRevenueData(List<String> lines) {
        List<RevenueRecord> revenues = new ArrayList<>();
        boolean inRevenueSection = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            // Check if we've entered the ΕΣΟΔΑ section
            if (line.contains("ΕΣΟΔΑ") && !inRevenueSection) {
                inRevenueSection = true;
                continue;
            }

            // Check if we've left the revenue section
            if (inRevenueSection && (line.contains("STOP") || line.contains("ΕΞΟΔΑ"))) {
                break;
            }

            // Process revenue data lines (lines that contain revenue codes and amounts)
            if (inRevenueSection && isRevenueDataLine(line)) {
                RevenueRecord record = parseRevenueLine(line);
                if (record != null) {
                    revenues.add(record);
                }
            }
        }

        return revenues;
    }

    /**
     * Checks if a line contains revenue data (has a revenue code and amount)
     */
    private static boolean isRevenueDataLine(String line) {
        // Revenue lines typically start with numbers (classification codes)
        // and contain numeric amounts at the end
        if (line.isEmpty())
            return false;

        String[] parts = line.trim().split("\\s+");
        if (parts.length < 2)
            return false;

        // Check if first part looks like a revenue code (starts with numbers)
        String firstPart = parts[0];
        if (!firstPart.matches("^\\d+.*"))
            return false;

        // Check if last part looks like a monetary amount
        String lastPart = parts[parts.length - 1];
        return lastPart.matches("[\\d.,]+");
    }

    /**
     * Parses a single line of revenue data into a RevenueRecord object
     */
    private static RevenueRecord parseRevenueLine(String line) {
        try {
            // Remove extra spaces and normalize the line
            line = line.replaceAll("\\s+", " ").trim();

            // Find the last numeric value (the amount)
            String[] parts = line.split(" ");
            if (parts.length < 2)
                return null;

            String amountStr = parts[parts.length - 1].replace(".", "").replace(",", "");
            long amount = Long.parseLong(amountStr);

            // Extract the code (first part)
            String code = parts[0];

            // Extract description (everything between code and amount)
            StringBuilder description = new StringBuilder();
            for (int i = 1; i < parts.length - 1; i++) {
                if (i > 1)
                    description.append(" ");
                description.append(parts[i]);
            }

            return new RevenueRecord(code, description.toString(), amount);

        } catch (NumberFormatException e) {
            System.err.println("Error parsing amount in line: " + line);
            return null;
        } catch (Exception e) {
            System.err.println("Error processing line: " + line);
            return null;
        }
    }

    /**
     * Saves the revenue data to a CSV file
     */
    private static void saveToCsv(List<RevenueRecord> revenues, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write CSV header
            writer.println("Code,Description,Amount");

            // Write data rows
            for (RevenueRecord record : revenues) {
                writer.printf("%s,\"%s\",%d%n",
                        record.getCode(),
                        record.getDescription(),
                        record.getAmount());
            }
        }
    }

    /**
     * Displays the revenue data in a formatted table
     */
    private static void displayRevenueData(List<RevenueRecord> revenues) {
        System.out.println("=== REVENUE DATA ===");
        System.out.printf("%-10s %-60s %-15s%n", "Code", "Description", "Amount");
        System.out.println("-".repeat(90));

        for (RevenueRecord record : revenues) {
            // Format amount with thousand separators
            String formattedAmount = String.format("%,d", record.getAmount());
            System.out.printf("%-10s %-60s %-15s%n",
                    record.getCode(),
                    truncate(record.getDescription(), 55),
                    formattedAmount);
        }

        // Display summary
        long total = revenues.stream().mapToLong(RevenueRecord::getAmount).sum();
        System.out.println("-".repeat(90));
        System.out.printf("%-71s %-,15d%n", "TOTAL:", total);
    }

    /**
     * Helper method to truncate long descriptions for display
     */
    private static String truncate(String text, int maxLength) {
        if (text.length() <= maxLength)
            return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    /**
     * Inner class to represent a revenue record
     */
    static class RevenueRecord {
        private String code;
        private String description;
        private long amount;

        public RevenueRecord(String code, String description, long amount) {
            this.code = code;
            this.description = description;
            this.amount = amount;
        }

        // Getters
        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public long getAmount() {
            return amount;
        }

        @Override
        public String toString() {
            return String.format("RevenueRecord{code='%s', description='%s', amount=%,d}",
                    code, description, amount);
        }
    }
}