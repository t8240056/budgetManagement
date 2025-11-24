/**
 * Represents a component responsible for displaying
 * budget-related information to the user.
 *
 * Any class that implements this interface must define
 * how the budget data is displayed.
 */
public interface BudgetDisplay {

    /**
     * Displays the budget data.
     * Implementing classes should specify the exact
     * format and content of the output (e.g., tables, charts, text).
     */
    void displayData();
}
