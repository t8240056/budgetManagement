package auebprogramming;

import org.junit.Test;
import org.junit.Assert;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExpenseDisplayMinistriesTest {

    @Test
    public void testReadMinistriesCSV() throws Exception {
        // Create temporary CSV file
        Path tempFile = Files.createTempFile("ministries_test", ".csv");

        try (FileWriter fw = new FileWriter(tempFile.toFile())) {
            fw.write("code,ministry,regular,investment,total\n");
            fw.write("10, Υπουργείο Οικονομικών, 200000000, 50000000, 250000000\n");
            fw.write("20, Υπουργείο Άμυνας, 300000000, 100000000, 400000000\n");
        }

        ExpenseDisplay display = new ExpenseDisplay();
        List<ExpenseDisplay.MinistryExpense> result =
                display.readMinistriesCSV(tempFile.toString());

        Assert.assertEquals(2, result.size());

        ExpenseDisplay.MinistryExpense m1 = result.get(0);
        Assert.assertEquals("10", m1.code);
        Assert.assertEquals("Υπουργείο Οικονομικών", m1.ministry);
        Assert.assertEquals(200000000L, m1.regularBudget);
        Assert.assertEquals(50000000L, m1.investmentBudget);
        Assert.assertEquals(250000000L, m1.total);

        ExpenseDisplay.MinistryExpense m2 = result.get(1);
        Assert.assertEquals("20", m2.code);
        Assert.assertEquals("Υπουργείο Άμυνας", m2.ministry);
        Assert.assertEquals(300000000L, m2.regularBudget);
        Assert.assertEquals(100000000L, m2.investmentBudget);
        Assert.assertEquals(400000000L, m2.total);

        Files.deleteIfExists(tempFile);
    }
}
