package auebprogramming;

import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;

/**entry point of the application */
public class App {
    /* 
    public static void main(final String[] args) {
        // DownloadingOutputFiles.run();  run only once 
        
        Scanner scanner = new Scanner(System.in);
       
        
        System.out.println(" Which year do you want to see ?");
        int year = scanner.nextInt();
      

        PrintExpensesArticle2.printbudget();

        scanner.close();

        //RevenueExtractor re = new RevenueExtractor();
        //re.printRevenues();
        //RevenueManager.showRevenues();
        Article_1new.printArticle1New(year);
        
    }
    */



    public static void main(String[] args) {
        // Ονόματα αρχείων
        String inputFile = "src/main/resources/output2025.csv";
        String outputFile = "src/main/resources/filtered_output_up_to_4_digits.csv";

        // ΝΕΟΣ κανονικός τύπος (Regex) για γραμμές που ξεκινούν με 1 έως 4 ψηφία
        // ^\d{1,4},.* σημαίνει:
        // ^        -> στην αρχή της γραμμής
        // \d{1,4}  -> 1, 2, 3, ή 4 ψηφία
        // ,        -> ακολουθούμενα από το κόμμα-οριοθέτη
        // .* -> και οτιδήποτε άλλο ακολουθεί
        String regexPattern = "^\\d{1,4},.*";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            int keptCount = 0;
            int removedCount = 0;
            
            // 1. Επεξεργασία του Header (πρώτη γραμμή). Πάντα το κρατάμε.
            if ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            // 2. Επεξεργασία των υπόλοιπων γραμμών
            while ((line = reader.readLine()) != null) {
                
                // Ελέγχει αν η γραμμή ΤΑΙΡΙΑΖΕΙ με τον κανονικό τύπο (1-4 ψηφία)
                if (line.matches(regexPattern)) {
                    // Η γραμμή ΤΑΙΡΙΑΖΕΙ με το μοτίβο (ΠΡΕΠΕΙ ΝΑ ΚΡΑΤΗΘΕΙ)
                    writer.write(line);
                    writer.newLine();
                    keptCount++;
                } else {
                    // Η γραμμή ΔΕΝ ταιριάζει (ΠΡΕΠΕΙ ΝΑ ΑΦΑΙΡΕΘΕΙ)
                    removedCount++;
                }
            }

            System.out.println("✅ Το φιλτράρισμα ολοκληρώθηκε.");
            System.out.println("---");
            System.out.println("Αρχείο εισόδου: " + inputFile);
            System.out.println("Αρχείο εξόδου: " + outputFile);
            System.out.println("Γραμμές που κρατήθηκαν: " + keptCount);
            System.out.println("Γραμμές που αφαιρέθηκαν: " + removedCount);
            

        } catch (IOException e) {
            System.err.println("Προέκυψε σφάλμα κατά την επεξεργασία του αρχείου: " + e.getMessage());
        }
    }

}
