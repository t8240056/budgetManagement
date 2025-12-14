package auebprogramming;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter; // ΝΕΟ IMPORT
import java.io.IOException; // ΝΕΟ IMPORT
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Stack; 

public class Main1 {
    
    private static final String CURRENT_USER = "admin"; 
    private static final String RESOURCES_PATH = "src/main/resources/";
    
    // --- STATE VARIABLES ---
    private static Stack<BudgetChange> changeHistory = new Stack<>();
    private static List<String> auditLog = new ArrayList<>();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    // Κρατάμε ποιο αρχείο είναι φορτωμένο για να ξέρουμε πού να αποθηκεύσουμε
    private static String currentLoadedFilePath = null; 
    // Κρατάμε τον τύπο για να ξέρουμε ποια μέθοδο save να καλέσουμε
    private static int currentBudgetType = -1; 

    public static void main(String[] args) {
        BudgetRepository repository = new BudgetRepository();
        Scanner scanner = new Scanner(System.in);

        logAction("Εκκίνηση εφαρμογής από τον χρήστη " + CURRENT_USER);

        System.out.println("Please choose budget type (0 for revenue, 1 for expense): ");
        
        try {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (!input.trim().isEmpty()) {
                    currentBudgetType = Integer.parseInt(input);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }

        if (currentBudgetType == 0) { 
            loadRevenueData(repository); 
            logAction("Φόρτωση δεδομένων Εσόδων");
        } else if (currentBudgetType == 1) { 
            loadMinistries(); 
            boolean orgLoaded = false;
            while (!orgLoaded) {
                System.out.print("\nΕπίλεξε Κωδικό Φορέα (π.χ. 1003) για επεξεργασία: ");
                String orgCode = scanner.nextLine().trim();
                orgLoaded = loadOrganizationExpenses(repository, orgCode);
                if (!orgLoaded) {
                    System.out.println("⚠️ Παρακαλώ έλεγξε τον κωδικό και προσπάθησε ξανά.");
                } else {
                    logAction("Φόρτωση δεδομένων Φορέα: " + orgCode);
                    printAllEntries(repository);
                }
            }
        } else {
            System.out.println("Invalid choice. Please enter 0 or 1.");
            scanner.close();
            return;
        }

        // --- ΚΥΡΙΟ ΜΕΝΟΥ ---
        boolean keepRunning = true;
        while (keepRunning) {
            System.out.println("\n=== BUDGET MANAGEMENT MENU ===");
            System.out.println("1. Προβολή όλων των εγγραφών");
            System.out.println("2. Αλλαγή Ποσού (Απόλυτη τιμή)");
            System.out.println("3. Αλλαγή Ποσού (Ποσοστό %)");
            System.out.println("4. Μεταφορά Ποσού (Transfer)");
            System.out.println("5. Undo (Αναίρεση) 🔙"); 
            System.out.println("6. Προβολή Ιστορικού (Audit Log) 📜"); 
            System.out.println("7. Αποθήκευση Αλλαγών (Save) 💾"); // ΝΕΑ ΕΠΙΛΟΓΗ
            System.out.println("8. Έξοδος");
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
                    handleUndo(repository); 
                    break;
                case "6":
                    printAuditLog(); 
                    break;
                case "7":
                    handleSave(repository); // ΝΕΑ ΜΕΘΟΔΟΣ
                    break;
                case "8":
                    keepRunning = false;
                    logAction("Έξοδος από την εφαρμογή");
                    System.out.println("Έξοδος...");
                    break;
                default:
                    System.out.println("Μη έγκυρη επιλογή.");
            }
        }
        scanner.close();
    }

    // =========================================================================
    //                        SAVE FUNCTIONALITY (NEW)
    // =========================================================================

    private static void handleSave(BudgetRepository repo) {
        if (currentLoadedFilePath == null) {
            System.out.println("❌ Σφάλμα: Δεν υπάρχει φορτωμένο αρχείο για αποθήκευση.");
            return;
        }

        System.out.println("💾 Αποθήκευση αλλαγών στο αρχείο: " + currentLoadedFilePath + " ...");
        
        boolean success = false;
        if (currentBudgetType == 0) {
            success = saveRevenueData(repo);
        } else {
            success = saveExpenseData(repo);
        }

        if (success) {
            System.out.println("✅ Η αποθήκευση ολοκληρώθηκε επιτυχώς!");
            logAction("Αποθήκευση αλλαγών στο δίσκο (" + currentLoadedFilePath + ")");
            // Καθαρίζουμε τη στοίβα undo μετά το save (προαιρετικό, αλλά καλή πρακτική)
            // changeHistory.clear(); 
        } else {
            System.out.println("❌ Η αποθήκευση απέτυχε.");
        }
    }

    private static boolean saveRevenueData(BudgetRepository repo) {
        try (FileWriter writer = new FileWriter(currentLoadedFilePath)) {
            // Γράφουμε την επικεφαλίδα (αν υπάρχει, αλλιώς μπορείς να την παραλείψεις)
            // Στο αρχείο σου δεν είδαμε ξεκάθαρη επικεφαλίδα στην πρώτη γραμμή που να κρατάμε, 
            // αλλά ας βάλουμε μια τυπική ή ας ξεκινήσουμε κατευθείαν τα data αν το αρχείο δεν είχε header.
            // Βάσει του κώδικα load, αγνοούσαμε γραμμές που ξεκινούν με "Κωδικός". Ας τη βάλουμε.
            writer.write("Κωδικός,Κατηγορία,Ποσό\n");

            // Γράφουμε τα δεδομένα
            // Προσοχή: Χρησιμοποιούμε stream για να τα γράψουμε ταξινομημένα
            repo.findAll().stream()
                .sorted(Comparator.comparing(BudgetChangesEntry::getCode))
                .forEach(entry -> {
                    try {
                        // Format: Code,Description,Amount (χωρίς formatting αριθμών π.χ. κόμματα)
                        writer.write(String.format("%s,%s,%s\n", 
                            entry.getCode(), 
                            entry.getDescription(), 
                            entry.getAmount().toPlainString())); // toPlainString για να μην βγάλει επιστημονική μορφή
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            return true;
        } catch (IOException e) {
            System.out.println("Σφάλμα εγγραφής: " + e.getMessage());
            return false;
        }
    }

    private static boolean saveExpenseData(BudgetRepository repo) {
        // Εδώ είναι πιο δύσκολα τα πράγματα. Πρέπει να διατηρήσουμε τις πρώτες γραμμές (Metadata)
        // του αρχικού αρχείου (Υπουργείο, Έτος κλπ).
        
        List<String> headerLines = new ArrayList<>();
        File file = new File(currentLoadedFilePath);

        // 1. Διαβάζουμε τις επικεφαλίδες από το υπάρχον αρχείο
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                // Αν η γραμμή ξεκινάει με νούμερο, θεωρούμε ότι αρχίζουν τα data, άρα σταματάμε
                if (!line.trim().isEmpty() && Character.isDigit(line.charAt(0))) {
                    break;
                }
                headerLines.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Το αρχείο χάθηκε πριν την αποθήκευση!");
            return false;
        }

        // 2. Γράφουμε τα πάντα στο αρχείο (Overwriting)
        try (FileWriter writer = new FileWriter(file)) {
            // Α. Γράφουμε τις επικεφαλίδες που κρατήσαμε
            for (String header : headerLines) {
                writer.write(header + "\n");
            }

            // Β. Γράφουμε τα δεδομένα από το Repository
            repo.findAll().stream()
                .sorted(Comparator.comparing(BudgetChangesEntry::getCode))
                .forEach(entry -> {
                    try {
                        // Format των εξόδων: Code,"Description",Amount
                        // Πρέπει να προσθέσουμε τα εισαγωγικά στην περιγραφή
                        writer.write(String.format("%s,\"%s\",%s\n", 
                            entry.getCode(), 
                            entry.getDescription(), 
                            entry.getAmount().toPlainString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            return true;
        } catch (IOException e) {
            System.out.println("Σφάλμα εγγραφής: " + e.getMessage());
            return false;
        }
    }


    // =========================================================================
    //                        LOGGING & UNDO (UNCHANGED)
    // =========================================================================

    private static void logAction(String actionDetail) {
        String timestamp = dtf.format(LocalDateTime.now());
        String entry = String.format("[%s] USER: %s | %s", timestamp, CURRENT_USER, actionDetail);
        auditLog.add(entry);
    }

    private static void printAuditLog() {
        System.out.println("\n=================== SYSTEM AUDIT LOG ===================");
        if (auditLog.isEmpty()) {
            System.out.println("   (Κανένα καταγεγραμμένο συμβάν)");
        } else {
            for (String entry : auditLog) {
                System.out.println(entry);
            }
        }
        System.out.println("========================================================");
    }

    private static void handleUndo(BudgetRepository repo) {
        if (changeHistory.isEmpty()) {
            System.out.println("⚠️ Δεν υπάρχουν κινήσεις για αναίρεση.");
            return;
        }
        BudgetChange lastChange = changeHistory.pop();
        System.out.println("🔄 Αναίρεση κίνησης: " + lastChange.getType());
        
        logAction("UNDO ACTION: Αναιρέθηκε η κίνηση -> " + lastChange.getDescription());

        if (lastChange instanceof TransferChange) {
            TransferChange transfer = (TransferChange) lastChange;
            Optional<BudgetChangesEntry> sourceOpt = repo.findByCode(transfer.getEntryCode());
            Optional<BudgetChangesEntry> targetOpt = repo.findByCode(transfer.getTargetEntryCode());
            if (sourceOpt.isPresent() && targetOpt.isPresent()) {
                transfer.undo(sourceOpt.get());       
                transfer.undoFromTarget(targetOpt.get()); 
                System.out.println("✅ Η μεταφορά αναιρέθηκε επιτυχώς.");
            }
        } else {
            Optional<BudgetChangesEntry> entryOpt = repo.findByCode(lastChange.getEntryCode());
            if (entryOpt.isPresent()) {
                lastChange.undo(entryOpt.get());
                System.out.println("✅ Η αλλαγή αναιρέθηκε.");
            }
        }
    }

    // =========================================================================
    //                        LOAD & HELPER METHODS (UPDATED PATH SAVING)
    // =========================================================================

    private static void loadMinistries() {
        System.out.println("\n--- Λίστα Φορέων Κεντρικής Διοίκησης ---");
        System.out.printf("%-10s %-70s %20s%n", "ΚΩΔΙΚΟΣ", "ΦΟΡΕΑΣ", "ΣΥΝΟΛΟ (€)");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        try {
            File file = new File(RESOURCES_PATH + "expense_ministries_2025.csv");
            Scanner csvScanner = new Scanner(file);
            while (csvScanner.hasNextLine()) {
                String line = csvScanner.nextLine();
                if (line.trim().isEmpty() || line.startsWith("Κωδικός")) continue;
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    try {
                        BigDecimal total = new BigDecimal(parts[4].trim());
                        System.out.printf("%-10s %-70s %20s%n", parts[0].trim(), 
                            parts[1].trim().length() > 68 ? parts[1].trim().substring(0, 68) + ".." : parts[1].trim(), 
                            NumberFormat.getInstance().format(total));
                    } catch (NumberFormatException e) { }
                }
            }
            csvScanner.close();
            System.out.println("--------------------------------------------------------------------------------------------------------");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    private static boolean loadOrganizationExpenses(BudgetRepository repository, String orgCode) {
        String filename = RESOURCES_PATH + orgCode + ".csv";
        System.out.println("--- Φόρτωση εξόδων από: " + filename + " ---");

        try {
            File file = new File(filename);
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty() || !Character.isDigit(line.charAt(0))) { continue; }

                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); 
                if (parts.length >= 3) {
                    try {
                        String code = parts[0].trim();
                        String desc = parts[1].trim().replace("\"", ""); 
                        BigDecimal amount = new BigDecimal(parts[2].trim());
                        BudgetChangesEntry entry = new BudgetChangesEntry(code, desc, amount);
                        repository.save(entry);
                    } catch (NumberFormatException e) { }
                }
            }
            fileScanner.close();
            System.out.println("Επιτυχία! Φορτώθηκαν " + repository.count() + " κατηγορίες.");
            
            // --- ΣΗΜΑΝΤΙΚΟ: Αποθηκεύουμε το μονοπάτι του αρχείου ---
            currentLoadedFilePath = filename; 
            
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("❌ Σφάλμα: Δεν υπάρχει αρχείο για τον φορέα " + orgCode);
            return false;
        }
    }

    private static void loadRevenueData(BudgetRepository repository) {
        String filename = RESOURCES_PATH + "revenue_categories2_2025.csv";
        // Preview logic omitted for brevity, keep yours
        System.out.println("\n--- Προεπισκόπηση Αρχείου Εσόδων ---");
        // ... (η λογική preview παραμένει ίδια)

        try {
            File file = new File(filename);
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty() || line.startsWith("Κωδικός")) continue;
                String[] parts = line.split(","); 
                if (parts.length >= 3) {
                    try {
                        String code = parts[0].trim().replace("\uFEFF", ""); 
                        String desc = parts[1].trim();
                        BudgetChangesEntry entry = new BudgetChangesEntry(code, desc, new BigDecimal(parts[2].trim()));
                        repository.save(entry);
                    } catch (Exception ex) { }
                }
            }
            fileScanner.close();
            System.out.println("Φορτώθηκαν επιτυχώς " + repository.count() + " εγγραφές εσόδων.");
            
            // --- ΣΗΜΑΝΤΙΚΟ: Αποθηκεύουμε το μονοπάτι του αρχείου ---
            currentLoadedFilePath = filename;

        } catch (Exception e) {
            System.out.println("Σφάλμα: " + e.getMessage());
        }
    }

    private static void printAllEntries(BudgetRepository repo) {
        System.out.println("\n--- Λίστα Εγγραφών ---");
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
        System.out.println("Σύνολο: " + NumberFormat.getInstance().format(repo.calculateTotal()) + " €");
    }

    private static void handleAbsoluteChange(BudgetRepository repo, Scanner scanner) {
        System.out.print("Κωδικός: "); String code = scanner.nextLine();
        Optional<BudgetChangesEntry> entryOpt = repo.findByCode(code);
        if (entryOpt.isEmpty()) { System.out.println("Δεν βρέθηκε."); return; }
        BudgetChangesEntry entry = entryOpt.get();

        System.out.print("Ποσό: ");
        try {
            BigDecimal amount = new BigDecimal(scanner.nextLine()); 
            if (entry.getAmount().add(amount).compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("❌ Ανεπαρκές υπόλοιπο!"); return; 
            }
            System.out.print("Αιτιολογία: "); String just = scanner.nextLine();
            AbsoluteAmountChange change = new AbsoluteAmountChange(code, amount, just, CURRENT_USER);
            change.apply(entry); 
            changeHistory.push(change); 
            logAction("Αλλαγή Ποσού: " + amount + " σε " + code);
            System.out.println("✅ Νέο ποσό: " + NumberFormat.getInstance().format(entry.getAmount()));
        } catch (Exception e) { System.out.println("Σφάλμα: " + e.getMessage()); }
    }

    private static void handlePercentageChange(BudgetRepository repo, Scanner scanner) {
        System.out.print("Κωδικός: "); String code = scanner.nextLine();
        Optional<BudgetChangesEntry> entryOpt = repo.findByCode(code);
        if (entryOpt.isEmpty()) { System.out.println("Δεν βρέθηκε."); return; }
        BudgetChangesEntry entry = entryOpt.get();

        System.out.print("Ποσοστό %: ");
        try {
            double percent = Double.parseDouble(scanner.nextLine());
            // Precheck logic here (simplified for space)
            System.out.print("Αιτιολογία: "); String just = scanner.nextLine();
            PercentageChange change = new PercentageChange(code, percent, just, CURRENT_USER);
            change.apply(entry);
            changeHistory.push(change);
            logAction("Ποσοστιαία Αλλαγή (" + percent + "%) σε " + code);
            System.out.println("✅ Νέο ποσό: " + NumberFormat.getInstance().format(entry.getAmount()));
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
            System.out.print("Αιτιολογία: "); String just = scanner.nextLine();
            TransferChange transfer = new TransferChange(sourceCode, targetCode, amount, just, CURRENT_USER);
            transfer.apply(sourceOpt.get());        
            transfer.applyToTarget(targetOpt.get()); 
            changeHistory.push(transfer); 
            logAction("Μεταφορά: " + amount + " από " + sourceCode + " σε " + targetCode);
            System.out.println("✅ Μεταφορά ολοκληρώθηκε.");
        } catch (Exception e) { System.out.println("Σφάλμα: " + e.getMessage()); }
    }
}
