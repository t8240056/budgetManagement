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