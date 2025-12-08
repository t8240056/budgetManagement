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