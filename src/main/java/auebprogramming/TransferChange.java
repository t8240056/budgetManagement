package auebprogramming;

import java.math.BigDecimal;

public class TransferChange extends BudgetChange {
    private final String targetCode;
    private final BigDecimal amount;

    public TransferChange(String sourceCode, String targetCode, BigDecimal amount, 
                         String justification, String userId) {
        super(sourceCode, justification, userId); // entryCode is sourceCode
        this.targetCode = targetCode;
        this.amount = amount;
    }

    public String getTargetCode() { return targetCode; }

    @Override
    public BigDecimal apply(BudgetChangesEntry sourceEntry) {
        // Αφαιρούμε από την πηγή
        BigDecimal newSourceAmount = sourceEntry.getAmount().subtract(amount);
        if (newSourceAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds in source");
        }
        sourceEntry.setAmount(newSourceAmount);
        return newSourceAmount;
    }

    // Ειδική μέθοδος για τον προορισμό
    public void applyToTarget(BudgetChangesEntry targetEntry) {
        targetEntry.setAmount(targetEntry.getAmount().add(amount));
    }

    @Override
    public void undo(BudgetChangesEntry sourceEntry) {
        // Επιστρέφουμε τα λεφτά στην πηγή
        sourceEntry.setAmount(sourceEntry.getAmount().add(amount));
    }
    
    // Ειδική undo για τον προορισμό
    public void undoTarget(BudgetChangesEntry targetEntry) {
        // Παίρνουμε τα λεφτά πίσω από τον προορισμό
        targetEntry.setAmount(targetEntry.getAmount().subtract(amount));
    }

    @Override
    public ChangeType getType() {
        return ChangeType.TRANSFER;
    }
}
