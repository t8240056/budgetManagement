package auebprogramming;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Validates budget changes before they are applied
 * Ensures changes are valid and won't cause issues
 */
public class ChangeValidator {
    private final BudgetRepository repository;
    
    /**
     * Constructs a ChangeValidator with the given repository
     * @param repository the repository to validate against
     */
    public ChangeValidator(BudgetRepository repository) {
        this.repository = repository;
    }

        /**
     * Validates an absolute amount change
     * @param code entry code to validate
     * @param changeAmount amount to change by
     * @return ValidationResult containing any errors found
     */
    public ValidationResult validateAbsoluteChange(String code, BigDecimal changeAmount) {
        List<String> errors = new ArrayList<>();
        
        // Check if entry exists
        if (!repository.exists(code)) {
            errors.add("Code does not exist: " + code);
            return new ValidationResult(errors);
        }
        
        // Calculate new amount and validate
        BudgetChangesEntry entry = repository.findByCode(code).get();
        BigDecimal newAmount = entry.getAmount().add(changeAmount);
        
        // Check for negative amount
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            errors.add("New amount cannot be negative: " + newAmount);
        }
        
        // Check for unreasonably large change
        if (changeAmount.abs().compareTo(new BigDecimal("1000000000")) > 0) {
            errors.add("Change amount is too large (>1 billion)");
        }
        
        return new ValidationResult(errors);
    }
    
        /**
     * Validates a percentage change
     * @param code entry code to validate
     * @param percentage percentage to change by
     * @return ValidationResult containing any errors found
     */
    public ValidationResult validatePercentageChange(String code, double percentage) {
        List<String> errors = new ArrayList<>();
        
        // Check if entry exists
        if (!repository.exists(code)) {
            errors.add("Code does not exist: " + code);
            return new ValidationResult(errors);
        }
        
        // Validate percentage range
        if (Math.abs(percentage) > 100) {
            errors.add("Percentage cannot exceed 100%: " + percentage + "%");
        }
        
        if (percentage < -100) {
            errors.add("Percentage cannot be below -100%: " + percentage + "%");
        }
        
        return new ValidationResult(errors);
    }

        /**
     * Validates a transfer between entries
     * @param sourceCode entry code giving amount
     * @param targetCode entry code receiving amount
     * @param amount amount to transfer
     * @return ValidationResult containing any errors found
     */
    public ValidationResult validateTransfer(String sourceCode, String targetCode, 
                                           BigDecimal amount) {
        List<String> errors = new ArrayList<>();
        
        // Check if both entries exist
        if (!repository.exists(sourceCode)) {
            errors.add("Source does not exist: " + sourceCode);
        }
        
        if (!repository.exists(targetCode)) {
            errors.add("Target does not exist: " + targetCode);
        }
        
        // If both exist, check if source has enough amount
        if (errors.isEmpty()) {
            BudgetChangesEntry source = repository.findByCode(sourceCode).get();
            
            if (source.getAmount().compareTo(amount) < 0) {
                errors.add("Insufficient amount: " + source.getAmount() + 
                          " < " + amount);
            }
        }
        
        return new ValidationResult(errors);
    }

         /**
     * Represents the result of a validation check
     * Contains any errors found during validation
     */
    public static class ValidationResult {
        private final List<String> errors;
        private final boolean isValid;    
        /**
         * Constructs a ValidationResult with the given errors
         * @param errors list of error messages (empty list means valid)
         */
        public ValidationResult(List<String> errors) {
            this.errors = errors;
            this.isValid = errors.isEmpty();
        }
        
        /**
         * Checks if the validation passed (no errors)
         * @return true if valid, false otherwise
         */
        public boolean isValid() { return isValid; }
        
        /**
         * Returns the list of error messages
         * @return list of errors (empty if valid)
         */
        public List<String> getErrors() { return errors; }
        
        /**
         * Throws an IllegalArgumentException if validation failed
         * @throws IllegalArgumentException with error messages if not valid
         */
        public void throwIfInvalid() {
            if (!isValid) {
                throw new IllegalArgumentException(
                    "Invalid change: " + String.join(", ", errors));
            }
        }
    }

}
