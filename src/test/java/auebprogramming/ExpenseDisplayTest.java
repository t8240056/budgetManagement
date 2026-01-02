package auebprogramming;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for the ExpenseDisplay class.
 * Verifies correct loading of data, report generation, and amount updates.
 */
public class ExpenseDisplayTest {

    private File categoriesFile;
    private File ministriesFile;
    private ExpenseDisplay display;

    /**
     * Sets up the test environment.
     * Creates temporary CSV files with dummy data for categories and ministries.
     *
     * @throws IOException if file creation fails.
     */
    @Before
    public void setUp() throws IOException {
        // 1. Create temporary Categories CSV
        categoriesFile = File.createTempFile("test_categories", ".csv");
        try (FileWriter writer = new FileWriter(categoriesFile)) {
            // Header row (to be skipped)
            writer.write("Skip,Code,Description,Amount\n");
            // Data rows
            writer.write("X,21,Salaries,1000\n");
            writer.write("X,23,Supplies,500\n");
            // Special category 29 (should use hardcoded values)
            writer.write("X,29,Appropriations,0\n");
        }

        // 2. Create temporary Ministries CSV
        ministriesFile = File.createTempFile("test_ministries", ".csv");
        try (FileWriter writer = new FileWriter(ministriesFile)) {
            // Header row
            writer.write("Code,Ministry,Regular,Investment,Total\n");
            // Data row: 1001, Ministry A, 100, 50, 150
            writer.write("1001,Ministry A,100,50,150\n");
        }

        // 3. Initialize ExpenseDisplay with temp files
        display = new ExpenseDisplay(categoriesFile.getAbsolutePath(),
                                     ministriesFile.getAbsolutePath());
    }

    /**
     * Cleans up temporary files after each test.
     */
    @After
    public void tearDown() {
        if (categoriesFile != null && categoriesFile.exists()) {
            categoriesFile.delete();
        }
        if (ministriesFile != null && ministriesFile.exists()) {
            ministriesFile.delete();
        }
    }

    /**
     * Tests that the categories report is generated correctly for State Budget.
     */
    @Test
    public void testGetCategoriesReportKratikos() {
        final String report = display.getCategoriesReport("ΚΡΑΤΙΚΟΣ");

        Assert.assertNotNull("Report should not be null", report);
        
        // Verify content
        Assert.assertTrue("Should contain salaries", report.contains("Salaries"));
        Assert.assertTrue("Should contain amount 1.000", report.contains("1.000"));
        
        // Category 29 should use the hardcoded constant (17,283,053,000)
        Assert.assertTrue("Should contain Cat 29 hardcoded value", 
                report.contains("17.283.053.000"));
    }

    /**
     * Tests that the categories report correctly handles Investment Budget.
     * Normal categories should be 0, except special ones.
     */
    @Test
    public void testGetCategoriesReportInvestment() {
        final String report = display.getCategoriesReport("ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ");

        // Normal category "Salaries" should show 0 for Investment Budget
        Assert.assertTrue("Salaries should be 0 in Investment Budget", 
                report.contains("Salaries") && report.contains(" 0"));
        
        // Category 29 should use hardcoded investment value (14,100,000,000)
        Assert.assertTrue("Cat 29 should match investment constant", 
                report.contains("14.100.000.000"));
    }

    /**
     * Tests that the ministries report is generated correctly.
     */
    @Test
    public void testGetMinistriesReport() {
        final String report = display.getMinistriesReport("ΚΡΑΤΙΚΟΣ");

        Assert.assertTrue("Should contain Ministry A", report.contains("Ministry A"));
        Assert.assertTrue("Should contain Total amount 150", report.contains("150"));
    }

    /**
     * Tests generating all 6 reports at once.
     */
    @Test
    public void testGetAllExpenseReports() {
        final String[] reports = display.getAllExpenseReports();

        Assert.assertEquals("Should return exactly 6 reports", 6, reports.length);
        Assert.assertNotNull("First report should not be null", reports[0]);
        Assert.assertTrue("First report should be Kratikos", reports[0].contains("ΚΡΑΤΙΚΟΣ"));
    }

    /**
     * Tests updating a category amount.
     */
    @Test
    public void testUpdateCategoryAmountSuccess() {
        // Update Salaries (21) from 1000 to 2000
        final boolean success = display.updateCategoryAmount("21", 2000L);

        Assert.assertTrue("Update should return true", success);

        // Verify the change in the report
        final String report = display.getCategoriesReport("ΚΡΑΤΙΚΟΣ");
        Assert.assertTrue("Report should show updated amount 2.000", report.contains("2.000"));
    }

    /**
     * Tests updating a non-existent category code.
     */
    @Test
    public void testUpdateCategoryAmountNotFound() {
        final boolean success = display.updateCategoryAmount("999", 5000L);

        Assert.assertFalse("Update should return false for invalid code", success);
    }
}
