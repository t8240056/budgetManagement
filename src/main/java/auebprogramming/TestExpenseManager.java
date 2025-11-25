package auebprogramming;

public class TestExpenseManager {

    public static void main(String[] args) {
        ExpenseManager manager = new ExpenseManager();

        System.out.println("=== ALL CATEGORIES ===");
        manager.showCategories();

        System.out.println("\n=== TEST EXPENSE DETAILS ===");
        manager.showExpenseDetails("21", "23", "999"); // 999 = invalid example
    }
}
