package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class BudgetChartJFree extends JPanel {

    // GUI Components
    private ChartPanel chartPanel;
    private JFreeChart chart;
    private CategoryDataset dataset;
    private Map<String, Integer> categoryToIndexMap = new HashMap<>();
    private Object[][] budgetData;

    // Constructor that accepts the data array
    public BudgetChartJFree(final Object[][] data) {
        super(new BorderLayout());
        this.budgetData = data;

        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Data array cannot be empty!");
        }

        initializeComponents();
    }

    // Initialize all components
    private void initializeComponents() {
        // Create dataset
        dataset = createDataset();

        // Create chart
        chart = createChart(dataset);

        // Create chart panel
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1200, calculateHeight(budgetData.length)));
        chartPanel.setMouseZoomable(true, false);
        chartPanel.setDisplayToolTips(true);

        // Add custom tooltips and interactivity
        addCustomTooltips(chartPanel, dataset);

        // Add to panel
        add(chartPanel, BorderLayout.CENTER);
    }

    // Method to get the ChartPanel (for adding to another container)
    public final ChartPanel getChartPanel() {
        return chartPanel;
    }

    // Method to get the JFreeChart
    public final JFreeChart getChart() {
        return chart;
    }

    // Method to update data
    public final void updateData(final Object[][] newData) {
        this.budgetData = newData;
        categoryToIndexMap.clear();
        dataset = createDataset();

        // Update chart
        chart.getCategoryPlot().setDataset(dataset);

        // Update tooltips
        addCustomTooltips(chartPanel, dataset);

        // Refresh panel
        revalidate();
        repaint();
    }

    // Method to change title
    public final void setChartTitle(final String title) {
        TextTitle mainTitle = new TextTitle(
                title,
                new Font("Arial", Font.BOLD, 18));
        mainTitle.setPaint(new Color(30, 60, 90));
        chart.setTitle(mainTitle);
    }

    // Method to change subtitle
    public final void setChartSubtitle(final String subtitle) {
        // Remove existing subtitle if exists
        if (chart.getSubtitleCount() > 0) {
            chart.removeSubtitle(chart.getSubtitle(0));
        }

        TextTitle subTitle = new TextTitle(
                subtitle,
                new Font("Arial", Font.ITALIC, 12));
        subTitle.setPaint(new Color(100, 100, 100));
        chart.addSubtitle(subTitle);
    }

    // Method to change size
    public final void setChartSize(final int width, final int height) {
        chartPanel.setPreferredSize(new Dimension(width, height));
        chartPanel.revalidate();
    }

    // Method to get current data
    public final Object[][] getCurrentData() {
        return budgetData;
    }

    // Method to get category count
    public final int getCategoryCount() {
        return budgetData.length;
    }

    // Method to get total amount
    public final double getTotalAmount() {
        return getTotalBudget();
    }

    // ========== PRIVATE METHODS ==========

    private int calculateHeight(final int rowCount) {
        int baseHeight = 500;
        int extraHeightPerRow = 25;
        int maxHeight = 1000;

        int calculatedHeight = baseHeight + (rowCount * extraHeightPerRow);
        return Math.min(calculatedHeight, maxHeight);
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset newDataset = new DefaultCategoryDataset();

        // Clear map
        categoryToIndexMap.clear();

        // Add data from array to dataset
        for (int i = 0; i < budgetData.length; i++) {
            Object[] item = budgetData[i];

            // Verify that array has 3 columns
            if (item.length < 3) {
                throw new IllegalArgumentException(
                        "Each row must have 3 elements: [Code, Category, Amount]");
            }

            String code = item[0].toString();
            String category = item[1].toString();
            double amount;

            // Convert amount to double
            try {
                if (item[2] instanceof Number) {
                    amount = ((Number) item[2]).doubleValue();
                } else {
                    amount = Double.parseDouble(item[2].toString());
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Amount in row " + (i + 1) + " is not a valid number: " + item[2]);
            }

            // Convert to billions for better display
            double chartValue = Math.max(amount, 1_000_000);

            // Shortened name for better display on axis
            String displayName = code + " - " + getShortName(category, 35);

            newDataset.addValue(chartValue, "Budget", displayName);

            // Store mapping for quick lookup
            categoryToIndexMap.put(displayName, i);
        }

        return newDataset;
    }

    private String getShortName(final String fullName, final int maxLength) {
        if (fullName.length() <= maxLength) {
            return fullName;
        }
        return fullName.substring(0, maxLength - 3) + "...";
    }

    private JFreeChart createChart(final CategoryDataset dataSet) {
        // Create horizontal bar chart
        JFreeChart newChart = ChartFactory.createBarChart(
                null, // No title (we'll add custom)
                null, // No label for vertical axis
                "Amount (€ - logarithmic scale)", // Label for horizontal axis
                dataSet,
                PlotOrientation.HORIZONTAL,
                false, // No legend
                true,  // Tooltips
                false  // URLs
        );

        // Customize appearance
        CategoryPlot plot = (CategoryPlot) newChart.getPlot();

        // Customize renderer
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Gradient colors from green to blue
        Color[] colors = createColorGradient(dataSet.getColumnCount());

        // Apply colors
        for (int i = 0; i < dataSet.getColumnCount(); i++) {
            renderer.setSeriesPaint(i, colors[i]);
        }

        // Add values on bars
        DecimalFormat df = new DecimalFormat("#,##0.00");
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator(
                "{2}", df));
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelPaint(Color.DARK_GRAY);
        renderer.setDefaultItemLabelFont(new Font("Arial", Font.BOLD, 11));
        renderer.setDefaultPositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT));

        // Configure bars (automatic adjustment based on row count)
        int rowCount = budgetData.length;
        double itemMargin = Math.max(0.05, Math.min(0.20, 0.20 - (rowCount * 0.005)));
        double maxBarWidth = Math.max(0.03, Math.min(0.08, 0.08 - (rowCount * 0.001)));

        renderer.setItemMargin(itemMargin);
        renderer.setMaximumBarWidth(maxBarWidth);

        // Configure category axis (vertical)
        CategoryAxis domainAxis = plot.getDomainAxis();
        double categoryMargin = Math.max(0.10, Math.min(0.30, 0.30 - (rowCount * 0.01)));
        domainAxis.setCategoryMargin(categoryMargin);

        // Configure font size based on row count
        int fontSize = Math.max(8, 11 - (rowCount / 20));
        domainAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, fontSize));
        domainAxis.setTickLabelPaint(new Color(60, 60, 60));
        domainAxis.setTickLabelInsets(new RectangleInsets(2, 2, 2, 2));

        // Configure value axis (horizontal)
        LogAxis rangeAxis = new LogAxis("Amount (€ - logarithmic scale)");
        rangeAxis.setBase(10);
        rangeAxis.setSmallestValue(1_000_000); // Minimum 1 million
        rangeAxis.setNumberFormatOverride(new DecimalFormat("###,###"));
        rangeAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, 12));
        rangeAxis.setTickLabelPaint(new Color(70, 70, 70));

        plot.setRangeAxis(rangeAxis);

        // Configure background
        plot.setBackgroundPaint(new Color(250, 250, 250));
        plot.setDomainGridlinePaint(new Color(220, 220, 220));
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.setOutlinePaint(new Color(200, 200, 200));

        return newChart;
    }

    private Color[] createColorGradient(final int count) {
        Color[] colors = new Color[count];

        if (count <= 1) {
            colors[0] = new Color(52, 152, 219);
            return colors;
        }

        for (int i = 0; i < count; i++) {
            float ratio = (float) i / (float) count;

            int red = (int) (46 + (52 - 46) * ratio);
            int green = (int) (204 + (152 - 204) * ratio);
            int blue = (int) (113 + (219 - 113) * ratio);

            colors[i] = new Color(red, green, blue);
        }

        return colors;
    }

    private void addCustomTooltips(final ChartPanel panel, final CategoryDataset dataSet) {
        // Custom tooltip generator
        StandardCategoryToolTipGenerator toolTipGenerator = new StandardCategoryToolTipGenerator() {
            @Override
            public String generateToolTip(final CategoryDataset dataSet, final int row, final int column) {
                String category = (String) dataSet.getColumnKey(column);
                String[] parts = category.split(" - ", 2);
                String code = parts.length > 0 ? parts[0].trim() : "";
                String categoryName = parts.length > 1 ? parts[1].trim() : "";

                String fullCategoryName = findFullCategoryName(code);

                Object[] itemData = findItemData(code);

                double originalValue = 0.0;
                if (itemData != null) {
                    if (itemData[2] instanceof Number) {
                        originalValue = ((Number) itemData[2]).doubleValue();
                    } else {
                        try {
                            originalValue = Double.parseDouble(itemData[2].toString());
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }

                double valueInBillions = originalValue / 1_000_000_000.0;

                String formattedBillions = String.format("%,.2f", valueInBillions);
                String formattedOriginal = String.format("%,.0f", originalValue);

                return String.format(
                        "<html>"
                                + "<div style='padding:8px;font-family:Arial;"
                                + "background:#f9f9f9;border:1px solid #ccc;'>"
                                + "<div style='color:#2C3E50;font-weight:bold;"
                                + "font-size:13px;margin-bottom:8px;'>"
                                + "Code: <span style='color:#2980B9;'>%s</span>"
                                + "</div>"
                                + "<div style='margin-bottom:6px;'>"
                                + "<b>Category:</b><br>%s</div>"
                                + "<hr style='border:none;border-top:1px dashed #ccc;"
                                + "margin:8px 0;'>"
                                + "<table style='width:100%%;border-collapse:collapse;'>"
                                + "<tr><td style='padding:4px;'>"
                                + "<b>Amount:</b></td>"
                                + "<td style='padding:4px;color:#27AE60;"
                                + "font-weight:bold;'>%s billion €</td></tr>"
                                + "<tr><td style='padding:4px;'>"
                                + "<b>Full Amount:</b></td>"
                                + "<td style='padding:4px;color:#2C3E50;'>"
                                + "%s €</td></tr>"
                                + "</table>"
                                + "<div style='margin-top:8px;font-size:11px;"
                                + "color:#7F8C8D;font-style:italic;'>"
                                + "Click for more details"
                                + "</div>"
                                + "</div></html>",
                        code,
                        fullCategoryName != null ? fullCategoryName : categoryName,
                        formattedBillions,
                        formattedOriginal);

            }
        };

        BarRenderer renderer = (BarRenderer) ((CategoryPlot) panel.getChart().getPlot()).getRenderer();
        renderer.setDefaultToolTipGenerator(toolTipGenerator);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                ChartEntity entity = panel.getEntityForPoint(e.getX(), e.getY());
                if (entity instanceof CategoryItemEntity) {
                    CategoryItemEntity itemEntity = (CategoryItemEntity) entity;
                    Comparable<?> columnKey = itemEntity.getColumnKey();

                    if (columnKey instanceof String) {
                        String category = (String) columnKey;
                        String[] parts = category.split(" - ", 2);
                        String code = parts.length > 0 ? parts[0].trim() : "";

                        Object[] itemData = findItemData(code);
                        if (itemData != null) {
                            showDetailsDialog(itemData);
                        }
                    }
                }
            }
        });
    }

    private int findColumnIndex(final CategoryDataset dataSet, final String category) {
        int columnCount = dataSet.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            Comparable<?> key = dataSet.getColumnKey(i);
            if (key instanceof String && key.equals(category)) {
                return i;
            }
        }
        return -1;
    }

    private String findFullCategoryName(final String code) {
        for (Object[] item : budgetData) {
            if (item[0].toString().equals(code)) {
                return item[1].toString();
            }
        }
        return null;
    }

    private Object[] findItemData(final String code) {
        for (Object[] item : budgetData) {
            if (item[0].toString().equals(code)) {
                return item;
            }
        }
        return null;
    }

    private void showDetailsDialog(final Object[] itemData) {
        String code = itemData[0].toString();
        String category = itemData[1].toString();
        double amount;

        try {
            if (itemData[2] instanceof Number) {
                amount = ((Number) itemData[2]).doubleValue();
            } else {
                amount = Double.parseDouble(itemData[2].toString());
            }
        } catch (NumberFormatException e) {
            amount = 0.0;
        }

        double amountInBillions = amount / 1_000_000_000;
        double amountInMillions = amount / 1_000_000;

        String percentage = String.format("%.1f%%", (amount / getTotalBudget()) * 100);

        String message = String.format(
                "<html>"
                        + "<div style='font-family:Arial;font-size:12px;width:400px;'>"
                        + "<div style='background:#2C3E50;color:white;"
                        + "padding:10px;margin:-10px -10px 10px -10px;'>"
                        + "<h3 style='margin:0;'>"
                        + "CATEGORY DETAILS"
                        + "</h3></div>"
                        + "<table style='width:100%%;border-collapse:collapse;"
                        + "margin:10px 0;'>"
                        + "<tr style='background:#f8f9fa;'>"
                        + "<td style='padding:8px;border:1px solid #ddd;"
                        + "width:120px;'><b>Code:</b></td>"
                        + "<td style='padding:8px;border:1px solid #ddd;'>"
                        + "<span style='color:#2980B9;font-weight:bold;"
                        + "font-size:14px;'>%s</span></td></tr>"
                        + "<tr><td style='padding:8px;border:1px solid #ddd;'>"
                        + "<b>Category:</b></td>"
                        + "<td style='padding:8px;border:1px solid #ddd;'>%s</td>"
                        + "</tr>"
                        + "<tr style='background:#f8f9fa;'>"
                        + "<td style='padding:8px;border:1px solid #ddd;'>"
                        + "<b>Amount:</b></td>"
                        + "<td style='padding:8px;border:1px solid #ddd;"
                        + "color:#27AE60;font-weight:bold;font-size:14px;'>"
                        + "%,.0f €</td></tr>"
                        + "<tr style='background:#f8f9fa;'>"
                        + "<td style='padding:8px;border:1px solid #ddd;'>"
                        + "<b>In billions:</b></td>"
                        + "<td style='padding:8px;border:1px solid #ddd;"
                        + "color:#2980B9;font-weight:bold;'>%,.2f billion €</td>"
                        + "</tr>"
                        + "<tr style='background:#f8f9fa;'>"
                        + "<td style='padding:8px;border:1px solid #ddd;'>"
                        + "<b>In millions:</b></td>"
                        + "<td style='padding:8px;border:1px solid #ddd;"
                        + "color:#8E44AD;font-weight:bold;'>%,.0f million €</td>"
                        + "</tr>"
                        + "<tr><td style='padding:8px;border:1px solid #ddd;'>"
                        + "<b>Percentage of Total:</b></td>"
                        + "<td style='padding:8px;border:1px solid #ddd;"
                        + "color:#E74C3C;font-weight:bold;font-size:14px;'>"
                        + "%s</td></tr>"
                        + "</table>"
                        + "<div style='margin-top:15px;padding:10px;"
                        + "background:#ECF0F1;border-left:4px solid #3498DB;'>"
                        + "<span style='font-size:11px;color:#7F8C8D;'>"
                        + "Source: Ministry of Finance - Budget 2025"
                        + "</span></div>"
                        + "</div></html>",
                code,
                category,
                amount,
                amountInBillions,
                amountInMillions,
                percentage);

        JOptionPane.showMessageDialog(this,
                new JLabel(message),
                "BUDGET DETAILS",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private double getTotalBudget() {
        double total = 0;
        for (Object[] item : budgetData) {
            try {
                if (item[2] instanceof Number) {
                    total += ((Number) item[2]).doubleValue();
                } else {
                    total += Double.parseDouble(item[2].toString());
                }
            } catch (NumberFormatException e) {
                // Ignore invalid amounts
            }
        }
        return total;
    }
}
