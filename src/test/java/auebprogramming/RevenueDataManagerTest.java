package auebprogramming;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RevenueDataManagerTest {
    
    private static RevenueDataManager manager;

    @BeforeAll
    public static void setup() {
        manager = new RevenueDataManager();
    }
    
    /* =====================
       Positive tests: Valid codes
       ===================== */

    @Test
    public void testAll2DigitCodes() {
        // Test all 2-digit codes are valid
        for (String[] row : manager.get2DigitCodes()) {
            String code = row[0];
            assertDoesNotThrow(() -> manager.validateUserInput(null, code, 2),
                    "Validation failed for 2-digit code: " + code);
        }
    }

    @Test
    public void testAll3DigitCodes() {
        // Test all 3-digit codes are valid (each belongs to a 2-digit parent)
        for (String[] row : manager.get3DigitCodes("")) {
            String code = row[0];
            String parent = code.substring(0, 2); // Extract 2-digit parent code
            assertDoesNotThrow(() -> manager.validateUserInput(parent, code, 3),
                    "Validation failed for 3-digit code: " + code);
        }
    }

    @Test
    public void testAll5DigitCodes() {
        // Test all 5-digit codes are valid (each belongs to a 3-digit parent)
        for (String[] row : manager.get5DigitCodes("")) {
            String code = row[0];
            String parent = code.substring(0, 3); // Extract 3-digit parent code
            assertDoesNotThrow(() -> manager.validateUserInput(parent, code, 5),
                    "Validation failed for 5-digit code: " + code);
        }
    }

    @Test
    public void testAll7DigitCodes() {
        // Test all 7-digit codes are valid (each belongs to a 5-digit parent)
        for (String[] row : manager.get7DigitCodes("")) {
            String code = row[0];
            String parent = code.substring(0, 5); // Extract 5-digit parent code
            assertDoesNotThrow(() -> manager.validateUserInput(parent, code, 7),
                    "Validation failed for 7-digit code: " + code);
        }
    }

    /* =====================
       Negative tests: Error cases
       ===================== */

    @Test
    public void testInvalidLength() {
        // Test invalid code lengths for each hierarchy level
        assertThrows(AppException.class, () -> manager.validateUserInput(null, "1234", 2),
                "Expected exception for invalid length (4 digits for 2-digit code)");
        assertThrows(AppException.class, () -> manager.validateUserInput(null, "1", 3),
                "Expected exception for invalid length (1 digit for 3-digit code)");
        assertThrows(AppException.class, () -> manager.validateUserInput(null, "11111111", 5),
                "Expected exception for invalid length (8 digits for 5-digit code)");
    }

    @Test
    public void testNonExistingCode() {
        // Test non-existing codes at each level
        assertThrows(AppException.class, () -> manager.validateUserInput(null, "99", 2),
                "Expected exception for non-existing 2-digit code");
        assertThrows(AppException.class, () -> manager.validateUserInput("11", "999", 3),
                "Expected exception for non-existing 3-digit code");
    }

    @Test
    public void testHierarchyViolation() {
        // Test that a code doesn't belong to the specified parent
        String valid2Digit = manager.get2DigitCodes()[0][0]; // Get a valid 2-digit code
        String invalid3Digit = "999"; // This should NOT start with the valid2Digit prefix
        
        assertThrows(AppException.class, () -> manager.validateUserInput(valid2Digit, invalid3Digit, 3),
                "Expected exception for hierarchy violation (3-digit code doesn't belong to parent)");
    }
}