package auebProgramming2;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Links {

    // it gets the links for each year's budget
    public void linkGovBudget() throws Exception {
        String url = "https://minfin.gov.gr/kratikos-proypologismos/";
        Document doc = Jsoup.connect(url).get();

        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String href = link.attr("abs:href");
            if (href.contains("kratikos-proypologismos-")) {
                System.out.println(href);
            }
        }
    } 

    // Source - https://stackoverflow.com/q
    // Posted by Dahlin, modified by community. See post 'Timeline' for change history
    // Retrieved 2025-11-08, License - CC BY-SA 4.0

    public final ArrayList<String> extractLines(URL pdfUrl) throws IOException {
        try (InputStream inputStream = pdfUrl.openStream();
            PDDocument doc = PDDocument.load(inputStream)) {

            PDFTextStripper strip = new PDFTextStripper();
            String txt = strip.getText(doc);
            String[] arr = txt.split("\n");
            final ArrayList<String> lines = new ArrayList<>(Arrays.asList(arr));
            return lines;
        }
    }
}
