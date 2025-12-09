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

}
