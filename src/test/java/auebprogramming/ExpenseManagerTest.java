package auebprogramming;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for the ExpenseManager class.
 * Uses in-memory mock data to verify logic without relying on file I/O.
 */
public class ExpenseManagerTest {

    private ExpenseManager manager;

    /**
     * Sets up the test environment with mock data.
     */
    @Before
    public void setUp() {
        // Create Mock Data directly in memory (Skip file creation)
        // Format matches CSV: [Skip, Code, Description, Amount]
        String[][] mockData = {
            {"Skip", "Code", "Description", "Amount"}, // Row 0: Header
            {"X", "21", "Salary", "1000"},             // Row 1: Valid Code
            {"X", "29", "Appropriations", "0"},        // Row 2: Special Code 29
            {"X", "24", "Travel", "500"}               // Row 3: Another valid code
        };

        // Initialize ExpenseManager using the new constructor for testing
        manager = new ExpenseManager(mockData);
    }

    // No @After needed since we don't create files anymore!

    @Test
    public void testValidateExpenseCodeValid() throws AppException {
        manager.validateExpenseCode("21");
    }

    @Test(expected = AppException.class)
    public void testValidateExpenseCodeInvalid() throws AppException {
        manager.validateExpenseCode("999");
    }

    @Test
    public void testGetCategoryListReport() {
        final String report = manager.getCategoryListReport();
        
        Assert.assertNotNull("Report should not be null", report);
        Assert.assertTrue("Should contain header", report.contains("ΚΩΔΙΚΟΣ"));
        Assert.assertTrue("Should contain code 21", report.contains("21"));
        Assert.assertTrue("Should contain Salary", report.contains("Salary"));
    }

    @Test
    public void testGetExpenseDetailsReportValid() {
        final String report = manager.getExpenseDetailsReport("21");
        
        Assert.assertNotNull("Report should not be null", report);
        Assert.assertTrue("Should contain Code 21", report.contains("21"));
        // Note: Formatting depends on Locale inside the class. 
        // 1000 with Locale.GERMAN is "1.000"
        Assert.assertTrue("Should contain Amount 1.000", report.contains("1.000"));
    }

    @Test
    public void testGetExpenseDetailsReportSpecialCode29() {
        final String report = manager.getExpenseDetailsReport("29");
        
        Assert.assertTrue("Should contain Code 29", report.contains("29"));
        // Check for the hardcoded State Budget amount for code 29
        Assert.assertTrue("Should contain special amount 17.283.053.000", 
                report.contains("17.283.053.000"));
    }

    @Test
    public void testGetExpenseDetailsReportInvalid() {
        final String report = manager.getExpenseDetailsReport("999");
        
        Assert.assertTrue("Should contain error message", 
                report.contains("Μη έγκυρος κωδικός"));
    }

    @Test
    public void testGetFullExpensesReport() {
        final String report = manager.getFullExpensesReport();
        
        Assert.assertNotNull("Report should not be null", report);
        Assert.assertTrue("Should contain Total", report.contains("Σύνολο"));
        Assert.assertTrue("Should contain Travel entry", report.contains("Travel"));
        Assert.assertTrue("Should contain Salary entry", report.contains("Salary"));
    }
}
