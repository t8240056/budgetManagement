
package auebprogramming;

import java.math.BigDecimal;

/**
 * Represents a transfer of amount from one budget entry to another
 */
public class TransferChange extends BudgetChange {
    private final String targetEntryCode; // Code of the entry receiving the amount
    private final BigDecimal transferAmount; // Amount to transfer

     /**
     * Constructs a transfer change
     * @param sourceEntryCode code of the entry giving the amount
     * @param targetEntryCode code of the entry receiving the amount
     * @param transferAmount amount to transfer (must be positive)
     * @param justification reason for the transfer
     * @param userId who is making the transfer
     */
    public TransferChange(String sourceEntryCode, String targetEntryCode,
                         BigDecimal transferAmount, String justification, 
                         String userId) {
        super(sourceEntryCode, justification, userId);
        this.targetEntryCode = targetEntryCode;
        this.transferAmount = transferAmount;
    }

        /**
     * Applies the transfer to the source entry (subtracts amount)
     * This is called by ChangeManager for the source entry
     * @param sourceEntry the entry giving the amount
     * @return the new amount of the source entry after transfer
     * @throws IllegalArgumentException if source has insufficient amount
     */
    @Override
    public BigDecimal apply(BudgetChangeEntry sourceEntry) {
        BigDecimal oldAmount = sourceEntry.getAmount();
        BigDecimal newAmount = oldAmount.subtract(transferAmount);
        
        // Check if source has enough amount to transfer
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                "Insufficient amount for transfer: " + oldAmount);
        }
        
        sourceEntry.setAmount(newAmount);
        return newAmount;
    }

        /**
     * Applies the transfer to the target entry (adds amount)
     * This is called separately by ChangeManager for the target entry
     * @param targetEntry the entry receiving the amount
     */
    public void applyToTarget(BudgetChangeEntry targetEntry) {
        BigDecimal oldAmount = targetEntry.getAmount();
        BigDecimal newAmount = oldAmount.add(transferAmount);
        targetEntry.setAmount(newAmount);
    }
    
    /**
     * Reverses the transfer on the source entry (adds back the amount)
     * @param sourceEntry the source entry to restore
     * @return the original amount of the source entry
     */
    @Override
    public BigDecimal undo(BudgetChangeEntry sourceEntry) {
        BigDecimal currentAmount = sourceEntry.getAmount();
        BigDecimal oldAmount = currentAmount.add(transferAmount);
        sourceEntry.setAmount(oldAmount);
        return oldAmount;
    }
    
    /**
     * Reverses the transfer on the target entry (subtracts the amount)
     * @param targetEntry the target entry to restore
     */
    public void undoFromTarget(BudgetEntry targetEntry) {
        BigDecimal currentAmount = targetEntry.getAmount();
        BigDecimal oldAmount = currentAmount.subtract(transferAmount);
        targetEntry.setAmount(oldAmount);
    }
