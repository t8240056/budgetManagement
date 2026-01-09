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
 * Comprehensive Unit tests for the BudgetManager class.
 */
public class BudgetManagerTest {

    // Revenue Data Constants
    private static final String REV_CODE_1 = "1001";
    private static final String REV_DESC_1 = "Revenue A";
    private static final String REV_AMOUNT_1 = "1000.00";

    // Expense Data Constants (Simulating quoted CSV)
    private static final String EXP_CODE_1 = "5001";
    private static final String EXP_DESC_1 = "\"Expense with, comma\""; // Quoted description
    private static final String EXP_AMOUNT_1 = "500.00";

    private BudgetManager manager;
    private File tempRevenueFile;
    private File tempExpenseFile;

    @Before
    public void setUp() throws IOException, AppException {
        manager = new BudgetManager();

        // 1. Create dummy Revenue File (Simple CSV)
        tempRevenueFile = File.createTempFile("test_revenue", ".csv");
        try (FileWriter writer = new FileWriter(tempRevenueFile)) {
            writer.write("Code,Category,Amount\n");
            writer.write(REV_CODE_1 + "," + REV_DESC_1 + "," + REV_AMOUNT_1 + "\n");
            writer.write("1002,Revenue B,2000.00\n");
        }

        // 2. Create dummy Expense File (Complex CSV with quotes and headers)
        tempExpenseFile = File.createTempFile("test_expense", ".csv");
        try (FileWriter writer = new FileWriter(tempExpenseFile)) {
            writer.write("METADATA HEADER 1\n");
            writer.write("METADATA HEADER 2\n");
            // Note the quoted string to test regex parsing
            writer.write(EXP_CODE_1 + "," + EXP_DESC_1 + "," + EXP_AMOUNT_1 + "\n");
        }

        // Default setup: Load Revenue
        manager.setBudgetType(0); 
        manager.loadRevenueData(tempRevenueFile);
    }

    @After
    public void tearDown() {
        if (tempRevenueFile != null && tempRevenueFile.exists()) tempRevenueFile.delete();
        if (tempExpenseFile != null && tempExpenseFile.exists()) tempExpenseFile.delete();
    }

    // =========================================================================
    // 1. INITIALIZATION & FILE PARSING TESTS
    // =========================================================================

    @Test
    public void testLoadExpenseDataWithQuotes() throws AppException {
        // Switch to Expense mode and load complex file
        manager.setBudgetType(1);
        manager.loadOrganizationExpenses("5001", tempExpenseFile);

        BigDecimal amount = findAmountByCode(EXP_CODE_1);
        
        // Assert amount is parsed correctly
        Assert.assertEquals(500.00, amount.doubleValue(), 0.001);
        
        // Assert description stripped quotes correctly
        BudgetChangesEntry entry = getEntryByCode(EXP_CODE_1);
        Assert.assertEquals("Expense with, comma", entry.getDescription());
    }

    @Test(expected = AppException.class)
    public void testLoadNonExistentFile() throws AppException {
        manager.loadRevenueData(new File("non_existent_file.csv"));
    }

    // =========================================================================
    // 2. MODIFICATION TESTS (ABSOLUTE & PERCENTAGE)
    // =========================================================================

    @Test
    public void testMakeAbsoluteChange_InvalidFormat() {
        try {
            manager.makeAbsoluteChange(REV_CODE_1, "abc", "Invalid");
            Assert.fail("Should have thrown AppException");
        } catch (AppException e) {
            Assert.assertTrue(e.getMessage().contains("Invalid amount format"));
        }
    }

    @Test
    public void testMakePercentageChange_DecreaseSuccess() throws AppException {
        // Decrease 1000 by 50% -> 500
        manager.makePercentageChange(REV_CODE_1, "-50", "Half cut");
        Assert.assertEquals(500.00, findAmountByCode(REV_CODE_1).doubleValue(), 0.001);
    }

    @Test(expected = AppException.class)
    public void testMakePercentageChange_DecreaseTooMuch() throws AppException {
        // Decrease 1000 by 110% -> Should fail (Negative result)
        manager.makePercentageChange(REV_CODE_1, "-110", "Crash");
    }

    // =========================================================================
    // 3. TRANSFER TESTS
    // =========================================================================

    @Test
    public void testTransfer_FailSourceNotFound() {
        try {
            manager.makeTransfer("9999", REV_CODE_1, "100", "Bad Source");
            Assert.fail("Should fail");
        } catch (AppException e) {
            Assert.assertTrue(e.getMessage().contains("codes not found"));
        }
    }

    @Test
    public void testTransfer_FailInsufficientFunds() {
        try {
            // Source has 2000 (Code 1002). Try to transfer 2500.
            manager.makeTransfer("1002", REV_CODE_1, "2500", "Too much");
            Assert.fail("Should fail");
        } catch (AppException e) {
            Assert.assertTrue(e.getMessage().contains("Insufficient funds"));
        }
    }

    // =========================================================================
    // 4. UNDO LOGIC TESTS (CRITICAL)
    // =========================================================================

    @Test
    public void testUndoTransfer_RestoresBothAccounts() throws AppException {
        // Initial: 1001=1000, 1002=2000
        
        // 1. Transfer 500 from 1002 to 1001
        manager.makeTransfer("1002", REV_CODE_1, "500", "Test Transfer");
        
        Assert.assertEquals(1500.00, findAmountByCode(REV_CODE_1).doubleValue(), 0.001);
        Assert.assertEquals(1500.00, findAmountByCode("1002").doubleValue(), 0.001);

        // 2. Undo
        manager.undoLastAction();

        // 3. Verify Restoration
        Assert.assertEquals("Target should revert to 1000", 
                1000.00, findAmountByCode(REV_CODE_1).doubleValue(), 0.001);
        Assert.assertEquals("Source should revert to 2000", 
                2000.00, findAmountByCode("1002").doubleValue(), 0.001);
    }

    @Test(expected = AppException.class)
    public void testUndo_EmptyHistory() throws AppException {
        // No actions performed yet
        manager.undoLastAction();
    }

    @Test
    public void testUndo_MultipleActions() throws AppException {
        // 1. Add 100 to 1001 (1000 -> 1100)
        manager.makeAbsoluteChange(REV_CODE_1, "100", "Step 1");
        // 2. Add 200 to 1001 (1100 -> 1300)
        manager.makeAbsoluteChange(REV_CODE_1, "200", "Step 2");

        Assert.assertEquals(1300.00, findAmountByCode(REV_CODE_1).doubleValue(), 0.001);

        // Undo Step 2
        manager.undoLastAction();
        Assert.assertEquals(1100.00, findAmountByCode(REV_CODE_1).doubleValue(), 0.001);

        // Undo Step 1
        manager.undoLastAction();
        Assert.assertEquals(1000.00, findAmountByCode(REV_CODE_1).doubleValue(), 0.001);
    }

    // =========================================================================
    // 5. AUDIT LOG & SAVING
    // =========================================================================

    @Test
    public void testAuditLogUpdates() throws AppException {
        int initialSize = manager.getAuditLog().size();
        manager.makeAbsoluteChange(REV_CODE_1, "10", "Log Test");
        
        List<String> logs = manager.getAuditLog();
        Assert.assertEquals(initialSize + 1, logs.size());
        Assert.assertTrue(logs.get(logs.size() - 1).contains("Log Test"));
    }

    @Test
    public void testSaveWork_CreatesFile() throws AppException {
        // Save as a specific test name
        String saveName = "test_save_output.csv";
        String savedPath = manager.saveWork(saveName);

        File savedFile = new File(savedPath);
        
        Assert.assertTrue("File should be created", savedFile.exists());
        Assert.assertTrue("File size should be > 0", savedFile.length() > 0);

        // Cleanup the saved file immediately
        savedFile.delete();
    }

    // =========================================================================
    // HELPER METHODS
    // =========================================================================

    private BigDecimal findAmountByCode(final String code) {
        return manager.getEntriesList().stream()
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .map(BudgetChangesEntry::getAmount)
                .orElse(BigDecimal.ZERO);
    }
    
    private BudgetChangesEntry getEntryByCode(final String code) {
         return manager.getEntriesList().stream()
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}