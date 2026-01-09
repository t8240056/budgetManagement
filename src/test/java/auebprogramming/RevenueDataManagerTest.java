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
       Positive tests: Έγκυροι κωδικοί
       ===================== */

    @Test
    public void testAll2DigitCodes() {
        for (String[] row : manager.get2DigitCodes()) {
            String code = row[0];
            assertDoesNotThrow(() -> manager.validateUserInput(null, code, 2),
                    "Validation failed for 2-digit code: " + code);
        }
    }

    @Test
    public void testAll3DigitCodes() {
        for (String[] row : manager.get3DigitCodes("")) {
            String code = row[0];
            String parent = code.substring(0, 2); // 2-digit parent
            assertDoesNotThrow(() -> manager.validateUserInput(parent, code, 3),
                    "Validation failed for 3-digit code: " + code);
        }
    }

    @Test
    public void testAll5DigitCodes() {
        for (String[] row : manager.get5DigitCodes("")) {
            String code = row[0];
            String parent = code.substring(0, 3); // 3-digit parent
            assertDoesNotThrow(() -> manager.validateUserInput(parent, code, 5),
                    "Validation failed for 5-digit code: " + code);
        }
    }

    @Test
    public void testAll7DigitCodes() {
        for (String[] row : manager.get7DigitCodes("")) {
            String code = row[0];
            String parent = code.substring(0, 5); // 5-digit parent
            assertDoesNotThrow(() -> manager.validateUserInput(parent, code, 7),
                    "Validation failed for 7-digit code: " + code);
        }
    }

    /* =====================
       Negative tests: Λάθη
       ===================== */

    @Test
    public void testInvalidLength() {
        assertThrows(AppException.class, () -> manager.validateUserInput(null, "1234", 2),
                "Expected exception for invalid length");
        assertThrows(AppException.class, () -> manager.validateUserInput(null, "1", 3),
                "Expected exception for invalid length");
        assertThrows(AppException.class, () -> manager.validateUserInput(null, "11111111", 5),
                "Expected exception for invalid length");
    }

    @Test
    public void testNonExistingCode() {
        assertThrows(AppException.class, () -> manager.validateUserInput(null, "99", 2),
                "Expected exception for non-existing 2-digit code");
        assertThrows(AppException.class, () -> manager.validateUserInput("11", "999", 3),
                "Expected exception for non-existing 3-digit code");
    }

    @Test
    public void testHierarchyViolation() {
        // 3-digit code δεν ανήκει στον 2-digit γονέα
        String valid2Digit = manager.get2DigitCodes()[0][0];
        String invalid3Digit = "999"; // πρέπει να μην ξεκινά με valid2Digit
        assertThrows(AppException.class, () -> manager.validateUserInput(valid2Digit, invalid3Digit, 3),
                "Expected exception for hierarchy violation");
    }
}

