package auebprogramming;

/**
 * Enumeration defining the types of changes that can be applied to the budget.
 */
public enum ChangeType {
    /** Represents an absolute increase in the amount. */
    ABSOLUTE_INCREASE,
    /** Represents an absolute decrease in the amount. */
    ABSOLUTE_DECREASE,
    /** Represents a transfer of funds between two entries. */
    TRANSFER,
    /** Represents a creation of a new entry. */
    CREATION,
    /** Represents a deletion of an entry. */
    DELETION, 
    PERCENTAGE_DECREASE, 
    PERCENTAGE_INCREASE
}
