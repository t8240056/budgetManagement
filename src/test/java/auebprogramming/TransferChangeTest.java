package auebprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the TransferChange class.
 * Verifies the logic for transferring amounts between budget entries,
 * including applying changes, undoing them, and validating constraints.
 */
class TransferChangeTest {

    private BudgetChangesEntry sourceEntry;
    private BudgetChangesEntry targetEntry;
    private TransferChange transferChange;
    private final BigDecimal amount = new BigDecimal("50.00");

    /**
     * Sets up the test environment before each test method.
     * Initializes a source entry with 100.00 and a target with 0.00.
     */
    @BeforeEach
    void setUp() {
        sourceEntry = new BudgetChangesEntry("SRC", "Source",
                new BigDecimal("100.00"));
        // Initialize with ZERO (scale 0)
        targetEntry = new BudgetChangesEntry("TGT", "Target",
                BigDecimal.ZERO);

        transferChange = new TransferChange("SRC", "TGT", amount,
                "Test Transfer", "User1");
    }

    /**
     * Tests applying the transfer to the source entry.
     * The amount should be subtracted from the source.
     */
    @Test
    void testApplyToSourceSuccess() {
        final BigDecimal result = transferChange.apply(sourceEntry);

        // 100 - 50 = 50
        assertEquals(new BigDecimal("50.00"), result,
                "Returned amount should be 50.00");
        assertEquals(new BigDecimal("50.00"), sourceEntry.getAmount(),
                "Source entry amount should be updated to 50.00");
    }

    /**
     * Tests applying a transfer that drains the entire balance.
     * Boundary condition: Result should be exactly 0.00.
     */
    @Test
    void testApplyExactBalance() {
        final BigDecimal fullAmount = new BigDecimal("100.00");
        final TransferChange fullTransfer = new TransferChange("SRC", "TGT",
                fullAmount, "All money", "User1");

        fullTransfer.apply(sourceEntry);

        // FIX: Expect "0.00" instead of ZERO to match the scale
        assertEquals(new BigDecimal("0.00"), sourceEntry.getAmount(),
                "Source balance should be 0.00 after full transfer");
    }

    /**
     * Tests applying the transfer to the target entry.
     * The amount should be added to the target.
     */
    @Test
    void testApplyToTargetSuccess() {
        transferChange.applyToTarget(targetEntry);

        // 0 + 50.00 = 50.00
        assertEquals(new BigDecimal("50.00"), targetEntry.getAmount(),
                "Target entry amount should be updated to 50.00");
    }

    /**
     * Tests applying a transfer when the source has insufficient funds.
     * Should throw IllegalArgumentException.
     */
    @Test
    void testApplyInsufficientFunds() {
        // Create a transfer larger than the source amount (150 > 100)
        final TransferChange bigTransfer = new TransferChange("SRC", "TGT",
                new BigDecimal("150.00"), "Justification", "User1");

        assertThrows(IllegalArgumentException.class, () -> {
            bigTransfer.apply(sourceEntry);
        }, "Should throw exception if source balance becomes negative");
    }

    /**
     * Tests undoing the transfer on the source entry.
     * The amount should be added back to the source.
     */
    @Test
    void testUndoSource() {
        // First apply the change (100 -> 50)
        transferChange.apply(sourceEntry);

        // Then undo it (50 + 50 -> 100)
        final BigDecimal result = transferChange.undo(sourceEntry);

        assertEquals(new BigDecimal("100.00"), result,
                "Returned amount should be restored to 100.00");
        assertEquals(new BigDecimal("100.00"), sourceEntry.getAmount(),
                "Source entry amount should be restored");
    }

    /**
     * Tests undoing the transfer on the target entry.
     * The amount should be subtracted from the target.
     */
    @Test
    void testUndoFromTarget() {
        // First apply to target (0 -> 50.00)
        transferChange.applyToTarget(targetEntry);

        // Then undo from target (50.00 - 50.00 -> 0.00)
        transferChange.undoFromTarget(targetEntry);

        // FIX: Expect "0.00" instead of ZERO to match the scale resulted form math
        assertEquals(new BigDecimal("0.00"), targetEntry.getAmount(),
                "Target entry amount should be restored to 0.00");
    }

    /**
     * Tests the getDifference method.
     * For a transfer, the difference on the source is negative.
     */
    @Test
    void testGetDifference() {
        // Expecting -50.00
        assertEquals(amount.negate(), transferChange.getDifference(),
                "Difference should be the negative transfer amount");
    }

    /**
     * Tests the getters for metadata (target code, type, amount).
     */
    @Test
    void testGetters() {
        assertEquals("TGT", transferChange.getTargetEntryCode(),
                "Target code should match");
        assertEquals(amount, transferChange.getTransferAmount(),
                "Transfer amount should match");
        assertEquals(ChangeType.TRANSFER, transferChange.getType(),
                "ChangeType should be TRANSFER");
    }
}
