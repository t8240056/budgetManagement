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
import javax.swing.table.TableColumn; // ΝΕΟ IMPORT

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
        // Αρχικό κενό μοντέλο πίνακα (Οι ονομασίες θα αντικατασταθούν κατά τη φόρτωση)
        final String[] columnNames = {"Κωδικός", "Περιγραφή", "Ποσό"};
        // Κενά δεδομένα για αρχική εμφάνιση
        final String[][] data = new String[][]{{"", "", ""}}; 
        
        tableModel = new DefaultTableModel(data, columnNames);
        detailsTable = new JTable(tableModel);
        detailsTable.setEnabled(false); // Ο πίνακας δεν είναι επεξεργάσιμος
        
        // ΔΙΟΡΘΩΣΗ ΟΡΑΤΟΤΗΤΑΣ: Απενεργοποίηση αυτόματου re-sizing για να εμφανιστούν όλες οι στήλες
        detailsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        final JScrollPane scrollPane = new JScrollPane(detailsTable);
        
        // ΑΦΑΙΡΕΘΗΚΕ ΤΟ setPreferredSize ΓΙΑ ΝΑ ΠΙΑΣΕΙ ΟΛΟ ΤΟΝ ΧΩΡΟ
        
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
     * Εφαρμόζει το πλάτος των στηλών ώστε να χωράει το κείμενο (π.χ., 60% για την Ονομασία).
     */
    private void applyColumnWidths() {
        final int preferredWidth = 581; // Χρησιμοποιούμε το μέγεθος του JFrame (900px)
        
        // Ελέγχουμε ότι έχουμε τουλάχιστον 3 στήλες (Κωδικός, Ονομασία, Ποσό)
        if (detailsTable.getColumnCount() >= 3) {
            
            // 1. Στήλη Κωδικός (10%)
            TableColumn col1 = detailsTable.getColumnModel().getColumn(0);
            col1.setPreferredWidth(preferredWidth * 10 / 100); 

            // 2. Στήλη Ονομασία (60%)
            TableColumn col2 = detailsTable.getColumnModel().getColumn(1);
            col2.setPreferredWidth(preferredWidth * 60 / 100); 
            
            // 3. Στήλη Ποσό (30%)
            TableColumn col3 = detailsTable.getColumnModel().getColumn(2);
            col3.setPreferredWidth(preferredWidth * 30 / 100); 
        }
    }

    /**
     * Μέθοδος που καλείται από τη MainFrame για φόρτωση των αναλυτικών δεδομένων.
     * @param agencyCode ο κωδικός του φορέα.
     */
    public void loadDetails(final int agencyCode) {
        try {
            // ... (Λογική φόρτωσης δεδομένων) ...
            final String[][] detailedData = analyzer.getDetailedBudget(agencyCode);
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
            
            // 2. ΝΕΟ ΒΗΜΑ: Εφαρμόζουμε τα πλάτη των στηλών τώρα που υπάρχουν δεδομένα!
            applyColumnWidths();
            
            // 3. Ενημερώνουμε την οθόνη
            this.revalidate();
            this.repaint();
            
        } catch (IllegalArgumentException e) {
            AppException.showError("Σφάλμα: " + e.getMessage());
            frame.switchTo("expenseByAgency"); 
        }
    }
}
