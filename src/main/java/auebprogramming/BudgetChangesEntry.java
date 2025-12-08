package auebprogramming;

import java.math.BigDecimal;
import java.util.Objects;

public class BudgetChangesEntry {
        private final String code;          
    private final String description;   
    private BigDecimal amount; 

    // Constructs a new BudgetEntry with the given code, description and amount
    public BudgetChangesEntry(String code, String description, BigDecimal amount) {
        this.code = Objects.requireNonNull(code, "Code cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
    }
    
    // Returns the unique code of this entry
    public String getCode() { return code; }
    
    // Returns the description of this entry
    public String getDescription() { return description; }
    
    
    // Returns the current amount of this entry
    public BigDecimal getAmount() { return amount; }
    
    //Updates the amount of this entry
    public void setAmount(BigDecimal newAmount) {
        this.amount = Objects.requireNonNull(newAmount, "New amount cannot be null");
   /**
     * Compares this entry with another object for equality
     * Two entries are considered equal if they have the same code
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BudgetChangesEntry)) return false;
        BudgetChangesEntry that = (BudgetChangesEntry) o;
        return code.equals(that.code);
    }
 }

    
    /**
     * Returns the hash code of this entry based on its code
     */
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
    
    /**
     * Returns a string representation of this entry
     * Format: "CODE: DESCRIPTION - AMOUNT €"
     */
    @Override
    public String toString() {
        return String.format("%s: %s - %,.2f €", code, description, amount);
    }
}
