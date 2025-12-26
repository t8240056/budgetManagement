package auebprogramming;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents an entry for budget changes.
 * This class is immutable regarding its identity (code),
 * but the amount can be updated.
 */
public final class BudgetChangesEntry {

    private final String code;
    private final String description;
    private BigDecimal amount;

    /**
     * Constructs a new BudgetEntry.
     *
     * @param entryCode        The unique code of the entry.
     * @param entryDescription The description of the entry.
     * @param entryAmount      The initial amount.
     */
    public BudgetChangesEntry(final String entryCode,
                              final String entryDescription,
                              final BigDecimal entryAmount) {
        this.code = Objects.requireNonNull(entryCode, "Code cannot be null");
        
        // Σπάσιμο γραμμής για να μην υπερβαίνει τους 80 χαρακτήρες
        this.description = Objects.requireNonNull(entryDescription,
                "Description cannot be null");
        
        // Αυτή η γραμμή χωράει στους 80 χαρακτήρες, οπότε την κρατάμε ενιαία
        this.amount = Objects.requireNonNull(entryAmount,
                "Amount cannot be null");
    }

    /**
     * Returns the unique code of this entry.
     *
     * @return the entry code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the description of this entry.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the current amount of this entry.
     *
     * @return the amount.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Updates the amount of this entry.
     *
     * @param newAmount The new amount to set.
     */
    public void setAmount(final BigDecimal newAmount) {
        this.amount = Objects.requireNonNull(newAmount,
                "New amount cannot be null");
    }

    /**
     * Compares this entry with another object for equality.
     * Two entries are considered equal if they have the same code.
     *
     * @param obj The object to compare with.
     * @return true if the objects are equal based on code.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BudgetChangesEntry)) {
            return false;
        }
        final BudgetChangesEntry that = (BudgetChangesEntry) obj;
        return code.equals(that.code);
    }

    /**
     * Returns the hash code of this entry based on its code.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    /**
     * Returns a string representation of this entry.
     * Format: "CODE: DESCRIPTION - AMOUNT €"
     *
     * @return the formatted string.
     */
    @Override
    public String toString() {
        return String.format("%s: %s - %,.2f €", code, description, amount);
    }
}
