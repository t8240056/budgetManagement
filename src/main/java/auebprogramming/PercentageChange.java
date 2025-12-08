import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a change by a percentage (positive for increase, negative for decrease)
 */
public class PercentageChange extends BudgetChange {
    private final double percentage;     // Percentage to change by (e.g., 10.0 for 10%)
    private BigDecimal actualChange;    // The actual amount calculated from percentage
    
    /**
     * Constructs a percentage-based change
     * @param entryCode code of the entry to change
     * @param percentage percentage to change by (positive for increase, negative for decrease)
     * @param justification reason for the change
     * @param userId who is making the change
     */
    public PercentageChange(String entryCode, double percentage,
                           String justification, String userId) {
        super(entryCode, justification, userId);
        this.percentage = percentage;
    }

        /**
     * Applies this percentage change to the given entry
     * Calculates the actual amount based on percentage and adds it to current amount
     * @param entry the entry to modify
     * @return the new amount after the change
     * @throws IllegalArgumentException if the new amount would be negative
     */
    @Override
    public BigDecimal apply(BudgetChangesEntry entry) {
        BigDecimal oldAmount = entry.getAmount();
        BigDecimal percentageDecimal = BigDecimal.valueOf(percentage / 100.0);
        
        // Calculate the actual change amount
        actualChange = oldAmount.multiply(percentageDecimal)
                              .setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal newAmount = oldAmount.add(actualChange);
        
        // Validate that the new amount is not negative
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                "Το Καινούριο ποσό δεν μπορεί να είναι αρνητικό: " + newAmount);
        }
        
        entry.setAmount(newAmount);
        return newAmount;
    }

     @Override
    public BigDecimal undo(BudgetChangesEntry entry) {
        if (actualChange == null) {
            throw new IllegalStateException("Η αλλαγή δεν έχει εκχωρηθεί ακόμα");
        }
        
        BigDecimal currentAmount = entry.getAmount();
        BigDecimal oldAmount = currentAmount.subtract(actualChange);
        entry.setAmount(oldAmount);
        return oldAmount;
    }