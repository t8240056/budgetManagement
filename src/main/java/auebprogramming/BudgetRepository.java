package auebprogramming;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository for managing BudgetChangesEntry objects using in-memory storage.
 * Implements the Repository pattern for data access abstraction.
 */
public final class BudgetRepository {

    private final Map<String, BudgetChangesEntry> entries = new HashMap<>();

    /**
     * Saves or updates a budget entry in the repository.
     * If an entry with the same code exists, it will be replaced.
     *
     * @param entry the entry to save
     */
    public void save(final BudgetChangesEntry entry) {
        entries.put(entry.getCode(), entry);
    }

    /**
     * Finds a budget entry by its unique code.
     *
     * @param code the code to search for
     * @return an Optional containing the entry if found, or empty
     */
    public Optional<BudgetChangesEntry> findByCode(final String code) {
        return Optional.ofNullable(entries.get(code));
    }

    /**
     * Returns all budget entries in the repository.
     *
     * @return a list of all entries
     */
    public List<BudgetChangesEntry> findAll() {
        return new ArrayList<>(entries.values());
    }

    /**
     * Finds entries whose description contains the given keyword.
     * The search is case-insensitive.
     *
     * @param keyword the keyword to search for
     * @return list of matching entries
     */
    public List<BudgetChangesEntry> findByDescriptionContaining(
            final String keyword) {
        final List<BudgetChangesEntry> results = new ArrayList<>();
        final String lowerKeyword = keyword.toLowerCase();

        for (final BudgetChangesEntry entry : entries.values()) {
            if (entry.getDescription().toLowerCase().contains(lowerKeyword)) {
                results.add(entry);
            }
        }
        return results;
    }

    /**
     * Checks if an entry with the given code exists in the repository.
     *
     * @param code the code to check
     * @return true if an entry with this code exists, false otherwise
     */
    public boolean exists(final String code) {
        return entries.containsKey(code);
    }

    /**
     * Removes an entry from the repository by its code.
     *
     * @param code the code of the entry to remove
     */
    public void delete(final String code) {
        entries.remove(code);
    }

    /**
     * Saves multiple budget entries to the repository.
     *
     * @param entryCollection the collection of entries to save
     */
    public void saveAll(final Collection<BudgetChangesEntry> entryCollection) {
        for (final BudgetChangesEntry entry : entryCollection) {
            save(entry);
        }
    }

    /**
     * Returns the total number of entries in the repository.
     *
     * @return count of entries
     */
    public int count() {
        return entries.size();
    }

    /**
     * Calculates the sum of all amounts in the repository.
     *
     * @return total sum of all entry amounts
     */
    public BigDecimal calculateTotal() {
        return entries.values().stream()
                .map(BudgetChangesEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Clears all entries from the repository.
     */
    public void clear() {
        this.entries.clear();
    }
}
