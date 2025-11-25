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