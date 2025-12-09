package auebprogramming;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the BudgetStructureManager utility class.
 * verifies dynamic addition and removal of rows in the data array.
 */
public class BudgetStructureManagerTest {

    private String[][] initialData;

    /**
     * Initializes the test data before each test execution.
     */
    @Before
    public void setUp() {
        initialData = new String[][] {
            {"CODE", "NAME", "AMOUNT"},
            {"10", "Category 1", "100"},
            {"20", "Category 2", "200"}
        };
    }

    /**
     * Tests adding a new row to the data array.
     */
    @Test
    public void testAddRow() {
        final String[] newRow = {"30", "Category 3", "300"};
        final String[][] newData = BudgetStructureManager.addRow(initialData, newRow);

        Assert.assertEquals("New array length should be old length + 1",
                initialData.length + 1, newData.length);

        // Verify the last row matches the new data
        Assert.assertArrayEquals("Last row should match the added row",
                newRow, newData[newData.length - 1]);
    }

    /**
     * Tests deleting an existing row from the data array.
     */
    @Test
    public void testDeleteRowSuccess() {
        final String codeToDelete = "10";
        final String[][] newData = BudgetStructureManager.deleteRow(initialData, codeToDelete, 0);

        Assert.assertEquals("New array length should be old length - 1",
                initialData.length - 1, newData.length);

        // Verify "Category 1" (code 10) is gone. "Category 2" (code 20) should now be at index 1.
        Assert.assertEquals("Row with code 20 should shift up", "20", newData[1][0]);
    }

    /**
     * Tests attempting to delete a row that does not exist.
     */
    @Test
    public void testDeleteRowNotFound() {
        final String codeToDelete = "99";
        final String[][] newData = BudgetStructureManager.deleteRow(initialData, codeToDelete, 0);

        Assert.assertEquals("Array length should remain unchanged",
                initialData.length, newData.length);
        Assert.assertArrayEquals("Array content should remain unchanged",
                initialData, newData);
    }
}
