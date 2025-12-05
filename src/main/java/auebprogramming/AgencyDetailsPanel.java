package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Panel υπεύθυνο για την εμφάνιση του αναλυτικού προϋπολογισμού
 * ενός συγκεκριμένου φορέα.
 */
public final class AgencyDetailsPanel extends JPanel {
    
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
        
        final JScrollPane scrollPane = new JScrollPane(detailsTable);
        scrollPane.setPreferredSize(new Dimension(550, 400));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Αρχικοποιεί τα κουμπιά πλοήγησης στο κάτω μέρος του Panel.
     */
    private void initializeBottomButtons() {
        final JPanel bottomPanel = new JPanel(new GridLayout(1, 1, 10, 10)); // Μόνο 1 κουμπί (Επιστροφή)
        bottomPanel.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        backButton = new JButton("Επιστροφή");
        
        // Επιστροφή στο Summary Panel
        backButton.addActionListener(e -> {
            frame.switchTo("expenseByAgency"); 
        });

        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Μέθοδος που καλείται από τη MainFrame για φόρτωση των αναλυτικών δεδομένων.
     * @param agencyCode ο κωδικός του φορέα.
     */
    public void loadDetails(final int agencyCode) {
        try {
            // 1. Καλεί τη λογική σου για να πάρει τον πίνακα από το ΧΧΧΧ.csv
            final String[][] detailedData = analyzer.getDetailedBudget(agencyCode);
            
            // 2. Η πρώτη γραμμή είναι η κεφαλίδα
            final String[] columnNames = detailedData[0]; 
            
            // 3. Δημιουργούμε έναν πίνακα μόνο με τις γραμμές δεδομένων (από τη 2η γραμμή και κάτω)
            final int numRows = detailedData.length - 1;
            final String[][] dataRows = new String[numRows][];
            
            for (int i = 0; i < numRows; i++) {
                dataRows[i] = detailedData[i + 1]; // Παίρνουμε τη γραμμή i+1 (μετά την κεφαλίδα)
            }
            
            // 4. Ενημερώνουμε το μοντέλο του πίνακα με τα νέα δεδομένα (όλες τις στήλες)
            tableModel.setDataVector(dataRows, columnNames);
            
            // 5. Ενημερώνουμε την οθόνη
            this.revalidate();
            this.repaint();
            
        } catch (IllegalArgumentException e) {
            // 6. Χειρισμός του λάθους
            AppException.showError("Σφάλμα: " + e.getMessage());
            
            // Επιστρέφουμε στο πρώτο Panel σε περίπτωση λάθους
            frame.switchTo("expenseByAgency"); 
        }
    }
}
