package auebprogramming;

import java.math.BigDecimal;

/**
 * Represents a transfer of amount from one budget entry to another.
 */
public final class TransferChange extends BudgetChange {

    /** Code of the entry receiving the amount. */
    private final String targetEntryCode;

    /** Amount to transfer. */
    private final BigDecimal transferAmount;

    /**
     * Constructs a transfer change.
     *
     * @param sourceEntryCode the code of the source entry
     * @param targetCode      the code of the target entry
     * @param amount          the amount to transfer
     * @param justification   the reason for the transfer
     * @param userId          the user performing the transfer
     */
    public TransferChange(final String sourceEntryCode,
                          final String targetCode,
                          final BigDecimal amount,
                          final String justification,
                          final String userId) {
        super(sourceEntryCode, justification, userId);
        this.targetEntryCode = targetCode;
        this.transferAmount = amount;
    }

    /**
     * Applies the transfer to the source entry (subtracts amount).
     *
     * @param sourceEntry the entry to subtract from
     * @return the new amount of the source entry
     */
    @Override
    public BigDecimal apply(final BudgetChangesEntry sourceEntry) {
        final BigDecimal oldAmount = sourceEntry.getAmount();
        final BigDecimal newAmount = oldAmount.subtract(transferAmount);

        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient amount for transfer: " + oldAmount);
        }

        sourceEntry.setAmount(newAmount);
        return newAmount;
    }

    /**
     * Applies the transfer to the target entry (adds amount).
     *
     * @param targetEntry the entry to add amount to
     */
    public void applyToTarget(final BudgetChangesEntry targetEntry) {
        final BigDecimal oldAmount = targetEntry.getAmount();
        final BigDecimal newAmount = oldAmount.add(transferAmount);
        targetEntry.setAmount(newAmount);
    }

    /**
     * Reverses the transfer on the source entry (adds the amount back).
     *
     * @param sourceEntry the entry to restore
     * @return the restored amount
     */
    @Override
    public BigDecimal undo(final BudgetChangesEntry sourceEntry) {
        final BigDecimal currentAmount = sourceEntry.getAmount();
        final BigDecimal oldAmount = currentAmount.add(transferAmount);
        sourceEntry.setAmount(oldAmount);
        return oldAmount;
    }

    /**
     * Reverses the transfer on the target entry (subtracts the amount).
     *
     * @param targetEntry the entry to subtract amount from
     */
    public void undoFromTarget(final BudgetChangesEntry targetEntry) {
        final BigDecimal currentAmount = targetEntry.getAmount();
        final BigDecimal oldAmount = currentAmount.subtract(transferAmount);
        targetEntry.setAmount(oldAmount);
    }

    /**
     * Returns the difference caused by this change on the source entry.
     *
     * @return negative transfer amount
     */
    @Override
    public BigDecimal getDifference() {
        return transferAmount.negate(); // Negative for source entry
    }

    /**
     * Returns the type of this change.
     *
     * @return ChangeType.TRANSFER
     */
    @Override
    public ChangeType getType() {
        return ChangeType.TRANSFER;
    }

    /**
     * Returns the target entry code.
     *
     * @return the target code
     */
    public String getTargetEntryCode() {
        return targetEntryCode;
    }

    /**
     * Returns the transfer amount.
     *
     * @return the amount
     */
    public BigDecimal getTransferAmount() {
        return transferAmount;
    }
}
