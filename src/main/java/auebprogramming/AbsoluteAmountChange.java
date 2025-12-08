package auebprogramming;

import java.math.BigDecimal;

/**
 * Represents a change by an absolute amount (positive for increase, negative for decrease)
 */
public class AbsoluteAmountChange extends BudgetChange {
    private final BigDecimal changeAmount; // The amount to add/subtract

    /**
     * Constructs a change by an absolute amount
     * @param entryCode code of the entry to change
     * @param changeAmount amount to change by (positive for increase, negative for decrease)
     * @param justification reason for the change
     * @param userId who is making the change
     */
    public AbsoluteAmountChange(String entryCode, BigDecimal changeAmount, 
                               String justification, String userId) {
        super(entryCode, justification, userId);
        this.changeAmount = changeAmount;
    }

     /** Applies this absolute amount change to the given entry
     * Adds the changeAmount to the current amount
     * @param entry the entry to modify
     * @return the new amount after the change
     * @throws IllegalArgumentException if the new amount would be negative
     */
    @Override
    public BigDecimal apply(BudgetChangesEntry entry) {
        BigDecimal oldAmount = entry.getAmount();
        BigDecimal newAmount = oldAmount.add(changeAmount);
        
        // Validate that the new amount is not negative
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                "New amount cannot be negative: " + newAmount);
        }
        
        entry.setAmount(newAmount);
        return newAmount;
    }
    