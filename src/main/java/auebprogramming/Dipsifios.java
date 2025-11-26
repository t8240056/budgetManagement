package auebprogramming;
import java.util.*;



public class Dipsifios {
    private Map<String, List<BudgetItem>> budgetMap;
    private Scanner scanner;
    
    // Constructor initializes the data structure and scanner
    public Dipsifios() {
        budgetMap = new HashMap<>();
        scanner = new Scanner(System.in);
        initializeData();
    }
    
    /**
     * Initializes the budget data and organizes it in a map
     * where key is the two-digit code and value is list of related items
     */
    private void initializeData() {
        // Create all budget items
        List<RevenuetItem> allItems = Arrays.asList(
            new RevenueItem("11", "Φόροι", 62055000000L),
            new RevenueItem("111", "Φόροι επί αγαθών και υπηρεσιών", 33667000000L),
            new RevenueItem("112", "Φόροι και δασμοί επί εισαγωγών", 362000000L),
            new RevenueItem("113", "Τακτικοί φόροι ακίνητης περιουσίας", 2353000000L),
            new RevenueItem("114", "Λοιποί φόροι επί παραγωγής", 355000000L),
            new RevenueItem("115", "Φόρος εισοδήματος", 23719000000L),
            new RevenueItem("116", "Φόροι κεφαλαίου", 232000000L),
            new RevenueItem("119", "Λοιποί τρέχοντες φόροι", 1367000000L),
            new RevenueItem("12", "Κοινωνικές εισφορές", 60000000L),
            new RevenueItem("122", "Λοιπές κοινωνικές εισφορές", 60000000L),
            new RevenueItem("13", "Μεταβιβάσεις", 8131000000L),
            new RevenueItem("131", "Τρέχουσες εγχώριες μεταβιβάσεις", 322000000L),
            new RevenueItem("132", "Τρέχουσες μεταβιβάσεις από οργανισμούς και κράτη-μέλη της Ε.Ε", 15000000L),
            new RevenueItem("133", "Τρέχουσες μεταβιβάσεις από φορείς του εξωτερικού", 8000000L),
            new RevenueItem("134", "Επιχορηγήσεις επενδύσεων εσωτερικού", 35000000L),
            new RevenueItem("135", "Επιχορηγήσεις επενδύσεων από την Ε.Ε.", 7645000000L),
            new RevenueItem("139", "Λοιπές κεφαλαιακές μεταβιβάσεις", 106000000L),
            new RevenueItem("14", "Πωλήσεις αγαθών και υπηρεσιών", 2405000000L),
            new RevenueItem("141", "Πωλήσεις αγαθών", 2000000L),
            new RevenueItem("142", "Παροχή υπηρεσιών", 338000000L),
            new RevenueItem("143", "Μισθώματα", 1418000000L),
            new RevenueItem("144", "Προμήθειες", 445000000L),
            new RevenueItem("145", "Διοικητικές αμοιβές", 199000000L),
            new RevenueItem("149", "Λοιπές πωλήσεις", 3000000L),
            new RevenueItem("15", "Λοιπά τρέχοντα έσοδα", 2775000000L),
            new RevenueItem("151", "Τόκοι", 588000000L),
            new RevenueItem("152", "Διανεμόμενο εισόδημα εταιριών", 356000000L),
            new RevenueItem("153", "Ενοίκια φυσικών πόρων", 75000000L),
            new RevenueItem("156", "Πρόστιμα, ποινές και καταλογισμοί", 1102000000L),
            new RevenueItem("159", "Επιστροφές δαπανών", 654000000L),
            new RevenueItem("31", "Πάγια περιουσιακά στοιχεία", 37000000L),
            new RevenueItem("311", "Κτίρια και συναφείς υποδομές", 37000000L),
            new RevenueItem("43", "Χρεωστικοί τίτλοι", 11000000L),
            new RevenueItem("432", "Μακροπρόθεσμοι χρεωστικοί τίτλοι", 11000000L),
            new RevenueItem("44", "Δάνεια", 20000000L),
            new RevenueItem("442", "Μακροπρόθεσμα Δάνεια", 20000000L),
            new RevenueItem("45", "Συμμετοχικοί τίτλοι και μερίδια επενδυτικών κεφαλαίων", 467000000L),
            new RevenueItem("451", "Εισηγμένες μετοχές", 239000000L),
            new RevenueItem("452", "Μη εισηγμένες μετοχές", 228000000L),
            new RevenueItem("52", "Υποχρεώσεις από νόμισμα και καταθέσεις", 66000000L),
            new RevenueItem("521", "Υποχρεώσεις από νόμισμα σε κυκλοφορία", 66000000L),
            new RevenueItem("53", "Χρεωστικοί τίτλοι (Υποχρεώσεις)", 25973000000L),
            new RevenueItem("531", "Βραχυπρόθεσμοι χρεωστικοί τίτλοι", 17000000000L),
            new RevenueItem("532", "Μακροπρόθεσμοι χρεωστικοί τίτλοι", 8973000000L),
            new RevenueItem("54", "Δάνεια", 1202027000000L),
            new RevenueItem("541", "Βραχυπρόθεσμα δάνεια", 1200000000000L),
            new RevenueItem("542", "Μακροπρόθεσμα δάνεια", 2027000000L),
            new RevenueItem("57", "Χρηματοοικονομικά παράγωγα", 800000000L),
            new RevenueItem("571", "Χρηματοοικονομικά παράγωγα", 800000000L)
        );
        
        // Organize items in the map by their two-digit parent code
        for (RevenueItem item : allItems) {
            String twoDigitCode = item.getCode().substring(0, 2);
            budgetMap.computeIfAbsent(twoDigitCode, k -> new ArrayList<>()).add(item);
        }
    }
    
    /**
     * Validates if the input is a correct two-digit code
     * @param input the user input to validate
     * @return true if input is valid two-digit code that exists in data
     */
    private boolean validateTwoDigitCode(String input) {
        // Check if input has exactly 2 digits and exists in our map
        return input != null && 
               input.matches("\\d{2}") && 
               budgetMap.containsKey(input);
    }
    
    /**
     * Gets and validates user input for two-digit code
     * @return validated two-digit code
     */
    private String getUserInput() {
        String input;
        while (true) {
            System.out.print("Enter two-digit code (or 'quit' to exit): ");
            input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("quit")) {
                return null;
            }
            
            if (validateTwoDigitCode(input)) {
                return input;
            } else {
                System.out.println("Invalid code! Please enter a valid two-digit code.");
                System.out.println("Available codes: " + String.join(", ", budgetMap.keySet()));
            }
        }
    }
    
    /**
     * Displays all subcategories for the given two-digit code
     * @param twoDigitCode the parent code to display subcategories for
     */
    private void displaySubcategories(String twoDigitCode) {
        System.out.println("\n=== Subcategories for code " + twoDigitCode + " ===");
        
        List<RevenueItem> items = budgetMap.get(twoDigitCode);
        
        // Display items, making sure two-digit parent comes first
        items.stream()
             .sorted(Comparator.comparing(RevenueItem::getCode))
             .forEach(item -> {
                 if (item.getCode().equals(twoDigitCode)) {
                     System.out.println("MAIN CATEGORY: " + item);
                 } else {
                     System.out.println("  - " + item);
                 }
             });
        
        System.out.println("=== Total items: " + (items.size() - 1) + " subcategories ===\n");
    }
    
    /**
     * Main method that runs the budget manager application
     */
    public void run() {
        System.out.println("=== Budget Manager ===");
        System.out.println("Available two-digit codes: " + String.join(", ", budgetMap.keySet()));
        
        while (true) {
            String userInput = getUserInput();
            
            if (userInput == null) {
                break;
            }
            
            displaySubcategories(userInput);
        }
        
        scanner.close();
        System.out.println("Thank you for using Budget Manager!");
    }
    
    /* Main method to start the application
    public static void main(String[] args) {
        RevenueManager manager = new BudgetManager();
        manager.run();
    }
    */    
}
