package auebprogramming;

import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExpenseManagerShowCategoriesTest {

    @Test
    public void testShowCategories() {
        // Capture printed output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        ExpenseManager.showCategories();

        String printed = out.toString();

        // Basic checks
        Assert.assertTrue(printed.contains("CODE\tEXPENSE NAME"));
        Assert.assertTrue(printed.contains("21\tΠαροχές σε εργαζομένους"));
        Assert.assertTrue(printed.contains("23\tΜεταβιβάσεις"));
        Assert.assertTrue(printed.contains("53\tΧρεωστικοί τίτιλοι (υποχρεώσεις)"));

        // ensure number of lines = header + 14 categories
        int lines = printed.split("\n").length;
        Assert.assertEquals(1 + 14, lines);

        System.setOut(System.out);
    }
}
