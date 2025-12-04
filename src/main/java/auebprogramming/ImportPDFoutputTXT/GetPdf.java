package auebprogramming.ImportPDFoutputTXT;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class GetPdf {

    public void fileDownloader(final String urlBudget, final int year) {
        String fileUrl = urlBudget;
        String destinationFile = "src/main/java/auebprogramming/resources/budget" + year + ".pdf";

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