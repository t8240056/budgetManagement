package auebprogramming;

import java.math.BigDecimal;

public abstract class BudgetChange {
    protected String entryCode;
    protected String justification;
    protected String userId;

    public BudgetChange(String entryCode, String justification, String userId) {
        this.entryCode = entryCode;
        this.justification = justification;
        this.userId = userId;
    }

    public String getEntryCode() { return entryCode; }
    
    // Abstract μέθοδος που πρέπει να υλοποιήσουν όλοι
    public abstract BigDecimal apply(BudgetChangesEntry entry);
    
    // ΝΕΟ: Μέθοδος αναίρεσης
    public abstract void undo(BudgetChangesEntry entry);

    public abstract ChangeType getType();
    
    // Βοηθητική για να βλέπουμε τι αναιρούμε
    public String getDescription() {
        return justification;
    }
}
