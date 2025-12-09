package auebprogramming;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a fund transfer between two budget entries.
 * Decreases amount from source entry and increases amount in target entry.
 */
public class TransferChange extends BudgetChange {
    
    private final BudgetChangesEntry targetEntry; // Ο λογαριασμός που θα πάρει τα λεφτά
    private final BigDecimal transferAmount;      // Το ποσό της μεταφοράς

    /**
     * Constructs a transfer change.
     * * @param sourceCode Code of the entry giving the money (Source)
     * @param targetEntry The actual object of the entry receiving the money (Target)
     * @param amount The amount to transfer
     * @param justification Reason for transfer
     * @param userId Who makes the transfer
     */
    public TransferChange(String sourceCode, BudgetChangesEntry targetEntry, 
                          BigDecimal amount, String justification, String userId) {
        super(sourceCode, justification, userId);
        
        // Έλεγχοι ασφαλείας
        this.targetEntry = Objects.requireNonNull(targetEntry, "Target entry cannot be null");
        this.transferAmount = Objects.requireNonNull(amount, "Amount cannot be null");
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Το ποσό μεταφοράς πρέπει να είναι θετικό: " + amount);
        }
    }

    /**
     * Executes the transfer.
     * 1. Checks if source has enough money.
     * 2. Subtracts from Source.
     * 3. Adds to Target.
     * * @param sourceEntry The entry giving the money (must match the sourceCode)
     */
    @Override
    public BigDecimal apply(BudgetChangesEntry sourceEntry) {
        // Σιγουρευόμαστε ότι πήραμε τον σωστό "δότης"
        if (!sourceEntry.getCode().equals(entryCode)) {
            throw new IllegalArgumentException("Λάθος λογαριασμός πηγής. Αναμενόταν: " + entryCode);
        }

        BigDecimal currentSourceAmount = sourceEntry.getAmount();

        // Βήμα 1: Έλεγχος Υπολοίπου (Ο "Δότης" έχει λεφτά;)
        if (currentSourceAmount.compareTo(transferAmount) < 0) {
            throw new IllegalArgumentException(
                "Ανεπαρκές υπόλοιπο για μεταφορά. Υπάρχουν: " + currentSourceAmount + 
                ", Απαιτούνται: " + transferAmount);
        }

        // Βήμα 2: Αφαίρεση από την Πηγή
        BigDecimal newSourceAmount = currentSourceAmount.subtract(transferAmount);
        sourceEntry.setAmount(newSourceAmount);

        // Βήμα 3: Πρόσθεση στον Προορισμό
        BigDecimal currentTargetAmount = targetEntry.getAmount();
        BigDecimal newTargetAmount = currentTargetAmount.add(transferAmount);
        targetEntry.setAmount(newTargetAmount);

        return newSourceAmount; // Επιστρέφει το νέο υπόλοιπο της πηγής
    }

    /**
     * Undoes the transfer (Reverses the transaction).
     * Takes money back from Target and returns it to Source.
     */
    @Override
    public BigDecimal undo(BudgetChangesEntry sourceEntry) {
        // Επιστροφή χρημάτων στην Πηγή
        BigDecimal currentSourceAmount = sourceEntry.getAmount();
        BigDecimal restoredSourceAmount = currentSourceAmount.add(transferAmount);
        sourceEntry.setAmount(restoredSourceAmount);

        // Αφαίρεση χρημάτων από τον Προορισμό (τα παίρνουμε πίσω)
        BigDecimal currentTargetAmount = targetEntry.getAmount();
        BigDecimal restoredTargetAmount = currentTargetAmount.subtract(transferAmount);
        targetEntry.setAmount(restoredTargetAmount);

        return restoredSourceAmount;
    }

    /**
     * Returns the negative impact on the source entry
     */
    @Override
    public BigDecimal getDifference() {
        return transferAmount.negate(); // Για την πηγή είναι μείωση (-amount)
    }

    @Override
    public ChangeType getType() {
        return ChangeType.TRANSFER;
    }
    
    public BudgetChangesEntry getTargetEntry() {
        return targetEntry;
    }
    
    public BigDecimal getTransferAmount() {
        return transferAmount;
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s -> %s (%,.2f €) [%s]", 
            getType(), entryCode, targetEntry.getCode(), transferAmount, justification);
    }
}
