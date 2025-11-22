package auebprogramming;

public final class ExpenseManager {

    // Array with expense codes and names (fixed)
    private static final String[][] CATEGORIES = {
        {"21", "Παροχές σε εργαζομένους"},
        {"22", "Κοινωνικές παροχές"},
        {"23", "Μεταβιβάσεις"},
        {"24", "Αγορές αγαθών και υπηρεσιών"},
        {"25", "Επιδοτήσεις"},
        {"26", "Τόκοι"},
        {"27", "Λοιπές δαπάνες"},
        {"29", "Πιστώσεις υπό κατανομή"},
        {"31", "Πάγια περιουσιακά στοιχεία"},
        {"33", "Τιμαλφή"},
        {"44", "Δάνεια"},
        {"45", "Συμμετοχικοί τίτλοι και μερίδια"},
        {"53", "Χρεωστικοί τίτλοι (υποχρεώσεις)"},
        {"54", "Δάνεια"}
    };

    // Amounts array [StateBudget, RegularBudget, Investments]
    private static final long[][] AMOUNTS = {
        {14889199000L, 14889199000L, 0},
        {425136000L, 425136000L, 0},
        {34741365000L, 34741365000L, 0},
        {2039542000L, 2039542000L, 0},
        {80630000L, 80630000L, 0},
        {7701101000L, 7701101000L, 0},
        {101553000L, 101553000L, 0},
        {17283053000L, 3183053000L, 14100000000L},
        {2609600000L, 2609600000L, 0},
        {85000L, 85000L, 0},
        {3741000000L, 3741000000L, 0},
        {1755112000L, 1755112000L, 0},
        {19375000000L, 19375000000L, 0},
        {1203165130000L, 1203165130000L, 0}
    };

    /*//
     * Display all available expense categories with their codes
    //*/
    public void showCategories() {
        System.out.println("CODE\tEXPENSE NAME");
        for (String[] category : CATEGORIES) {
            System.out.printf("%s\t%s%n", category[0], category[1]);
        }
    }

    /*//
     * Display details for one or more expense codes
     * User can input multiple codes, e.g., "21", "23"
    //*/
    public void showExpenseDetails(String... codes) {
        System.out.println("CODE\tNAME\tSTATE\tREGULAR\tINVESTMENTS");
        for (String code : codes) {
            int index = findIndexByCode(code);
            if (index != -1) {
                System.out.printf("%s\t%s\t%,d\t%,d\t%,d%n",
                        CATEGORIES[index][0],
                        CATEGORIES[index][1],
                        AMOUNTS[index][0],
                        AMOUNTS[index][1],
                        AMOUNTS[index][2]);
            } else {
                System.out.println(code + " : Invalid code");
            }
        }
    }

    // Helper method to find the index in CATEGORIES by code
    private int findIndexByCode(String code) {
        for (int i = 0; i < CATEGORIES.length; i++) {
            if (CATEGORIES[i][0].equals(code)) return i;
        }
        return -1;
    }

}
