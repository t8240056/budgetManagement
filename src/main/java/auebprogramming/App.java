package auebprogramming;

import java.util.Scanner;

/**entry point of the application */
public class App {
    public static void main(final String[] args) throws Exception {

        //making objects to work with 
        Scanner input = new Scanner(System.in);
        Links link = new Links();
        GetPdf download = new GetPdf();
        //
        
        //getting the year from the user
        System.out.println("Which year's government budget do you want ? Choose from 2020 until 2025");
        int year = input.nextInt(); 
        
        //getting the general link and downloading the pdf
        String urlBudget =link.linksForBudget(year); 
        download.fileDownloader(urlBudget , year);

        ReadPDF read = new ReadPDF();
        read.getInFile(year);

        input.close();
    }

}