package auebprogramming;

public class BudgetTransferManager {

    /**
     * Μεταφέρει ποσό από έναν κωδικό σε έναν άλλο.
     */
    public static boolean transfer(String[][] data, String fromCode, String toCode, int codeCol, int amountCol, long amount) {
        int fromIndex = -1;
        int toIndex = -1;

        // 1. Εύρεση των γραμμών
        for (int i = 1; i < data.length; i++) {
            String currentCode = data[i][codeCol].trim();
            if (currentCode.equals(fromCode)) fromIndex = i;
            else if (currentCode.equals(toCode)) toIndex = i;
        }

        if (fromIndex == -1 || toIndex == -1) return false;

        // 2. Parsing και Έλεγχος Υπολοίπου
        try {
            long currentFrom = parseAmount(data[fromIndex][amountCol]);
            long currentTo = parseAmount(data[toIndex][amountCol]);

            if (currentFrom < amount) return false; // Δεν φτάνουν τα λεφτά

            // 3. Ενημέρωση
            data[fromIndex][amountCol] = String.valueOf(currentFrom - amount);
            data[toIndex][amountCol] = String.valueOf(currentTo + amount);
            
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Βοηθητική μέθοδος για καθαρισμό των String (αφαίρεση τελείων, κενών)
    private static long parseAmount(String amountStr) {
        return Long.parseLong(amountStr.replace(" ", "").replace(".", "").replace(",", ""));
    }
}