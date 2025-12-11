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
            assertDoesNotThrow(() -> manager.validateUserInput(null, code),
                    "Validation failed for 2-digit code: " + code);
        }
    }

    @Test
    public void testAll3DigitCodes() {
        for (String[] row : manager.get3DigitCodes("")) {
            String code = row[0];
            String parent = code.substring(0, 2); // 2-digit parent
            assertDoesNotThrow(() -> manager.validateUserInput(parent, code),
                    "Validation failed for 3-digit code: " + code);
        }
    }

    @Test
    public void testAll5DigitCodes() {
        for (String[] row : manager.get5DigitCodes("")) {
            String code = row[0];
            String parent = code.substring(0, 3); // 3-digit parent
            assertDoesNotThrow(() -> manager.validateUserInput(parent, code),
                    "Validation failed for 5-digit code: " + code);
        }
    }

    @Test
    public void testAll7DigitCodes() {
        for (String[] row : manager.get7DigitCodes("")) {
            String code = row[0];
            String parent = code.substring(0, 5); // 5-digit parent
            assertDoesNotThrow(() -> manager.validateUserInput(parent, code),
                    "Validation failed for 7-digit code: " + code);
        }
    }

}
