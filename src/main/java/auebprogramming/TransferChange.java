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