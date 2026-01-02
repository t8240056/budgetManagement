package auebprogramming;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the BudgetManager class.
 * Verifies file loading, budget modification, transfers, and undo functionality.
 */
public class BudgetManagerTest {

    private static final String TEST_CODE_1 = "1001";
    private static final String TEST_DESC_1 = "Test Category A";
    private static final String TEST_AMOUNT_1 = "1000.00";

    private static final String TEST_CODE_2 = "1002";
    private static final String TEST_DESC_2 = "Test Category B";
    private static final String TEST_AMOUNT_2 = "2000.00";

    private BudgetManager manager;
    private File tempFile;

    /**
     * Sets up the test environment before each test.
     * Initializes the manager and creates a temporary CSV file with dummy data.
     *
     * @throws IOException if file creation fails.
     * @throws AppException if manager initialization fails.
     */
    @Before
    public void setUp() throws IOException, AppException {
        manager = new BudgetManager();
        tempFile = File.createTempFile("test_budget", ".csv");

        // Write dummy data to the temp file
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Code,Category,Amount\n");
            writer.write(TEST_CODE_1 + "," + TEST_DESC_1 + "," + TEST_AMOUNT_1 + "\n");
            writer.write(TEST_CODE_2 + "," + TEST_DESC_2 + "," + TEST_AMOUNT_2 + "\n");
        }

        // Initialize manager with Revenue type (0) and load the temp file
        manager.setBudgetType(0); // 0 = Revenue
        manager.loadRevenueData(tempFile);
    }

    /**
     * Cleans up the test environment after each test.
     * Deletes the temporary file.
     */
    @After
    public void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            final boolean deleted = tempFile.delete();
            if (!deleted) {
                System.err.println("Warning: Could not delete temp file "
                        + tempFile.getAbsolutePath());
            }
        }
    }

    /**
     * Tests that setting an invalid budget type throws an exception.
     *
     * @throws AppException expected exception.
     */
    @Test(expected = AppException.class)
    public void testSetInvalidBudgetType() throws AppException {
        manager.setBudgetType(99);
    }

    /**
     * Tests that data is loaded correctly from the CSV file.
     */
    @Test
    public void testLoadDataSuccess() {
        final List<BudgetChangesEntry> entries = manager.getEntriesList();
        
        Assert.assertNotNull("Entries list should not be null", entries);
        Assert.assertEquals("Should have 2 entries", 2, entries.size());
        
        // Verify total amount (1000 + 2000 = 3000)
        final BigDecimal expectedTotal = new BigDecimal("3000.00");
        Assert.assertEquals("Total amount should match sum",
                expectedTotal.doubleValue(),
                manager.getTotalAmount().doubleValue(),
                0.001);
    }

    /**
     * Tests making an absolute amount increase.
     *
     * @throws AppException if update fails.
     */
    @Test
    public void testMakeAbsoluteChangeIncrease() throws AppException {
        final String result = manager.makeAbsoluteChange(TEST_CODE_1, "500", "Bonus");
        
        Assert.assertTrue("Result should indicate success", result.contains("Success"));
        
        // 1000 + 500 = 1500
        final BigDecimal expected = new BigDecimal("1500.00");
        final BigDecimal actual = findAmountByCode(TEST_CODE_1);
        
        Assert.assertEquals("Amount should increase by 500",
                expected.doubleValue(), actual.doubleValue(), 0.001);
    }

    /**
     * Tests making an absolute amount decrease that results in negative funds.
     * Should throw an exception.
     *
     * @throws AppException expected exception.
     */
    @Test(expected = AppException.class)
    public void testMakeAbsoluteChangeInsufficientFunds() throws AppException {
        // Try to subtract 1500 from 1000
        manager.makeAbsoluteChange(TEST_CODE_1, "-1500", "Error Test");
    }

    /**
     * Tests making a percentage increase.
     *
     * @throws AppException if update fails.
     */
    @Test
    public void testMakePercentageChange() throws AppException {
        // Increase 1000 by 10% -> 1100
        final String result = manager.makePercentageChange(TEST_CODE_1, "10", "Inflation");
        
        Assert.assertTrue("Result should indicate success", result.contains("Success"));
        
        final BigDecimal expected = new BigDecimal("1100.00");
        final BigDecimal actual = findAmountByCode(TEST_CODE_1);
        
        Assert.assertEquals("Amount should increase by 10%",
                expected.doubleValue(), actual.doubleValue(), 0.001);
    }

    /**
     * Tests transferring funds between two valid entries.
     *
     * @throws AppException if transfer fails.
     */
    @Test
    public void testMakeTransferSuccess() throws AppException {
        // Transfer 500 from 1002 (2000) to 1001 (1000)
        final String result = manager.makeTransfer(TEST_CODE_2, TEST_CODE_1, "500", "Support");
        
        Assert.assertTrue("Result should indicate completion",
                result.contains("Transfer Complete"));

        // Check Source (1002): 2000 - 500 = 1500
        Assert.assertEquals("Source should decrease",
                1500.00, findAmountByCode(TEST_CODE_2).doubleValue(), 0.001);

        // Check Target (1001): 1000 + 500 = 1500
        Assert.assertEquals("Target should increase",
                1500.00, findAmountByCode(TEST_CODE_1).doubleValue(), 0.001);
    }

    /**
     * Tests the undo functionality after a change.
     *
     * @throws AppException if undo fails.
     */
    @Test
    public void testUndoLastAction() throws AppException {
        // 1. Perform action: 1000 -> 1500
        manager.makeAbsoluteChange(TEST_CODE_1, "500", "Mistake");
        Assert.assertEquals(1500.00, findAmountByCode(TEST_CODE_1).doubleValue(), 0.001);

        // 2. Undo
        final String undoMsg = manager.undoLastAction();
        Assert.assertTrue("Undo message should correspond", undoMsg.contains("Undone"));

        // 3. Verify return to original: 1000
        Assert.assertEquals("Amount should revert to original",
                1000.00, findAmountByCode(TEST_CODE_1).doubleValue(), 0.001);
    }

    /**
     * Tests saving the work to a new file.
     *
     * @throws AppException if save fails.
     */
    @Test
    public void testSaveWork() throws AppException {
        final String saveName = "test_output.csv";
        
        // Save the current state
        final String savedPath = manager.saveWork(saveName);
        
        final File savedFile = new File(savedPath);
        Assert.assertTrue("Saved file should exist", savedFile.exists());
        Assert.assertTrue("Saved file path should contain name",
                savedPath.contains(saveName));
        
        // Cleanup the saved file
        savedFile.delete();
    }

    /**
     * Helper method to find the amount of an entry by code from the manager.
     *
     * @param code The code to search for.
     * @return The amount as BigDecimal, or ZERO if not found.
     */
    private BigDecimal findAmountByCode(final String code) {
        return manager.getEntriesList().stream()
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .map(BudgetChangesEntry::getAmount)
                .orElse(BigDecimal.ZERO);
    }
}
