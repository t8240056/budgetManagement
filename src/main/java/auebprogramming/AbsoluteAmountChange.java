package auebprogramming;

import java.math.BigDecimal;

public class AbsoluteAmountChange extends BudgetChange {
    private final BigDecimal changeAmount;

    public AbsoluteAmountChange(String entryCode, BigDecimal changeAmount, 
                               String justification, String userId) {
        super(entryCode, justification, userId);
        this.changeAmount = changeAmount;
    }

    @Override
    public BigDecimal apply(BudgetChangesEntry entry) {
        BigDecimal newAmount = entry.getAmount().add(changeAmount);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("New amount cannot be negative");
        }
        entry.setAmount(newAmount);
        return newAmount;
    }

    @Override
    public void undo(BudgetChangesEntry entry) {
        // Η αναίρεση είναι απλώς η αφαίρεση του ποσού που προσθέσαμε
        // (Αν το changeAmount ήταν αρνητικό, η αφαίρεσή του σημαίνει πρόσθεση, άρα δουλεύει σωστά)
        BigDecimal oldAmount = entry.getAmount().subtract(changeAmount);
        entry.setAmount(oldAmount);
    }

    @Override
    public ChangeType getType() {
        return changeAmount.compareTo(BigDecimal.ZERO) > 0 
            ? ChangeType.ABSOLUTE_INCREASE 
            : ChangeType.ABSOLUTE_DECREASE;
    }
}
