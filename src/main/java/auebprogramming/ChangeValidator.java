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
}
