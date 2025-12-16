package auebprogramming;

import java.math.BigDecimal;

/**
 * Represents a change by an absolute amount.
 * The amount can be positive for increase or negative for decrease.
 */
public final class AbsoluteAmountChange extends BudgetChange {

    /** The amount to add/subtract. */
    private final BigDecimal changeAmount;

    /**
     * Constructs a change by an absolute amount.
     *
     * @param entryCode     code of the entry to change
     * @param amount        amount to change by (positive for increase,
     * negative for decrease)
     * @param justification reason for the change
     * @param userId        who is making the change
     */
    public AbsoluteAmountChange(final String entryCode,
                                final BigDecimal amount,
                                final String justification,
                                final String userId) {
        super(entryCode, justification, userId);
        this.changeAmount = amount;
    }

    /**
     * Applies this absolute amount change to the given entry.
     * Adds the changeAmount to the current amount.
     *
     * @param entry the entry to apply the change to
     * @return the new calculated amount
     * @throws IllegalArgumentException if the new amount is negative
     */
    @Override
    public BigDecimal apply(final BudgetChangesEntry entry) {
        final BigDecimal oldAmount = entry.getAmount();
        final BigDecimal newAmount = oldAmount.add(changeAmount);

        // Validate that the new amount is not negative
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                "New amount cannot be negative: " + newAmount);
        }

        entry.setAmount(newAmount);
        return newAmount;
    }

    /**
     * Reverses the change: Subtracts the amount that was added.
     *
     * @param entry the entry to undo the change from
     * @return the restored amount (old amount)
     */
    @Override
    public BigDecimal undo(final BudgetChangesEntry entry) {
        final BigDecimal currentAmount = entry.getAmount();
        final BigDecimal oldAmount = currentAmount.subtract(changeAmount);
        entry.setAmount(oldAmount);
        return oldAmount;
    }

    /**
     * Returns the difference caused by this change.
     *
     * @return the change amount
     */
    @Override
    public BigDecimal getDifference() {
        return changeAmount;
    }

    /**
     * Returns the type of this change based on the amount sign.
     *
     * @return ABSOLUTE_INCREASE if positive, ABSOLUTE_DECREASE otherwise
     */
    @Override
    public ChangeType getType() {
        return changeAmount.compareTo(BigDecimal.ZERO) > 0
                ? ChangeType.ABSOLUTE_INCREASE
                : ChangeType.ABSOLUTE_DECREASE;
    }

    /**
     * Gets the absolute amount of change.
     *
     * @return the change amount
     */
    public BigDecimal getChangeAmount() {
        return changeAmount;
    }
}
