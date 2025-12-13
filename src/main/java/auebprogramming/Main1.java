package auebprogramming;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal; // Δεν το χρειαζόμαστε πια, αλλά το αφήνω αν θες
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

public class Main1 {
    
    // Ένα σταθερό User ID για το παράδειγμα
    private static final String CURRENT_USER = "admin"; 

    public static void main(String[] args) {
        BudgetRepository repository = new BudgetRepository();
        Scanner scanner = new Scanner(System.in);

        // --- ΒΗΜΑ 1: Φόρτωμα Δεδομένων ---
        System.out.println("Please choose budget type (0 for revenue, 1 for expense): ");
        int chooseBudgetType = -1;
        
        try {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (!input.trim().isEmpty()) {
                    chooseBudgetType = Integer.parseInt(input);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }

        if (chooseBudgetType == 0) { // Revenue
            // --- ΝΕΟΣ ΚΩΔΙΚΑΣ ΕΜΦΑΝΙΣΗΣ ΑΡΧΙΚΟΥ ΠΙΝΑΚΑ ---
            System.out.println("\n--- Προεπισκόπηση Αρχείου Εσόδων ---");
            System.out.printf("%-10s %-50s %20s%n", "ΚΩΔΙΚΟΣ", "ΚΑΤΗΓΟΡΙΑ", "ΠΟΣΟ (€)");
            System.out.println("----------------------------------------------------------------------------------");

            try {
                File file = new File("src/main/resources/revenue_categories2_2025.csv");
                Scanner csvScanner = new Scanner(file);

                while (csvScanner.hasNextLine()) {
                    String line = csvScanner.nextLine();
                    
                    // Αγνοούμε κενές γραμμές και επικεφαλίδες
                    if (line.trim().isEmpty()) continue;
                    if (line.startsWith("Κωδικός") || line.startsWith("Code")) continue;

                    String[] parts = line.split(",");
                    
                    if (parts.length >= 3) {
                        String code = parts[0].trim().replace("\uFEFF", ""); // Καθαρισμός BOM
                        String desc = parts[1].trim();
                        // Κόβουμε την περιγραφή αν είναι τεράστια
                        if (desc.length() > 48) desc = desc.substring(0, 48) + "..";
                        
                        try {
                            BigDecimal amount = new BigDecimal(parts[2].trim());
                            // Εκτύπωση με στοίχιση
                            System.out.printf("%-10s %-50s %20s%n", 
                                code, 
                                desc, 
                                NumberFormat.getInstance().format(amount));
                        } catch (NumberFormatException e) {
                            // Αν το ποσό δεν είναι αριθμός, το τυπώνουμε ως string
                            System.out.printf("%-10s %-50s %20s%n", code, desc, parts[2].trim());
                        }
                    }
                }
                csvScanner.close();
                System.out.println("----------------------------------------------------------------------------------");
            } catch (FileNotFoundException e) {
                System.out.println("Το αρχείο CSV δεν βρέθηκε για προεπισκόπηση.");
            }
            
            System.out.println(); // Κενή γραμμή
            loadRevenueData(repository);
            
        } else if (chooseBudgetType == 1) { // Expense
            System.out.println("Expense/Other functionality not loaded for this demo.");
        } else {
            System.out.println("Invalid choice. Please enter 0 or 1.");
        }

        // --- ΒΗΜΑ 2: Κύριο Μενού Επεξεργασίας ---
        boolean keepRunning = true;
        while (keepRunning) {
            System.out.println("\n=== BUDGET MANAGEMENT MENU ===");
            System.out.println("1. Προβολή όλων των εγγραφών (Ταξινομημένη)");
            System.out.println("2. Αλλαγή Ποσού (Απόλυτη τιμή)");
            System.out.println("3. Αλλαγή Ποσού (Ποσοστό %)");
            System.out.println("4. Μεταφορά Ποσού (Transfer)");
            System.out.println("5. Έξοδος");
            System.out.print("Επιλογή: ");

            String choice = "";
            if (scanner.hasNextLine()) {
                choice = scanner.nextLine();
            }

            switch (choice) {
                case "1":
                    printAllEntries(repository);
                    break;
                case "2":
                    handleAbsoluteChange(repository, scanner);
                    break;
                case "3":
                    handlePercentageChange(repository, scanner);
                    break;
                case "4":
                    handleTransfer(repository, scanner);
                    break;
                case "5":
                    keepRunning = false;
                    System.out.println("Έξοδος...");
                    break;
                default:
                    System.out.println("Μη έγκυρη επιλογή.");
            }
        }
        scanner.close();
    }

    // --- Βοηθητικές Μέθοδοι ---

    private static void handleAbsoluteChange(BudgetRepository repo, Scanner scanner) {
        System.out.print("Δώσε τον Κωδικό (Code) της εγγραφής: ");
        String code = scanner.nextLine();
        
        Optional<BudgetChangesEntry> entryOpt = repo.findByCode(code);
        if (entryOpt.isEmpty()) {
            System.out.println("Ο κωδικός '" + code + "' δεν βρέθηκε.");
            return;
        }
        BudgetChangesEntry entry = entryOpt.get();

        System.out.print("Δώσε ποσό αλλαγής (π.χ. +500 για αύξηση, -200 για μείωση): ");
        try {
            String amountInput = scanner.nextLine();
            BigDecimal amount = new BigDecimal(amountInput); 

            System.out.print("Αιτιολογία: ");
            String just = scanner.nextLine();

            AbsoluteAmountChange change = new AbsoluteAmountChange(code, amount, just, CURRENT_USER);
            
            change.apply(entry); 
            
            System.out.println("Επιτυχία! Τύπος: " + change.getType());
            System.out.println("Νέο ποσό: " + NumberFormat.getInstance().format(entry.getAmount()) + " €");
            
        } catch (Exception e) {
            System.out.println("Σφάλμα: " + e.getMessage());
        }
    }

    private static void handlePercentageChange(BudgetRepository repo, Scanner scanner) {
        System.out.print("Δώσε τον Κωδικό (Code) της εγγραφής: ");
        String code = scanner.nextLine();

        Optional<BudgetChangesEntry> entryOpt = repo.findByCode(code);
        if (entryOpt.isEmpty()) {
            System.out.println("Ο κωδικός '" + code + "' δεν βρέθηκε.");
            return;
        }
        BudgetChangesEntry entry = entryOpt.get();

        System.out.print("Δώσε ποσοστό % (π.χ. +10 για +10%, -50 για -50%): ");
        try {
            double percent = Double.parseDouble(scanner.nextLine());
            System.out.print("Αιτιολογία: ");
            String just = scanner.nextLine();

            PercentageChange change = new PercentageChange(code, percent, just, CURRENT_USER);
            change.apply(entry);
            
            System.out.println("Επιτυχία! Διαφορά ποσού: " + NumberFormat.getInstance().format(change.getDifference()));
            System.out.println("Νέο ποσό: " + NumberFormat.getInstance().format(entry.getAmount()) + " €");

        } catch (Exception e) {
            System.out.println("Σφάλμα: " + e.getMessage());
        }
    }

    private static void handleTransfer(BudgetRepository repo, Scanner scanner) {
        System.out.print("Δώσε τον Κωδικό ΠΗΓΗΣ (Source Code): ");
        String sourceCode = scanner.nextLine();
        
        System.out.print("Δώσε τον Κωδικό ΠΡΟΟΡΙΣΜΟΥ (Target Code): ");
        String targetCode = scanner.nextLine();

        Optional<BudgetChangesEntry> sourceOpt = repo.findByCode(sourceCode);
        Optional<BudgetChangesEntry> targetOpt = repo.findByCode(targetCode);

        if (sourceOpt.isEmpty() || targetOpt.isEmpty()) {
            System.out.println("Ένας από τους κωδικούς δεν βρέθηκε.");
            return;
        }

        System.out.print("Δώσε ποσό μεταφοράς: ");
        try {
            BigDecimal amount = new BigDecimal(scanner.nextLine());
            System.out.print("Αιτιολογία: ");
            String just = scanner.nextLine();

            TransferChange transfer = new TransferChange(sourceCode, targetCode, amount, just, CURRENT_USER);
            
            transfer.apply(sourceOpt.get());        
            transfer.applyToTarget(targetOpt.get()); 

            System.out.println("Μεταφορά ολοκληρώθηκε.");
            System.out.println("Νέο ποσό Πηγής: " + NumberFormat.getInstance().format(sourceOpt.get().getAmount()));
            System.out.println("Νέο ποσό Προορισμού: " + NumberFormat.getInstance().format(targetOpt.get().getAmount()));

        } catch (Exception e) {
            System.out.println("Σφάλμα μεταφοράς: " + e.getMessage());
        }
    }

    private static void loadRevenueData(BudgetRepository repository) {
        // System.out.println("--- Loading Revenue Categories ---"); 
        try {
            File file = new File("src/main/resources/revenue_categories2_2025.csv");
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                
                if (line.trim().isEmpty()) continue;
                if (line.startsWith("Κωδικός") || line.startsWith("Code")) continue;

                String[] parts = line.split(","); 
                
                if (parts.length >= 3) {
                    String code = parts[0].trim().replace("\uFEFF", ""); 
                    String desc = parts[1].trim();
                    String amountStr = parts[2].trim();
                    
                    BudgetChangesEntry entry = new BudgetChangesEntry(code, desc, new BigDecimal(amountStr));
                    repository.save(entry);
                }
            }
            fileScanner.close();
            System.out.println("Φορτώθηκαν επιτυχώς " + repository.count() + " εγγραφές στη μνήμη.");
        } catch (FileNotFoundException e) {
            System.out.println("Το αρχείο CSV δεν βρέθηκε, ξεκινάμε με κενή βάση.");
        } catch (Exception e) {
            System.out.println("Σφάλμα κατά την ανάγνωση: " + e.getMessage());
        }
    }

    private static void printAllEntries(BudgetRepository repo) {
        System.out.println("\n--- Λίστα Εγγραφών (Ταξινομημένη κατά Κωδικό) ---");
        System.out.printf("%-10s %-50s %20s%n", "ΚΩΔΙΚΟΣ", "ΚΑΤΗΓΟΡΙΑ", "ΠΟΣΟ (€)");
        System.out.println("----------------------------------------------------------------------------------");

        repo.findAll().stream()
            .sorted(Comparator.comparing(BudgetChangesEntry::getCode))
            .forEach(entry -> {
                System.out.printf("%-10s %-50s %20s%n", 
                    entry.getCode(), 
                    entry.getDescription().length() > 48 ? entry.getDescription().substring(0, 48) + ".." : entry.getDescription(),
                    NumberFormat.getInstance().format(entry.getAmount())
                );
            });
            
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("Σύνολο Προϋπολογισμού: " + NumberFormat.getInstance().format(repo.calculateTotal()) + " €");
    }
}
