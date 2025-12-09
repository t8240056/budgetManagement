package auebprogramming;

import org.junit.Test;
import org.junit.Assert;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExpenseDisplayCategoriesTest {

    @Test
    public void testReadCategoriesCSV() throws Exception {
        // Create temporary CSV file
        Path tempFile = Files.createTempFile("categories_test", ".csv");

        try (FileWriter fw = new FileWriter(tempFile.toFile())) {
            fw.write("index,code,description,amount\n");
            fw.write("1, 23, Μεταβιβάσεις, 34741365000\n");
            fw.write("2, 27, Λοιπές δαπάνες, 101553000\n");
        }

        ExpenseDisplay display = new ExpenseDisplay();
        List<ExpenseDisplay.ExpenseCategory> result =
                display.readCategoriesCSV(tempFile.toString());

        Assert.assertEquals(2, result.size());

        ExpenseDisplay.ExpenseCategory c1 = result.get(0);
        Assert.assertEquals("23", c1.code);
        Assert.assertEquals("Μεταβιβάσεις", c1.description);
        Assert.assertEquals(34741365000L, c1.amount);

        ExpenseDisplay.ExpenseCategory c2 = result.get(1);
        Assert.assertEquals("27", c2.code);
        Assert.assertEquals("Λοιπές δαπάνες", c2.description);
        Assert.assertEquals(101553000L, c2.amount);

        Files.deleteIfExists(tempFile);
    }
}
