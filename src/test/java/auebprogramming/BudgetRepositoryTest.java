package auebprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the BudgetRepository class.
 * Ensures that CRUD operations and calculations work correctly.
 * Follows Sun coding conventions.
 */
public class BudgetRepositoryTest {

    private BudgetRepository repository;
    private BudgetChangesEntry entry1;
    private BudgetChangesEntry entry2;

    /**
     * Set up the test environment before each test method.
     */
    @BeforeEach
    public void setUp() {
        repository = new BudgetRepository();
        
        // Sample entries for testing
        entry1 = new BudgetChangesEntry("1110103", "VAT on E-commerce", 
                new BigDecimal("500000.00"));
        entry2 = new BudgetChangesEntry("1110302", "Fuel Tax", 
                new BigDecimal("200000.00"));
    }

    /**
     * Tests the save and findByCode functionality.
     */
    @Test
    public void testSaveAndFindByCode() {
        repository.save(entry1);
        
        Optional<BudgetChangesEntry> found = repository.findByCode("1110103");
        
        assertTrue(found.isPresent(), "Entry should be found by code");
        assertEquals(entry1.getDescription(), found.get().getDescription(), 
                "Descriptions should match");
    }

    /**
     * Tests the findAll functionality.
     */
    @Test
    public void testFindAll() {
        repository.save(entry1);
        repository.save(entry2);
        
        List<BudgetChangesEntry> allEntries = repository.findAll();
        
        assertEquals(2, allEntries.size(), "Repository should contain 2 entries");
    }

    /**
     * Tests case-insensitive description search.
     */
    @Test
    public void testFindByDescriptionContaining() {
        repository.save(entry1); // VAT on E-commerce
        repository.save(entry2); // Fuel Tax
        
        List<BudgetChangesEntry> results = 
                repository.findByDescriptionContaining("vat");
        
        assertEquals(1, results.size(), "Should find 1 entry containing 'vat'");
        assertEquals("1110103", results.get(0).getCode());
    }

    /**
     * Tests the exists functionality.
     */
    @Test
    public void testExists() {
        repository.save(entry1);
        
        assertTrue(repository.exists("1110103"), "Code should exist");
        assertFalse(repository.exists("9999999"), "Code should not exist");
    }

    /**
     * Tests the delete functionality.
     */
    @Test
    public void testDelete() {
        repository.save(entry1);
        assertTrue(repository.exists("1110103"));
        
        repository.delete("1110103");
        
        assertFalse(repository.exists("1110103"), "Entry should be deleted");
    }

    /**
     * Tests saving a collection of entries.
     */
    @Test
    public void testSaveAll() {
        List<BudgetChangesEntry> list = Arrays.asList(entry1, entry2);
        
        repository.saveAll(list);
        
        assertEquals(2, repository.count(), "Repository should have 2 entries");
    }

    /**
     * Tests the total sum calculation of all entries.
     */
    @Test
    public void testCalculateTotal() {
        repository.save(entry1); // 500,000.00
        repository.save(entry2); // 200,000.00
        
        BigDecimal expectedTotal = new BigDecimal("700000.00");
        BigDecimal actualTotal = repository.calculateTotal();
        
        assertEquals(0, expectedTotal.compareTo(actualTotal), 
                "Total sum should be 700,000.00");
    }

    /**
     * Tests clearing the repository.
     */
    @Test
    public void testClear() {
        repository.save(entry1);
        repository.clear();
        
        assertEquals(0, repository.count(), "Repository should be empty after clear");
    }
}
