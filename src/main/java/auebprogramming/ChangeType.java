package auebprogramming;

/**
 * Enumeration of all supported change types in the budget system
 */
public enum ChangeType {
    /** Absolute increase by a fixed amount (e.g., +1,000,000) */
    ABSOLUTE_INCREASE,
    
    /** Absolute decrease by a fixed amount (e.g., -500,000) */
    ABSOLUTE_DECREASE,
    
    /** Percentage increase (e.g., +10%) */
    PERCENTAGE_INCREASE,
    
    /** Percentage decrease (e.g., -5%) */
    PERCENTAGE_DECREASE,
    
    /** Transfer amount from one entry to another */
    TRANSFER,
    
    /** Correction of an error in an entry */
    CORRECTION
}
