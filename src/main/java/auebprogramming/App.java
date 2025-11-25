package auebprogramming;

import java.util.Scanner;

/**entry point of the application */
public class App {

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
}
