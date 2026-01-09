package auebprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for AbsoluteAmountChange.
 * Follows Sun coding conventions.
 */
public class AbsoluteAmountChangeTest {

    private BudgetChangesEntry entry;
    private final String entryCode = "1110103";
    private final String userId = "tester123";

    /**
     * Initializes a new entry before each test.
     */
    @BeforeEach
    public void setUp() {
        // Initial amount: 1000.00
        entry = new BudgetChangesEntry(entryCode, "Initial Description", 
                new BigDecimal("1000.00"));
    }

    /**
     * Tests the application of a positive amount (increase).
     */
    @Test
    public void testApplyIncrease() {
        BigDecimal increase = new BigDecimal("500.00");
        AbsoluteAmountChange change = new AbsoluteAmountChange(entryCode, 
                increase, "Budget Increase", userId);

        BigDecimal result = change.apply(entry);

        assertEquals(new BigDecimal("1500.00"), result, 
                "The amount should increase by 500");
        assertEquals(new BigDecimal("1500.00"), entry.getAmount(), 
                "The entry amount should be updated");
    }

    /**
     * Tests the application of a negative amount (decrease).
     */
    @Test
    public void testApplyDecrease() {
        BigDecimal decrease = new BigDecimal("-300.00");
        AbsoluteAmountChange change = new AbsoluteAmountChange(entryCode, 
                decrease, "Budget Reduction", userId);

        BigDecimal result = change.apply(entry);

        assertEquals(new BigDecimal("700.00"), result, 
                "The amount should decrease by 300");
    }

    /**
     * Tests that an IllegalArgumentException is thrown when the new amount 
     * becomes negative.
     */
    @Test
    public void testApplyNegativeResultThrowsException() {
        BigDecimal excessiveDecrease = new BigDecimal("-1100.00");
        AbsoluteAmountChange change = new AbsoluteAmountChange(entryCode, 
                excessiveDecrease, "Invalid Reduction", userId);

        assertThrows(IllegalArgumentException.class, () -> {
            change.apply(entry);
        }, "Should throw exception as result would be -100");
    }

    /**
     * Tests the undo functionality.
     */
    @Test
    public void testUndo() {
        BigDecimal amount = new BigDecimal("200.00");
        AbsoluteAmountChange change = new AbsoluteAmountChange(entryCode, 
                amount, "Temporary Change", userId);

        // First apply the change
        change.apply(entry); // Becomes 1200
        
        // Then undo the change
        BigDecimal result = change.undo(entry);

        assertEquals(new BigDecimal("1000.00"), result, 
                "Undo should return amount to 1000");
        assertEquals(new BigDecimal("1000.00"), entry.getAmount(), 
                "The entry amount should be restored");
    }

    /**
     * Tests the getType method for both increase and decrease.
     */
    @Test
    public void testGetType() {
        AbsoluteAmountChange increase = new AbsoluteAmountChange(entryCode, 
                new BigDecimal("100.00"), "test", userId);
        AbsoluteAmountChange decrease = new AbsoluteAmountChange(entryCode, 
                new BigDecimal("-100.00"), "test", userId);

        assertEquals(ChangeType.ABSOLUTE_INCREASE, increase.getType());
        assertEquals(ChangeType.ABSOLUTE_DECREASE, decrease.getType());
    }

    /**
     * Tests the getDifference method.
     */
    @Test
    public void testGetDifference() {
        BigDecimal diff = new BigDecimal("250.00");
        AbsoluteAmountChange change = new AbsoluteAmountChange(entryCode, 
                diff, "test", userId);

        assertEquals(diff, change.getDifference());
    }
}
