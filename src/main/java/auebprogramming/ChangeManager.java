package auebprogramming;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Main facade class for managing all budget changes.
 * Provides a unified interface for applying, undoing, and redoing changes.
 * Implements the Facade pattern.
 */
public final class ChangeManager {

    /** Data repository. */
    private final BudgetRepository repository;

    /** History stack for undo. */
    private final Stack<BudgetChange> changeHistory = new Stack<>();

    /** Redo stack. */
    private final Stack<BudgetChange> redoStack = new Stack<>();

    /**
     * Constructs a ChangeManager with the given repository.
     *
     * @param repo the repository containing budget entries
     */
    public ChangeManager(final BudgetRepository repo) {
        this.repository = repo;
    }

    /**
     * Increases an entry's amount by an absolute value.
     *
     * @param code          entry code to increase
     * @param amount        amount to add (must be positive)
     * @param justification reason for the increase
     * @param userId        who is making the change
     */
    public void increaseAmount(final String code,
                               final BigDecimal amount,
                               final String justification,
                               final String userId) {
        final BudgetChange change = new AbsoluteAmountChange(
                code, amount, justification, userId);
        applyChange(change);
    }

    /**
     * Decreases an entry's amount by an absolute value.
     *
     * @param code          entry code to decrease
     * @param amount        amount to subtract (must be positive)
     * @param justification reason for the decrease
     * @param userId        who is making the change
     */
    public void decreaseAmount(final String code,
                               final BigDecimal amount,
                               final String justification,
                               final String userId) {
        final BudgetChange change = new AbsoluteAmountChange(
                code, amount.negate(), justification, userId);
        applyChange(change);
    }

    /**
     * Increases an entry's amount by a percentage.
     *
     * @param code          entry code to increase
     * @param percentage    percentage to increase by (e.g., 10.0 for 10%)
     * @param justification reason for the increase
     * @param userId        who is making the change
     */
    public void increaseByPercentage(final String code,
                                     final double percentage,
                                     final String justification,
                                     final String userId) {
        final BudgetChange change = new PercentageChange(
                code, percentage, justification, userId);
        applyChange(change);
    }

    /**
     * Decreases an entry's amount by a percentage.
     *
     * @param code          entry code to decrease
     * @param percentage    percentage to decrease by (e.g., 5.0 for 5%)
     * @param justification reason for the decrease
     * @param userId        who is making the change
     */
    public void decreaseByPercentage(final String code,
                                     final double percentage,
                                     final String justification,
                                     final String userId) {
        final BudgetChange change = new PercentageChange(
                code, -percentage, justification, userId);
        applyChange(change);
    }

    /**
     * Transfers amount from one entry to another.
     *
     * @param sourceCode    entry code giving the amount
     * @param targetCode    entry code receiving the amount
     * @param amount        amount to transfer (must be positive)
     * @param justification reason for the transfer
     * @param userId        who is making the transfer
     * @throws IllegalArgumentException if source or target doesn't exist
     */
    public void transferAmount(final String sourceCode,
                               final String targetCode,
                               final BigDecimal amount,
                               final String justification,
                               final String userId) {
        // Find source and target entries
        final BudgetChangesEntry source = repository.findByCode(sourceCode)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Source not found: " + sourceCode));

        final BudgetChangesEntry target = repository.findByCode(targetCode)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Target not found: " + targetCode));

        // Create transfer change
        final TransferChange change = new TransferChange(
                sourceCode, targetCode, amount, justification, userId);

        // Apply to both entries
        change.apply(source);
        change.applyToTarget(target);

        // Record in history
        changeHistory.push(change);
        redoStack.clear();

        System.out.println("✅ Transfer " + amount + " from " + sourceCode
                + " to " + targetCode);
    }

    /**
     * Internal helper method to apply a change to an entry.
     *
     * @param change the change to apply
     * @throws IllegalArgumentException if entry doesn't exist
     */
    private void applyChange(final BudgetChange change) {
        final BudgetChangesEntry entry = repository
                .findByCode(change.getEntryCode())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Entry not found: " + change.getEntryCode()));

        final BigDecimal newAmount = change.apply(entry);

        changeHistory.push(change);
        redoStack.clear();

        System.out.println("✅ Applied: " + change);
        System.out.println("   New amount: " + newAmount);
    }

    /**
     * Undoes the most recent change.
     * Restores the entry to its state before the change.
     */
    public void undo() {
        if (changeHistory.isEmpty()) {
            System.out.println("⚠️ No changes to undo");
            return;
        }

        final BudgetChange change = changeHistory.pop();

        if (change instanceof TransferChange) {
            undoTransfer((TransferChange) change);
        } else {
            final BudgetChangesEntry entry = repository
                    .findByCode(change.getEntryCode())
                    .orElseThrow(() -> new IllegalStateException(
                            "Entry no longer exists: "
                            + change.getEntryCode()));

            change.undo(entry);
        }

        redoStack.push(change);
        System.out.println("↩️ Undone: " + change);
    }

    /**
     * Helper method to undo a transfer change (both source and target).
     *
     * @param change the transfer change to undo
     */
    private void undoTransfer(final TransferChange change) {
        final BudgetChangesEntry source = repository
                .findByCode(change.getEntryCode())
                .orElseThrow(() -> new IllegalStateException(
                        "Source no longer exists: " + change.getEntryCode()));

        final BudgetChangesEntry target = repository
                .findByCode(change.getTargetEntryCode())
                .orElseThrow(() -> new IllegalStateException(
                        "Target no longer exists: "
                        + change.getTargetEntryCode()));

        change.undo(source);
        change.undoFromTarget(target);
    }

    /**
     * Redoes the most recently undone change.
     * Reapplies the change that was last undone.
     */
    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("⚠️ No changes to redo");
            return;
        }

        final BudgetChange change = redoStack.pop();

        if (change instanceof TransferChange) {
            redoTransfer((TransferChange) change);
        } else {
            final BudgetChangesEntry entry = repository
                    .findByCode(change.getEntryCode())
                    .orElseThrow(() -> new IllegalStateException(
                            "Entry no longer exists: "
                            + change.getEntryCode()));

            change.apply(entry);
        }

        changeHistory.push(change);
        System.out.println("↪️ Redone: " + change);
    }

    /**
     * Helper method to redo a transfer change (both source and target).
     *
     * @param change the transfer change to redo
     */
    private void redoTransfer(final TransferChange change) {
        final BudgetChangesEntry source = repository
                .findByCode(change.getEntryCode())
                .orElseThrow(() -> new IllegalStateException(
                        "Source no longer exists: " + change.getEntryCode()));

        final BudgetChangesEntry target = repository
                .findByCode(change.getTargetEntryCode())
                .orElseThrow(() -> new IllegalStateException(
                        "Target no longer exists: "
                        + change.getTargetEntryCode()));

        change.apply(source);
        change.applyToTarget(target);
    }

    /**
     * Returns a list of all changes made so far (in application order).
     *
     * @return list of budget changes
     */
    public List<BudgetChange> getChangeHistory() {
        return new ArrayList<>(changeHistory);
    }

    /**
     * Clears all change history (both undo and redo stacks).
     * Useful for starting a new session.
     */
    public void clearHistory() {
        changeHistory.clear();
        redoStack.clear();
    }

    /**
     * Returns the total number of changes made.
     *
     * @return count of changes in history
     */
    public int getTotalChanges() {
        return changeHistory.size();
    }
}
