package auebprogramming;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public final class ReadPDF {
    /*
        a method that gets the downloaded file and converts it to a txt file
    */
    public void outpulFile(final int year) {
        try {
            // path to get the PDF file
            File file = new File("src/main/resources/budget" + year + ".pdf");

            // loading the file
            PDDocument document = PDDocument.load(file);

            // checks if the doc is encrypted
            if (!document.isEncrypted()) {

                PDFTextStripper pdfStripper = new PDFTextStripper();
                String text = pdfStripper.getText(document);

                FileWriter writer = new FileWriter("src/main/resources/output" + year + ".csv");
                writer.write(text);
                writer.close();

                System.out.println("file saved as output" + year + ".txt");
            }
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
