package auebprogramming;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class GetPdf {

    public void fileDownloader(String urlPdf) {
        String fileUrl = "https://minfin.gov.gr/wp-content/uploads/2024/11/Κρατικός-Προϋπολογισμός-2025_ΟΕ.pdf";
        String destinationFile = "budget2025.pdf";

        try {
            URL url = new URL(fileUrl);
            try (InputStream in = url.openStream()) {
                Files.copy(in, Paths.get(destinationFile), StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("File downloaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}