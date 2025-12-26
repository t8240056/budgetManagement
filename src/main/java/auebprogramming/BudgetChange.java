package auebprogramming;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Abstract base class for all budget change operations.
 * Implements the Command pattern: each change is a command that can be
 * executed and undone.
 */
public abstract class BudgetChange {

    /** Code of the entry being changed. */
    private final String entryCode;

    /** Reason for the change. */
    private final String justification;

    /** When the change was made. */
    private final LocalDateTime timestamp;

    /** Who made the change. */
    private final String userId;

    /**
     * Constructs a new budget change.
     *
     * @param code   the code of the entry to change
     * @param reason the reason for making this change
     * @param user   the identifier of the user making the change
     */
    public BudgetChange(final String code,
                        final String reason,
                        final String user) {
        this.entryCode = code;
        this.justification = reason;
        this.userId = user;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Applies this change to the given budget entry.
     *
     * @param entry the entry to modify
     * @return the new amount after applying the change
     */
    public abstract BigDecimal apply(BudgetChangesEntry entry);

    /**
     * Reverses this change on the given budget entry.
     *
     * @param entry the entry to restore
     * @return the original amount before the change was applied
     */
    public abstract BigDecimal undo(BudgetChangesEntry entry);

    /**
     * Returns the difference in amount caused by this change.
     * Positive for increases, negative for decreases.
     *
     * @return the amount difference
     */
    public abstract BigDecimal getDifference();

    /**
     * Returns the type of this change.
     *
     * @return the change type
     */
    public abstract ChangeType getType();

    /**
     * Returns the code of the entry being changed.
     *
     * @return entry code
     */
    public final String getEntryCode() {
        return entryCode;
    }

    /**
     * Returns the justification for the change.
     *
     * @return the justification
     */
    public final String getJustification() {
        return justification;
    }

    /**
     * Returns the timestamp when the change was made.
     *
     * @return the timestamp
     */
    public final LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public final String getUserId() {
        return userId;
    }

    /**
     * Returns a string representation of this change.
     *
     * @return formatted string showing type, code and justification
     */
    @Override
    public final String toString() {
        return String.format("%s [%s]: %s", getType(),
                entryCode, justification);
    }

    /**
     * Helper method to retrieve the description (used in logs).
     *
     * @return the description
     */
    public final String getDescription() {
        return justification;
    }
}
