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

    /** Returns 2-digit codes starting with the given prefix. */
    public String[][] get2DigitCodes(final String prefix) {
        return filterByPrefix(codes2, prefix);
    }

    /** Returns 3-digit codes starting with the given prefix. */
    public String[][] get3DigitCodes(final String prefix) {
        return filterByPrefix(codes3, prefix);
    }

    /** Returns 5-digit codes starting with the given prefix. */
    public String[][] get5DigitCodes(final String prefix) {
        return filterByPrefix(codes5, prefix);
    }

    /** Returns 7-digit codes starting with the given prefix. */
    public String[][] get7DigitCodes(final String prefix) {
        return filterByPrefix(codes7, prefix);
    }
}
