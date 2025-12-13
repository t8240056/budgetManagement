package auebprogramming;

import java.math.BigDecimal;

public class Main1 {
    public static void main(String[] args) {
    // 1. Δημιουργία της Αποθήκης (Κενή)
    BudgetRepository repo = new BudgetRepository();

    // 2. Φόρτωση από το αρχείο (Η Γέφυρα δουλεύει εδώ)
    BudgetIOHandler.loadDataFromFile("my_budget.csv", repo);

    // 3. Κάνουμε δουλειά με το "Έξυπνο Σύστημα" (Ομάδα Α)
    // Βρίσκουμε μια εγγραφή
    BudgetChangesEntry food = repo.findByCode("FOOD-01").orElseThrow();
    
    // Κάνουμε μια αλλαγή
    BudgetChange increase = new AbsoluteAmountChange(food.getCode(), new BigDecimal("50.0"), "Bonus", "User1");
    increase.apply(food); // Το ποσό άλλαξε στη μνήμη

    // 4. Αποθήκευση πίσω στο αρχείο (Η Γέφυρα δουλεύει πάλι)
    // Θα πάρει τα νέα δεδομένα από το Repo και θα τα γράψει στο CSV
    BudgetIOHandler.saveDataToFile("my_budget.csv", repo);
    }
}
