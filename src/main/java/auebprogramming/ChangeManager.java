package auebprogramming;
import java.math.BigDecimal;
import java.util.*;

/**
 * Main facade class for managing all budget changes
 * Provides a unified interface for applying, undoing, and redoing changes
 * Implements the Facade pattern
 */
public class ChangeManager {
    private final BudgetRepository repository;      // Data repository
    private final Stack<BudgetChange> changeHistory = new Stack<>(); // History stack for undo
    private final Stack<BudgetChange> redoStack = new Stack<>();     // Redo stack
  
     /**
     * Constructs a ChangeManager with the given repository
     * @param repository the repository containing budget entries
     */
    public ChangeManager(BudgetRepository repository) {
        this.repository = repository;
    }

     /**
     * Increases an entry's amount by an absolute value
     * @param code entry code to increase
     * @param amount amount to add (must be positive)
     * @param justification reason for the increase
     * @param userId who is making the change
     */
    public void increaseAmount(String code, BigDecimal amount, 
                              String justification, String userId) {
        BudgetChange change = new AbsoluteAmountChange(
            code, amount, justification, userId);
        applyChange(change);
    }

        /**
     * Decreases an entry's amount by an absolute value
     * @param code entry code to decrease
     * @param amount amount to subtract (must be positive)
     * @param justification reason for the decrease
     * @param userId who is making the change
     */
    public void decreaseAmount(String code, BigDecimal amount,
                              String justification, String userId) {
        BudgetChange change = new AbsoluteAmountChange(
            code, amount.negate(), justification, userId);
        applyChange(change);
    }

     /**
     * Increases an entry's amount by a percentage
     * @param code entry code to increase
     * @param percentage percentage to increase by (e.g., 10.0 for 10%)
     * @param justification reason for the increase
     * @param userId who is making the change
     */
    public void increaseByPercentage(String code, double percentage,
                                    String justification, String userId) {
        BudgetChange change = new PercentageChange(
            code, percentage, justification, userId);
        applyChange(change);
    }

    /**
     * Decreases an entry's amount by a percentage
     * @param code entry code to decrease
     * @param percentage percentage to decrease by (e.g., 5.0 for 5%)
     * @param justification reason for the decrease
     * @param userId who is making the change
     */
    public void decreaseByPercentage(String code, double percentage,
                                    String justification, String userId) {
        BudgetChange change = new PercentageChange(
            code, -percentage, justification, userId);
        applyChange(change);
    }

     /**
     * Transfers amount from one entry to another
     * @param sourceCode entry code giving the amount
     * @param targetCode entry code receiving the amount
     * @param amount amount to transfer (must be positive)
     * @param justification reason for the transfer
     * @param userId who is making the transfer
     * @throws IllegalArgumentException if source or target doesn't exist
     */
    public void transferAmount(String sourceCode, String targetCode,
                              BigDecimal amount, String justification,
                              String userId) {
        // Find source and target entries
        BudgetChangeEntry source = repository.findByCode(sourceCode)
            .orElseThrow(() -> new IllegalArgumentException(
                "Source not found: " + sourceCode));
        
        BudgetChangeEntry target = repository.findByCode(targetCode)
            .orElseThrow(() -> new IllegalArgumentException(
                "Target not found: " + targetCode));
        
        // Create transfer change
        TransferChange change = new TransferChange(
            sourceCode, targetCode, amount, justification, userId);
        
        // Apply to both entries
        change.apply(source);
         change.applyToTarget(target);
        
        // Record in history
        changeHistory.push(change);
        redoStack.clear();
        
        System.out.println("✅ Transfer " + amount + " from " + sourceCode + 
                          " to " + targetCode);
    }
