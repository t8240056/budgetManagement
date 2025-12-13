package auebprogramming;

import java.util.Scanner;

public class Main1 {
    public static void main(String[] args) {
        int chooseBudgetType; //0 for revenue, 1 for expense
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please choose budget type (0 for revenue, 1 for expense): ");
        chooseBudgetType = scanner.nextInt();

        if (chooseBudgetType == 0) { //Revenue
            //Display revenue categories
            
        } else if (chooseBudgetType == 1) { //Expense
            //Display expense categories
        } else {
            System.out.println("Invalid choice. Please enter 0 or 1.");
        }
    }
}
