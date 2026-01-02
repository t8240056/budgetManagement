package auebprogramming;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for the ExpenseManager class.
 * Verifies code validation, report generation, and data querying capabilities.
 */
public class ExpenseManagerTest {

    private File tempCsvFile;
    private ExpenseManager manager;

    /**
     * Sets up the test environment.
     * Creates a temporary CSV file with dummy data for testing.
     *
     * @throws IOException if file creation fails.
     */
    @Before
    public void setUp() throws IOException {
        // Create temporary CSV file
        tempCsvFile = File.createTempFile("test_expenses", ".csv");
        
        try (FileWriter writer = new FileWriter(tempCsvFile)) {
            // Write Header
            writer.write("Skip,Code,Description,Amount\n");
            
            // Write Data Rows
            // Normal row: Code 21, Salary, 1000
            writer.write("X,21,Salary,1000\n");
            
            // Special row: Code 29 (should trigger special logic)
            writer.write("X,29,Appropriations,0\n");
            
            // Another normal row
            writer.write("X,24,Travel,500\n");
        }

        // Initialize ExpenseManager with the temp file path
        manager = new ExpenseManager(tempCsvFile.getAbsolutePath());
    }

    /**
     * Cleans up the test environment by deleting the temp file.
     */
    @After
    public void tearDown() {
        if (tempCsvFile != null && tempCsvFile.exists()) {
            tempCsvFile.delete();
        }
    }

    /**
     * Tests validation of a valid expense code.
     * Should not throw any exception.
     *
     * @throws AppException if validation fails unexpectedly.
     */
    @Test
    public void testValidateExpenseCodeValid() throws AppException {
        manager.validateExpenseCode("21");
    }

    /**
     * Tests validation of an invalid expense code.
     * Should throw AppException.
     *
     * @throws AppException expected exception.
     */
    @Test(expected = AppException.class)
    public void testValidateExpenseCodeInvalid() throws AppException {
        manager.validateExpenseCode("999");
    }

    /**
     * Tests generation of the category list report.
     */
    @Test
    public void testGetCategoryListReport() {
        final String report = manager.getCategoryListReport();
        
        Assert.assertNotNull("Report should not be null", report);
        Assert.assertTrue("Should contain header", report.contains("ΚΩΔΙΚΟΣ"));
        Assert.assertTrue("Should contain code 21", report.contains("21"));
        Assert.assertTrue("Should contain Salary", report.contains("Salary"));
    }

    /**
     * Tests generation of the expense details report for a valid code.
     */
    @Test
    public void testGetExpenseDetailsReportValid() {
        final String report = manager.getExpenseDetailsReport("21");
        
        Assert.assertNotNull("Report should not be null", report);
        Assert.assertTrue("Should contain Code 21", report.contains("21"));
        Assert.assertTrue("Should contain Amount 1.000", report.contains("1.000"));
    }

    /**
     * Tests generation of the expense details report for the special code 29.
     * Verifies that hardcoded values are returned (e.g. 17.283.053.000).
     */
    @Test
    public void testGetExpenseDetailsReportSpecialCode29() {
        final String report = manager.getExpenseDetailsReport("29");
        
        Assert.assertTrue("Should contain Code 29", report.contains("29"));
        // Check for the hardcoded State Budget amount for code 29
        Assert.assertTrue("Should contain special amount 17.283.053.000", 
                report.contains("17.283.053.000"));
    }

    /**
     * Tests generation of the expense details report for an invalid code.
     * Should return an error message in the string, not throw exception.
     */
    @Test
    public void testGetExpenseDetailsReportInvalid() {
        final String report = manager.getExpenseDetailsReport("999");
        
        Assert.assertTrue("Should contain error message", 
                report.contains("Μη έγκυρος κωδικός"));
    }

    /**
     * Tests the full expenses report generation.
     */
    @Test
    public void testGetFullExpensesReport() {
        final String report = manager.getFullExpensesReport();
        
        Assert.assertNotNull("Report should not be null", report);
        Assert.assertTrue("Should contain Total", report.contains("Σύνολο"));
        
        // It should contain the sum of 1000 (Salary) + 500 (Travel) + 17283053000 (Cat 29)
        // We just check that it contains one of the entries to ensure it processed rows
        Assert.assertTrue("Should contain Travel entry", report.contains("Travel"));
        Assert.assertTrue("Should contain Salary entry", report.contains("Salary"));
    }
}
