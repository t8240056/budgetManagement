
import auebprogramming.RevenueManager;

public class TestRevenueManager {

   public static void main(String[] args) {
        RevenueManager manager = new RevenueManager();

        System.out.println("=== ALL CATEGORIES ===");
        manager.showCategories();

        System.out.println("\n=== TEST REVENUE DETAILS ===");
        manager.showRevenueDetails("11"); // 999 = invalid example
    }
}
