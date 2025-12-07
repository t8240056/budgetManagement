package auebprogramming;
import java.math.BigDecimal;
import java.util.*;

import main.java.auebprogramming.BudgetChangesEntry;

   
/**
 * Repository for managing BudgetChangesEntry objects using in-memory storage
 * Implements the Repository pattern for data access abstraction
 */
public class BudgetRepository {
    private final Map<String, BudgetChangesEntry> entries = new HashMap<>();
    
    /**
     * Saves or updates a budget entry in the repository
     * If an entry with the same code exists, it will be replaced
     */
    public void save(BudgetChangesEntry entry) {
        entries.put(entry.getCode(), entry);
    }

     /**
     * Finds a budget entry by its unique code
     */
    public Optional<BudgetChangesEntry> findByCode(String code) {
        return Optional.ofNullable(entries.get(code));
    }
}