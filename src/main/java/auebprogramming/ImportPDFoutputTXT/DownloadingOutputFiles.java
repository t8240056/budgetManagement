package auebprogramming.ImportPDFoutputTXT;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class DownloadingOutputFiles{
    public static void run(){
        
        //making objects to work with
        Links link = new Links();
        GetPdf download = new GetPdf();
        //

        // in order to avoid seeing warning for trying to load ariel font and have a clearer output
        Logger.getLogger("org.apache.pdfbox").setLevel(Level.SEVERE);
        //
        int startYear = 2020;
        int endYear = 2026;

        for (int year = startYear ; year <= endYear ; year++ ) {

            //getting the general link and downloading the pdf
            String urlBudget =link.linksForBudget(year); 
            download.fileDownloader(urlBudget , year);

            ReadPDF read = new ReadPDF();
            read.getInFile(year);
        }
    }
}