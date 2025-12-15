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
import java.util.stream.Collectors;

/**
 * Controller class for managing budget operations.
 * Designed to be used by a GUI or CLI.
 */
public class BudgetManager {

    private static final String CURRENT_USER = "admin";
    private static final String RESOURCES_PATH = "src/main/resources/";
    private static final String SAVED_PATH = RESOURCES_PATH + "saved_budgets/";
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final BudgetRepository repository;
    private final Stack<BudgetChange> changeHistory;
    private final List<String> auditLog;

    // State variables
    private String currentLoadedFilePath;
    private String currentEntityPrefix; // e.g., "1003" or "revenue_categories2_2025"
    private int currentBudgetType; // 0 for Revenue, 1 for Expense

    /**
     * Constructor initializes repository and state.
     */
    public BudgetManager() {
        this.repository = new BudgetRepository();
        this.changeHistory = new Stack<>();
        this.auditLog = new ArrayList<>();
        this.currentLoadedFilePath = null;
        this.currentEntityPrefix = null;
        this.currentBudgetType = -1;
        
        logAction("Application started by user " + CURRENT_USER);
    }

    // =========================================================================
    //                        INITIALIZATION & LOADING
    // =========================================================================

    /**
     * Sets the budget type (Revenue or Expense).
     * @param type 0 for Revenue, 1 for Expense
     * @throws AppException if type is invalid
     */
    public void setBudgetType(int type) throws AppException {
        if (type != 0 && type != 1) {
            throw new AppException("Invalid budget type selected. Please choose 0 or 1.");
        }
        this.currentBudgetType = type;
        
        if (type == 0) {
            // Automatically load revenue data
            loadRevenueData(null);
            logAction("Revenue data loaded.");
        }
        // If type == 1, the GUI should call getMinistriesList() next.
    }

    /**
     * Loads revenue data into the repository.
     * @param overrideFile optional file to load from (can be null for default)
     * @throws AppException if file not found or load fails
     */
    public void loadRevenueData(File overrideFile) throws AppException {
        this.currentEntityPrefix = "revenue_categories2_2025";
        File fileToLoad = (overrideFile != null) 
                ? overrideFile 
                : new File(RESOURCES_PATH + "revenue_categories2_2025.csv");

        // Check for auto-load of saved file (logic can be handled by GUI, 
        // here we load what is requested).
        
        try (Scanner fileScanner = new Scanner(fileToLoad)) {
            repository.clear(); // Clear previous data
            
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty() || line.startsWith("Κωδικός") || line.startsWith("Code")) {
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    try {
                        String code = parts[0].trim().replace("\uFEFF", "");
                        String desc = parts[1].trim();
                        BigDecimal amount = new BigDecimal(parts[2].trim());
                        
                        BudgetChangesEntry entry = new BudgetChangesEntry(code, desc, amount);
                        repository.save(entry);
                    } catch (Exception ex) {
                        // Ignore malformed lines
                    }
                }
            }
            this.currentLoadedFilePath = fileToLoad.getPath();
        } catch (FileNotFoundException e) {
            throw new AppException("Revenue file not found: " + fileToLoad.getPath());
        }
    }

    /**
     * Gets the list of Ministries for display.
     * Shows ONLY Code and Name, NOT amounts.
     * @return List of strings (e.g., "1003 - Parliament")
     * @throws AppException if file missing
     */
    public List<String> getMinistriesList() throws AppException {
        List<String> ministries = new ArrayList<>();
        File file = new File(RESOURCES_PATH + "expense_ministries_2025.csv");
        
        try (Scanner csvScanner = new Scanner(file)) {
            while (csvScanner.hasNextLine()) {
                String line = csvScanner.nextLine();
                if (line.trim().isEmpty() || line.startsWith("Κωδικός")) {
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    // Format: "1003 - Parliament of Greece"
                    ministries.add(parts[0].trim() + " - " + parts[1].trim());
                }
            }
        } catch (FileNotFoundException e) {
            throw new AppException("Ministries file not found.");
        }
        return ministries;
    }

    /**
     * Loads expenses for a specific organization code.
     * @param orgCode the organization code (e.g., "1003")
     * @param overrideFile optional file to load from
     * @throws AppException if loading fails
     */
    public void loadOrganizationExpenses(String orgCode, File overrideFile) throws AppException {
        this.currentEntityPrefix = orgCode;
        File fileToLoad = (overrideFile != null) 
                ? overrideFile 
                : new File(RESOURCES_PATH + orgCode + ".csv");

        try (Scanner fileScanner = new Scanner(fileToLoad)) {
            repository.clear(); // Clear previous data
            
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                // Skip empty lines or metadata lines (lines not starting with digit)
                if (line.isEmpty() || !Character.isDigit(line.charAt(0))) { 
                    continue; 
                }

                // Regex to split by comma ignoring quotes
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                if (parts.length >= 3) {
                    try {
                        String code = parts[0].trim();
                        String desc = parts[1].trim().replace("\"", "");
                        BigDecimal amount = new BigDecimal(parts[2].trim());
                        
                        BudgetChangesEntry entry = new BudgetChangesEntry(code, desc, amount);
                        repository.save(entry);
                    } catch (NumberFormatException e) {
                        // Ignore lines where amount is not a number
                    }
                }
            }
            this.currentLoadedFilePath = fileToLoad.getPath();
            logAction("Loaded expenses for organization: " + orgCode);
            
        } catch (FileNotFoundException e) {
            throw new AppException("Budget file for organization " + orgCode + " not found.");
        }
    }

    // =========================================================================
    //                        VIEW METHODS
    // =========================================================================

    /**
     * Returns the formatted table view of all entries.
     * To be used when user clicks "View All Entries" (Option 1).
     * @return Formatted String table
     */
    public String getEntriesView() {
        StringBuilder sb = new StringBuilder();
        
        // Header
        sb.append(String.format("%-10s %-50s %20s%n", "CODE", "CATEGORY", "AMOUNT (€)"));
        sb.append("----------------------------------------------------------------------------------\n");

        // Data rows
        repository.findAll().stream()
            .sorted(Comparator.comparing(BudgetChangesEntry::getCode))
            .forEach(entry -> {
                String desc = entry.getDescription().length() > 48 
                    ? entry.getDescription().substring(0, 48) + ".." 
                    : entry.getDescription();
                
                sb.append(String.format("%-10s %-50s %20s%n", 
                    entry.getCode(), 
                    desc,
                    NumberFormat.getInstance().format(entry.getAmount())));
            });

        sb.append("----------------------------------------------------------------------------------\n");
        sb.append("Total: ").append(NumberFormat.getInstance().format(repository.calculateTotal())).append(" €\n");
        
        return sb.toString();
    }
    
    /**
     * Returns the raw list of entries (useful for GUI Tables).
     * @return List of BudgetChangesEntry
     */
    public List<BudgetChangesEntry> getEntriesList() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(BudgetChangesEntry::getCode))
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalAmount() {
        return repository.calculateTotal();
    }

    // =========================================================================
    //                        MODIFICATION HANDLERS
    // =========================================================================

    /**
     * Applies an absolute amount change.
     * @param code Entry code
     * @param amountStr Amount as string
     * @param justification Reason
     * @return Success message
     * @throws AppException if validation fails
     */
    public String makeAbsoluteChange(String code, String amountStr, String justification) throws AppException {
        Optional<BudgetChangesEntry> entryOpt = repository.findByCode(code);
        if (entryOpt.isEmpty()) {
            throw new AppException("Entry code '" + code + "' not found.");
        }
        
        try {
            BigDecimal amount = new BigDecimal(amountStr);
            BudgetChangesEntry entry = entryOpt.get();
            
            // Pre-check
            if (entry.getAmount().add(amount).compareTo(BigDecimal.ZERO) < 0) {
                throw new AppException("Insufficient funds. Result would be negative.");
            }

            AbsoluteAmountChange change = new AbsoluteAmountChange(code, amount, justification, CURRENT_USER);
            change.apply(entry);
            changeHistory.push(change);
            
            logAction("Absolute Change: " + amount + " on " + code + ". Reason: " + justification);
            return "Success! New Amount: " + NumberFormat.getInstance().format(entry.getAmount()) + " €";
            
        } catch (NumberFormatException e) {
            throw new AppException("Invalid amount format.");
        }
    }

    /**
     * Applies a percentage change.
     * @param code Entry code
     * @param percentStr Percentage as string (e.g. "10", "-5")
     * @param justification Reason
     * @return Success message
     * @throws AppException if validation fails
     */
    public String makePercentageChange(String code, String percentStr, String justification) throws AppException {
        Optional<BudgetChangesEntry> entryOpt = repository.findByCode(code);
        if (entryOpt.isEmpty()) {
            throw new AppException("Entry code '" + code + "' not found.");
        }

        try {
            double percent = Double.parseDouble(percentStr);
            BudgetChangesEntry entry = entryOpt.get();
            
            // Pre-check
            BigDecimal currentAmount = entry.getAmount();
            BigDecimal percentageDecimal = BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100));
            if (currentAmount.add(currentAmount.multiply(percentageDecimal)).compareTo(BigDecimal.ZERO) < 0) {
                throw new AppException("Percentage decrease results in negative amount.");
            }

            PercentageChange change = new PercentageChange(code, percent, justification, CURRENT_USER);
            change.apply(entry);
            changeHistory.push(change);
            
            logAction("Percentage Change (" + percent + "%) on " + code);
            return "Success! New Amount: " + NumberFormat.getInstance().format(entry.getAmount()) + " €";
            
        } catch (NumberFormatException e) {
            throw new AppException("Invalid percentage format.");
        }
    }

    /**
     * Transfers funds between entries.
     * @param sourceCode Source code
     * @param targetCode Target code
     * @param amountStr Amount to transfer
     * @param justification Reason
     * @return Success message
     * @throws AppException if validation fails
     */
    public String makeTransfer(String sourceCode, String targetCode, String amountStr, String justification) throws AppException {
        Optional<BudgetChangesEntry> sourceOpt = repository.findByCode(sourceCode);
        Optional<BudgetChangesEntry> targetOpt = repository.findByCode(targetCode);

        if (sourceOpt.isEmpty() || targetOpt.isEmpty()) {
            throw new AppException("One or both codes not found.");
        }

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            if (sourceOpt.get().getAmount().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
                throw new AppException("Insufficient funds in source: " + sourceCode);
            }

            TransferChange transfer = new TransferChange(sourceCode, targetCode, amount, justification, CURRENT_USER);
            transfer.apply(sourceOpt.get());
            transfer.applyToTarget(targetOpt.get());
            changeHistory.push(transfer);
            
            logAction("Transfer: " + amount + " from " + sourceCode + " to " + targetCode);
            return "Transfer Complete.";
            
        } catch (NumberFormatException e) {
            throw new AppException("Invalid amount format.");
        }
    }

    /**
     * Undoes the last action.
     * @return Message describing what was undone
     * @throws AppException if nothing to undo
     */
    public String undoLastAction() throws AppException {
        if (changeHistory.isEmpty()) {
            throw new AppException("Nothing to undo.");
        }

        BudgetChange lastChange = changeHistory.pop();
        
        if (lastChange instanceof TransferChange) {
            TransferChange t = (TransferChange) lastChange;
            Optional<BudgetChangesEntry> s = repository.findByCode(t.getEntryCode());
            Optional<BudgetChangesEntry> tr = repository.findByCode(t.getTargetEntryCode());
            
            if (s.isPresent() && tr.isPresent()) {
                t.undo(s.get());
                t.undoFromTarget(tr.get());
            }
        } else {
            Optional<BudgetChangesEntry> e = repository.findByCode(lastChange.getEntryCode());
            if (e.isPresent()) {
                lastChange.undo(e.get());
            }
        }
        
        logAction("UNDO: " + lastChange.getDescription());
        return "Undone: " + lastChange.getType();
    }

    // =========================================================================
    //                        FILE MANAGEMENT (SAVE / LOAD)
    // =========================================================================

    /**
     * Gets a list of saved scenario files for the current context.
     * Used for populating GUI dropdowns.
     * @return List of filenames
     * @throws AppException if directory issues
     */
    public List<String> getAvailableSavedFiles() throws AppException {
        if (currentEntityPrefix == null) {
            throw new AppException("No active budget entity loaded.");
        }

        File savedDir = new File(SAVED_PATH);
        if (!savedDir.exists()) {
            return new ArrayList<>(); // Return empty list
        }

        File[] files = savedDir.listFiles((dir, name) -> name.startsWith(currentEntityPrefix));
        
        if (files == null) {
            return new ArrayList<>();
        }

        List<String> filenames = new ArrayList<>();
        for (File f : files) {
            filenames.add(f.getName());
        }
        return filenames;
    }

    /**
     * Loads a specific saved scenario file.
     * @param filename Name of the file in saved_budgets
     * @throws AppException if load fails
     */
    public void loadSavedScenario(String filename) throws AppException {
        File file = new File(SAVED_PATH + filename);
        if (!file.exists()) {
            throw new AppException("File not found: " + filename);
        }

        // Clear history as we are loading a new state
        changeHistory.clear();

        if (currentBudgetType == 0) {
            loadRevenueData(file);
        } else {
            // Prefix is presumably set, but we pass null override to use the logic inside
            // Actually, we need to call loadOrganizationExpenses with this file
            loadOrganizationExpenses(currentEntityPrefix, file);
        }
        logAction("Loaded scenario: " + filename);
    }

    /**
     * Saves the current state to a file.
     * @param userFilename Desired filename (can be empty for default)
     * @return The full path where it was saved
     * @throws AppException if save fails
     */
    public String saveWork(String userFilename) throws AppException {
        if (currentLoadedFilePath == null) {
            throw new AppException("No data loaded to save.");
        }

        File saveDir = new File(SAVED_PATH);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        String filename;
        if (userFilename == null || userFilename.trim().isEmpty()) {
            File originalFile = new File(currentLoadedFilePath);
            filename = originalFile.getName();
            if (!filename.contains("_updated")) {
                filename = filename.replace(".csv", "_updated.csv");
            }
        } else {
            if (!userFilename.endsWith(".csv")) {
                userFilename += ".csv";
            }
            // Enforce prefix consistency
            if (!userFilename.startsWith(currentEntityPrefix)) {
                userFilename = currentEntityPrefix + "_" + userFilename;
            }
            filename = userFilename;
        }

        File destinationFile = new File(saveDir, filename);
        
        boolean success;
        if (currentBudgetType == 0) {
            success = saveRevenueDataInternal(destinationFile.getPath());
        } else {
            success = saveExpenseDataInternal(destinationFile.getPath());
        }

        if (!success) {
            throw new AppException("Save operation failed.");
        }

        this.currentLoadedFilePath = destinationFile.getPath();
        logAction("Saved to: " + filename);
        return destinationFile.getPath();
    }

    // --- Internal Save Helpers ---

    private boolean saveRevenueDataInternal(String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write("Code,Category,Amount\n");
            repository.findAll().stream()
                .sorted(Comparator.comparing(BudgetChangesEntry::getCode))
                .forEach(entry -> {
                    try {
                        writer.write(String.format("%s,%s,%s%n", 
                            entry.getCode(), 
                            entry.getDescription(), 
                            entry.getAmount().toPlainString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean saveExpenseDataInternal(String path) {
        List<String> headerLines = new ArrayList<>();
        File sourceFile = new File(currentLoadedFilePath);
        
        try (Scanner fileScanner = new Scanner(sourceFile)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (!line.trim().isEmpty() && Character.isDigit(line.charAt(0))) {
                    break;
                }
                headerLines.add(line);
            }
        } catch (FileNotFoundException e) {
            // Proceed without headers if source lost
        }

        try (FileWriter writer = new FileWriter(path)) {
            for (String header : headerLines) {
                writer.write(header + "\n");
            }
            repository.findAll().stream()
                .sorted(Comparator.comparing(BudgetChangesEntry::getCode))
                .forEach(entry -> {
                    try {
                        writer.write(String.format("%s,\"%s\",%s%n", 
                            entry.getCode(), 
                            entry.getDescription(), 
                            entry.getAmount().toPlainString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // =========================================================================
    //                        LOGGING & UTILS
    // =========================================================================

    public List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }

    private void logAction(String detail) {
        auditLog.add(String.format("[%s] USER: %s | %s", 
            DTF.format(LocalDateTime.now()), CURRENT_USER, detail));
    }
}
