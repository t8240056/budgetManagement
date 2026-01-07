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

    // Components για το GUI
    private ChartPanel chartPanel;
    private JFreeChart chart;
    private CategoryDataset dataset;
    private Map<String, Integer> categoryToIndexMap = new HashMap<>();
    private Object[][] budgetData;

    // Constructor που δέχεται τον πίνακα με τα δεδομένα
    public BudgetChartJFree(final Object[][] data) {  // Αλλαγή: budgetData -> data
        super(new BorderLayout());
        this.budgetData = data;  // Εδώ δεν υπάρχει hiding, γιατί χρησιμοποιούμε this.

        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Ο πίνακας δεδομένων δεν μπορεί να είναι κενός!");
        }

        initializeComponents();
    }

    // Αρχικοποίηση όλων των components
    private void initializeComponents() {
        // Δημιουργία dataset
        dataset = createDataset();

        // Δημιουργία chart
        chart = createChart(dataset);

        // Δημιουργία chart panel
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1200, calculateHeight(budgetData.length)));
        chartPanel.setMouseZoomable(true, false);
        chartPanel.setDisplayToolTips(true);

        // Προσθήκη custom tooltips και interactivity
        addCustomTooltips(chartPanel, dataset);

        // Προσθήκη στο panel
        add(chartPanel, BorderLayout.CENTER);
    }

    // Μέθοδος για να πάρει κάποιος το ChartPanel (για να το προσθέσει σε άλλο
    // container)
    public final ChartPanel getChartPanel() {
        return chartPanel;
    }

    // Μέθοδος για να πάρει κάποιος το JFreeChart
    public final JFreeChart getChart() {
        return chart;
    }

    // Μέθοδος για να ενημερώσει τα δεδομένα
    public final void updateData(final Object[][] newData) {
        this.budgetData = newData;
        categoryToIndexMap.clear();
        dataset = createDataset();

        // Ενημέρωση του chart
        chart.getCategoryPlot().setDataset(dataset);

        // Ενημέρωση tooltips
        addCustomTooltips(chartPanel, dataset);

        // Ανανέωση του panel
        revalidate();
        repaint();
    }

    // Μέθοδος για αλλαγή τίτλου
    public final void setChartTitle(final String title) {
        TextTitle mainTitle = new TextTitle(
                title,
                new Font("Arial", Font.BOLD, 18));
        mainTitle.setPaint(new Color(30, 60, 90));
        chart.setTitle(mainTitle);
    }

    // Μέθοδος για αλλαγή υποτίτλου
    public final void setChartSubtitle(final String subtitle) {
        // Αφαίρεση υπάρχοντος υποτίτλου αν υπάρχει
        if (chart.getSubtitleCount() > 0) {
            chart.removeSubtitle(chart.getSubtitle(0));
        }

        TextTitle subTitle = new TextTitle(
                subtitle,
                new Font("Arial", Font.ITALIC, 12));
        subTitle.setPaint(new Color(100, 100, 100));
        chart.addSubtitle(subTitle);
    }

    // Μέθοδος για αλλαγή μεγέθους
    public final void setChartSize(final int width, final int height) {
        chartPanel.setPreferredSize(new Dimension(width, height));
        chartPanel.revalidate();
    }

    // Μέθοδος για να πάρει τα τρέχοντα δεδομένα
    public final Object[][] getCurrentData() {
        return budgetData;
    }

    // Μέθοδος για να πάρει τον αριθμό των κατηγοριών
    public final int getCategoryCount() {
        return budgetData.length;
    }

    // Μέθοδος για να πάρει το συνολικό ποσό
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
        DefaultCategoryDataset newDataset = new DefaultCategoryDataset();  // Αλλαγή: dataset -> newDataset

        // Καθαρισμός του map
        categoryToIndexMap.clear();

        // Προσθήκη δεδομένων από τον πίνακα στο dataset
        for (int i = 0; i < budgetData.length; i++) {
            Object[] item = budgetData[i];

            // Επαλήθευση ότι ο πίνακας έχει 3 στήλες
            if (item.length < 3) {
                throw new IllegalArgumentException(
                        "Κάθε γραμμή πρέπει να έχει 3 στοιχεία: [Κωδικός, Κατηγορία, Ποσό]");
            }

            String kodikos = item[0].toString();
            String kathgoria = item[1].toString();
            double poso;

            // Μετατροπή του ποσού σε double
            try {
                if (item[2] instanceof Number) {
                    poso = ((Number) item[2]).doubleValue();
                } else {
                    poso = Double.parseDouble(item[2].toString());
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Το ποσό στη γραμμή " + (i + 1) + " δεν είναι έγκυρος αριθμός: " + item[2]);
            }

            // Μετατροπή σε δισεκατομμύρια για καλύτερη εμφάνιση
            double chartValue = Math.max(poso, 1_000_000);

            // Συντομευμένο όνομα για καλύτερη εμφάνιση στον άξονα
            String displayName = kodikos + " - " + getShortName(kathgoria, 35);

            newDataset.addValue(chartValue, "Προϋπολογισμός", displayName);

            // Αποθήκευση αντιστοιχίας για γρήγορη αναζήτηση
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

    private JFreeChart createChart(final CategoryDataset dataSet) {  // Αλλαγή: dataset -> dataSet
        // Δημιουργία οριζόντιου bar chart
        JFreeChart newChart = ChartFactory.createBarChart(  // Αλλαγή: chart -> newChart
                null, // Χωρίς τίτλο (θα βάλουμε custom)
                null, // Χωρίς label για τον κάθετο άξονα
                "Ποσό (€ – λογαριθμική κλίμακα)", // Label για οριζόντιο άξονα
                dataSet,
                PlotOrientation.HORIZONTAL,
                false, // No legend
                true, // Tooltips
                false // URLs
        );

        // Προσαρμογή εμφάνισης
        CategoryPlot plot = (CategoryPlot) newChart.getPlot();

        // Προσαρμογή renderer
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Gradient χρώματα από πράσινο προς μπλε
        Color[] colors = createColorGradient(dataSet.getColumnCount());

        // Εφαρμογή χρωμάτων
        for (int i = 0; i < dataSet.getColumnCount(); i++) {
            renderer.setSeriesPaint(i, colors[i]);
        }

        // Προσθήκη τιμών στα μπάρ
        DecimalFormat df = new DecimalFormat("#,##0.00");
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator(
                "{2}", df));
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelPaint(Color.DARK_GRAY);
        renderer.setDefaultItemLabelFont(new Font("Arial", Font.BOLD, 11));
        renderer.setDefaultPositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT));

        // Ρύθμιση μπάρ (αυτόματη ρύθμιση βάσει αριθμού γραμμών)
        int rowCount = budgetData.length;
        double itemMargin = Math.max(0.05, Math.min(0.20, 0.20 - (rowCount * 0.005)));
        double maxBarWidth = Math.max(0.03, Math.min(0.08, 0.08 - (rowCount * 0.001)));

        renderer.setItemMargin(itemMargin);
        renderer.setMaximumBarWidth(maxBarWidth);

        // Προσαρμογή άξονα κατηγοριών (κάθετος)
        CategoryAxis domainAxis = plot.getDomainAxis();
        double categoryMargin = Math.max(0.10, Math.min(0.30, 0.30 - (rowCount * 0.01)));
        domainAxis.setCategoryMargin(categoryMargin);

        // Προσαρμογή μεγέθους γραμματοσειράς βάσει αριθμού γραμμών
        int fontSize = Math.max(8, 11 - (rowCount / 20));
        domainAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, fontSize));
        domainAxis.setTickLabelPaint(new Color(60, 60, 60));
        domainAxis.setTickLabelInsets(new RectangleInsets(2, 2, 2, 2));

        // Προσαρμογή άξονα τιμών (οριζόντιος)
        LogAxis rangeAxis = new LogAxis("Ποσό (€ - λογαριθμική κλίμακα)");
        rangeAxis.setBase(10);
        rangeAxis.setSmallestValue(1_000_000); // 1 εκ. ελάχιστο
        rangeAxis.setNumberFormatOverride(new DecimalFormat("###,###"));
        rangeAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, 12));
        rangeAxis.setTickLabelPaint(new Color(70, 70, 70));

        plot.setRangeAxis(rangeAxis);

        // Προσαρμογή background
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

    private void addCustomTooltips(final ChartPanel panel, final CategoryDataset dataSet) {  // Αλλαγή: chartPanel -> panel, dataset -> dataSet
        // Custom tooltip generator
        StandardCategoryToolTipGenerator toolTipGenerator = new StandardCategoryToolTipGenerator() {
            @Override
            public String generateToolTip(final CategoryDataset dataset, final int row, final int column) {
                String category = (String) dataset.getColumnKey(column);
                String[] parts = category.split(" - ", 2);
                String kodikos = parts.length > 0 ? parts[0].trim() : "";
                String kathgoria = parts.length > 1 ? parts[1].trim() : "";

                String fullKathgoria = findFullCategoryName(kodikos);

                Object[] itemData = findItemData(kodikos);

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
                                + "Κωδικός: <span style='color:#2980B9;'>%s</span>"
                                + "</div>"
                                + "<div style='margin-bottom:6px;'>"
                                + "<b>Κατηγορία:</b><br>%s</div>"
                                + "<hr style='border:none;border-top:1px dashed #ccc;"
                                + "margin:8px 0;'>"
                                + "<table style='width:100%%;border-collapse:collapse;'>"
                                + "<tr><td style='padding:4px;'>"
                                + "<b>Ποσό:</b></td>"
                                + "<td style='padding:4px;color:#27AE60;"
                                + "font-weight:bold;'>%s δις €</td></tr>"
                                + "<tr><td style='padding:4px;'>"
                                + "<b>Πλήρες Ποσό:</b></td>"
                                + "<td style='padding:4px;color:#2C3E50;'>"
                                + "%s €</td></tr>"
                                + "</table>"
                                + "<div style='margin-top:8px;font-size:11px;"
                                + "color:#7F8C8D;font-style:italic;'>"
                                + "Κάντε κλικ για περισσότερες λεπτομέρειες"
                                + "</div>"
                                + "</div></html>",
                        kodikos,
                        fullKathgoria != null ? fullKathgoria : kathgoria,
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
                        String kodikos = parts.length > 0 ? parts[0].trim() : "";

                        Object[] itemData = findItemData(kodikos);
                        if (itemData != null) {
                            showDetailsDialog(itemData);
                        }
                    }
                }
            }
        });
    }

    private int findColumnIndex(final CategoryDataset dataSet, final String category) {  // Αλλαγή: dataset -> dataSet
        int columnCount = dataSet.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            Comparable<?> key = dataSet.getColumnKey(i);
            if (key instanceof String && key.equals(category)) {
                return i;
            }
        }
        return -1;
    }

    private String findFullCategoryName(final String kodikos) {
        for (Object[] item : budgetData) {
            if (item[0].toString().equals(kodikos)) {
                return item[1].toString();
            }
        }
        return null;
    }

    private Object[] findItemData(final String kodikos) {
        for (Object[] item : budgetData) {
            if (item[0].toString().equals(kodikos)) {
                return item;
            }
        }
        return null;
    }

    private void showDetailsDialog(final Object[] itemData) {
        String kodikos = itemData[0].toString();
        String kathgoria = itemData[1].toString();
        double poso;

        try {
            if (itemData[2] instanceof Number) {
                poso = ((Number) itemData[2]).doubleValue();
            } else {
                poso = Double.parseDouble(itemData[2].toString());
            }
        } catch (NumberFormatException e) {
            poso = 0.0;
        }

        double posoInBillions = poso / 1_000_000_000;
        double posoInMillions = poso / 1_000_000;

        String percentage = String.format("%.1f%%", (poso / getTotalBudget()) * 100);

        String message = String.format(
                "<html>"
                        + "<div style='font-family:Arial;font-size:12px;width:400px;'>"
                        + "<div style='background:#2C3E50;color:white;"
                        + "padding:10px;margin:-10px -10px 10px -10px;'>"
                        + "<h3 style='margin:0;'>"
                        + "ΛΕΠΤΟΜΕΡΕΙΕΣ ΚΑΤΗΓΟΡΙΑΣ"
                        + "</h3></div>"
                        + "<table style='width:100%%;border-collapse:collapse;"
                        + "margin:10px 0;'>"
                        + "<tr style='background:#f8f9fa;'>"
                        + "<td style='padding:8px;border:1px solid #ddd;"
                        + "width:120px;'><b>Κωδικός:</b></td>"
                        + "<td style='padding:8px;border:1px solid #7b1515ff;'>"
                        + "<span style='color:#2980B9;font-weight:bold;"
                        + "font-size:14px;'>%s</span></td></tr>"
                        + "<tr><td style='padding:8px;border:1px solid #ddd;'>"
                        + "<b>Κατηγορία:</b></td>"
                        + "<td style='padding:8px;border:1px solid #ddd;'>%s</td>"
                        + "</tr>"
                        + "<tr style='background:#f8f9fa;'>"
                        + "<td style='padding:8px;border:1px solid #ddd;'>"
                        + "<b>Ποσό:</b></td>"
                        + "<td style='padding:8px;border:1px solid #ddd;"
                        + "color:#27AE60;font-weight:bold;font-size:14px;'>"
                        + "%,.0f €</td></tr>"
                        + "<tr style='background:#f8f9fa;'>"
                        + "<td style='padding:8px;border:1px solid #ddd;'>"
                        + "<b>Σε δισεκατομμύρια:</b></td>"
                        + "<td style='padding:8px;border:1px solid #ddd;"
                        + "color:#2980B9;font-weight:bold;'>%,.2f δις €</td>"
                        + "</tr>"
                        + "<tr style='background:#f8f9fa;'>"
                        + "<td style='padding:8px;border:1px solid #ddd;'>"
                        + "<b>Σε εκατομμύρια:</b></td>"
                        + "<td style='padding:8px;border:1px solid #ddd;"
                        + "color:#8E44AD;font-weight:bold;'>%,.0f εκ €</td>"
                        + "</tr>"
                        + "<tr><td style='padding:8px;border:1px solid #ddd;'>"
                        + "<b>Ποσοστό Σύνολου:</b></td>"
                        + "<td style='padding:8px;border:1px solid #ddd;"
                        + "color:#E74C3C;font-weight:bold;font-size:14px;'>"
                        + "%s</td></tr>"
                        + "</table>"
                        + "<div style='margin-top:15px;padding:10px;"
                        + "background:#ECF0F1;border-left:4px solid #3498DB;'>"
                        + "<span style='font-size:11px;color:#7F8C8D;'>"
                        + "Πηγή: Υπουργείο Οικονομικών - Προϋπολογισμός 2025"
                        + "</span></div>"
                        + "</div></html>",
                kodikos,
                kathgoria,
                poso,
                posoInBillions,
                posoInMillions,
                percentage);

        JOptionPane.showMessageDialog(this,
                new JLabel(message),
                "ΛΕΠΤΟΜΕΡΕΙΕΣ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ",
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
                // Αγνοούμε μη έγκυρα ποσά
            }
        }
        return total;
    }
}
