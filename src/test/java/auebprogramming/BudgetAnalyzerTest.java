package auebprogramming;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for BudgetAnalyzer class
 */
public class BudgetAnalyzerTest {

    @Test
    public void testFindTaktikosSynolo_ValidCode() {
        BudgetAnalyzer analyzer = new BudgetAnalyzer();
        Double result = analyzer.findTaktikosSynolo("1007");
        assertEquals(3449276000.0, result);
    }

    @Test 
    public void testFindTaktikosSynolo_InvalidCode() {
        BudgetAnalyzer analyzer = new BudgetAnalyzer();
        Double result = analyzer.findTaktikosSynolo("9999");
        assertNull(result);
    }

    @Test
    public void testFindOnomaForea_ValidCode() {
        BudgetAnalyzer analyzer = new BudgetAnalyzer();
        String result = analyzer.findOnomaForea("1011");
        assertEquals("YPOURGEIO ETHNIKIS AMYNAS", result);
    }

    @Test
    public void testIsValidKodikos() {
        BudgetAnalyzer analyzer = new BudgetAnalyzer();
        assertTrue(analyzer.isValidKodikos("1020"));
        assertFalse(analyzer.isValidKodikos("0000"));
    }
}
