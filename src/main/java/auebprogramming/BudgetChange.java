package auebprogramming;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Abstract base class for all budget change operations
 * Implements the Command pattern: each change is a command that can be executed and undone
 */
public abstract class BudgetChange {
    protected final String entryCode;        // Code of the entry being changed
    protected final String justification;    // Reason for the change
    protected final LocalDateTime timestamp; // When the change was made
    protected final String userId;          // Who made the change

      /**
     * Constructs a new budget change
     * @param entryCode the code of the entry to change
     * @param justification the reason for making this change
     * @param userId the identifier of the user making the change
     */
    public BudgetChange(String entryCode, String justification, String userId) {
        this.entryCode = entryCode;
        this.justification = justification;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }
/**
     * Applies this change to the given budget entry
     * @param entry the entry to modify
     * @return the new amount after applying the change
     */
    public abstract BigDecimal apply(BudgetChangesEntry entry);
    
    /**
     * Reverses this change on the given budget entry
     * @param entry the entry to restore
     * @return the original amount before the change was applied
     */
    public abstract BigDecimal undo(BudgetChangesEntry entry);
    
/**
     * Returns the difference in amount caused by this change
     * Positive for increases, negative for decreases
     * @return the amount difference
     */
    public abstract BigDecimal getDifference();
    
    /**
     * Returns the type of this change
     * @return the change type
     */
    public abstract ChangeType getType();

    /**
     * Returns the code of the entry being changed
     * @return entry code
     */
    public String getEntryCode() { return entryCode; }
    
    /**
     * Returns the justification for this change
     * @return change justification
     */
    public String getJustification() { return justification; }

     /**
     * Returns when this change was made
     * @return timestamp of the change
     */
    public LocalDateTime getTimestamp() { return timestamp; }
    
    /**
     * Returns who made this change
     * @return user identifier
     */
    public String getUserId() { return userId; }

}
