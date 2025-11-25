package auebprogramming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for BudgetAnalyzer class
 * Testing budget analysis functionality for Greek ministries
 */
public class BudgetAnalyzerTest {
    
    private BudgetAnalyzer budgetAnalyzer;
    
    @BeforeEach
    void setUp() {
        budgetAnalyzer = new BudgetAnalyzer();
    }
}
@Test
void testIsValidKodikos_WithValidCode_ReturnsTrue() {
    // Given
    String validKodikos = "1001";
    
    // When
    boolean result = budgetAnalyzer.isValidKodikos(validKodikos);
    
    // Then
    assertTrue(result, "Should return true for valid ministry code");
}

@Test
void testIsValidKodikos_WithInvalidCode_ReturnsFalse() {
    // Given
    String invalidKodikos = "9999";
    
    // When
    boolean result = budgetAnalyzer.isValidKodikos(invalidKodikos);
    
    // Then
    assertFalse(result, "Should return false for invalid ministry code");
}

@Test
void testIsValidKodikos_WithNullCode_ReturnsFalse() {
    // Given
    String nullKodikos = null;
    
    // When
    boolean result = budgetAnalyzer.isValidKodikos(nullKodikos);
    
    // Then
    assertFalse(result, "Should return false for null code");
}
@Test
void testFindOnomaForea_WithExistingCode_ReturnsCorrectName() {
    // Given
    String kodikos = "1015";
    String expectedName = "YPOURGEIO YGEIAS";
    
    // When
    String actualName = budgetAnalyzer.findOnomaForea(kodikos);
    
    // Then
    assertEquals(expectedName, actualName, "Should return correct ministry name");
}

@Test
void testFindOnomaForea_WithNonExistingCode_ReturnsNull() {
    // Given
    String kodikos = "0000";
    
    // When
    String result = budgetAnalyzer.findOnomaForea(kodikos);
    
    // Then
    assertNull(result, "Should return null for non-existing ministry code");
}

@Test
void testFindOnomaForea_WithDifferentMinistries_ReturnsCorrectNames() {
    // Test multiple ministries to ensure data consistency
    assertEquals("YPOURGEIO ETHNIKIS AMYNAS", budgetAnalyzer.findOnomaForea("1011"));
    assertEquals("YPOURGEIO PAIDEIAS, THRISKEVMATON KAI ATHLITISMOU", budgetAnalyzer.findOnomaForea("1020"));
    assertEquals("YPOURGEIO POLITISMOU", budgetAnalyzer.findOnomaForea("1022"));
}
@Test
void testFindTaktikosSynolo_WithExistingCode_ReturnsCorrectAmount() {
    // Given
    String kodikos = "1024"; // Υπουργείο Οικονομικών
    Double expectedAmount = 1243381464000.0;
    
    // When
    Double actualAmount = budgetAnalyzer.findTaktikosSynolo(kodikos);
    
    // Then
    assertEquals(expectedAmount, actualAmount, "Should return correct regular budget amount");
}

@Test
void testFindTaktikosSynolo_WithNonExistingCode_ReturnsNull() {
    // Given
    String kodikos = "9999";
    
    // When
    Double result = budgetAnalyzer.findTaktikosSynolo(kodikos);
    
    // Then
    assertNull(result, "Should return null for non-existing ministry code");
}

@Test
void testFindDimosiesSynolo_WithExistingCode_ReturnsCorrectAmount() {
    // Given
    String kodikos = "1020"; // Υπουργείο Παιδείας
    Double expectedAmount = 1012000000.0;
    
    // When
    Double actualAmount = budgetAnalyzer.findDimosiesSynolo(kodikos);
    
    // Then
    assertEquals(expectedAmount, actualAmount, "Should return correct public investments amount");
}

@Test
void testFindDimosiesSynolo_WithZeroInvestment_ReturnsZero() {
    // Given
    String kodikos = "1001"; // Προεδρία Δημοκρατίας
    Double expectedAmount = 0.0;
    
    // When
    Double actualAmount = budgetAnalyzer.findDimosiesSynolo(kodikos);
    
    // Then
    assertEquals(expectedAmount, actualAmount, "Should return zero for ministries with no public investments");
}
@Test
void testDisplaySynola_WithValidCode_ReturnsTrue() {
    // Given
    String validKodikos = "1007"; // Υπουργείο Εσωτερικών
    
    // When
    boolean result = budgetAnalyzer.displaySynola(validKodikos);
    
    // Then
    assertTrue(result, "Should return true for valid ministry code");
}

@Test
void testDisplaySynola_WithInvalidCode_ReturnsFalse() {
    // Given
    String invalidKodikos = "9999";
    
    // When
    boolean result = budgetAnalyzer.displaySynola(invalidKodikos);
    
    // Then
    assertFalse(result, "Should return false for invalid ministry code");
}

@Test
void testDisplaySynola_WithNullCode_ReturnsFalse() {
    // Given
    String nullKodikos = null;
    
    // When
    boolean result = budgetAnalyzer.displaySynola(nullKodikos);
    
    // Then
    assertFalse(result, "Should return false for null code");
}
@Test
void testCompleteBudgetScenario_WithMinistryHavingBothBudgets() {
    // Given - Ministry with both regular budget and public investments
    String kodikos = "1015"; // Υπουργείο Υγείας
    String expectedName = "YPOURGEIO YGEIAS";
    Double expectedTaktikos = 6608424000.0;
    Double expectedDimosies = 569000000.0;
    
    // When & Then - Test individual components
    assertTrue(budgetAnalyzer.isValidKodikos(kodikos));
    assertEquals(expectedName, budgetAnalyzer.findOnomaForea(kodikos));
    assertEquals(expectedTaktikos, budgetAnalyzer.findTaktikosSynolo(kodikos));
    assertEquals(expectedDimosies, budgetAnalyzer.findDimosiesSynolo(kodikos));
    assertTrue(budgetAnalyzer.displaySynola(kodikos));
}

@Test
void testCompleteBudgetScenario_WithMinistryHavingOnlyRegularBudget() {
    // Given - Ministry with only regular budget (zero public investments)
    String kodikos = "1001"; // Προεδρία Δημοκρατίας
    
    // When & Then
    assertTrue(budgetAnalyzer.isValidKodikos(kodikos));
    assertNotNull(budgetAnalyzer.findOnomaForea(kodikos));
    assertNotNull(budgetAnalyzer.findTaktikosSynolo(kodikos));
    assertEquals(0.0, budgetAnalyzer.findDimosiesSynolo(kodikos));
    assertTrue(budgetAnalyzer.displaySynola(kodikos));
}

@Test
void testRegionalAdministrations_HaveValidData() {
    // Test that regional administrations have proper data
    String[] regionalCodes = {"1901", "1902", "1903", "1904", "1905", "1906", "1907"};
    
    for (String code : regionalCodes) {
        assertTrue(budgetAnalyzer.isValidKodikos(code), "Regional code " + code + " should be valid");
        assertNotNull(budgetAnalyzer.findOnomaForea(code), "Should have name for regional code " + code);
        assertNotNull(budgetAnalyzer.findTaktikosSynolo(code), "Should have regular budget for regional code " + code);
    }
}
