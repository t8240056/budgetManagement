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
