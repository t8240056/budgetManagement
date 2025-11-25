package auebprogramming;

import java.util.Scanner;

/**entry point of the application */
public class App {

    public static void main(final String[] args) {
        // DownloadingOutputFiles.run();  run only once 
        
        Scanner scanner = new Scanner(System.in);
                
        System.out.println(" Which year do you want to see ?");
        int year = scanner.nextInt();
       
        
        scanner.close();

        Article_1new.printArticle1New(year);
        
    }
}
