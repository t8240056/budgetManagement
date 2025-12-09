package auebprogramming;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public final class ExpensePanel extends JPanel {

    private final JTextArea displayArea;
    private final JRadioButton byAgencyButton;
    private final JRadioButton byCategoryButton;
    private final JButton backButton;
    private final JButton confirmButton;

//*Constructor with a MainFrame parameter */
    public ExpensePanel(MainFrame frame) {

        setLayout(new BorderLayout());

        // ============================
        // 1) Top Panel (Radio Buttons)
        // ============================
        final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        byAgencyButton = new JRadioButton("Έξοδα ανά φορέα");
        byAgencyButton.setFont(new Font("Arial", Font.BOLD, 15));
        byCategoryButton = new JRadioButton("Έξοδα ανά δαπάνη");
        byCategoryButton.setFont(new Font("Arial", Font.BOLD, 15));

        final ButtonGroup group = new ButtonGroup();
        group.add(byAgencyButton);
        group.add(byCategoryButton);

        topPanel.add(byAgencyButton);
        topPanel.add(byCategoryButton);

        add(topPanel, BorderLayout.NORTH);

        final JPanel centerPanel = new JPanel(new BorderLayout());
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        loadReports();
        // ============================
        // 3) Bottom Panel (Buttons)
        // ============================
        final JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(
            1, 2, 10, 0));

        backButton = new JButton("Επιστροφή");
        frame.backButtonColors(backButton);
        confirmButton = new JButton("Επιβεβαίωση");
        frame.confButtonColors(confirmButton);
        //*BackButton's method */
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                frame.switchTo("budget"); // πίσω στο BudgetPanel
            }
        });
        //*ConfirmButton's method */
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {

                if (byAgencyButton.isSelected()) {
                    frame.switchTo("expenseByAgency");
                } else if (byCategoryButton.isSelected()) {
                    frame.switchTo("expenseByCategory");
                } else {
                    // GUI-friendly μήνυμα
                    JOptionPane.showMessageDialog(
                            ExpensePanel.this,
                            "Παρακαλώ επιλέξτε τύπο προβολής.",
                            "Σφάλμα",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    //*This method loads all reports. */
    private void loadReports() {
    try {
        final String categoriesFile = "expense_categories_2025.csv";
        final String ministriesFile = "expense_ministries_2025.csv";

        ExpenseDisplay display = new ExpenseDisplay(
            categoriesFile, ministriesFile);

        final String KRATIKOS = "ΚΡΑΤΙΚΟΣ";
        final String TAKTIKOS = "ΤΑΚΤΙΚΟΣ";
        final String EPENDYSEON = "ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ";

        // 1. Αναφορές ανά δαπάνη
        String reportCategoriesKratikos = display.getCategoriesReport(KRATIKOS);
        String reportCategoriesTaktikos = display.getCategoriesReport(TAKTIKOS);
        String reportCategoriesEpendyseon =
        display.getCategoriesReport(EPENDYSEON);

        // 2. Αναφορές ανά φορέα
        String reportMinistriesKratikos = display.getMinistriesReport(KRATIKOS);
        String reportMinistriesTaktikos = display.getMinistriesReport(TAKTIKOS);
        String reportMinistriesEpendyseon =
        display.getMinistriesReport(EPENDYSEON);

        // 3. Εμφάνιση στο JTextArea
        displayArea.setText("");

        displayArea.append("=== Κατηγορίες - Κρατικός ===\n");
        displayArea.append(reportCategoriesKratikos + "\n\n");

        displayArea.append("=== Κατηγορίες - Τακτικός ===\n");
        displayArea.append(reportCategoriesTaktikos + "\n\n");

        displayArea.append("=== Κατηγορίες - ΠΔΕ ===\n");
        displayArea.append(reportCategoriesEpendyseon + "\n\n");

        displayArea.append("=== Φορείς - Κρατικός ===\n");
        displayArea.append(reportMinistriesKratikos + "\n\n");

        displayArea.append("=== Φορείς - Τακτικός ===\n");
        displayArea.append(reportMinistriesTaktikos + "\n\n");

        displayArea.append("=== Φορείς - ΠΔΕ ===\n");
        displayArea.append(reportMinistriesEpendyseon + "\n\n");

    } catch (Exception e) {
        displayArea.setText(
            "Σφάλμα κατά την φόρτωση δεδομένων:\n" + e.getMessage());
    }
}
}
