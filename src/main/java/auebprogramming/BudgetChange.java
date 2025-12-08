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
}
