import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for reading and parsing Greek state budget data from text files
 */
public class BudgetRevenueReader {
    
    private List<RevenueItem> revenueItems;
    
    /**
     * Represents a single revenue item with category and amount
     */
    public static class RevenueItem {
        private String categoryCode;
        private String categoryName;
        private long amount;
        
        public RevenueItem(String categoryCode, String categoryName, long amount) {
            this.categoryCode = categoryCode;
            this.categoryName = categoryName;
            this.amount = amount;
        }
        
        // Getters
        public String getCategoryCode() { return categoryCode; }
        public String getCategoryName() { return categoryName; }
        public long getAmount() { return amount; }
        
        @Override
        public String toString() {
            return String.format("%s %s: %,d €", categoryCode, categoryName, amount);
        }
    }
    
    /**
     * Constructor initializes the revenue items list
     */
    public BudgetRevenueReader() {
        this.revenueItems = new ArrayList<>();
    }
    
    /**
     * Reads the budget file and extracts revenue data from Article 1
     * @param filename Path to the budget file
     * @throws IOException If file reading fails
     */
    public void readBudgetFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        boolean inArticle1 = false;
        boolean inRevenueSection = false;
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            // Check if we're entering Article 1
            if (line.contains("Άρθρο 1")) {
                inArticle1 = true;
                continue;
            }
            
            // Check if we're entering revenue section within Article 1
            if (inArticle1 && line.contains("1. ΕΣΟΔΑ")) {
                inRevenueSection = true;
                continue;
            }
            
            // Check if we're leaving the revenue section
            if (inRevenueSection && line.contains("2. ΕΞΟΔΑ")) {
                break;
            }
            
            // Process revenue lines
            if (inRevenueSection && !line.isEmpty()) {
                processRevenueLine(line);
            }
        }
        
        reader.close();
    }
    
    /**
     * Processes a single line to extract revenue data using regex patterns
     * @param line The line to process
     */
    private void processRevenueLine(String line) {
        // Pattern to match revenue items like "11. Φόροι » 62.055.000.000"
        Pattern revenuePattern = Pattern.compile("(\\d+)\\.\\s+([^»]+)»\\s+([\\d.]+)");
        Matcher matcher = revenuePattern.matcher(line);
        
        if (matcher.find()) {
            String categoryCode = matcher.group(1).trim();
            String categoryName = matcher.group(2).trim();
            String amountStr = matcher.group(3).trim().replace(".", "");
            
            try {
                long amount = Long.parseLong(amountStr);
                revenueItems.add(new RevenueItem(categoryCode, categoryName, amount));
            } catch (NumberFormatException e) {
                System.err.println("Error parsing amount: " + amountStr);
            }
        }
    }
    
    /**
     * Returns all extracted revenue items
     * @return List of revenue items
     */
    public List<RevenueItem> getRevenueItems() {
        return new ArrayList<>(revenueItems);
    }
    
    /**
     * Prints all revenue items to console
     */
    public void printRevenueItems() {
        System.out.println("ΕΣΟΔΑ ΚΡΑΤΙΚΟΥ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ 2025");
        System.out.println("==================================");
        
        long total = 0;
        for (RevenueItem item : revenueItems) {
            System.out.println(item);
            total += item.getAmount();
        }
        
        System.out.println("==================================");
        System.out.printf("ΣΥΝΟΛΟ: %,d €%n", total);
    }
    
    /**
     * Main method for testing the class functionality
     */
    public static void main(String[] args) {
        BudgetRevenueReader reader = new BudgetRevenueReader();
        
        try {
            reader.readBudgetFile("output.txt");
            reader.printRevenueItems();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}