package auebprogramming;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for PercentageChange.
 * Ensures percentage calculations and logic follow financial requirements.
 * Adheres to Sun coding conventions.
 */
public class PercentageChangeTest {

    private BudgetChangesEntry entry;
    private final String entryCode = "1110103";
    private final String userId = "user456";

    /**
     * Initializes a sample budget entry before each test.
     */
    @BeforeEach
    public void setUp() {
        // Initial amount: 2000.00
        entry = new BudgetChangesEntry(entryCode, "VAT Revenue", 
                new BigDecimal("2000.00"));
    }

    /**
     * Tests applying a 10% increase to the budget entry.
     */
    @Test
    public void testApplyPositivePercentage() {
        double percent = 10.0;
        PercentageChange change = new PercentageChange(entryCode, percent, 
                "Standard Increase", userId);

        BigDecimal result = change.apply(entry);

        // 2000 + (2000 * 0.10) = 2200.00
        BigDecimal expected = new BigDecimal("2200.00");
        assertEquals(0, expected.compareTo(result), 
                "Result should be 2200.00 after 10% increase");
        assertEquals(0, expected.compareTo(entry.getAmount()), 
                "Entry amount should be updated correctly");
    }

    /**
     * Tests applying a 15% decrease to the budget entry.
     */
    @Test
    public void testApplyNegativePercentage() {
        double percent = -15.0;
        PercentageChange change = new PercentageChange(entryCode, percent, 
                "Budget Cut", userId);

        BigDecimal result = change.apply(entry);

        // 2000 - (2000 * 0.15) = 1700.00
        BigDecimal expected = new BigDecimal("1700.00");
        assertEquals(0, expected.compareTo(result), 
                "Result should be 1700.00 after 15% decrease");
    }

    /**
     * Tests that reducing the amount below zero throws an exception.
     */
    @Test
    public void testApplyExcessiveDecreaseThrowsException() {
        double percent = -110.0; // Reduction more than 100%
        PercentageChange change = new PercentageChange(entryCode, percent, 
                "Invalid Decrease", userId);

        assertThrows(IllegalArgumentException.class, () -> {
            change.apply(entry);
        }, "Should throw exception when result is negative");
    }

    /**
     * Tests undoing a change to restore the original amount.
     */
    @Test
    public void testUndo() {
        double percent = 20.0;
        PercentageChange change = new PercentageChange(entryCode, percent, 
                "One-time Adjustment", userId);

        // Apply change: 2000 + 400 = 2400
        change.apply(entry);
        
        // Undo change: 2400 - 400 = 2000
        BigDecimal result = change.undo(entry);

        BigDecimal expected = new BigDecimal("2000.00");
        assertEquals(0, expected.compareTo(result), 
                "Undo should return amount to 2000.00");
    }

    /**
     * Tests that undo() throws IllegalStateException if change wasn't applied.
     */
    @Test
    public void testUndoWithoutApplyThrowsException() {
        PercentageChange change = new PercentageChange(entryCode, 5.0, 
                "Test", userId);

        assertThrows(IllegalStateException.class, () -> {
            change.undo(entry);
        }, "Cannot undo a change that has not been applied yet");
    }

    /**
     * Tests if the correct ChangeType is returned.
     */
    @Test
    public void testGetType() {
        PercentageChange increase = new PercentageChange(entryCode, 5.0, 
                "t1", userId);
        PercentageChange decrease = new PercentageChange(entryCode, -5.0, 
                "t2", userId);

        assertEquals(ChangeType.PERCENTAGE_INCREASE, increase.getType());
        assertEquals(ChangeType.PERCENTAGE_DECREASE, decrease.getType());
    }

    /**
     * Tests the getDifference method after application.
     */
    @Test
    public void testGetDifference() {
        double percent = 10.0;
        PercentageChange change = new PercentageChange(entryCode, percent, 
                "t1", userId);

        // Difference before apply
        assertEquals(0, BigDecimal.ZERO.compareTo(change.getDifference()));

        // Difference after apply (10% of 2000 = 200)
        change.apply(entry);
        BigDecimal expectedDiff = new BigDecimal("200.00");
        assertEquals(0, expectedDiff.compareTo(change.getDifference()), 
                "Difference should be 200.00");
    }
}
