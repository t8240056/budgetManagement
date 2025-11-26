package auebprogramming;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExpenseManagerTest {

    private ExpenseManager manager;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        manager = new ExpenseManager();

        // Capture console output
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testShowCategoriesPrintsAll() {
        ExpenseManager.showCategories();

        String output = outContent.toString();

        // At least test that 3 known categories appear
        assertTrue(output.contains("21"), "Should contain category 21");
        assertTrue(output.contains("Παροχές σε εργαζομένους"), "Should contain correct label");
        assertTrue(output.contains("44"), "Should contain category 44");
        assertTrue(output.contains("Δάνεια"), "Should contain category name");
    }

    @Test
    void testShowExpenseDetailsValidCode() {
        manager.showExpenseDetails("21");

        String output = outContent.toString();

        assertTrue(output.contains("21"), "Should display code 21");
        assertTrue(output.contains("Παροχές σε εργαζομένους"), "Should display the name");
        assertTrue(output.contains("14,889,199,000") || output.contains("14889199000"),
                "Should display the correct State Budget amount");
    }

    @Test
    void testShowExpenseDetailsMultipleCodes() {
        manager.showExpenseDetails("21", "23");

        String output = outContent.toString();

        assertTrue(output.contains("21"), "Should include first code");
        assertTrue(output.contains("23"), "Should include second code");
    }

    @Test
    void testShowExpenseDetailsInvalidCode() {
        manager.showExpenseDetails("999");

        String output = outContent.toString();

        assertTrue(output.contains("Invalid code"),
                "Should show invalid code message");
    }
}
