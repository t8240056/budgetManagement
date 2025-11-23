package auebprogramming;


/**entry point of the application */
public class App {
    public static void main(final String[] args) {

        //making objects to work with
        Links link = new Links();
        GetPdf download = new GetPdf();
        //
        
        for (int year = 2020 ; year <= 2026 ; year++ ) {

            //getting the general link and downloading the pdf
            String urlBudget =link.linksForBudget(year); 
            download.fileDownloader(urlBudget , year);

            ReadPDF read = new ReadPDF();
            read.getInFile(year);
        }
    }
}
