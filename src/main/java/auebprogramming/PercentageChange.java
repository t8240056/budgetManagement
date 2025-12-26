package auebprogramming;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a change by a percentage (positive for increase,
 * negative for decrease).
 */
public final class PercentageChange extends BudgetChange {

    /** Percentage to change by (e.g., 10.0 for 10%). */
    private final double percentage;

    /** The actual amount calculated from percentage. */
    private BigDecimal actualChange;

    /**
     * Constructs a percentage-based change.
     *
     * @param entryCode     the code of the entry to change
     * @param percent       percentage to change by
     * @param justification reason for the change
     * @param userId        who is making the change
     */
    public PercentageChange(final String entryCode,
                            final double percent,
                            final String justification,
                            final String userId) {
        super(entryCode, justification, userId);
        this.percentage = percent;
    }

    /**
     * Applies this percentage change to the given entry.
     * Calculates the actual amount based on percentage and adds it
     * to current amount.
     *
     * @param entry the entry to apply the change to
     * @return the new calculated amount
     */
    @Override
    public BigDecimal apply(final BudgetChangesEntry entry) {
        final BigDecimal oldAmount = entry.getAmount();
        final BigDecimal percentageDecimal = BigDecimal.valueOf(
                percentage / 100.0);

        // Calculate the actual change amount
        actualChange = oldAmount.multiply(percentageDecimal)
                .setScale(2, RoundingMode.HALF_UP);

        final BigDecimal newAmount = oldAmount.add(actualChange);

        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Το Καινούριο ποσό δεν μπορεί να είναι αρνητικό: "
                    + newAmount);
        }

        entry.setAmount(newAmount);
        return newAmount;
    }

    /**
     * Reverses the change using the stored actualChange value.
     *
     * @param entry the entry to undo the change from
     * @return the restored amount
     */
    @Override
    public BigDecimal undo(final BudgetChangesEntry entry) {
        if (actualChange == null) {
            throw new IllegalStateException(
                    "Η αλλαγή δεν έχει εκχωρηθεί ακόμα");
        }

        final BigDecimal currentAmount = entry.getAmount();
        final BigDecimal oldAmount = currentAmount.subtract(actualChange);
        entry.setAmount(oldAmount);
        return oldAmount;
    }

    /**
     * Returns the difference caused by this change.
     *
     * @return the actual change amount
     */
    @Override
    public BigDecimal getDifference() {
        return actualChange != null ? actualChange : BigDecimal.ZERO;
    }

    /**
     * Returns the type of this change based on percentage sign.
     *
     * @return the change type
     */
    @Override
    public ChangeType getType() {
        return percentage > 0
                ? ChangeType.PERCENTAGE_INCREASE
                : ChangeType.PERCENTAGE_DECREASE;
    }

    /**
     * Returns the percentage value.
     *
     * @return the percentage
     */
    public double getPercentage() {
        return percentage;
    }
}
