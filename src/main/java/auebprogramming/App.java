package auebprogramming;

import java.util.Scanner;

/**entry point of the application */
public class App {
    public static void main(final String[] args) throws Exception {
        //ReadPDF read = new ReadPDF();
        //read.getInFile();

        //making objects to work with 
        Scanner input = new Scanner(System.in);
        Links link = new Links();
        GetPdf download = new GetPdf();
        //

        //getting the year from the user
        System.out.println("Which year's government budget do you want ?");
        int year = input.nextInt(); 
        
        //getting the general link and downloading the pdf
        String urlPdf =link.linkGovBudget(year);
        download.fileDownloader(urlPdf);
        input.close();
    }

}