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
     */
    public TransferChange(String sourceEntryCode, String targetEntryCode,
                          BigDecimal transferAmount, String justification, 
                          String userId) {
        super(sourceEntryCode, justification, userId);
        this.targetEntryCode = targetEntryCode;
        this.transferAmount = transferAmount;
    }

    @Override
    public BigDecimal apply(BudgetChangesEntry sourceEntry) {
        BigDecimal oldAmount = sourceEntry.getAmount();
        BigDecimal newAmount = oldAmount.subtract(transferAmount);
        
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                "Insufficient amount for transfer: " + oldAmount);
        }
        
        sourceEntry.setAmount(newAmount);
        return newAmount;
    }

    /**
     * Applies the transfer to the target entry (adds amount)
     */
    public void applyToTarget(BudgetChangesEntry targetEntry) {
        BigDecimal oldAmount = targetEntry.getAmount();
        BigDecimal newAmount = oldAmount.add(transferAmount);
        targetEntry.setAmount(newAmount);
    }
    
    @Override
    public BigDecimal undo(BudgetChangesEntry sourceEntry) {
        BigDecimal currentAmount = sourceEntry.getAmount();
        BigDecimal oldAmount = currentAmount.add(transferAmount);
        sourceEntry.setAmount(oldAmount);
        return oldAmount;
    }
    
    /**
     * Reverses the transfer on the target entry (subtracts the amount)
     * (Επανήλθε το παλιό όνομα undoFromTarget)
     */
    public void undoFromTarget(BudgetChangesEntry targetEntry) {
        BigDecimal currentAmount = targetEntry.getAmount();
        BigDecimal oldAmount = currentAmount.subtract(transferAmount);
        targetEntry.setAmount(oldAmount);
    }
    
    @Override
    public BigDecimal getDifference() {
        return transferAmount.negate(); // Negative for source entry
    }
    
    @Override
    public ChangeType getType() {
        return ChangeType.TRANSFER;
    }
    
    // (Επανήλθε το παλιό όνομα getTargetEntryCode)
    public String getTargetEntryCode() {
        return targetEntryCode;
    }
    
    public BigDecimal getTransferAmount() {
        return transferAmount;
    }
}
