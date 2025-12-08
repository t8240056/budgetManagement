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
}