package auebprogramming;

import java.math.BigDecimal;

public class PercentageChange extends BudgetChange {
    private final double percent;
    private BigDecimal calculatedDifference; // Αποθηκεύουμε τη διαφορά για το undo

    public PercentageChange(String entryCode, double percent, 
                           String justification, String userId) {
        super(entryCode, justification, userId);
        this.percent = percent;
    }

    @Override
    public BigDecimal apply(BudgetChangesEntry entry) {
        BigDecimal currentAmount = entry.getAmount();
        BigDecimal percentageDecimal = BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100));
        
        // Υπολογίζουμε και αποθηκεύουμε τη διαφορά
        this.calculatedDifference = currentAmount.multiply(percentageDecimal);
        
        BigDecimal newAmount = currentAmount.add(calculatedDifference);
        
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
             throw new IllegalArgumentException("New amount cannot be negative");
        }
        
        entry.setAmount(newAmount);
        return newAmount;
    }

    @Override
    public void undo(BudgetChangesEntry entry) {
        // Αφαιρούμε ακριβώς το ποσό που είχε προστεθεί/αφαιρεθεί
        if (calculatedDifference != null) {
            entry.setAmount(entry.getAmount().subtract(calculatedDifference));
        }
    }

    public BigDecimal getDifference() {
        return calculatedDifference;
    }

    @Override
    public ChangeType getType() {
        return percent > 0 ? ChangeType.PERCENTAGE_INCREASE : ChangeType.PERCENTAGE_DECREASE;
    }
}
