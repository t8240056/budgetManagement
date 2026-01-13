package auebprogramming;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for BudgetChange.
 * Since BudgetChange is abstract, we use a concrete stub to test
 * the base functionality of the constructor and common getters.
 */
public class BudgetChangeTest {

    private BudgetChange stubChange;
    private final String testCode = "1111";
    private final String testReason = "Test justification";
    private final String testUser = "User01";

    /**
     * Concrete stub class to facilitate testing of the abstract base class.
     */
    private static class BudgetChangeStub extends BudgetChange {
        public BudgetChangeStub(final String code, final String reason, final String user) {
            super(code, reason, user);
        }

        @Override
        public BigDecimal apply(final BudgetChangesEntry entry) {
            return BigDecimal.ZERO;
        }

        @Override
        public BigDecimal undo(final BudgetChangesEntry entry) {
            return BigDecimal.ZERO;
        }

        @Override
        public BigDecimal getDifference() {
            return BigDecimal.ZERO;
        }

        @Override
        public ChangeType getType() {
            return ChangeType.ABSOLUTE_INCREASE;
        }
    }

    /**
     * Sets up a stub instance before each test.
     */
    @BeforeEach
    public void setUp() {
        stubChange = new BudgetChangeStub(testCode, testReason, testUser);
    }

    /**
     * Verifies that the constructor correctly initializes all base fields.
     */
    @Test
    @DisplayName("Test Constructor Initialization")
    public void testConstructor() {
        assertEquals(testCode, stubChange.getEntryCode());
        assertEquals(testReason, stubChange.getJustification());
        assertEquals(testUser, stubChange.getUserId());
        assertNotNull(stubChange.getTimestamp(), "Timestamp should be generated");
        // Check if timestamp is close to current time
        assertTrue(stubChange.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    /**
     * Verifies the toString formatting.
     */
    @Test
    @DisplayName("Test toString Format")
    public void testToString() {
        final String expected = String.format("%s [%s]: %s",
                stubChange.getType(), testCode, testReason);
        assertEquals(expected, stubChange.toString());
    }

    /**
     * Verifies the getDescription helper method.
     */
    @Test
    @DisplayName("Test getDescription helper")
    public void testGetDescription() {
        assertEquals(testReason, stubChange.getDescription());
    }
}
