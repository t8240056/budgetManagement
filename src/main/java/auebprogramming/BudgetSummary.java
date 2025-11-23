package auebprogramming;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class BudgetSummary {
    
    public static void main(String[] args) {
        String filePath = "output.txt";
        BudgetData budgetData = parseBudgetData(filePath);
        printBudgetAnalysis(budgetData);
    }
    
    // Parse budget data from text file
    public static BudgetData parseBudgetData(String filePath) {
        BudgetData data = new BudgetData();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            extractRevenueData(content, data);
            extractExpenseData(content, data);
            calculateDeficit(data);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return data;
    }
    
    // Extract revenue categories and amounts
    public static void extractRevenueData(String content, BudgetData data) {
        // Find revenue section using Greek keywords
        Pattern revenuePattern = Pattern.compile("1\\.\\s*ΕΣΟΔΑ\\s*Ευρώ\\s*([\\d.,]+)(.*?)2\\.\\s*ΕΞΟΔΑ", Pattern.DOTALL);
        Matcher revenueMatcher = revenuePattern.matcher(content);
        
        if (revenueMatcher.find()) {
            data.totalRevenue = parseAmount(revenueMatcher.group(1));
            String revenueDetails = revenueMatcher.group(2);
            
            // Extract individual revenue categories
            extractRevenueCategories(revenueDetails, data);
        }
    }
    
    // Extract expense categories and amounts
    public static void extractExpenseData(String content, BudgetData data) {
        // Find expense section using Greek keywords
        Pattern expensePattern = Pattern.compile("2\\.\\s*ΕΞΟΔΑ\\s*»\\s*([\\d.,]+)(.*?)3\\.\\s*ΑΠΟΤΕΛΕΣΜΑ", Pattern.DOTALL);
        Matcher expenseMatcher = expensePattern.matcher(content);
        
        if (expenseMatcher.find()) {
            data.totalExpenses = parseAmount(expenseMatcher.group(1));
            String expenseDetails = expenseMatcher.group(2);
            
            // Extract individual expense categories
            extractExpenseCategories(expenseDetails, data);
        }
    }
    
    // Parse individual revenue categories
    public static void extractRevenueCategories(String revenueText, BudgetData data) {
        // Pattern for revenue lines: number, Greek description, amount
        Pattern categoryPattern = Pattern.compile("(\\d+\\.?\\d*)\\s*([^»]+)»\\s*([\\d.,]+)");
        Matcher matcher = categoryPattern.matcher(revenueText);
        
        while (matcher.find()) {
            String categoryCode = matcher.group(1).trim();
            String categoryName = matcher.group(2).trim();
            double amount = parseAmount(matcher.group(3));
            
            data.revenueCategories.put(categoryName, amount);
        }
    }
    
    // Parse individual expense categories
    public static void extractExpenseCategories(String expenseText, BudgetData data) {
        // Pattern for expense lines: number, Greek description, amount
        Pattern categoryPattern = Pattern.compile("(\\d+\\.?\\d*)\\s*([^»]+)»\\s*([\\d.,]+)");
        Matcher matcher = categoryPattern.matcher(expenseText);
        
        while (matcher.find()) {
            String categoryCode = matcher.group(1).trim();
            String categoryName = matcher.group(2).trim();
            double amount = parseAmount(matcher.group(3));
            
            data.expenseCategories.put(categoryName, amount);
        }
    }
    
    // Calculate budget deficit/surplus
    public static void calculateDeficit(BudgetData data) {
        data.deficit = data.totalRevenue - data.totalExpenses;
    }
    
    // Parse amount string to double (handle dots as thousand separators)
    public static double parseAmount(String amountStr) {
        try {
            // Remove dots (thousand separators) and replace comma with dot for decimal
            String cleaned = amountStr.replace(".", "").replace(",", ".");
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing amount: " + amountStr);
            return 0.0;
        }
    }
    
    // Format amount for display
    public static String formatAmount(double amount) {
        return String.format("%,.0f", amount);
    }
    
    // Print budget analysis results
    public static void printBudgetAnalysis(BudgetData data) {
        System.out.println("=== BUDGET ANALYSIS ===");
        System.out.println("Total Revenue: " + formatAmount(data.totalRevenue) + " €");
        System.out.println("Total Expenses: " + formatAmount(data.totalExpenses) + " €");
        System.out.println("Budget Result: " + formatAmount(data.deficit) + " €");
        System.out.println();
        
        System.out.println("=== REVENUE CATEGORIES ===");
        for (Map.Entry<String, Double> entry : data.revenueCategories.entrySet()) {
            System.out.println(entry.getKey() + ": " + formatAmount(entry.getValue()) + " €");
        }
        System.out.println();
        
        System.out.println("=== EXPENSE CATEGORIES ===");
        for (Map.Entry<String, Double> entry : data.expenseCategories.entrySet()) {
            System.out.println(entry.getKey() + ": " + formatAmount(entry.getValue()) + " €");
        }
    }
    
    // Data class to store budget information
    static class BudgetData {
        double totalRevenue;
        double totalExpenses;
        double deficit;
        Map<String, Double> revenueCategories = new LinkedHashMap<>();
        Map<String, Double> expenseCategories = new LinkedHashMap<>();
    }
}