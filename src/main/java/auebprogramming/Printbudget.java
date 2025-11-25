package auebprogramming;

public class Printbudget{
    public static void printbudget() {
        final String FILENAME = "budget_ministries.csv";
        String [][] budgetData = CVSmanagement.loadCsvToArray(FILENAME);
        for (int i = 0; i < budgetData.length; i++) { // κάθε γραμμή
            for (int j = 0; j < budgetData[i].length; j++) { // κάθε στήλη
            System.out.print(budgetData[i][j] + "\t"); // το \t προσθέτει κενό (tab)
            }
        System.out.println(); // νέα γραμμή μετά από κάθε σειρά
    }
    }
}