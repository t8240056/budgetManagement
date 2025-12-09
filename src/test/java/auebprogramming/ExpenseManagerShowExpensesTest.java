package auebprogramming;

import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExpenseManagerShowExpensesTest {

    @Test
    public void testShowExpenses() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        ExpenseManager.showExpenses();

        String printed = out.toString();

        Assert.assertTrue(printed.contains("1. ΕΞΟΔΑ"));
        Assert.assertTrue(printed.contains("21.  Παροχές σε εργαζομένους"));
        Assert.assertTrue(printed.contains("23.  Μεταβιβάσεις"));

        // Validate a number formatting exists
        Assert.assertTrue(printed.contains("14,889,199,000"));
        Assert.assertTrue(printed.contains("34,741,365,000"));
        Assert.assertTrue(printed.contains("1,203,165,130,000"));

        // Check for Σύνολο
        Assert.assertTrue(printed.contains("Σύνολο:"));

        System.setOut(System.out);
    }
}
