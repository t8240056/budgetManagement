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
public final class BudgetManager {

    private static final String CURRENT_USER = "admin";
    private static final String RESOURCES_PATH = "src/main/resources/";
    private static final String SAVED_PATH = RESOURCES_PATH + "saved_budgets/";
    private static final DateTimeFormatter DTF = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm:ss");

    private final BudgetRepository repository;
    private final Stack<BudgetChange> changeHistory;
    private final List<String> auditLog;

    // State variables
    private String currentLoadedFilePath;
    private String currentEntityPrefix; // e.g., "1003"
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
    //                            INITIALIZATION & LOADING
    // =========================================================================

    /**
     * Sets the budget type (Revenue or Expense).
     *
     * @param type 0 for Revenue, 1 for Expense
     * @throws AppException if type is invalid
     */
    public void setBudgetType(final int type) throws AppException {
        if (type != 0 && type != 1) {
            throw new AppException(
                    "Invalid budget type selected. Please choose 0 or 1.");
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
     *
     * @param overrideFile optional file to load from (can be null for default)
     * @throws AppException if file not found or load fails
     */
    public void loadRevenueData(final File overrideFile) throws AppException {
        this.currentEntityPrefix = "revenue_categories2_2025";
        final File fileToLoad = (overrideFile != null)
                ? overrideFile
                : new File(RESOURCES_PATH + "revenue_categories2_2025.csv");

        try (Scanner fileScanner = new Scanner(fileToLoad)) {
            repository.clear(); // Clear previous data

            while (fileScanner.hasNextLine()) {
                final String line = fileScanner.nextLine();
                if (line.trim().isEmpty() || line.startsWith("Κωδικός")
                        || line.startsWith("Code")) {
                    continue;
                }

                final String[] parts = line.split(",");
                if (parts.length >= 3) {
                    try {
                        final String code = parts[0].trim()
                                .replace("\uFEFF", "");
                        final String desc = parts[1].trim();
                        final BigDecimal amount = new BigDecimal(
                                parts[2].trim());

                        final BudgetChangesEntry entry = new BudgetChangesEntry(
                                code, desc, amount);
                        repository.save(entry);
                    } catch (final Exception ex) {
                        // Ignore malformed lines
                    }
                }
            }
            this.currentLoadedFilePath = fileToLoad.getPath();
        } catch (final FileNotFoundException e) {
            throw new AppException("Revenue file not found: "
                    + fileToLoad.getPath());
        }
    }

    /**
     * Gets the list of Ministries for display.
     * Shows ONLY Code and Name, NOT amounts.
     *
     * @return List of strings (e.g., "1003 - Parliament")
     * @throws AppException if file missing
     */
    public List<String> getMinistriesList() throws AppException {
        final List<String> ministries = new ArrayList<>();
        final File file = new File(RESOURCES_PATH
                + "expense_ministries_2025.csv");

        try (Scanner csvScanner = new Scanner(file)) {
            while (csvScanner.hasNextLine()) {
                final String line = csvScanner.nextLine();
                if (line.trim().isEmpty() || line.startsWith("Κωδικός")) {
                    continue;
                }

                final String[] parts = line.split(",");
                if (parts.length >= 2) {
                    // Format: "1003 - Parliament of Greece"
                    ministries.add(parts[0].trim() + " - " + parts[1].trim());
                }
            }
        } catch (final FileNotFoundException e) {
            throw new AppException("Ministries file not found.");
        }
        return ministries;
    }

    /**
     * Loads expenses for a specific organization code.
     *
     * @param orgCode      the organization code (e.g., "1003")
     * @param overrideFile optional file to load from
     * @throws AppException if loading fails
     */
    public void loadOrganizationExpenses(final String orgCode,
                                         final File overrideFile)
            throws AppException {
        this.currentEntityPrefix = orgCode;
        final File fileToLoad = (overrideFile != null)
                ? overrideFile
                : new File(RESOURCES_PATH + orgCode + ".csv");

        try (Scanner fileScanner = new Scanner(fileToLoad)) {
            repository.clear(); // Clear previous data

            while (fileScanner.hasNextLine()) {
                final String line = fileScanner.nextLine().trim();
                // Skip empty lines or metadata lines
                if (line.isEmpty() || !Character.isDigit(line.charAt(0))) {
                    continue;
                }

                // Regex to split by comma ignoring quotes
                final String[] parts = line.split(
                        ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (parts.length >= 3) {
                    try {
                        final String code = parts[0].trim();
                        final String desc = parts[1].trim().replace("\"", "");
                        final BigDecimal amount = new BigDecimal(
                                parts[2].trim());

                        final BudgetChangesEntry entry = new BudgetChangesEntry(
                                code, desc, amount);
                        repository.save(entry);
                    } catch (final NumberFormatException e) {
                        // Ignore lines where amount is not a number
                    }
                }
            }
            this.currentLoadedFilePath = fileToLoad.getPath();
            logAction("Loaded expenses for organization: " + orgCode);

        } catch (final FileNotFoundException e) {
            throw new AppException("Budget file for organization "
                    + orgCode + " not found.");
        }
    }

    // =========================================================================
    //                            VIEW METHODS
    // =========================================================================

    /**
     * Returns the formatted table view of all entries.
     *
     * @return Formatted String table
     */
    public String getEntriesView() {
        final StringBuilder sb = new StringBuilder();

        // Header
        sb.append(String.format("%-10s %-50s %20s%n",
                "CODE", "CATEGORY", "AMOUNT (€)"));
        sb.append("------------------------------------------"
                + "----------------------------------------\n");

        // Data rows
        repository.findAll().stream()
                .sorted(Comparator.comparing(BudgetChangesEntry::getCode))
                .forEach(entry -> {
                    String desc = entry.getDescription();
                    if (desc.length() > 48) {
                        desc = desc.substring(0, 48) + "..";
                    }

                    sb.append(String.format("%-10s %-50s %20s%n",
                            entry.getCode(),
                            desc,
                            NumberFormat.getInstance()
                                    .format(entry.getAmount())));
                });

        sb.append("------------------------------------------"
                + "----------------------------------------\n");
        sb.append("Total: ").append(NumberFormat.getInstance()
                .format(repository.calculateTotal())).append(" €\n");

        return sb.toString();
    }

    /**
     * Returns the raw list of entries (useful for GUI Tables).
     *
     * @return List of BudgetChangesEntry
     */
    public List<BudgetChangesEntry> getEntriesList() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(BudgetChangesEntry::getCode))
                .collect(Collectors.toList());
    }

    /**
     * Returns the total calculated amount.
     *
     * @return the total amount
     */
    public BigDecimal getTotalAmount() {
        return repository.calculateTotal();
    }

    // =========================================================================
    //                            MODIFICATION HANDLERS
    // =========================================================================

    /**
     * Applies an absolute amount change.
     *
     * @param code          Entry code
     * @param amountStr     Amount as string
     * @param justification Reason
     * @return Success message
     * @throws AppException if validation fails
     */
    public String makeAbsoluteChange(final String code,
                                     final String amountStr,
                                     final String justification)
            throws AppException {
        final Optional<BudgetChangesEntry> entryOpt = repository
                .findByCode(code);
        if (entryOpt.isEmpty()) {
            throw new AppException("Entry code '" + code + "' not found.");
        }

        try {
            final BigDecimal amount = new BigDecimal(amountStr);
            final BudgetChangesEntry entry = entryOpt.get();

            // Pre-check
            if (entry.getAmount().add(amount)
                    .compareTo(BigDecimal.ZERO) < 0) {
                throw new AppException(
                        "Insufficient funds. Result would be negative.");
            }

            final AbsoluteAmountChange change = new AbsoluteAmountChange(
                    code, amount, justification, CURRENT_USER);
            change.apply(entry);
            changeHistory.push(change);

            logAction("Absolute Change: " + amount + " on " + code
                    + ". Reason: " + justification);
            return "Success! New Amount: "
                    + NumberFormat.getInstance().format(entry.getAmount())
                    + " €";

        } catch (final NumberFormatException e) {
            throw new AppException("Invalid amount format.");
        }
    }

    /**
     * Applies a percentage change.
     *
     * @param code          Entry code
     * @param percentStr    Percentage as string (e.g. "10", "-5")
     * @param justification Reason
     * @return Success message
     * @throws AppException if validation fails
     */
    public String makePercentageChange(final String code,
                                       final String percentStr,
                                       final String justification)
            throws AppException {
        final Optional<BudgetChangesEntry> entryOpt = repository
                .findByCode(code);
        if (entryOpt.isEmpty()) {
            throw new AppException("Entry code '" + code + "' not found.");
        }

        try {
            final double percent = Double.parseDouble(percentStr);
            final BudgetChangesEntry entry = entryOpt.get();

            // Pre-check
            final BigDecimal currentAmount = entry.getAmount();
            final BigDecimal percentageDecimal = BigDecimal.valueOf(percent)
                    .divide(BigDecimal.valueOf(100));

            if (currentAmount.add(currentAmount.multiply(percentageDecimal))
                    .compareTo(BigDecimal.ZERO) < 0) {
                throw new AppException(
                        "Percentage decrease results in negative amount.");
            }

            final PercentageChange change = new PercentageChange(
                    code, percent, justification, CURRENT_USER);
            change.apply(entry);
            changeHistory.push(change);

            logAction("Percentage Change (" + percent + "%) on " + code);
            return "Success! New Amount: "
                    + NumberFormat.getInstance().format(entry.getAmount())
                    + " €";

        } catch (final NumberFormatException e) {
            throw new AppException("Invalid percentage format.");
        }
    }

    /**
     * Transfers funds between entries.
     *
     * @param sourceCode    Source code
     * @param targetCode    Target code
     * @param amountStr     Amount to transfer
     * @param justification Reason
     * @return Success message
     * @throws AppException if validation fails
     */
    public String makeTransfer(final String sourceCode,
                               final String targetCode,
                               final String amountStr,
                               final String justification)
            throws AppException {
        final Optional<BudgetChangesEntry> sourceOpt = repository
                .findByCode(sourceCode);
        final Optional<BudgetChangesEntry> targetOpt = repository
                .findByCode(targetCode);

        if (sourceOpt.isEmpty() || targetOpt.isEmpty()) {
            throw new AppException("One or both codes not found.");
        }

        try {
            final BigDecimal amount = new BigDecimal(amountStr);
            if (sourceOpt.get().getAmount().subtract(amount)
                    .compareTo(BigDecimal.ZERO) < 0) {
                throw new AppException("Insufficient funds in source: "
                        + sourceCode);
            }

            final TransferChange transfer = new TransferChange(
                    sourceCode, targetCode, amount, justification,
                    CURRENT_USER);
            transfer.apply(sourceOpt.get());
            transfer.applyToTarget(targetOpt.get());
            changeHistory.push(transfer);

            logAction("Transfer: " + amount + " from " + sourceCode
                    + " to " + targetCode);
            return "Transfer Complete.";

        } catch (final NumberFormatException e) {
            throw new AppException("Invalid amount format.");
        }
    }

    /**
     * Undoes the last action.
     *
     * @return Message describing what was undone
     * @throws AppException if nothing to undo
     */
    public String undoLastAction() throws AppException {
        if (changeHistory.isEmpty()) {
            throw new AppException("Nothing to undo.");
        }

        final BudgetChange lastChange = changeHistory.pop();

        if (lastChange instanceof TransferChange) {
            final TransferChange t = (TransferChange) lastChange;
            final Optional<BudgetChangesEntry> s = repository
                    .findByCode(t.getEntryCode());
            final Optional<BudgetChangesEntry> tr = repository
                    .findByCode(t.getTargetEntryCode());

            if (s.isPresent() && tr.isPresent()) {
                t.undo(s.get());
                t.undoFromTarget(tr.get());
            }
        } else {
            final Optional<BudgetChangesEntry> e = repository
                    .findByCode(lastChange.getEntryCode());
            if (e.isPresent()) {
                lastChange.undo(e.get());
            }
        }

        logAction("UNDO: " + lastChange.getDescription());
        return "Undone: " + lastChange.getType();
    }

    // =========================================================================
    //                            FILE MANAGEMENT (SAVE / LOAD)
    // =========================================================================

    /**
     * Gets a list of saved scenario files for the current context.
     *
     * @return List of filenames
     * @throws AppException if directory issues
     */
    public List<String> getAvailableSavedFiles() throws AppException {
        if (currentEntityPrefix == null) {
            throw new AppException("No active budget entity loaded.");
        }

        final File savedDir = new File(SAVED_PATH);
        if (!savedDir.exists()) {
            return new ArrayList<>(); // Return empty list
        }

        final File[] files = savedDir.listFiles((dir, name) ->
                name.startsWith(currentEntityPrefix));

        if (files == null) {
            return new ArrayList<>();
        }

        final List<String> filenames = new ArrayList<>();
        for (final File f : files) {
            filenames.add(f.getName());
        }
        return filenames;
    }

    /**
     * Loads a specific saved scenario file.
     *
     * @param filename Name of the file in saved_budgets
     * @throws AppException if load fails
     */
    public void loadSavedScenario(final String filename) throws AppException {
        final File file = new File(SAVED_PATH + filename);
        if (!file.exists()) {
            throw new AppException("File not found: " + filename);
        }

        // Clear history as we are loading a new state
        changeHistory.clear();

        if (currentBudgetType == 0) {
            loadRevenueData(file);
        } else {
            loadOrganizationExpenses(currentEntityPrefix, file);
        }
        logAction("Loaded scenario: " + filename);
    }

    /**
     * Saves the current state to a file.
     *
     * @param userFilename Desired filename (can be empty for default)
     * @return The full path where it was saved
     * @throws AppException if save fails
     */
    public String saveWork(final String userFilename) throws AppException {
        if (currentLoadedFilePath == null) {
            throw new AppException("No data loaded to save.");
        }

        final File saveDir = new File(SAVED_PATH);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        String filename;
        if (userFilename == null || userFilename.trim().isEmpty()) {
            final File originalFile = new File(currentLoadedFilePath);
            filename = originalFile.getName();
            if (!filename.contains("_updated")) {
                filename = filename.replace(".csv", "_updated.csv");
            }
        } else {
            String tempName = userFilename;
            if (!tempName.endsWith(".csv")) {
                tempName += ".csv";
            }
            // Enforce prefix consistency
            if (!tempName.startsWith(currentEntityPrefix)) {
                tempName = currentEntityPrefix + "_" + tempName;
            }
            filename = tempName;
        }

        final File destinationFile = new File(saveDir, filename);

        final boolean success;
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

    /**
     * Internal helper to save revenue data.
     * @param path The path to save to.
     * @return true if successful.
     */
    private boolean saveRevenueDataInternal(final String path) {
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
                        } catch (final IOException e) {
                            System.err.println("Error writing entry: "
                                    + e.getMessage());
                        }
                    });
            return true;
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * Internal helper to save expense data.
     * @param path The path to save to.
     * @return true if successful.
     */
    private boolean saveExpenseDataInternal(final String path) {
        final List<String> headerLines = new ArrayList<>();
        final File sourceFile = new File(currentLoadedFilePath);

        try (Scanner fileScanner = new Scanner(sourceFile)) {
            while (fileScanner.hasNextLine()) {
                final String line = fileScanner.nextLine();
                if (!line.trim().isEmpty()
                        && Character.isDigit(line.charAt(0))) {
                    break;
                }
                headerLines.add(line);
            }
        } catch (final FileNotFoundException e) {
            // Proceed without headers if source lost
        }

        try (FileWriter writer = new FileWriter(path)) {
            for (final String header : headerLines) {
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
                        } catch (final IOException e) {
                            System.err.println("Error writing entry: "
                                    + e.getMessage());
                        }
                    });
            return true;
        } catch (final IOException e) {
            return false;
        }
    }

    // =========================================================================
    //                            LOGGING & UTILS
    // =========================================================================

    /**
     * Gets the audit log.
     *
     * @return a copy of the log list.
     */
    public List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }

    /**
     * Appends an action to the log.
     *
     * @param detail The details of the action.
     */
    private void logAction(final String detail) {
        auditLog.add(String.format("[%s] USER: %s | %s",
                DTF.format(LocalDateTime.now()), CURRENT_USER, detail));
    }
}
