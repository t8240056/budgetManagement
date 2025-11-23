package auebprogramming;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MyBudget {
    public static void main(String[] args) {
        String csvFile = "../../resources/budget_ministries.csv";
        String line;
        int lineNumber = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(csvFile), StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                lineNumber++;
                String[] data = line.split(",");

                if (lineNumber >= 9) {
                    String ministryName = data[1].replace("\"", "").trim();
                    String revenueStr = data[2].replace("\"", "").replaceAll("[^0-9]", "").trim();
                    String expenseStr = data[3].replace("\"", "").replaceAll("[^0-9]", "").trim();

                    long revenueNum = 0;
                    long expenseNum = 0;

                    if (!revenueStr.isEmpty()) {
                        revenueNum = Long.parseLong(revenueStr);
                    }
                    if (!expenseStr.isEmpty()) {
                        expenseNum = Long.parseLong(expenseStr);
                    }

                    System.out.println("Ministry: " + ministryName);
                    System.out.println("  Revenue: " + revenueNum);
                    System.out.println("  Expense: " + expenseNum);
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 