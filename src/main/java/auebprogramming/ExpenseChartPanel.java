package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public final class ExpenseChartPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final MainFrame mainFrame;
    private final BudgetAnalyzer analyzer;
    private final BudgetChartJFree currentChart;

    public ExpenseChartPanel(final MainFrame frame, final BudgetAnalyzer analyzer) {
        this.mainFrame = frame;
        this.analyzer = analyzer;
        setLayout(new BorderLayout());

        final JLabel titleLabel = new JLabel("Γραφική Απεικόνιση Εξόδων ανά Φορέα", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        final String[][] rawData = analyzer.getArticle2Data();
        final Object[][] chartData = convertData(rawData);

        currentChart = new BudgetChartJFree(chartData);
        currentChart.getChartPanel().setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(currentChart, BorderLayout.CENTER);

        final JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setPreferredSize(new Dimension(200, 70));
        final JButton backButton = new JButton("Επιστροφή");
        mainFrame.backButtonColors(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                mainFrame.switchTo("chartsMenu");
            }
        });
        bottomPanel.add(backButton, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private Object[][] convertData(final String[][] data) {
        if (data == null || data.length <= 1) { return new Object[0][0]; }
        final Object[][] converted = new Object[data.length - 1][3];
        for (int i = 1; i < data.length; i++) {
            converted[i - 1][0] = data[i][0];
            converted[i - 1][1] = data[i][1];
            try {
                // Αφαιρούμε τελείες (χιλιάδες) και αλλάζουμε κόμμα σε τελεία
                String clean = data[i][2].trim().replace(".", "").replace(",", ".");
                converted[i - 1][2] = Double.parseDouble(clean);
            } catch (Exception e) {
                converted[i - 1][2] = 0.0;
            }
        }
        return converted;
    }
}
