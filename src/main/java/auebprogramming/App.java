package auebprogramming;

import java.util.Scanner;

/**entry point of the application */
public class App {
    public static void main(final String[] args) throws Exception {
        //ReadPDF read = new ReadPDF();
        //read.getInFile();
        Scanner input = new Scanner(System.in);
        Links link = new Links();
        GetPdf download = new GetPdf();

        System.out.println("Which year's government budget do you want ?");
        int year = input.nextInt(); 
        


        String urlPdf =link.linkGovBudget(year);
        //download.fileDownloader(urlPdf);
        System.out.println(link.getUrlPdf(year));


        input.close();
    }

}