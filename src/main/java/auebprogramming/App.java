package auebprogramming;

import java.net.URI;
import java.net.URL;


/**entry point of the application */
public class App {
    public static void main(final String[] args) throws Exception {
        var link = new Links();
        link.linkGovBudget();

        String pdfUrl = "https://minfin.gov.gr/wp-content/uploads/2024/11/%CE%9A%CF%81%CE%B1%CF%84%CE%B9%CE%BA%CF%8C%CF%82-%CE%A0%CF%81%CE%BF%CF%8B%CF%80%CE%BF%CE%BB%CE%BF%CE%B3%CE%B9%CF%83%CE%BC%CF%8C%CF%82-2025_%CE%9F%CE%95.pdf";
        URI uri = URI.create(pdfUrl);
        URL url = uri.toURL();
        
        BudgetPdfProcessor downloader = new BudgetPdfProcessor();
        BudgetPdfProcessor.downloadBudgetPdf();
        // ArrayList<String> lines = link.extractLines(url);       
        // ConnectToSql table = new ConnectToSql();
        //table.getTableElements(pdfUrl);

    }

}
