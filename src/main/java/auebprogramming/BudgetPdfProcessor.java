package auebprogramming;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BudgetPdfProcessor {
    
    public static void downloadBudgetPdf() throws Exception {
        String pdfUrl = "https://minfin.gov.gr/wp-content/uploads/2024/11/Κρατικός-Προυπολογισμός-2025_ΟΕ.pdf";
        String fileName = "κρατικός_προϋπολογισμός_2025.pdf";
        
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
                
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(pdfUrl))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .GET()
                .build();
        
        HttpResponse<Path> response = client.send(
            request, 
            HttpResponse.BodyHandlers.ofFile(Paths.get(fileName))
        );
        
        if (response.statusCode() == 200) {
            System.out.println("PDF downloaded successfully: " + response.body());
        } else {
            throw new RuntimeException("Failed to download PDF. Status: " + response.statusCode());
        }
    }
}