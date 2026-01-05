package auebprogramming;

import java.math.BigDecimal;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the ChangeManager facade class.
 * Verifies application of changes, undo/redo functionality, and history tracking.
 */
public class ChangeManagerTest {

    private static final String TEST_USER = "TestUser";
    private static final String CODE_A = "1001";
    private static final String CODE_B = "1002";

    private BudgetRepository repository;
    private ChangeManager manager;

    /**
     * Sets up the test environment.
     * Initializes repository with two dummy entries and creates the manager.
     */
    @Before
    public void setUp() {
        repository = new BudgetRepository();
        
        // Initialize with 1000 and 2000
        repository.save(new BudgetChangesEntry(CODE_A, "Entity A", new BigDecimal("1000")));
        repository.save(new BudgetChangesEntry(CODE_B, "Entity B", new BigDecimal("2000")));
        
        manager = new ChangeManager(repository);
    }

    /**
     * Tests increasing an amount by an absolute value.
     */
    @Test
    public void testIncreaseAmount() {
        manager.increaseAmount(CODE_A, new BigDecimal("500"), "Bonus", TEST_USER);
        
        final BigDecimal expected = new BigDecimal("1500");
        final BigDecimal actual = repository.findByCode(CODE_A).get().getAmount();
        
        Assert.assertEquals("Amount should be increased by 500", expected, actual);
        Assert.assertEquals("History should contain 1 change", 1, manager.getTotalChanges());
    }

    /**
     * Tests decreasing an amount by an absolute value.
     */
    @Test
    public void testDecreaseAmount() {
        manager.decreaseAmount(CODE_B, new BigDecimal("500"), "Cut", TEST_USER);
        
        final BigDecimal expected = new BigDecimal("1500");
        final BigDecimal actual = repository.findByCode(CODE_B).get().getAmount();
        
        Assert.assertEquals("Amount should be decreased by 500", expected, actual);
    }

    /**
     * Tests increasing an amount by a percentage.
     */
    @Test
    public void testIncreaseByPercentage() {
        // Increase 1000 by 10% -> 1100
        manager.increaseByPercentage(CODE_A, 10.0, "Inflation", TEST_USER);
        
        // Use double comparison for percentage calculations
        final double expected = 1100.0;
        final double actual = repository.findByCode(CODE_A).get().getAmount().doubleValue();
        
        Assert.assertEquals("Amount should increase by 10%", expected, actual, 0.001);
    }

    /**
     * Tests decreasing an amount by a percentage.
     */
    @Test
    public void testDecreaseByPercentage() {
        // Decrease 2000 by 50% -> 1000
        manager.decreaseByPercentage(CODE_B, 50.0, "Budget Cut", TEST_USER);
        
        final double expected = 1000.0;
        final double actual = repository.findByCode(CODE_B).get().getAmount().doubleValue();
        
        Assert.assertEquals("Amount should decrease by 50%", expected, actual, 0.001);
    }

    /**
     * Tests transferring funds between two entries.
     */
    @Test
    public void testTransferAmount() {
        // Transfer 500 from B (2000) to A (1000)
        manager.transferAmount(CODE_B, CODE_A, new BigDecimal("500"), "Support", TEST_USER);
        
        final BigDecimal expectedA = new BigDecimal("1500");
        final BigDecimal expectedB = new BigDecimal("1500");
        
        Assert.assertEquals("Target should receive 500", 
                expectedA, repository.findByCode(CODE_A).get().getAmount());
        Assert.assertEquals("Source should lose 500", 
                expectedB, repository.findByCode(CODE_B).get().getAmount());
    }

    /**
     * Tests undoing the last change (Simple Absolute Change).
     */
    @Test
    public void testUndoSimpleChange() {
        // 1000 -> 1500
        manager.increaseAmount(CODE_A, new BigDecimal("500"), "Test", TEST_USER);
        Assert.assertEquals(new BigDecimal("1500"), repository.findByCode(CODE_A).get().getAmount());
        
        // Undo -> 1000
        manager.undo();
        Assert.assertEquals("Should revert to 1000", 
                new BigDecimal("1000"), repository.findByCode(CODE_A).get().getAmount());
        
        Assert.assertEquals("History stack should be empty", 0, manager.getTotalChanges());
    }

    /**
     * Tests undoing a transfer operation.
     * Use double check to ensure both source and target revert.
     */
    @Test
    public void testUndoTransfer() {
        // Transfer 500 from B to A
        manager.transferAmount(CODE_B, CODE_A, new BigDecimal("500"), "Test", TEST_USER);
        
        // Undo
        manager.undo();
        
        Assert.assertEquals("Target should revert to 1000", 
                new BigDecimal("1000"), repository.findByCode(CODE_A).get().getAmount());
        Assert.assertEquals("Source should revert to 2000", 
                new BigDecimal("2000"), repository.findByCode(CODE_B).get().getAmount());
    }

    /**
     * Tests redo functionality after an undo.
     */
    @Test
    public void testRedo() {
        // 1. Change: 1000 -> 1500
        manager.increaseAmount(CODE_A, new BigDecimal("500"), "Test", TEST_USER);
        
        // 2. Undo: 1500 -> 1000
        manager.undo();
        Assert.assertEquals(new BigDecimal("1000"), repository.findByCode(CODE_A).get().getAmount());
        
        // 3. Redo: 1000 -> 1500
        manager.redo();
        Assert.assertEquals("Should re-apply increase to 1500", 
                new BigDecimal("1500"), repository.findByCode(CODE_A).get().getAmount());
        
        Assert.assertEquals("History should have 1 change again", 1, manager.getTotalChanges());
    }

    /**
     * Tests that attempting to modify a non-existent code throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChangeInvalidCode() {
        manager.increaseAmount("9999", new BigDecimal("100"), "Fail", TEST_USER);
    }
    
    /**
     * Tests clearing the history.
     */
    @Test
    public void testClearHistory() {
        manager.increaseAmount(CODE_A, new BigDecimal("100"), "Test", TEST_USER);
        Assert.assertEquals(1, manager.getTotalChanges());
        
        manager.clearHistory();
        Assert.assertEquals("History should be empty", 0, manager.getTotalChanges());
        
        // Undo should do nothing now
        manager.undo();
        Assert.assertEquals(new BigDecimal("1100"), repository.findByCode(CODE_A).get().getAmount());
    }
    
    /**
     * Verifies that getChangeHistory returns the correct list of changes.
     */
    @Test
    public void testGetChangeHistory() {
        manager.increaseAmount(CODE_A, new BigDecimal("100"), "1", TEST_USER);
        manager.decreaseAmount(CODE_B, new BigDecimal("100"), "2", TEST_USER);
        
        final List<BudgetChange> history = manager.getChangeHistory();
        Assert.assertEquals(2, history.size());
        Assert.assertEquals(ChangeType.ABSOLUTE_INCREASE, history.get(0).getType());
        Assert.assertEquals(ChangeType.ABSOLUTE_DECREASE, history.get(1).getType());
    }
}
