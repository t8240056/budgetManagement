package auebprogramming;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the BudgetEditor utility class.
 * Checks if amount updates are performed correctly on the data array.
 */
public class BudgetEditorTest {

    private String[][] testData;

    /**
     * Initializes the test data before each test execution.
     * Creates a simulated budget table with headers and two rows.
     */
    @Before
    public void setUp() {
        testData = new String[][] {
            {"CODE", "NAME", "AMOUNT"},   // Header (row 0)
            {"1001", "Expense A", "1000"}, // Row 1
            {"1002", "Expense B", "2000"}  // Row 2
        };
    }

    /**
     * Tests that a valid update operation correctly changes the amount.
     */
    @Test
    public void testUpdateAmountSuccess() {
        final String targetCode = "1001";
        final long newAmount = 1500;
        final int codeCol = 0;
        final int amountCol = 2;

        final boolean result = BudgetEditor.updateAmount(testData, targetCode,
                codeCol, amountCol, newAmount);

        Assert.assertTrue("Update should return true for existing code", result);
        Assert.assertEquals("Amount should be updated to 1500", "1500", testData[1][amountCol]);
    }

    /**
     * Tests that the method returns false when the code does not exist.
     */
    @Test
    public void testUpdateAmountCodeNotFound() {
        final String targetCode = "9999"; // Non-existent code
        final long newAmount = 500;

        final boolean result = BudgetEditor.updateAmount(testData, targetCode, 0, 2, newAmount);

        Assert.assertFalse("Update should return false for non-existent code", result);
    }
}
