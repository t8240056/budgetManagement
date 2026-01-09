package auebprogramming;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

/**
 * Πλήρης κλάση ελέγχου για το BudgetChangesEntry.
 */
public class BudgetChangesEntryTest {

    /**
     * Test 1: Ελέγχουμε αν ο κατασκευαστής και οι getters λειτουργούν.
     */
    @Test
    public void testConstructorAndGetters() {
        String code = "C001";
        String desc = "Γραφική Ύλη";
        BigDecimal amount = new BigDecimal("100.50");

        BudgetChangesEntry entry = new BudgetChangesEntry(code, desc, amount);

        assertEquals(code, entry.getCode(), "Ο κωδικός δεν αποθηκεύτηκε σωστά");
        assertEquals(desc, entry.getDescription(), "Η περιγραφή δεν αποθηκεύτηκε σωστά");
        assertEquals(amount, entry.getAmount(), "Το ποσό δεν αποθηκεύτηκε σωστά");
    }

    /**
     * Test 2: Ελέγχουμε την αλλαγή ποσού (setAmount).
     */
    @Test
    public void testSetAmount() {
        BudgetChangesEntry entry = new BudgetChangesEntry("C002", "Διάφορα", new BigDecimal("50.00"));
        BigDecimal newAmount = new BigDecimal("75.00");

        entry.setAmount(newAmount);

        assertEquals(newAmount, entry.getAmount(), "Το ποσό δεν ενημερώθηκε");
    }

    /**
     * Test 3: Ελέγχουμε την ισότητα (equals).
     * Δύο εγγραφές είναι ίσες αν έχουν τον ίδιο κωδικό.
     */
    @Test
    public void testEquality() {
        BudgetChangesEntry entry1 = new BudgetChangesEntry("K100", "Έξοδα Α", new BigDecimal("10"));
        BudgetChangesEntry entry2 = new BudgetChangesEntry("K100", "Έξοδα Β", new BigDecimal("20")); 
        BudgetChangesEntry entry3 = new BudgetChangesEntry("K999", "Έξοδα Γ", new BigDecimal("10"));

        // Πρέπει να είναι ίσα (ίδιος κωδικός K100)
        assertTrue(entry1.equals(entry2), "Τα αντικείμενα με ίδιο κωδικό έπρεπε να είναι ίσα");
        
        // Δεν πρέπει να είναι ίσα (άλλος κωδικός)
        assertFalse(entry1.equals(entry3), "Τα αντικείμενα με διαφορετικό κωδικό δεν έπρεπε να είναι ίσα");
    }

    /**
     * Test 4: Ελέγχουμε την toString.
     */
    @Test
    public void testToString() {
        BudgetChangesEntry entry = new BudgetChangesEntry("T1", "Test", new BigDecimal("1000.00"));
        String result = entry.toString();

        // Ελέγχουμε αν περιέχει τα βασικά στοιχεία
        assertTrue(result.contains("T1"));
        assertTrue(result.contains("Test"));
    }

    /**
     * Test 5: Ελέγχουμε αν ο κώδικας "σκάει" σωστά (πετάει Exception) 
     * όταν δώσουμε null τιμές.
     */
    @Test
    public void testNullValidation() {
        // Περίπτωση Α: Null κωδικός στον constructor
        assertThrows(NullPointerException.class, () -> {
            new BudgetChangesEntry(null, "Desc", new BigDecimal("10"));
        }, "Δεν πέταξε εξαίρεση για null κωδικό");

        // Περίπτωση Β: Null ποσό στον constructor
        assertThrows(NullPointerException.class, () -> {
            new BudgetChangesEntry("C1", "Desc", null);
        }, "Δεν πέταξε εξαίρεση για null ποσό");

        // Περίπτωση Γ: Null ποσό στον setter
        BudgetChangesEntry entry = new BudgetChangesEntry("C1", "Desc", new BigDecimal("10"));
        assertThrows(NullPointerException.class, () -> {
            entry.setAmount(null);
        }, "Δεν πέταξε εξαίρεση στο setAmount(null)");
    }
}
