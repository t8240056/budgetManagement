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
        this.codes2 = CsvToArray.loadCsvToArray(
                "revenue_categories2_2025.csv");
        this.codes3 = CsvToArray.loadCsvToArray(
                "revenue_categories3_2025.csv");
        this.codes5 = CsvToArray.loadCsvToArray(
                "revenue_categories5_2025.csv");
        this.codes7 = CsvToArray.loadCsvToArray(
                "revenue_categories7_2025.csv");
    }

    /**
     * Returns 2-digit codes.
     *
     * @return 2-digit codes array
     */
    public String[][] get2DigitCodes() {
        return codes2;
    }

    /**
     * Returns 3-digit codes starting with the given prefix.
     *
     * @param prefix the prefix to filter by
     * @return filtered 3-digit codes
     */
    public String[][] get3DigitCodes(final String prefix) {
        return filterByPrefix(codes3, prefix);
    }

    /**
     * Returns 5-digit codes starting with the given prefix.
     *
     * @param prefix the prefix to filter by
     * @return filtered 5-digit codes
     */
    public String[][] get5DigitCodes(final String prefix) {
        return filterByPrefix(codes5, prefix);
    }

    /**
     * Returns 7-digit codes starting with the given prefix.
     *
     * @param prefix the prefix to filter by
     * @return filtered 7-digit codes
     */
    public String[][] get7DigitCodes(final String prefix) {
        return filterByPrefix(codes7, prefix);
    }

    /**
     * Filters a 2D array by the prefix of the first column.
     *
     * @param data   The 2D array
     * @param prefix The prefix to filter by
     * @return Array containing only rows that start with the prefix
     */
    private String[][] filterByPrefix(final String[][] data,
                                      final String prefix) {
        final List<String[]> filtered = new ArrayList<>();

        for (final String[] row : data) {
            if (row.length > 0 && row[0] != null
                    && row[0].startsWith(prefix)) {
                filtered.add(row);
            }
        }

        return filtered.toArray(new String[0][]);
    }

    /* ============================================================
       VALIDATION METHODS
       ============================================================ */

    /**
     * Validates that the given code contains only digits and has
     * allowed length (2, 3, 5, 7).
     *
     * @param code the code to validate
     * @throws AppException if validation fails
     */
    public void validateCodeLength(final String code) throws AppException {
        if (code == null || !code.matches("\\d+")) {
            throw new AppException(
                    "Ο κωδικός πρέπει να περιέχει μόνο ψηφία.");
        }

        final int len = code.length();
        if (len != 2 && len != 3 && len != 5 && len != 7) {
            throw new AppException("Μη έγκυρο μήκος κωδικού: " + code);
        }
    }

    /**
     * Checks if a code exists in a specific CSV array.
     *
     * @param data the data array to search in
     * @param code the code to search for
     * @return true if found, false otherwise
     */
    private boolean codeExists(final String[][] data, final String code) {
        for (final String[] row : data) {
            if (row.length > 0 && row[0].equals(code)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates that the code exists in the correct CSV file.
     *
     * @param code the code to check
     * @throws AppException if code not found
     */
    public void validateCodeExists(final String code) throws AppException {
        boolean exists = false;

        switch (code.length()) {
            case 2:
                exists = codeExists(codes2, code);
                break;
            case 3:
                exists = codeExists(codes3, code);
                break;
            case 5:
                exists = codeExists(codes5, code);
                break;
            case 7:
                exists = codeExists(codes7, code);
                break;
            default:
                throw new AppException("Μη αποδεκτό μήκος κωδικού: " + code);
        }

        if (!exists) {
            throw new AppException(
                    "Ο κωδικός " + code + " δεν υπάρχει στα δεδομένα.");
        }
    }

    /**
     * Ensures child code starts with parent code (hierarchy rule).
     *
     * @param parent the parent code
     * @param child  the child code to check
     * @throws AppException if hierarchy is violated
     */
    public void validateHierarchy(final String parent,
                                  final String child) throws AppException {
        if (parent == null || parent.isEmpty()) {
            return; // πρώτη εισαγωγή
        }

        if (!child.startsWith(parent)) {
            throw new AppException(
                    "Ο κωδικός " + child
                    + " δεν ανήκει στην κατηγορία του " + parent + ".");
        }
    }

    /**
     * Validates that the given code contains only digits
     * and has exactly the expected length for the current panel.
     *
     * @param code           the code to check
     * @param expectedLength the expected length
     * @throws AppException if length is incorrect
     */
    public void validateExpectedLength(final String code,
                                       final int expectedLength)
            throws AppException {

        if (code.length() != expectedLength) {
            throw new AppException(
                    "Ο κωδικός πρέπει να έχει μήκος "
                    + expectedLength + " ψηφία.");
        }
    }

    /**
     * Full validation for user input:
     * 1. Check digits + correct length
     * 2. Check hierarchy with previous input
     * 3. Check existence in CSV
     *
     * @param previousCode   the previous code entered (for hierarchy)
     * @param userInput      the new code entered
     * @param expectedLength the expected length of the new code
     * @throws AppException if any validation fails
     */
    public void validateUserInput(final String previousCode,
                                  final String userInput,
                                  final int expectedLength)
            throws AppException {

        validateCodeLength(userInput);
        validateHierarchy(previousCode, userInput);
        validateCodeExists(userInput);
        validateExpectedLength(userInput, expectedLength);
    }
}
