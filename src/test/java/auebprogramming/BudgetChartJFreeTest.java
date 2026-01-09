package auebprogramming;

import org.jfree.chart.axis.LogAxis;
import org.jfree.data.category.CategoryDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class BudgetChartJFreeFullTest {

    private Object[][] validData;

    @BeforeEach
    void setup() {
        validData = new Object[][]{
            {"01", "Υγεία", 8_500_000_000.0},
            {"02", "Παιδεία", 4_200_000_000.0},
            {"03", "Άμυνα", 1_000_000_000_000.0}, // 1 τρις
            {"04", "Πολιτισμός", 250_000_000.0}, // πολύ μικρό
            {"05", "Τουρισμός", 15_000_000.0}    // πολύ μικρό
        };
    }

    // ================== CONSTRUCTOR ==================

    @Test
    void constructorWithValidDataCreatesChart() {
        BudgetChartJFree chart = new BudgetChartJFree(validData);
        assertNotNull(chart);
        assertNotNull(chart.getChart());
        assertNotNull(chart.getChartPanel());
        assertEquals(validData.length, chart.getCategoryCount());
    }

    @Test
    void constructorWithNullDataThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new BudgetChartJFree(null));
    }

    @Test
    void constructorWithEmptyDataThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new BudgetChartJFree(new Object[][]{}));
    }

    @Test
    void constructorWithInvalidRowThrowsException() {
        Object[][] invalid1 = {{"01", "Λάθος ποσό", "abc"}};
        Object[][] invalid2 = {{"01", "Λάθος"}};

        assertThrows(IllegalArgumentException.class, () -> new BudgetChartJFree(invalid1));
        assertThrows(IllegalArgumentException.class, () -> new BudgetChartJFree(invalid2));
    }

    // ================== DATASET ==================

    @Test
    void datasetContainsCorrectNumberOfCategories() {
        BudgetChartJFree chart = new BudgetChartJFree(validData);
        assertEquals(5, chart.getCategoryCount());

        CategoryDataset dataset = chart.getChart().getCategoryPlot().getDataset();
        assertEquals(5, dataset.getColumnCount());
    }

    @Test
    void datasetValuesRespectMinimum() {
        BudgetChartJFree chart = new BudgetChartJFree(validData);
        CategoryDataset dataset = chart.getChart().getCategoryPlot().getDataset();

        for (int r = 0; r < dataset.getRowCount(); r++) {
            for (int c = 0; c < dataset.getColumnCount(); c++) {
                assertTrue(dataset.getValue(r, c).doubleValue() >= 1_000_000, "Dataset value >= 1_000_000");
            }
        }
    }

    // ================== TOTAL BUDGET ==================

    @Test
    void totalBudgetIsCalculatedCorrectly() {
        BudgetChartJFree chart = new BudgetChartJFree(validData);

        double expected =
            8_500_000_000.0 +
            4_200_000_000.0 +
            1_000_000_000_000.0 +
            250_000_000.0 +
            15_000_000.0;

        assertEquals(expected, chart.getTotalAmount(), 0.001);
    }

    // ================== UPDATE DATA ==================

    @Test
    void updateDataReplacesDatasetCorrectly() {
        BudgetChartJFree chart = new BudgetChartJFree(validData);

        Object[][] newData = {
            {"10", "Ψηφιακή Διακυβέρνηση", 3_000_000_000.0},
            {"11", "Κοινωνική Πολιτική", 6_000_000_000.0}
        };

        chart.updateData(newData);

        assertEquals(2, chart.getCategoryCount());
        assertEquals(9_000_000_000.0, chart.getTotalAmount(), 0.001);
    }

    // ================== TITLES ==================

    @Test
    void setChartTitleChangesMainTitle() {
        BudgetChartJFree chart = new BudgetChartJFree(validData);
        chart.setChartTitle("Προϋπολογισμός 2025");
        assertEquals("Προϋπολογισμός 2025", chart.getChart().getTitle().getText());
    }

    @Test
    void setChartSubtitleAddsSubtitle() {
        BudgetChartJFree chart = new BudgetChartJFree(validData);
        chart.setChartSubtitle("Πηγή: ΥΠΟΙΚ");
        assertEquals(1, chart.getChart().getSubtitleCount());
    }

    // ================== VISUAL SETTINGS ==================

    @Test
    void chartHasLightBackground() {
        BudgetChartJFree chart = new BudgetChartJFree(validData);
        Paint bg = chart.getChart().getCategoryPlot().getBackgroundPaint();
        assertEquals(new Color(250, 250, 250), bg);
    }

    @Test
    void chartUsesLogarithmicAxis() {
        BudgetChartJFree chart = new BudgetChartJFree(validData);
        assertTrue(chart.getChart().getCategoryPlot().getRangeAxis() instanceof LogAxis);
    }

    @Test
    void logAxisHasCorrectBaseAndMinimum() {
        BudgetChartJFree chart = new BudgetChartJFree(validData);
        LogAxis axis = (LogAxis) chart.getChart().getCategoryPlot().getRangeAxis();
        assertEquals(10, axis.getBase());
        assertEquals(1_000_000, axis.getSmallestValue());
    }

    // ================== EXTREME VALUES ==================

    @Test
    void handlesVerySmallAndVeryLargeValues() {
        Object[][] extremeData = {
            {"A", "Πολύ Μικρό", 1.0},
            {"B", "Τεράστιο", 9_000_000_000_000.0}
        };

        BudgetChartJFree chart = new BudgetChartJFree(extremeData);
        CategoryDataset dataset = chart.getChart().getCategoryPlot().getDataset();

        assertEquals(2, dataset.getColumnCount());

        for (int r = 0; r < dataset.getRowCount(); r++) {
            for (int c = 0; c < dataset.getColumnCount(); c++) {
                assertTrue(dataset.getValue(r, c).doubleValue() >= 1_000_000);
            }
        }
    }

    // ================== CURRENT DATA ==================

    @Test
    void getCurrentDataReturnsOriginalData() {
        BudgetChartJFree chart = new BudgetChartJFree(validData);
        Object[][] currentData = chart.getCurrentData();
        assertArrayEquals(validData, currentData);
    }
}