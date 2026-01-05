package auebprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import auebprogramming.ChangeValidator.ValidationResult;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the ChangeValidator class.
 * Covers validation logic for absolute changes, percentage changes,
 * and transfers between budget entries.
 */
class ChangeValidatorTest {

    private BudgetRepository repository;
    private ChangeValidator validator;

    /**
     * Sets up a fresh repository and validator before each test.
     * Populates the repository with initial data for testing.
     */
    @BeforeEach
    void setUp() {
        repository = new BudgetRepository();
        validator = new ChangeValidator(repository);

        // Add a standard entry with 100.00 amount
        repository.save(new BudgetChangesEntry("C1", "Standard Entry",
                new BigDecimal("100.00")));
        
        // Add a wealthy entry for transfers
        repository.save(new BudgetChangesEntry("C2", "Wealthy Entry",
                new BigDecimal("1000.00")));
    }

    /**
     * Tests validation of a valid absolute amount change.
     */
    @Test
    void testValidateAbsoluteChangeValid() {
        // Adding 50 to 100 -> 150 (Valid)
        final ValidationResult result = validator.validateAbsoluteChange("C1",
                new BigDecimal("50.00"));

        assertTrue(result.isValid(), "Should be valid for reasonable amount");
        assertTrue(result.getErrors().isEmpty(), "Error list should be empty");
    }

    /**
     * Tests absolute change validation when the entry code does not exist.
     */
    @Test
    void testValidateAbsoluteChangeMissingCode() {
        final ValidationResult result = validator.validateAbsoluteChange(
                "GHOST", new BigDecimal("10.00"));

        assertFalse(result.isValid(), "Should be invalid for missing code");
        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().get(0).contains("Code does not exist"));
    }

    /**
     * Tests absolute change resulting in a negative balance.
     */
    @Test
    void testValidateAbsoluteChangeNegativeBalance() {
        // Subtracting 200 from 100 -> -100 (Invalid)
        final ValidationResult result = validator.validateAbsoluteChange("C1",
                new BigDecimal("-200.00"));

        assertFalse(result.isValid(), "Should be invalid for negative balance");
        assertTrue(result.getErrors().get(0).contains("cannot be negative"));
    }

    /**
     * Tests absolute change with an unreasonably large amount (> 1 billion).
     */
    @Test
    void testValidateAbsoluteChangeTooLarge() {
        final BigDecimal hugeAmount = new BigDecimal("2000000000"); // 2 billion
        final ValidationResult result = validator.validateAbsoluteChange("C1",
                hugeAmount);

        assertFalse(result.isValid(), "Should be invalid for huge amount");
        assertTrue(result.getErrors().get(0).contains("too large"));
    }

    /**
     * Tests validation of a valid percentage change.
     */
    @Test
    void testValidatePercentageChangeValid() {
        // Increasing by 10% (Valid)
        final ValidationResult result = validator.validatePercentageChange(
                "C1", 10.0);

        assertTrue(result.isValid(), "Should be valid for 10%");
    }

    /**
     * Tests percentage change with invalid range (> 100%).
     */
    @Test
    void testValidatePercentageChangeTooHigh() {
        final ValidationResult result = validator.validatePercentageChange(
                "C1", 150.0);

        assertFalse(result.isValid(), "Should be invalid for > 100%");
        assertTrue(result.getErrors().get(0).contains("exceed 100%"));
    }

    /**
     * Tests percentage change with invalid range (< -100%).
     */
    @Test
    void testValidatePercentageChangeTooLow() {
        final ValidationResult result = validator.validatePercentageChange(
                "C1", -150.0);

        assertFalse(result.isValid(), "Should be invalid for < -100%");
        assertTrue(result.getErrors().get(0).contains("below -100%"));
    }

    /**
     * Tests valid transfer between two entries.
     */
    @Test
    void testValidateTransferValid() {
        // Transfer 50 from C2 (1000) to C1 (100) -> Valid
        final ValidationResult result = validator.validateTransfer(
                "C2", "C1", new BigDecimal("50.00"));

        assertTrue(result.isValid(), "Transfer should be valid");
    }

    /**
     * Tests transfer where source or target codes are missing.
     */
    @Test
    void testValidateTransferMissingCodes() {
        // Case 1: Missing Source
        final ValidationResult res1 = validator.validateTransfer(
                "GHOST", "C1", BigDecimal.TEN);
        assertFalse(res1.isValid());
        assertTrue(res1.getErrors().get(0).contains("Source does not exist"));

        // Case 2: Missing Target
        final ValidationResult res2 = validator.validateTransfer(
                "C1", "GHOST", BigDecimal.TEN);
        assertFalse(res2.isValid());
        assertTrue(res2.getErrors().get(0).contains("Target does not exist"));
    }

    /**
     * Tests transfer where source has insufficient funds.
     */
    @Test
    void testValidateTransferInsufficientFunds() {
        // Try to transfer 500 from C1 (which has only 100)
        final ValidationResult result = validator.validateTransfer(
                "C1", "C2", new BigDecimal("500.00"));

        assertFalse(result.isValid(), "Should fail due to insufficient funds");
        assertTrue(result.getErrors().get(0).contains("Insufficient amount"));
    }

    /**
     * Tests the throwIfInvalid method of ValidationResult.
     * Verifies that it actually throws an exception when errors exist.
     */
    @Test
    void testThrowIfInvalid() {
        // Create a failing result (negative balance)
        final ValidationResult result = validator.validateAbsoluteChange("C1",
                new BigDecimal("-500.00"));

        // Verify that calling throwIfInvalid throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            result.throwIfInvalid();
        }, "Should throw exception when result is invalid");
    }
}
