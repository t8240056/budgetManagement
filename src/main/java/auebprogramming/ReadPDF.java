package auebprogramming;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public final class ReadPDF {

    /* this is a method that gets the downloaded file and converts it to a txt file */
    public void getInFile(int year) {
        try {
            // path to get the PDF file
            File file = new File("budget" + year + ".pdf");

            // loading the file
            PDDocument document = PDDocument.load(file);

            // checks if the doc is encrypted 
            if (!document.isEncrypted()) {
                
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String text = pdfStripper.getText(document);

           
                FileWriter writer = new FileWriter("../../resources/output" + year + ".txt");
                writer.write(text);
                writer.close();

                System.out.println("Το κείμενο αποθηκεύτηκε επιτυχώς στο αρχείο output.txt");
            }

            
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}