package auebprogramming;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

public class Main1 {
    
    private static final String CURRENT_USER = "admin"; 
    // Βασικό path για τα resources (φρόντισε να είναι σωστό στο σύστημά σου)
    private static final String RESOURCES_PATH = "src/main/resources/";

    public static void main(String[] args) {
        BudgetRepository repository = new BudgetRepository();
        Scanner scanner = new Scanner(System.in);

        // --- ΒΗΜΑ 1: Επιλογή Τύπου Προϋπολογισμού ---
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

        // --- ΛΟΓΙΚΗ ΕΣΟΔΩΝ ---
        if (chooseBudgetType == 0) { 
            loadRevenueData(repository); // (Φορτώνει και δείχνει preview όπως πριν)
            
        // --- ΛΟΓΙΚΗ ΕΞΟΔΩΝ (ΝΕΟ) ---
        } else if (chooseBudgetType == 1) { 
            
            // 1. Εμφάνιση λίστας Φορέων
            loadMinistries(); 

            // 2. Επιλογή Φορέα
            System.out.print("\nΕπίλεξε Κωδικό Φορέα (π.χ. 1003) για επεξεργασία: ");
            String orgCode = scanner.nextLine().trim();

            // 3. Φόρτωση δεδομένων του συγκεκριμένου Φορέα (π.χ. 1003.csv)
            boolean loaded = loadOrganizationExpenses(repository, orgCode);
            
            if (!loaded) {
                System.out.println("Δεν ήταν δυνατή η φόρτωση του φορέα. Τερματισμός.");
                scanner.close();
                return;
            }

        } else {
            System.out.println("Invalid choice. Please enter 0 or 1.");
            scanner.close();
            return;
        }

        // --- ΒΗΜΑ 2: Κύριο Μενού (ΚΟΙΝΟ ΓΙΑ ΕΣΟΔΑ & ΕΞΟΔΑ) ---
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

    // =========================================================================
    //                            ΜΕΘΟΔΟΙ ΕΞΟΔΩΝ (NEW)
    // =========================================================================

    /**
     * Διαβάζει το expense_ministries_2025.csv και εμφανίζει τους φορείς.
     */
    private static void loadMinistries() {
        System.out.println("\n--- Λίστα Φορέων Κεντρικής Διοίκησης ---");
        System.out.printf("%-10s %-70s %20s%n", "ΚΩΔΙΚΟΣ", "ΦΟΡΕΑΣ", "ΣΥΝΟΛΟ (€)");
        System.out.println("--------------------------------------------------------------------------------------------------------");

        try {
            File file = new File(RESOURCES_PATH + "expense_ministries_2025.csv");
            Scanner csvScanner = new Scanner(file);

            while (csvScanner.hasNextLine()) {
                String line = csvScanner.nextLine();
                if (line.trim().isEmpty()) continue;
                // Αγνοούμε την επικεφαλίδα
                if (line.startsWith("Κωδικός") || line.startsWith("Code")) continue;

                // Προσοχή: Το αρχείο αυτό δεν έχει quotes στα νούμερα, είναι απλό CSV
                String[] parts = line.split(",");
                
                // Δομή: Κωδικός(0), Φορέας(1), Τακτικός(2), ΠΔΕ(3), Σύνολο(4)
                if (parts.length >= 5) {
                    String code = parts[0].trim();
                    String name = parts[1].trim();
                    String totalStr = parts[4].trim(); // Παίρνουμε το γενικό σύνολο

                    try {
                        BigDecimal total = new BigDecimal(totalStr);
                        System.out.printf("%-10s %-70s %20s%n", 
                            code, 
                            name.length() > 68 ? name.substring(0, 68) + ".." : name, 
                            NumberFormat.getInstance().format(total));
                    } catch (NumberFormatException e) {
                        // Αν δεν είναι νούμερο, το αγνοούμε ή το τυπώνουμε απλά
                    }
                }
            }
            csvScanner.close();
            System.out.println("--------------------------------------------------------------------------------------------------------");
        } catch (FileNotFoundException e) {
            System.out.println("Σφάλμα: Το αρχείο expense_ministries_2025.csv δεν βρέθηκε.");
        }
    }

    /**
     * Φορτώνει τα έξοδα ενός συγκεκριμένου φορέα (π.χ. 1003.csv) στο Repository.
     */
    private static boolean loadOrganizationExpenses(BudgetRepository repository, String orgCode) {
        String filename = RESOURCES_PATH + orgCode + ".csv";
        System.out.println("--- Φόρτωση εξόδων από: " + filename + " ---");

        try {
            File file = new File(filename);
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                
                if (line.isEmpty()) continue;

                // Λογική Parsing για τα ειδικά αρχεία (π.χ. 1003.csv)
                // Αγνοούμε γραμμές που είναι metadata (π.χ. "ΒΟΥΛΗ..", "Οικονομικό έτος..")
                // Κρατάμε μόνο γραμμές που ξεκινάνε με νούμερο (Κωδικός Μείζ. κατηγορίας)
                if (!Character.isDigit(line.charAt(0))) {
                    continue; 
                }

                // Το αρχείο έχει μορφή: 21,"Παροχές σε εργαζομένους",127657000
                // Χρησιμοποιούμε split στο κόμμα
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); 
                // (Το παραπάνω regex κάνει split στο κόμμα ΑΛΛΑ αγνοεί κόμματα μέσα σε quotes " ", αν υπάρχουν)
                // Αν σε δυσκολεύει, το απλό split(",") δουλεύει αν δεν υπάρχουν κόμματα μέσα στα ονόματα.

                if (parts.length >= 3) {
                    String code = parts[0].trim();
                    String desc = parts[1].trim().replace("\"", ""); // Αφαιρούμε τα εισαγωγικά "
                    String amountStr = parts[2].trim();

                    try {
                        BigDecimal amount = new BigDecimal(amountStr);
                        BudgetChangesEntry entry = new BudgetChangesEntry(code, desc, amount);
                        repository.save(entry);
                    } catch (NumberFormatException e) {
                        // Αν η γραμμή μοιάζει με νούμερο αλλά δεν έχει ποσό, την αγνοούμε
                    }
                }
            }
            fileScanner.close();
            System.out.println("Επιτυχία! Φορτώθηκαν " + repository.count() + " κατηγορίες δαπανών για τον φορέα " + orgCode + ".");
            return true;

        } catch (FileNotFoundException e) {
            System.out.println("❌ Σφάλμα: Δεν υπάρχει αρχείο προϋπολογισμού για τον φορέα " + orgCode + ".");
            return false;
        } catch (Exception e) {
            System.out.println("Σφάλμα κατά την ανάγνωση του αρχείου: " + e.getMessage());
            return false;
        }
    }


    // =========================================================================
    //                        ΜΕΘΟΔΟΙ ΕΣΟΔΩΝ & ΚΟΙΝΕΣ
    // =========================================================================

    private static void loadRevenueData(BudgetRepository repository) {
        // --- PREVIEW (ΟΠΩΣ ΠΡΙΝ) ---
        System.out.println("\n--- Προεπισκόπηση Αρχείου Εσόδων ---");
        System.out.printf("%-10s %-50s %20s%n", "ΚΩΔΙΚΟΣ", "ΚΑΤΗΓΟΡΙΑ", "ΠΟΣΟ (€)");
        System.out.println("----------------------------------------------------------------------------------");

        try {
            File file = new File(RESOURCES_PATH + "revenue_categories2_2025.csv");
            Scanner csvScanner = new Scanner(file);

            while (csvScanner.hasNextLine()) {
                String line = csvScanner.nextLine();
                if (line.trim().isEmpty()) continue;
                if (line.startsWith("Κωδικός") || line.startsWith("Code")) continue;

                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String code = parts[0].trim().replace("\uFEFF", ""); 
                    String desc = parts[1].trim();
                    if (desc.length() > 48) desc = desc.substring(0, 48) + "..";
                    
                    try {
                        BigDecimal amount = new BigDecimal(parts[2].trim());
                        System.out.printf("%-10s %-50s %20s%n", 
                            code, desc, NumberFormat.getInstance().format(amount));
                    } catch (NumberFormatException e) {
                       // ignore
                    }
                }
            }
            csvScanner.close();
            System.out.println("----------------------------------------------------------------------------------");
        } catch (FileNotFoundException e) {
            System.out.println("Το αρχείο CSV δεν βρέθηκε.");
        }
        System.out.println(); 

        // --- LOAD INTO REPO ---
        try {
            File file = new File(RESOURCES_PATH + "revenue_categories2_2025.csv");
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
                    
                    try {
                        BudgetChangesEntry entry = new BudgetChangesEntry(code, desc, new BigDecimal(amountStr));
                        repository.save(entry);
                    } catch (Exception ex) {
                        // ignore malformed lines
                    }
                }
            }
            fileScanner.close();
            System.out.println("Φορτώθηκαν επιτυχώς " + repository.count() + " εγγραφές εσόδων.");
        } catch (Exception e) {
            System.out.println("Σφάλμα: " + e.getMessage());
        }
    }

    // --- ΟΙ ΓΝΩΣΤΕΣ ΜΕΘΟΔΟΙ ΕΠΕΞΕΡΓΑΣΙΑΣ (ΜΕ PRE-CHECKS) ---

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
        System.out.println("Σύνολο: " + NumberFormat.getInstance().format(repo.calculateTotal()) + " €");
    }

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

            // PRE-CHECK
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
        if (entryOpt.isEmpty()) {
            System.out.println("Ο κωδικός '" + code + "' δεν βρέθηκε.");
            return;
        }
        BudgetChangesEntry entry = entryOpt.get();

        System.out.print("Δώσε ποσοστό % (π.χ. 10 για +10%, -50 για -50%): ");
        try {
            double percent = Double.parseDouble(scanner.nextLine());
            
            // PRE-CHECK
            BigDecimal currentAmount = entry.getAmount();
            BigDecimal percentageDecimal = BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100));
            BigDecimal changeAmount = currentAmount.multiply(percentageDecimal);
            BigDecimal potentialNewAmount = currentAmount.add(changeAmount);

            if (potentialNewAmount.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("❌ Σφάλμα: Η ποσοστιαία μείωση οδηγεί σε αρνητικό ποσό.");
                return;
            }

            System.out.print("Αιτιολογία: ");
            String just = scanner.nextLine();

            PercentageChange change = new PercentageChange(code, percent, just, CURRENT_USER);
            change.apply(entry);
            
            System.out.println("✅ Επιτυχία! Διαφορά ποσού: " + NumberFormat.getInstance().format(change.getDifference()));
            System.out.println("   Νέο ποσό: " + NumberFormat.getInstance().format(entry.getAmount()) + " €");

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

            // PRE-CHECK
            BigDecimal sourceBalance = sourceOpt.get().getAmount();
            if (sourceBalance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("❌ Σφάλμα: Ανεπαρκές υπόλοιπο στην πηγή (" + sourceCode + ").");
                System.out.println("   Διαθέσιμα: " + NumberFormat.getInstance().format(sourceBalance));
                return;
            }

            System.out.print("Αιτιολογία: ");
            String just = scanner.nextLine();

            TransferChange transfer = new TransferChange(sourceCode, targetCode, amount, just, CURRENT_USER);
            transfer.apply(sourceOpt.get());        
            transfer.applyToTarget(targetOpt.get()); 

            System.out.println("✅ Μεταφορά ολοκληρώθηκε.");
            System.out.println("   Νέο ποσό Πηγής: " + NumberFormat.getInstance().format(sourceOpt.get().getAmount()));
            System.out.println("   Νέο ποσό Προορισμού: " + NumberFormat.getInstance().format(targetOpt.get().getAmount()));

        } catch (Exception e) {
            System.out.println("Σφάλμα μεταφοράς: " + e.getMessage());
        }
    }
}
