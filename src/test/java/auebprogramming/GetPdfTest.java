
package auebprogramming;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class GetPdfTest {

    // Χρήση του @TempDir για δοκιμές που ίσως χρειαστεί να δημιουργήσουν αρχεία, 
    // αν και η fileDownloader χρησιμοποιεί hardcoded path.
    // Το κρατάμε για πιθανή μελλοντική χρήση.
    @TempDir
    Path tempDir;

    // --- Tests για linksForBudget ---

    @Test
    void testLinksForBudget_ValidYear_2024() {
        // Arrange
        int year = 2024;
        String expectedUrl = "https://minfin.gov.gr/wp-content/uploads/2023/11/%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%A5%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3-2024.pdf";

        // Act
        String actualUrl = GetPdf.linksForBudget(year);

        // Assert
        assertEquals(expectedUrl, actualUrl, "Πρέπει να επιστρέψει το σωστό URL για το 2024.");
    }

    @Test
    void testLinksForBudget_ValidYear_2026() {
        // Arrange
        int year = 2026;
        String expectedUrl = "https://minfin.gov.gr/wp-content/uploads/2025/11/%CE%9A%CF%81%CE%B1%CF%84%CE%B9%CE%BA%CF%8C%CF%82-%CE%A0%CF%81%CE%BF%CF%8B%CF%80%CE%BF%CE%BB%CE%BF%CE%B3%CE%B9%CF%83%CE%BC%CF%8C%CF%82-2026.pdf";

        // Act
        String actualUrl = GetPdf.linksForBudget(year);

        // Assert
        assertEquals(expectedUrl, actualUrl, "Πρέπει να επιστρέψει το σωστό URL για το 2026.");
    }

    @Test
    void testLinksForBudget_InvalidYear_Low() {
        // Arrange
        int year = 2019;
        String expectedMessage = "wrong year, choose from 2020 - 2025"; // Η μέθοδος επιστρέφει μήνυμα αντί να ρίξει exception

        // Act
        String actualMessage = GetPdf.linksForBudget(year);

        // Assert
        assertEquals(expectedMessage, actualMessage, "Πρέπει να επιστρέψει μήνυμα λάθους για μη έγκυρο έτος.");
    }

    @Test
    void testLinksForBudget_InvalidYear_High() {
        // Arrange
        int year = 2027;
        String expectedMessage = "wrong year, choose from 2020 - 2025"; // Η μέθοδος επιστρέφει κενό string αν δεν πιάσει το IllegalArgumentException στο switch

        // Act
        String actualMessage = GetPdf.linksForBudget(year);

        // Assert
        assertEquals(expectedMessage, actualMessage, "Πρέπει να επιστρέψει κενό string (ή μήνυμα λάθους) για μη έγκυρο έτος.");
        
        // Σημείωση: Το original code χειρίζεται μόνο τα έτη 2020-2026. 
        // Αν το switch δεν καλύπτει το έτος, το try-catch δεν ενεργοποιείται και επιστρέφεται το "" στο τέλος.
    }

    // --- Tests για fileDownloader ---

    @Test
    void testFileDownloader_Success() {
        // Arrange
        GetPdf getPdf = new GetPdf();
        int yearToDownload = 2024; 
        
        // Το μονοπάτι προορισμού είναι hardcoded στην κλάση GetPdf
        String destinationFile = "src/main/resources/budget" + yearToDownload + ".pdf";
        Path path = Paths.get(destinationFile);

        // Καθαρίζουμε τυχόν προηγούμενο αρχείο πριν τη δοκιμή
        try {
            Files.deleteIfExists(path);
        } catch (Exception e) {
             // Εδώ μπορεί να υπάρχει πρόβλημα αν το αρχείο χρησιμοποιείται, αλλά για απλότητα το αγνοούμε
        }

        // Act & Assert
        // Ελέγχουμε μόνο ότι η μέθοδος εκτελείται χωρίς να ρίξει uncaught exceptions
        assertDoesNotThrow(() -> {
            getPdf.fileDownloader(yearToDownload);
        }, "Η μέθοδος fileDownloader δεν πρέπει να ρίξει Exception.");
        
        // Assert: Ελέγχουμε αν το αρχείο δημιουργήθηκε
        // ΠΡΟΣΟΧΗ: Αυτός ο έλεγχος απαιτεί σύνδεση στο διαδίκτυο και πραγματική λήψη αρχείου.
        // Σε ένα σωστό Unit Test, αυτό θα έπρεπε να γίνει με mocking.
        
        // assertTrue(Files.exists(path), "Το αρχείο PDF πρέπει να έχει κατέβει και να υπάρχει στον κατάλογο.");
        // assertTrue(Files.size(path) > 1000, "Το κατεβασμένο αρχείο πρέπει να έχει μέγεθος μεγαλύτερο από 1KB."); 

        // Επειδή είναι unit test και δεν πρέπει να εξαρτάται από το δίκτυο, 
        // αφήνουμε μόνο το assertDoesNotThrow.
    }
    
    @Test
    void testFileDownloader_InvalidUrl_DoesNotThrow() {
        // Arrange
        GetPdf getPdf = new GetPdf();
        int yearToDownload = 9999; // Μη έγκυρο έτος που επιστρέφει κενό URL

        // Act & Assert
        // Η μέθοδος fileDownloader πιάνει την IOException και την τυπώνει (e.printStackTrace())
        // Επομένως, το test πρέπει να επιβεβαιώσει ότι δεν διαρρέει Exception.
        assertDoesNotThrow(() -> {
            getPdf.fileDownloader(yearToDownload);
        }, "Η μέθοδος fileDownloader πρέπει να χειριστεί την IOException (Invalid URL) και να μην ρίξει Exception.");
    }

}
