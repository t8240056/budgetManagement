package auebprogramming;

import java.util.Scanner;

/**entry point of the application */
public class App {

    public static void main(final String[] args) {
        // DownloadingOutputFiles.run();  run only once 
        
        Scanner scanner = new Scanner(System.in);
        var article1 = new Article_1();
        
        System.out.println(" Which year do you want to see ?");
        int year = scanner.nextInt();
        article1.getArticle1(year); 

        Printbudget.printbudget();

    }
}
