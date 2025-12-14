package auebprogramming;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.Scanner;
import java.util.Stack;

public class Main1 {
    
    private static final String CURRENT_USER = "admin"; 
    private static final String RESOURCES_PATH = "src/main/resources/";
    
    // --- UNDO LOGIC: Η Στοίβα του Ιστορικού ---
    // Εδώ αποθηκεύουμε κάθε κίνηση που γίνεται επιτυχώς
    private static Stack<BudgetChange> changeHistory = new Stack<>();

    public static void main(String[] args) {
        BudgetRepository repository = new BudgetRepository();
        Scanner scanner = new Scanner(System.in);

        // ... (Κώδικας επιλογής τύπου και φόρτωσης ίδιος με πριν) ...
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

        if (chooseBudgetType == 0) { 
            loadRevenueData(repository); 
        } else if (chooseBudgetType == 1) { 
            loadMinistries(); 
            boolean orgLoaded = false;
            while (!orgLoaded) {
                System.out.print("\nΕπίλεξε Κωδικό Φορέα (π.χ. 1003) για επεξεργασία: ");
                String orgCode = scanner.nextLine().trim();
                orgLoaded = loadOrganizationExpenses(repository, orgCode);
                if (!orgLoaded) {
                    System.out.println("⚠️ Παρακαλώ έλεγξε τον κωδικό και προσπάθησε ξανά.");
                } else {
                    printAllEntries(repository);
                }
            }
        } else {
            System.out.println("Invalid choice. Please enter 0 or 1.");
            scanner.close();
            return;
        }

        // --- ΒΗΜΑ 2: Κύριο Μενού ---
        boolean keepRunning = true;
        while (keepRunning) {
            System.out.println("\n=== BUDGET MANAGEMENT MENU ===");
            System.out.println("1. Προβολή όλων των εγγραφών (Ταξινομημένη)");
            System.out.println("2. Αλλαγή Ποσού (Απόλυτη τιμή)");
            System.out.println("3. Αλλαγή Ποσού (Ποσοστό %)");
            System.out.println("4. Μεταφορά Ποσού (Transfer)");
            System.out.println("5. Undo (Αναίρεση Τελευταίας Κίνησης) 🔙"); // ΝΕΑ ΕΠΙΛΟΓΗ
            System.out.println("6. Έξοδος");
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
                    handleUndo(repository); // Κλήση της μεθόδου Undo
                    break;
                case "6":
                    keepRunning = false;
                    System.out.println("Έξοδος...");
                    break;
                default:
                    System.out.println("Μη έγκυρη επιλογή.");
            }
        }
        scanner.close();
    }

    // =========================================================================
    //                        UNDO FUNCTIONALITY
    // =========================================================================

    private static void handleUndo(BudgetRepository repo) {
        if (changeHistory.isEmpty()) {
            System.out.println("⚠️ Δεν υπάρχουν κινήσεις για αναίρεση.");
            return;
        }

        // 1. Παίρνουμε την τελευταία κίνηση από τη στοίβα (POP)
        BudgetChange lastChange = changeHistory.pop();

        System.out.println("🔄 Αναίρεση κίνησης: " + lastChange.getType());
        System.out.println("   Αιτιολογία αρχικής κίνησης: " + lastChange.getDescription());

        // 2. Ελέγχουμε αν είναι Transfer (θέλει ειδική μεταχείριση) ή απλή αλλαγή
        if (lastChange instanceof TransferChange) {
            TransferChange transfer = (TransferChange) lastChange;
            
            Optional<BudgetChangesEntry> sourceOpt = repo.findByCode(transfer.getEntryCode());
            Optional<BudgetChangesEntry> targetOpt = repo.findByCode(transfer.getTargetCode());

            if (sourceOpt.isPresent() && targetOpt.isPresent()) {
                transfer.undo(sourceOpt.get());       // Επιστροφή στην πηγή
                transfer.undoTarget(targetOpt.get()); // Αφαίρεση από προορισμό
                System.out.println("✅ Η μεταφορά αναιρέθηκε επιτυχώς.");
            } else {
                System.out.println("❌ Σφάλμα: Δεν βρέθηκαν οι εγγραφές για την αναίρεση.");
            }

        } else {
            // Απλή αλλαγή (Absolute ή Percentage)
            Optional<BudgetChangesEntry> entryOpt = repo.findByCode(lastChange.getEntryCode());
            
            if (entryOpt.isPresent()) {
                lastChange.undo(entryOpt.get());
                System.out.println("✅ Η αλλαγή αναιρέθηκε. Το ποσό επανήλθε.");
            } else {
                System.out.println("❌ Σφάλμα: Η εγγραφή με κωδικό " + lastChange.getEntryCode() + " δεν βρέθηκε.");
            }
        }
    }

    // =========================================================================
    //                        ΥΠΑΡΧΟΥΣΕΣ ΜΕΘΟΔΟΙ (ΕΝΗΜΕΡΩΜΕΝΕΣ ΜΕ PUSH)
    // =========================================================================
    
    // ... (loadMinistries, loadOrganizationExpenses, loadRevenueData, printAllEntries παραμένουν ίδια) ...
    // Θα τα βάλεις εδώ όπως ήταν στον προηγούμενο κώδικα, για οικονομία χώρου σου γράφω μόνο τις αλλαγές παρακάτω:
    
    private static void loadMinistries() { /* ... ΚΩΔΙΚΑΣ ΙΔΙΟΣ ... */ 
        // Αντέγραψε τον κώδικα από το προηγούμενο μήνυμα ή κράτα τον ίδιο 
        System.out.println("\n--- Λίστα Φορέων Κεντρικής Διοίκησης ---");
        // ... (copy paste από προηγούμενο)
        try {
            File file = new File(RESOURCES_PATH + "expense_ministries_2025.csv");
            Scanner csvScanner = new Scanner(file);
            while (csvScanner.hasNextLine()) {
                String line = csvScanner.nextLine();
                if (line.trim().isEmpty() || line.startsWith("Κωδικός")) continue;
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    System.out.printf("%-10s %-70s %20s%n", parts[0].trim(), 
                        parts[1].trim().length() > 68 ? parts[1].trim().substring(0, 68) + ".." : parts[1].trim(), 
                        parts[4].trim());
                }
            }
            csvScanner.close();
        } catch (FileNotFoundException e) { System.out.println("File not found"); }
    }
    
    private static boolean loadOrganizationExpenses(BudgetRepository repository, String orgCode) {
         // ... (copy paste τον κώδικα από το προηγούμενο μήνυμα - είναι ίδιος) ...
         return Main1Helper.loadOrganizationExpenses(repository, orgCode, RESOURCES_PATH); // Χάριν συντομίας στο παράδειγμα
         // Στην πραγματικότητα βάλε όλο το body της μεθόδου εδώ
    }
    
    private static void loadRevenueData(BudgetRepository repository) {
        // ... (copy paste τον κώδικα από το προηγούμενο μήνυμα - είναι ίδιος) ...
         Main1Helper.loadRevenueData(repository, RESOURCES_PATH);
    }
    
    private static void printAllEntries(BudgetRepository repo) {
        // ... (copy paste τον κώδικα από το προηγούμενο μήνυμα - είναι ίδιος) ...
        Main1Helper.printAllEntries(repo);
    }

    // --- ΤΡΟΠΟΠΟΙΗΜΕΝΕΣ ΜΕΘΟΔΟΙ ΧΕΙΡΙΣΜΟΥ ΓΙΑ ΝΑ ΚΑΝΟΥΝ PUSH ΣΤΗ ΣΤΟΙΒΑ ---

    private static void handleAbsoluteChange(BudgetRepository repo, Scanner scanner) {
        System.out.print("Δώσε τον Κωδικό (Code) της εγγραφής: ");
        String code = scanner.nextLine();
        
        Optional<BudgetChangesEntry> entryOpt = repo.findByCode(code);
        if (entryOpt.isEmpty()) { System.out.println("Δεν βρέθηκε."); return; }
        BudgetChangesEntry entry = entryOpt.get();

        System.out.print("Δώσε ποσό: ");
        try {
            BigDecimal amount = new BigDecimal(scanner.nextLine()); 
            if (entry.getAmount().add(amount).compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("❌ Ανεπαρκές υπόλοιπο!"); return; 
            }
            
            System.out.print("Αιτιολογία: ");
            String just = scanner.nextLine();

            AbsoluteAmountChange change = new AbsoluteAmountChange(code, amount, just, CURRENT_USER);
            change.apply(entry); 
            
            // --- UNDO: Αποθήκευση στο ιστορικό ---
            changeHistory.push(change); 

            System.out.println("✅ Επιτυχία! Νέο ποσό: " + NumberFormat.getInstance().format(entry.getAmount()));
            
        } catch (Exception e) { System.out.println("Σφάλμα: " + e.getMessage()); }
    }

    private static void handlePercentageChange(BudgetRepository repo, Scanner scanner) {
        System.out.print("Δώσε τον Κωδικό: ");
        String code = scanner.nextLine();
        Optional<BudgetChangesEntry> entryOpt = repo.findByCode(code);
        if (entryOpt.isEmpty()) { System.out.println("Δεν βρέθηκε."); return; }
        BudgetChangesEntry entry = entryOpt.get();

        System.out.print("Δώσε ποσοστό %: ");
        try {
            double percent = Double.parseDouble(scanner.nextLine());
            // Pre-check omitted for brevity (keep yours)
            
            System.out.print("Αιτιολογία: ");
            String just = scanner.nextLine();

            PercentageChange change = new PercentageChange(code, percent, just, CURRENT_USER);
            change.apply(entry);
            
            // --- UNDO: Αποθήκευση στο ιστορικό ---
            changeHistory.push(change);

            System.out.println("✅ Επιτυχία! Νέο ποσό: " + NumberFormat.getInstance().format(entry.getAmount()));
        } catch (Exception e) { System.out.println("Σφάλμα: " + e.getMessage()); }
    }

    private static void handleTransfer(BudgetRepository repo, Scanner scanner) {
        System.out.print("Πηγή: "); String sourceCode = scanner.nextLine();
        System.out.print("Προορισμός: "); String targetCode = scanner.nextLine();
        
        Optional<BudgetChangesEntry> sourceOpt = repo.findByCode(sourceCode);
        Optional<BudgetChangesEntry> targetOpt = repo.findByCode(targetCode);

        if (sourceOpt.isEmpty() || targetOpt.isEmpty()) { System.out.println("Λάθος κωδικοί."); return; }

        System.out.print("Ποσό: ");
        try {
            BigDecimal amount = new BigDecimal(scanner.nextLine());
            if (sourceOpt.get().getAmount().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("❌ Ανεπαρκές υπόλοιπο!"); return;
            }
            System.out.print("Αιτιολογία: ");
            String just = scanner.nextLine();

            TransferChange transfer = new TransferChange(sourceCode, targetCode, amount, just, CURRENT_USER);
            transfer.apply(sourceOpt.get());        
            transfer.applyToTarget(targetOpt.get()); 

            // --- UNDO: Αποθήκευση στο ιστορικό ---
            changeHistory.push(transfer);

            System.out.println("✅ Μεταφορά ολοκληρώθηκε.");
        } catch (Exception e) { System.out.println("Σφάλμα: " + e.getMessage()); }
    }
}

// Βοηθητική κλάση για να μην γράφω ξανά όλο τον κώδικα load στο παράδειγμα, 
// εσύ βάλε τα σώματα των μεθόδων κανονικά μέσα στη Main1 όπως τα είχες!
class Main1Helper {
    static boolean loadOrganizationExpenses(BudgetRepository r, String c, String p) { /*...κώδικας που είχες...*/ return true; }
    static void loadRevenueData(BudgetRepository r, String p) { /*...*/ }
    static void printAllEntries(BudgetRepository r) { /*...*/ }
}
