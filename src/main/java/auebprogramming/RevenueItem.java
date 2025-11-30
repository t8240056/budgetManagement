package auebprogramming;
// Class representing a budget item with code, description and amount
class RevenueItem {
    private String code;
    private String description;
    private long amount;
    
    // Constructor to initialize a budget item
    public RevenueItem(String code, String description, long amount) {
        this.code = code;
        this.description = description;
        this.amount = amount;
    }
    
    // Getters for accessing the private fields
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public long getAmount() { return amount; }
    
    // Method to display item in formatted way
    @Override
    public String toString() {
        return String.format("%s %s %,d", code, description, amount);
    }
}