package auebprogramming;

import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExpenseManagerShowExpenseDetailsTest {

    @Test
    public void testValidExpenseDetails() {
        ExpenseManager manager = new ExpenseManager();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        manager.showExpenseDetails("23"); // Μεταβιβάσεις

        String printed = out.toString();

        Assert.assertTrue(printed.contains("CODE: 23"));
        Assert.assertTrue(printed.contains("NAME: Μεταβιβάσεις"));
        Assert.assertTrue(printed.contains("State Budget       : 34,741,365,000"));
        Assert.assertTrue(printed.contains("Regular Budget     : 34,741,365,000"));
        Assert.assertTrue(printed.contains("Investment Budget  : 0"));

        System.setOut(System.out);
    }

    @Test
    public void testInvalidExpenseCode() {
        ExpenseManager manager = new ExpenseManager();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        manager.showExpenseDetails("999");

        String printed = out.toString();

        Assert.assertTrue(printed.contains("999 : Invalid code"));

        System.setOut(System.out);
    }
}
