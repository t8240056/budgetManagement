package auebprogramming;

public final class RevenueManager {

    // Array with expense codes and names 
    private static final String[][] CATEGORIES = {
        {"11", "Φόροι"},
        {"12", "Κοινωνικές εισφορές"},
        {"13", "Μεταβιβάσεις"},
        {"14", "Πωλήσεις αγαθών και υπηρεσιών"},
        {"15", "Λοιπά τρέχοντα έσοδα "},
        {"31", "Πάγια Περιουσιακά Στοιχεία"},
        {"43", "Χρεωστικοί τίτλοι "},
        {"44", "Δάνεια"},
        {"45", "Συμμετοχικοί τίτλοι και μερίδια επενδυτικών κεφαλαίων"},
        {"52", "Υποχρεώσεις από νόμισμα και καταθέσεις"},
        {"53", "Χρεωστικοί τίτλοι(υποχρεώσεις)"},
        {"54", "Δάνεια"},
        {"57", "Χρηματοοικονομικά παράγωγα "},
        
    };

        // Amounts array [StateBudget, RegularBudget, Investments]
    private static final long[][] AMOUNTS = {
        {62055000000L, 62055000000L, 0},
        {60000000L, 60000000L, 0},
        {83100000000L, 83100000000L, 0},        
        {2405000000L, 2405000000L, 0},
        {2775000000l, 2775000000L, 0},
        {37000000L, 37000000L, 0},  
        {11000000L, 11000000L, 0},  
        {20000000L, 20000000L, 0},                
        {467000000L, 467000000L, 0},  
        {66000000L, 66000000L, 0},
        {25973000000L, 25973000000L, 0},
        {1202027000000L, 1202027000000L, 0},
        {800000000L, 800000000L, 0}, 
    };
    /*//
     * Display all available revenue categories with their codes
    //*/
    public void showCategories() {
        System.out.println("CODE\tREVENUE NAME");
        for (String[] category : CATEGORIES) {
            System.out.printf("%s\t%s%n", category[0], category[1]);
        }
    }

    /*//
     * Display details for one or more revenue codes
     * User can input multiple codes, e.g., "21", "23"
    //*/
    public void showRevenueDetails(String[] codes) {
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

    public static void showRevenues() {

    RevenueManager rm = new RevenueManager();

    long total = 0;

    System.out.println("1. ΕΣΟΔΑ");
    System.out.println();
    
    // Print each row like the screenshot
    for (int i = 0; i < CATEGORIES.length; i++) {
        String code = CATEGORIES[i][0];
        String name = CATEGORIES[i][1];
        long amount = AMOUNTS[i][0];

        total += amount;

        System.out.printf("%-5s %-60s %,15d%n",
                code + ".",   // adds the "11." style numbering
                name,
                amount
        );
    }

    System.out.println();
    System.out.printf("Σύνολο: %,d Ευρώ%n", total);
    } 
}
