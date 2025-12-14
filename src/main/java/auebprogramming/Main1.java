package auebprogramming;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
    private static final String SAVED_PATH = RESOURCES_PATH + "saved_budgets/";
    
    // --- STATE VARIABLES ---
    private static Stack<BudgetChange> changeHistory = new Stack<>();
    private static List<String> auditLog = new ArrayList<>();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    // Μεταβλητές State
    private static String currentLoadedFilePath = null; 
    private static String currentEntityPrefix = null; 
    private static int currentBudgetType = -1; 
    
    private static Scanner scanner;

    public static void main(String[] args) {
        BudgetRepository repository = new BudgetRepository();
        scanner = new Scanner(System.in); 

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
            loadRevenueData(repository, null); 
            logAction("Φόρτωση δεδομένων Εσόδων");
        } else if (currentBudgetType == 1) { 
            loadMinistries(); 
            boolean orgLoaded = false;
            while (!orgLoaded) {
                System.out.print("\nΕπίλεξε Κωδικό Φορέα (π.χ. 1003) για επεξεργασία: ");
                String orgCode = scanner.nextLine().trim();
                orgLoaded = loadOrganizationExpenses(repository, orgCode, null); 
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
            System.out.println("7. Αποθήκευση Αλλαγών (Save As) 💾"); 
            System.out.println("8. Φόρτωση από Αρχείο (Load) 📂"); 
            System.out.println("9. Έξοδος");
            System.out.print("Επιλογή: ");

            String choice = "";
            if (scanner.hasNextLine()) {
                choice = scanner.nextLine();
            }

            switch (choice) {
                case "1": printAllEntries(repository); break;
                case "2": handleAbsoluteChange(repository, scanner); break;
                case "3": handlePercentageChange(repository, scanner); break;
                case "4": handleTransfer(repository, scanner); break;
                case "5": handleUndo(repository); break;
                case "6": printAuditLog(); break;
                case "7": handleSave(repository); break;
                case "8": handleLoadSaved(repository); break;
                case "9":
                    keepRunning = false;
                    logAction("Έξοδος από την εφαρμογή");
                    System.out.println("Έξοδος...");
                    break;
                default: System.out.println("Μη έγκυρη επιλογή.");
            }
        }
        scanner.close();
    }

    // =========================================================================
    //                        LOAD METHODS
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
        } catch (FileNotFoundException e) { System.out.println("File not found"); }
    }

    private static boolean loadOrganizationExpenses(BudgetRepository repository, String orgCode, File overrideFile) {
        currentEntityPrefix = orgCode; 
        
        File fileToLoad;
        if (overrideFile != null) {
            fileToLoad = overrideFile;
        } else {
            String originalPath = RESOURCES_PATH + orgCode + ".csv";
            fileToLoad = new File(originalPath);
        }

        System.out.println("--- Φόρτωση εξόδων από: " + fileToLoad.getName() + " ---");

        try {
            Scanner fileScanner = new Scanner(fileToLoad);
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
            currentLoadedFilePath = fileToLoad.getPath(); 
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("❌ Σφάλμα: Δεν υπάρχει αρχείο για τον φορέα " + orgCode);
            return false;
        }
    }

    private static boolean loadRevenueData(BudgetRepository repository, File overrideFile) {
        currentEntityPrefix = "revenue_categories2_2025"; 
        
        File fileToLoad;
        if (overrideFile != null) {
            fileToLoad = overrideFile;
        } else {
            String originalPath = RESOURCES_PATH + "revenue_categories2_2025.csv";
            fileToLoad = new File(originalPath);
        }
        
        System.out.println("\n--- Προεπισκόπηση Αρχείου Εσόδων ---");
        System.out.printf("%-10s %-50s %20s%n", "ΚΩΔΙΚΟΣ", "ΚΑΤΗΓΟΡΙΑ", "ΠΟΣΟ (€)");
        System.out.println("----------------------------------------------------------------------------------");
        try {
            Scanner csvScanner = new Scanner(fileToLoad);
            while (csvScanner.hasNextLine()) {
                String line = csvScanner.nextLine();
                if (line.trim().isEmpty() || line.startsWith("Κωδικός")) continue;
                String[] parts = line.split(","); 
                if (parts.length >= 3) {
                    try {
                        String code = parts[0].trim().replace("\uFEFF", ""); 
                        BigDecimal amount = new BigDecimal(parts[2].trim());
                        System.out.printf("%-10s %-50s %20s%n", code, 
                            parts[1].trim().length() > 48 ? parts[1].trim().substring(0, 48)+".." : parts[1].trim(), 
                            NumberFormat.getInstance().format(amount));
                    } catch (Exception ex) { }
                }
            }
            csvScanner.close();
            System.out.println("----------------------------------------------------------------------------------");
        } catch (FileNotFoundException e) { System.out.println("CSV not found for preview"); }
        System.out.println();
        
        try {
            Scanner fileScanner = new Scanner(fileToLoad);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty() || line.startsWith("Κωδικός")) continue;
                String[] parts = line.split(","); 
                if (parts.length >= 3) {
                    try {
                        String code = parts[0].trim().replace("\uFEFF", ""); 
                        BudgetChangesEntry entry = new BudgetChangesEntry(code, parts[1].trim(), new BigDecimal(parts[2].trim()));
                        repository.save(entry);
                    } catch (Exception ex) { }
                }
            }
            fileScanner.close();
            System.out.println("Φορτώθηκαν επιτυχώς " + repository.count() + " εγγραφές εσόδων.");
            currentLoadedFilePath = fileToLoad.getPath();
            return true;
        } catch (Exception e) { 
            System.out.println("Error loading revenue: " + e.getMessage());
            return false;
        }
    }

    private static void handleLoadSaved(BudgetRepository repo) {
        System.out.println("\n--- Φόρτωση Αποθηκευμένου Αρχείου ---");
        
        File savedDir = new File(SAVED_PATH);
        if (!savedDir.exists() || !savedDir.isDirectory()) {
            System.out.println("⚠️ Δεν βρέθηκε φάκελος saved_budgets.");
            return;
        }

        File[] files = savedDir.listFiles((dir, name) -> name.startsWith(currentEntityPrefix));

        if (files == null || files.length == 0) {
            System.out.println("⚠️ Δεν βρέθηκαν αποθηκευμένα αρχεία για: " + currentEntityPrefix);
            return;
        }

        System.out.println("Διαθέσιμα αρχεία:");
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + ". " + files[i].getName());
        }
        System.out.println("0. Ακύρωση");

        System.out.print("Επίλεξε αρχείο: ");
        try {
            int selection = Integer.parseInt(scanner.nextLine());
            if (selection == 0) return;
            
            if (selection > 0 && selection <= files.length) {
                File selectedFile = files[selection - 1];
                System.out.println("🔄 Φόρτωση: " + selectedFile.getName() + "...");
                
                repo.clear(); 
                changeHistory.clear();
                
                boolean success;
                if (currentBudgetType == 0) {
                    success = loadRevenueData(repo, selectedFile); 
                } else {
                    success = loadOrganizationExpenses(repo, currentEntityPrefix, selectedFile);
                }

                if (success) {
                    System.out.println("✅ Το αρχείο φορτώθηκε επιτυχώς!");
                    logAction("Φόρτωση αρχείου χρήστη: " + selectedFile.getName());
                    printAllEntries(repo);
                }
            } else {
                System.out.println("❌ Μη έγκυρη επιλογή.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Μη έγκυρη είσοδος.");
        }
    }

    // =========================================================================
    //                        HANDLERS (ΜΕ ΤΑ ΑΝΑΛΥΤΙΚΑ ΜΗΝΥΜΑΤΑ)
    // =========================================================================

    private static void handleAbsoluteChange(BudgetRepository repo, Scanner scanner) {
        System.out.print("Δώσε τον Κωδικό (Code) της εγγραφής: ");
        String code = scanner.nextLine();
        
        Optional<BudgetChangesEntry> entryOpt = repo.findByCode(code);
        if (entryOpt.isEmpty()) {
            System.out.println("Ο κωδικός '" + code + "' δεν βρέθηκε.");
            return;
        }
        BudgetChangesEntry entry = entryOpt.get();

        // --- RESTORED PROMPT ---
        System.out.print("Δώσε ποσό αλλαγής (π.χ. +500 για αύξηση, -200 για μείωση): ");
        try {
            String amountInput = scanner.nextLine();
            BigDecimal amount = new BigDecimal(amountInput); 

            BigDecimal potentialNewAmount = entry.getAmount().add(amount);
            if (potentialNewAmount.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("❌ Σφάλμα: Ανεπαρκές υπόλοιπο!"); 
                System.out.println("   Τρέχον ποσό: " + NumberFormat.getInstance().format(entry.getAmount()));
                System.out.println("   Αποτέλεσμα: " + NumberFormat.getInstance().format(potentialNewAmount));
                return; 
            }

            System.out.print("Αιτιολογία: ");
            String just = scanner.nextLine();

            AbsoluteAmountChange change = new AbsoluteAmountChange(code, amount, just, CURRENT_USER);
            change.apply(entry); 
            changeHistory.push(change); 
            
            logAction("Αλλαγή Ποσού (" + change.getType() + "): " + NumberFormat.getInstance().format(amount) + " € στον κωδικό " + code + ". Αιτία: " + just);

            System.out.println("✅ Επιτυχία! Τύπος: " + change.getType());
            System.out.println("   Νέο ποσό: " + NumberFormat.getInstance().format(entry.getAmount()) + " €");
            
        } catch (Exception e) {
            System.out.println("Σφάλμα: " + e.getMessage());
        }
    }

    private static void handlePercentageChange(BudgetRepository repo, Scanner scanner) {
        System.out.print("Δώσε τον Κωδικό (Code) της εγγραφής: ");
        String code = scanner.nextLine();
        Optional<BudgetChangesEntry> entryOpt = repo.findByCode(code);
        if (entryOpt.isEmpty()) { System.out.println("Ο κωδικός δεν βρέθηκε."); return; }
        BudgetChangesEntry entry = entryOpt.get();

        // --- RESTORED PROMPT ---
        System.out.print("Δώσε ποσοστό % (π.χ. 10 για +10%, -50 για -50%): ");
        try {
            double percent = Double.parseDouble(scanner.nextLine());
            BigDecimal currentAmount = entry.getAmount();
            BigDecimal percentageDecimal = BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100));
            if (currentAmount.add(currentAmount.multiply(percentageDecimal)).compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("❌ Σφάλμα: Αρνητικό υπόλοιπο."); return;
            }
            System.out.print("Αιτιολογία: "); String just = scanner.nextLine();
            PercentageChange change = new PercentageChange(code, percent, just, CURRENT_USER);
            change.apply(entry);
            changeHistory.push(change); 
            
            logAction("Ποσοστιαία Αλλαγή (" + percent + "%): " + code);
            
            System.out.println("✅ Επιτυχία! Διαφορά ποσού: " + NumberFormat.getInstance().format(change.getDifference()));
            System.out.println("   Νέο ποσό: " + NumberFormat.getInstance().format(entry.getAmount()) + " €");
        } catch (Exception e) { System.out.println("Σφάλμα: " + e.getMessage()); }
    }

    private static void handleTransfer(BudgetRepository repo, Scanner scanner) {
        System.out.print("Δώσε τον Κωδικό ΠΗΓΗΣ (Source Code): ");
        String sourceCode = scanner.nextLine();
        
        System.out.print("Δώσε τον Κωδικό ΠΡΟΟΡΙΣΜΟΥ (Target Code): ");
        String targetCode = scanner.nextLine();
        
        Optional<BudgetChangesEntry> sourceOpt = repo.findByCode(sourceCode);
        Optional<BudgetChangesEntry> targetOpt = repo.findByCode(targetCode);
        if (sourceOpt.isEmpty() || targetOpt.isEmpty()) { System.out.println("Λάθος κωδικοί."); return; }

        // --- RESTORED PROMPT ---
        System.out.print("Δώσε ποσό μεταφοράς: ");
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
            
            logAction("Μεταφορά: " + NumberFormat.getInstance().format(amount) + " € από " + sourceCode + " σε " + targetCode);
            
            System.out.println("✅ Μεταφορά ολοκληρώθηκε.");
            System.out.println("   Νέο ποσό Πηγής: " + NumberFormat.getInstance().format(sourceOpt.get().getAmount()));
            System.out.println("   Νέο ποσό Προορισμού: " + NumberFormat.getInstance().format(targetOpt.get().getAmount()));
        } catch (Exception e) { System.out.println("Σφάλμα: " + e.getMessage()); }
    }

    // =========================================================================
    //                        SAVE FUNCTIONALITY
    // =========================================================================

    private static void handleSave(BudgetRepository repo) {
        if (currentLoadedFilePath == null) {
            System.out.println("❌ Σφάλμα: Δεν υπάρχει φορτωμένο αρχείο."); return;
        }
        File saveDir = new File(SAVED_PATH);
        if (!saveDir.exists()) saveDir.mkdir();

        System.out.print("Δώσε όνομα για αποθήκευση (ή πάτα Enter για default '_updated'): ");
        // Το replaceAll παίρνει τα κενά (spaces) και τα κάνει κάτω παύλες (_)
        String userFilename = scanner.nextLine().trim().replaceAll("\\s+", "_");
        
        String filename;
        if (userFilename.isEmpty()) {
            File originalFile = new File(currentLoadedFilePath);
            filename = originalFile.getName();
            if (!filename.contains("_updated")) {
                filename = filename.replace(".csv", "_updated.csv");
            }
        } else {
            if (!userFilename.endsWith(".csv")) userFilename += ".csv";
            if (!userFilename.startsWith(currentEntityPrefix)) {
                userFilename = currentEntityPrefix + "_" + userFilename;
            }
            filename = userFilename;
        }

        File destinationFile = new File(saveDir, filename);
        System.out.println("💾 Αποθήκευση στο: " + destinationFile.getPath() + " ...");
        
        boolean success = (currentBudgetType == 0) ? saveRevenueData(repo, destinationFile.getPath()) : saveExpenseData(repo, destinationFile.getPath());

        if (success) {
            System.out.println("✅ Η αποθήκευση ολοκληρώθηκε!");
            logAction("Αποθήκευση: " + filename);
            currentLoadedFilePath = destinationFile.getPath();
        } else {
            System.out.println("❌ Η αποθήκευση απέτυχε.");
        }
    }

    private static boolean saveRevenueData(BudgetRepository repo, String destinationPath) {
        try (FileWriter writer = new FileWriter(destinationPath)) {
            writer.write("Κωδικός,Κατηγορία,Ποσό\n");
            repo.findAll().stream()
                .sorted(Comparator.comparing(BudgetChangesEntry::getCode))
                .forEach(entry -> {
                    try {
                        writer.write(String.format("%s,%s,%s\n", entry.getCode(), entry.getDescription(), entry.getAmount().toPlainString()));
                    } catch (IOException e) { e.printStackTrace(); }
                });
            return true;
        } catch (IOException e) { return false; }
    }

    private static boolean saveExpenseData(BudgetRepository repo, String destinationPath) {
        List<String> headerLines = new ArrayList<>();
        File sourceFile = new File(currentLoadedFilePath);
        try (Scanner fileScanner = new Scanner(sourceFile)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (!line.trim().isEmpty() && Character.isDigit(line.charAt(0))) break;
                headerLines.add(line);
            }
        } catch (FileNotFoundException e) { }

        try (FileWriter writer = new FileWriter(destinationPath)) {
            for (String header : headerLines) writer.write(header + "\n");
            repo.findAll().stream()
                .sorted(Comparator.comparing(BudgetChangesEntry::getCode))
                .forEach(entry -> {
                    try {
                        writer.write(String.format("%s,\"%s\",%s\n", entry.getCode(), entry.getDescription(), entry.getAmount().toPlainString()));
                    } catch (IOException e) { e.printStackTrace(); }
                });
            return true;
        } catch (IOException e) { return false; }
    }

    // =========================================================================
    //                        LOGGING & UNDO & HELPER
    // =========================================================================

    private static void logAction(String actionDetail) {
        auditLog.add(String.format("[%s] USER: %s | %s", dtf.format(LocalDateTime.now()), CURRENT_USER, actionDetail));
    }

    private static void printAuditLog() {
        System.out.println("\n=================== SYSTEM AUDIT LOG ===================");
        if (auditLog.isEmpty()) System.out.println("   (Κανένα καταγεγραμμένο συμβάν)");
        else for (String entry : auditLog) System.out.println(entry);
        System.out.println("========================================================");
    }

    private static void handleUndo(BudgetRepository repo) {
        if (changeHistory.isEmpty()) { System.out.println("⚠️ Τίποτα για αναίρεση."); return; }
        BudgetChange lastChange = changeHistory.pop();
        System.out.println("🔄 Αναίρεση: " + lastChange.getType());
        logAction("UNDO: " + lastChange.getDescription());
        if (lastChange instanceof TransferChange) {
            TransferChange t = (TransferChange) lastChange;
            Optional<BudgetChangesEntry> s = repo.findByCode(t.getEntryCode());
            Optional<BudgetChangesEntry> tr = repo.findByCode(t.getTargetEntryCode());
            if (s.isPresent() && tr.isPresent()) { t.undo(s.get()); t.undoFromTarget(tr.get()); }
        } else {
            Optional<BudgetChangesEntry> e = repo.findByCode(lastChange.getEntryCode());
            if (e.isPresent()) lastChange.undo(e.get());
        }
    }

    private static void printAllEntries(BudgetRepository repo) {
        System.out.println("\n--- Λίστα Εγγραφών ---");
        System.out.printf("%-10s %-50s %20s%n", "ΚΩΔΙΚΟΣ", "ΚΑΤΗΓΟΡΙΑ", "ΠΟΣΟ (€)");
        System.out.println("----------------------------------------------------------------------------------");
        repo.findAll().stream().sorted(Comparator.comparing(BudgetChangesEntry::getCode)).forEach(entry -> 
            System.out.printf("%-10s %-50s %20s%n", entry.getCode(), 
                entry.getDescription().length() > 48 ? entry.getDescription().substring(0, 48) + ".." : entry.getDescription(), 
                NumberFormat.getInstance().format(entry.getAmount())));
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("Σύνολο: " + NumberFormat.getInstance().format(repo.calculateTotal()) + " €");
    }
}
