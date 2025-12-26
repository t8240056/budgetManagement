package auebprogramming;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the BudgetTransferManager utility class.
 * Verifies fund transfers between budget categories.
 */
public class BudgetTransferManagerTest {

    private String[][] testData;

    /**
     * Initializes the test data before each test execution.
     */
    @Before
    public void setUp() {
        testData = new String[][] {
            {"CODE", "NAME", "AMOUNT"},
            {"1001", "Source Cat", "1000"}, // Index 1
            {"1002", "Dest Cat", "500"}     // Index 2
        };
    }

    /**
     * Tests a valid transfer where the source has sufficient funds.
     */
    @Test
    public void testTransferSuccess() {
        final long transferAmount = 200;
        final boolean result = BudgetTransferManager.transfer(testData, "1001", "1002", 0, 2, transferAmount);

        Assert.assertTrue("Transfer should succeed", result);
        Assert.assertEquals("Source should decrease by 200", "800", testData[1][2]);
        Assert.assertEquals("Destination should increase by 200", "700", testData[2][2]);
    }

    /**
     * Tests that transfer fails when source has insufficient funds.
     */
    @Test
    public void testTransferInsufficientFunds() {
        final long transferAmount = 2000; // More than available (1000)
        final boolean result = BudgetTransferManager.transfer(testData, "1001", "1002", 0, 2, transferAmount);

        Assert.assertFalse("Transfer should fail due to insufficient funds", result);
        Assert.assertEquals("Source amount should remain unchanged", "1000", testData[1][2]);
        Assert.assertEquals("Destination amount should remain unchanged", "500", testData[2][2]);
    }

    /**
     * Tests that transfer fails when one of the codes is invalid.
     */
    @Test
    public void testTransferInvalidCode() {
        final boolean result = BudgetTransferManager.transfer(testData, "1001", "9999", 0, 2, 100);

        Assert.assertFalse("Transfer should fail if a code is not found", result);
    }
}
