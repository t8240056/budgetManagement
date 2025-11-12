package auebprogramming;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ReadPDF {
    public void getInFile() {
        try {
            // Διαδρομή προς το αρχείο PDF
            File file = new File("budget2025.pdf");

            // Φόρτωση του PDF εγγράφου
            PDDocument document = PDDocument.load(file);

            // Έλεγχος αν είναι κρυπτογραφημένο
            if (!document.isEncrypted()) {
                // Εξαγωγή κειμένου
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String text = pdfStripper.getText(document);

                // Δημιουργία αρχείου εξόδου
                FileWriter writer = new FileWriter("output.txt");
                writer.write(text);
                writer.close();

                System.out.println("Το κείμενο αποθηκεύτηκε επιτυχώς στο αρχείο output.txt");
            }

            // Κλείσιμο του εγγράφου
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}