package auebprogramming;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * Panel υπεύθυνο για την εμφάνιση του αναλυτικού προϋπολογισμού
 * ενός συγκεκριμένου φορέα.
 */
public final class AgencyDetailsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Αναφορά στο κεντρικό JFrame. */
    private final MainFrame frame;

    /** Αναφορά στην κλάση λογικής για φόρτωση δεδομένων. */
    private final BudgetAnalyzer analyzer;

    /** Ο πίνακας που θα εμφανίζει τα αναλυτικά στοιχεία. */
    private JTable detailsTable;

    /** Το μοντέλο του πίνακα που επιτρέπει την αλλαγή δεδομένων. */
    private DefaultTableModel tableModel;

    /** Κουμπί Επιστροφής. */
    private JButton backButton;

    /**
     * Κατασκευαστής.
     *
     * @param mainFrame το κεντρικό παράθυρο της εφαρμογής.
     * @param budgetAnalyzer η κλάση λογικής για τα δεδομένα.
     */
    public AgencyDetailsPanel(final MainFrame mainFrame,
                              final BudgetAnalyzer budgetAnalyzer) {
        this.frame = mainFrame;
        this.analyzer = budgetAnalyzer;
        setLayout(new BorderLayout());

        // Αρχική δημιουργία πίνακα
        initializeTable();
        // Δημιουργία κουμπιών
        initializeBottomButtons();
    }

    /**
     * Αρχικοποιεί τον πίνακα (JTable) με κενά δεδομένα.
     */
    private void initializeTable() {
        // Αρχικό κενό μοντέλο πίνακα
        final String[] columnNames = {"Κωδικός", "Περιγραφή", "Ποσό"};
        // Κενά δεδομένα για αρχική εμφάνιση
        final String[][] data = new String[][]{{"", "", ""}};

        tableModel = new DefaultTableModel(data, columnNames);
        detailsTable = new JTable(tableModel);
        detailsTable.setEnabled(false); // Ο πίνακας δεν είναι επεξεργάσιμος

        // Απενεργοποίηση αυτόματου re-sizing
        detailsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        final JScrollPane scrollPane = new JScrollPane(detailsTable);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Αρχικοποιεί τα κουμπιά πλοήγησης στο κάτω μέρος του Panel.
     */
    private void initializeBottomButtons() {
        // Μόνο 1 κουμπί (Επιστροφή)
        final JPanel bottomPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        backButton = new JButton("Επιστροφή");
        frame.backButtonColors(backButton);
        // Επιστροφή στο Summary Panel
        backButton.addActionListener((final ActionEvent e) -> {
            frame.switchTo("expenseByAgency");
        });

        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Εφαρμόζει το πλάτος των στηλών ώστε να χωράει το κείμενο.
     */
    private void applyColumnWidths() {
        final int preferredWidth = 1300;

        // Ελέγχουμε ότι έχουμε τουλάχιστον 3 στήλες
        if (detailsTable.getColumnCount() >= 3) {

            // 1. Στήλη Κωδικός (10%)
            final TableColumn col1 = detailsTable.getColumnModel()
                    .getColumn(0);
            col1.setPreferredWidth(preferredWidth * 10 / 100);

            // 2. Στήλη Ονομασία (60%)
            final TableColumn col2 = detailsTable.getColumnModel()
                    .getColumn(1);
            col2.setPreferredWidth(preferredWidth * 60 / 100);

            // 3. Στήλη Ποσό (30%)
            final TableColumn col3 = detailsTable.getColumnModel()
                    .getColumn(2);
            col3.setPreferredWidth(preferredWidth * 30 / 100);
        }
    }

    /**
     * Μέθοδος που καλείται από τη MainFrame για φόρτωση των δεδομένων.
     *
     * @param agencyCode ο κωδικός του φορέα.
     */
    public void loadDetails(final int agencyCode) {
        try {
            final String[][] detailedData = analyzer
                    .getDetailedBudget(agencyCode);
            final String[] columnNames = detailedData[3];
            final int dataStartIndex = 4;
            final int numRows = detailedData.length - dataStartIndex;

            if (numRows <= 0) {
                tableModel.setDataVector(new String[0][0], columnNames);
                this.revalidate();
                this.repaint();
                return;
            }

            final String[][] dataRows = new String[numRows][];

            for (int i = 0; i < numRows; i++) {
                dataRows[i] = detailedData[i + dataStartIndex];
            }

            // 1. Ενημερώνουμε το μοντέλο του πίνακα
            tableModel.setDataVector(dataRows, columnNames);

            // 2. Εφαρμόζουμε τα πλάτη των στηλών
            applyColumnWidths();

            // 3. Ενημερώνουμε την οθόνη
            this.revalidate();
            this.repaint();

        } catch (final IllegalArgumentException e) {
            AppException.showError("Σφάλμα: " + e.getMessage());
            frame.switchTo("expenseByAgency");
        }
    }
}
