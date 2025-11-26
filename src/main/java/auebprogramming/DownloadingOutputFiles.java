package auebprogramming;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class DownloadingOutputFiles{
    public static void run(){

        //making objects to work with
        GetPdf download = new GetPdf();
        ReadPDF read = new ReadPDF();
        //

        // in order to avoid seeing warning for trying to load ariel font and have a clearer output
        Logger.getLogger("org.apache.pdfbox").setLevel(Level.SEVERE);
        //
        int startYear = 2020;
        int endYear = 2026;

        for (int year = startYear ; year <= endYear ; year++ ) {
            download.fileDownloader(year);
            read.outpulFile(year);
        }
    }
}
