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

    /**
     * Returns all budget entries in the repository
     */
    public List<BudgetChangesEntry> findAll() {
        return new ArrayList<>(entries.values());
    }

       /**
     * Finds entries whose description contains the given keyword (case-insensitive)
     * returns list of matching entries
     */
    public List<BudgetChangesEntry> findByDescriptionContaining(String keyword) {
        List<BudgetChangesEntry> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        
        for (BudgetChangesEntry entry : entries.values()) {
            if (entry.getDescription().toLowerCase().contains(lowerKeyword)) {
                results.add(entry);
            }
        }
        return results;
    }

    /**
     * Checks if an entry with the given code exists in the repository
     * returns true if an entry with this code exists, false otherwise
     */
    public boolean exists(String code) {
        return entries.containsKey(code);
    }

     /**
         * Removes an entry from the repository by its code
         * @param code the code of the entry to remove
         */
        public void delete(String code) {
            entries.remove(code);
        }
        
        /**
         * Saves multiple budget entries to the repository
         * @param entries collection of entries to save
         */
        public void saveAll(Collection<BudgetEntry> entries) {
            for (BudgetEntry entry : entries) {
                save(entry);
            }
        }

