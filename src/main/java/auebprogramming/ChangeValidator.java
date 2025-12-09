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
    
}
