package auebprogramming;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for BudgetAnalyzer.
 * Validates data loading and searching functionality.
 */
public class BudgetAnalyzerTest {

    /** The instance of BudgetAnalyzer under test. */
    private BudgetAnalyzer analyzer;

    /**
     * Set up the test environment before each test case.
     */
    @BeforeEach
    public void setUp() {
        analyzer = new BudgetAnalyzer();
    }

    /**
     * Tests if Article 2 data is loaded correctly.
     */
    @Test
    public void testLoadArticle2Data() {
        final String[][] data = analyzer.getArticle2Data();
        assertNotNull(data, "Article 2 data should not be null");
        assertTrue(data.length > 0, "Data table should have content");
    }

    /**
     * Tests if the detailed budget is returned for a valid entity code.
     */
    @Test
    public void testGetDetailedBudgetValidCode() {
        final int validCode = 1001;
        try {
            final String[][] detailed = analyzer.getDetailedBudget(validCode);
            assertNotNull(detailed, "Detailed data should not be null");
            assertTrue(detailed.length > 0, "Detailed file should contain data");
        } catch (IllegalArgumentException e) {
            fail("Should not throw exception for valid code 1001: " + e.getMessage());
        }
    }

    /**
     * Tests if the correct exception is thrown for an invalid entity code.
     */
    @Test
    public void testGetDetailedBudgetInvalidCode() {
        final int invalidCode = 9999;
        assertThrows(IllegalArgumentException.class, () -> {
            analyzer.getDetailedBudget(invalidCode);
        }, "Should throw IllegalArgumentException for code 9999");
    }
}
