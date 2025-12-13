package auebprogramming;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main1 {
    public static void main(String[] args) {
        int chooseBudgetType; //0 for revenue, 1 for expense
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please choose budget type (0 for revenue, 1 for expense): ");
        chooseBudgetType = scanner.nextInt();

        if (chooseBudgetType == 0) { //Revenue
            //Display revenue categories
            try {
                FileReader reader = new FileReader("src/main/resources/revenue_categories2_2025.csv");
                try {
                    int data = reader.read();
                    while (data != -1) {
                        System.out.print((char)data);
                        data = reader.read();
                    } 
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (chooseBudgetType == 1) { //Expense
            //Display expense categories
        } else {
            System.out.println("Invalid choice. Please enter 0 or 1.");
        }
        System.out.println();
    }
}
