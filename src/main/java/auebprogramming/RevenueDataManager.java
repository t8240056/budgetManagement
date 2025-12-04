package auebprogramming;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages budget data from CSV files.
 */
public final class RevenueDataManager {

    private final String[][] codes2; // 2-digit codes
    private final String[][] codes3; // 3-digit codes
    private final String[][] codes5; // 5-digit codes
    private final String[][] codes7; // 7-digit codes

    /**
     * Creates a manager and loads all CSV files.
     */
    public RevenueDataManager() {
        this.codes2 = CsvToArray.loadCsvToArray("revenue_categories2_2025.csv");
        this.codes3 = CsvToArray.loadCsvToArray("revenue_categories3_2025.csv");
        this.codes5 = CsvToArray.loadCsvToArray("revenue_categories5_2025.csv");
        this.codes7 = CsvToArray.loadCsvToArray("revenue_categories7_2025.csv");
    }
}