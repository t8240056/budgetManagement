package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * Panel υπεύθυνο για την εμφάνιση της σύνοψης του Άρθρου 2
 * και τη λήψη του κωδικού φορέα για περαιτέρω ανάλυση.
 */
public final class ExpenseByAgencySummaryPanel extends JPanel {
    
    /** Αναφορά στο κεντρικό JFrame. */
    private final MainFrame frame;
    
    /** Αναφορά στην κλάση λογικής για φόρτωση δεδομένων. */
    private final BudgetAnalyzer analyzer;

    /** Πίνακας που εμφανίζει τα συνοπτικά στοιχεία του Άρθρου 2. */
    private JTable summaryTable;

    /** Πεδίο κειμένου για την εισαγωγή του κωδικού φορέα. */
    private JTextField codeField;

    /** Κουμπί που αποκαλύπτει το πεδίο εισαγωγής κωδικού. */
    private JButton openCodeInputButton;

    /** Κουμπί Επιβεβαίωσης. */
    private JButton confirmButton;

    /** Κουμπί Επιστροφής. */
    private JButton backButton;

    /**
     * Κατασκευαστής.
     * @param mainFrame το κεντρικό παράθυρο της εφαρμογής.
     * @param budgetAnalyzer η κλάση λογικής για τα δεδομένα.
     */
    public ExpenseByAgencySummaryPanel(final MainFrame mainFrame, 
                                       final BudgetAnalyzer budgetAnalyzer) {
        this.frame = mainFrame;
        this.analyzer = budgetAnalyzer;
        setLayout(new BorderLayout());
        
        initializeTable();
        initializeInputArea();
        initializeBottomButtons();
    }
    
    /**
     * Αρχικοποιεί τον πίνακα (JTable) με τα δεδομένα του Άρθρου 2.
     * Χρησιμοποιεί την πρώτη γραμμή ως κεφαλίδα.
     */
    private void initializeTable() {
        // Παίρνουμε τα συνοπτικά δεδομένα από τον BudgetAnalyzer
        final String[][] data = analyzer.getArticle2Data();
        
        // Χειρισμός σφάλματος αν δεν υπάρχουν δεδομένα (λιγότερες από 2 γραμμές)
        if (data.length < 2) {
            final String[] emptyColumns = {"Κωδικός", "Φορέας", "Σύνολο"};
            summaryTable = new JTable(new String[0][3], emptyColumns);
        } else {
            // Η πρώτη γραμμή είναι η κεφαλίδα 
            final String[] columnNames = data[0]; 
            
            // Δημιουργούμε έναν πίνακα μόνο με τα δεδομένα (από τη 2η γραμμή και κάτω)
            final String[][] tableData = new String[data.length - 1][];
            System.arraycopy(data, 1, tableData, 0, data.length - 1);
            
            summaryTable = new JTable(tableData, columnNames);
        }

        // Απενεργοποιούμε την επεξεργασία του πίνακα 
        summaryTable.setEnabled(false); 
        
        final JScrollPane scrollPane = new JScrollPane(summaryTable);
        // Ρύθμιση μεγέθους για οπτική συνοχή
        scrollPane.setPreferredSize(new Dimension(550, 400)); 
        
        // Τοποθέτηση του πίνακα στο κέντρο του Panel (BorderLayout.CENTER)
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Αρχικοποιεί την περιοχή εισαγωγής κωδικού (όπως το RevenuePanel).
     */
    private void initializeInputArea() {
        // Χρησιμοποιούμε GridLayout και BorderFactory για το στυλ της ομάδας
        final JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        openCodeInputButton = new JButton(
            "Εισάγετε 4ψήφιο κωδικό φορέα προς ανάλυση");
        codeField = new JTextField();
        codeField.setVisible(false);

        // ActionListener: Όταν πατηθεί, εμφανίζεται το πεδίο κειμένου
        openCodeInputButton.addActionListener(e -> {
            codeField.setVisible(true);
            inputPanel.revalidate(); 
            inputPanel.repaint(); 
        });

        inputPanel.add(openCodeInputButton);
        inputPanel.add(codeField);
        
        // Τοποθέτηση στο πάνω μέρος του Panel (BorderLayout.NORTH)
        add(inputPanel, BorderLayout.NORTH);
    }
    
    /**
     * Αρχικοποιεί τα κουμπιά πλοήγησης στο κάτω μέρος του Panel.
     */
    private void initializeBottomButtons() {
        final JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        bottomPanel.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10));

        backButton = new JButton("Επιστροφή");
        confirmButton = new JButton("Επιβεβαίωση");
        frame.confButtonColors(confirmButton);
        frame.backButtonColors(backButton);

        // Ενέργεια Επιστροφής (Πίσω στο ExpensePanel)
        backButton.addActionListener(e -> {
            frame.switchTo("expensePanel"); 
        });

        // Ενέργεια Επιβεβαίωσης: Παίρνει τον κωδικό και καλεί τη MainFrame
        confirmButton.addActionListener(e -> {
            if (codeField.isVisible() && !codeField.getText().trim().isEmpty()) {
                try {
                    // Μετατροπή κωδικού σε int
                    final int code = Integer.parseInt(codeField.getText().trim());
                    
                    // Καλεί τη MainFrame για να αλλάξει οθόνη και να φορτώσει τα δεδομένα
                    frame.showAgencyDetails(code); 
                } catch (NumberFormatException ex) {
                    AppException.showError("Παρακαλώ εισάγετε έγκυρο αριθμητικό κωδικό.");
                }
            } else {
                AppException.showError(
                    "Πληκτρολογήστε κωδικό ή πατήστε το κουμπί επιστροφής.");
            }
        });

        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
