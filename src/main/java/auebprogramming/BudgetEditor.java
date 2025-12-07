package auebprogramming;

public class BudgetEditor {

    /**
     * Ενημερώνει το ποσό σε μια συγκεκριμένη γραμμή βάσει κωδικού.
     * * @param data Ο πίνακας δεδομένων (String[][]).
     * @param code Ο κωδικός που ψάχνουμε (π.χ. "21").
     * @param codeCol Το index της στήλης που έχει τους κωδικούς.
     * @param amountCol Το index της στήλης που έχει τα ποσά.
     * @param newAmount Το νέο ποσό.
     * @return true αν έγινε η αλλαγή, false αν δεν βρέθηκε ο κωδικός.
     */
    public static boolean updateAmount(String[][] data, String code, int codeCol, int amountCol, long newAmount) {
        for (int i = 1; i < data.length; i++) { // Ξεκινάμε από 1 για να γλιτώσουμε την επικεφαλίδα
            if (data[i].length > codeCol && data[i][codeCol].trim().equals(code)) {
                data[i][amountCol] = String.valueOf(newAmount);
                return true;
            }
        }
        return false;
    }
}