package auebprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for the BudgetIOHandler class.
 * This class verifies reading and writing operations to CSV files.
 * It uses a temporary directory to avoid creating persistent files.
 */
class BudgetIOHandlerTest {

    /**
     * Creates a temporary directory for file operations during tests.
     * JUnit 5 handles the cleanup automatically.
     */
    @TempDir
    Path tempDir;

    /**
     * Tests the complete cycle of saving data to a file and loading it back.
     * Verifies that data integrity is maintained.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void testSaveAndLoadDataSuccess() throws IOException {
        // 1. Setup paths and data
        final File testFile = tempDir.resolve("budget_test.csv").toFile();
        final String filePath = testFile.getAbsolutePath();

        final BudgetRepository saveRepo = new BudgetRepository();
        final BudgetChangesEntry entry1 = new BudgetChangesEntry("C1",
                "Stationery", new BigDecimal("50.00"));
        final BudgetChangesEntry entry2 = new BudgetChangesEntry("C2",
                "Office Chairs", new BigDecimal("120.50"));

        saveRepo.save(entry1);
        saveRepo.save(entry2);

        // 2. Execute Save
        BudgetIOHandler.saveDataToFile(filePath, saveRepo);
        assertTrue(testFile.exists(), "The CSV file should be created");

        // 3. Execute Load into a fresh repository
        final BudgetRepository loadRepo = new BudgetRepository();
        BudgetIOHandler.loadDataFromFile(filePath, loadRepo);

        // 4. Verify Results
        assertEquals(2, loadRepo.count(), "Should load exactly 2 entries");

        final BudgetChangesEntry loadedEntry1 = loadRepo.findByCode("C1")
                .orElseThrow();
        assertEquals("Stationery", loadedEntry1.getDescription(),
                "Description should match");
        assertEquals(new BigDecimal("50.00"), loadedEntry1.getAmount(),
                "Amount should match");
    }

    /**
     * Tests saving an empty repository.
     * Verifies that the file is created and contains only the header row.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void testSaveEmptyRepository() throws IOException {
        final File testFile = tempDir.resolve("empty_test.csv").toFile();
        final String filePath = testFile.getAbsolutePath();
        final BudgetRepository emptyRepo = new BudgetRepository();

        // Execute Save
        BudgetIOHandler.saveDataToFile(filePath, emptyRepo);

        assertTrue(testFile.exists(), "File should exist even if repo is empty");

        // Verify file content (should only have header)
        final List<String> lines = Files.readAllLines(testFile.toPath());
        assertEquals(1, lines.size(), "File should contain exactly 1 line");
        assertEquals("CODE,DESCRIPTION,AMOUNT", lines.get(0).trim(),
                "Header row should be correct");
    }

    /**
     * Tests loading from a file that does not exist.
     * The handler catches IOException internally, so the repository
     * should simply remain empty.
     */
    @Test
    void testLoadFromNonExistentFile() {
        final String invalidPath = tempDir.resolve("ghost.csv")
                .toAbsolutePath().toString();
        final BudgetRepository repo = new BudgetRepository();

        // Execute Load (Should handle IOException internally)
        BudgetIOHandler.loadDataFromFile(invalidPath, repo);

        assertEquals(0, repo.count(),
                "Repository should be empty after failing to load file");
    }

    /**
     * Tests loading a file with an invalid number format.
     * Since BudgetIOHandler does not catch NumberFormatException,
     * we expect the test to throw this exception.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void testLoadInvalidNumberFormat() throws IOException {
        final Path csvPath = tempDir.resolve("bad_number.csv");
        final String content = "CODE,DESCRIPTION,AMOUNT\n"
                + "C1,Desk,NotANumber";
        Files.writeString(csvPath, content);

        final BudgetRepository repo = new BudgetRepository();
        final String absolutePath = csvPath.toAbsolutePath().toString();

        // Expect NumberFormatException because "NotANumber" is invalid
        assertThrows(NumberFormatException.class, () -> {
            BudgetIOHandler.loadDataFromFile(absolutePath, repo);
        }, "Should throw NumberFormatException for invalid amount string");
    }

    /**
     * Tests loading a file with missing columns.
     * Since BudgetIOHandler assumes the split array has length 3,
     * this should throw an ArrayIndexOutOfBoundsException.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void testLoadMissingColumns() throws IOException {
        final Path csvPath = tempDir.resolve("missing_col.csv");
        // Missing amount column
        final String content = "CODE,DESCRIPTION,AMOUNT\nC1,Desk";
        Files.writeString(csvPath, content);

        final BudgetRepository repo = new BudgetRepository();
        final String absolutePath = csvPath.toAbsolutePath().toString();

        // Expect exception because parts[2] will not exist
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            BudgetIOHandler.loadDataFromFile(absolutePath, repo);
        }, "Should throw IndexOutOfBounds for missing CSV columns");
    }

    /**
     * Tests saving and loading with a complex filename.
     * This verifies support for:
     * <ul>
     * <li>Non-ASCII characters (e.g., Greek).</li>
     * <li>Spaces in filenames.</li>
     * <li>Special symbols.</li>
     * </ul>
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void testWeirdFilenames() throws IOException {
        // Scenario: Filename with Greek, spaces, and allowed symbols
        final String strangeName = "δοκιμή αρχείου με κενά #1.csv";
        final File testFile = tempDir.resolve(strangeName).toFile();
        final String filePath = testFile.getAbsolutePath();

        // Setup Data
        final BudgetRepository saveRepo = new BudgetRepository();
        saveRepo.save(new BudgetChangesEntry("W1", "Weird Name Test",
                BigDecimal.TEN));

        // 1. Save to the strange filename
        BudgetIOHandler.saveDataToFile(filePath, saveRepo);
        assertTrue(testFile.exists(), "File with strange name needs creation");

        // 2. Load from the strange filename
        final BudgetRepository loadRepo = new BudgetRepository();
        BudgetIOHandler.loadDataFromFile(filePath, loadRepo);

        // 3. Verify
        assertEquals(1, loadRepo.count(), "Should load data from weird name");
        assertEquals("Weird Name Test",
                loadRepo.findByCode("W1").orElseThrow().getDescription());
    }
}
