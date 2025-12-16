package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Panel for applying an absolute amount change to a budget entry.
 */
public class AbsoluteChangePanel extends JPanel {

    private final MainFrame mainFrame;
    private final BudgetManager manager;

    // Πεδία Εισόδου (Input Fields)
    private final JTextField codeField;
    private final JTextField amountField;
    private final JTextField justificationField;

    // Σταθερές Layout
    private static final int FIELD_COLS = 10; // <-- Μικρότερο πλάτος μπάρας
    private static final int FONT_SIZE = 20; // <-- Μεγάλη αλλά ισορροπημένη γραμματοσειρά
    private static final int BOTTOM_ROWS = 1;
    private static final int TWO = 2;
    private static final int TEN = 10;
    private static final int BOTTOM_PANEL_HEIGHT = 70;


    /**
     * Constructor for AbsoluteChangePanel.
     *
     * @param frame the main application frame
     * @param manager the BudgetManager instance
     */
    public AbsoluteChangePanel(final MainFrame frame, final BudgetManager manager) {
        this.mainFrame = frame;
        this.manager = manager;

        setLayout(new BorderLayout(10, 10));

        // Δημιουργία Font για ετικέτες και πεδία
        Font inputFont = new Font("SansSerif", Font.PLAIN, FONT_SIZE); 
        
        // ------------------------------------------------------------------
        // ΚΕΝΤΡΙΚΟ PANEL ΕΙΣΟΔΩΝ (CENTER PANEL)
        // ------------------------------------------------------------------
        
        // 1. Εσωτερικό Panel: GridLayout για στοίχιση Ετικέτας/Πεδίου
        // Αυξάνουμε το κενό μεταξύ των γραμμών για να χωράει η μεγάλη γραμματοσειρά
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 10, 30)); 
        gridPanel.setBorder(BorderFactory.createEmptyBorder(60, 20, 20, 20)); // ΠΕΡΙΘΩΡΙΑ

        // Κωδικός Φορέα
        JLabel label1 = new JLabel("Πληκτρολογήστε κωδικό φορέα:", SwingConstants.RIGHT);
        label1.setFont(inputFont);
        gridPanel.add(label1);
        codeField = new JTextField(FIELD_COLS);
        codeField.setFont(inputFont);
        gridPanel.add(codeField);

        // Ποσό Αλλαγής
        JLabel label2 = new JLabel("Πληκτρολογήστε ποσό αλλαγής:", SwingConstants.RIGHT);
        label2.setFont(inputFont);
        gridPanel.add(label2);
        amountField = new JTextField(FIELD_COLS);
        amountField.setFont(inputFont);
        gridPanel.add(amountField);

        // Αιτιολογία
        JLabel label3 = new JLabel("Αιτιολογία:", SwingConstants.RIGHT);
        label3.setFont(inputFont);
        gridPanel.add(label3);
        justificationField = new JTextField(FIELD_COLS);
        justificationField.setFont(inputFont);
        gridPanel.add(justificationField);

        // 2. Εξωτερικό Wrapper: Χρησιμοποιούμε BorderLayout.WEST για Αριστερή Στοίχιση
        JPanel alignWrapper = new JPanel(new BorderLayout()); 
        alignWrapper.add(gridPanel, BorderLayout.WEST); // ΣΤΟΙΧΙΣΗ ΑΡΙΣΤΕΡΑ
        
        add(alignWrapper, BorderLayout.CENTER);

        // ------------------------------------------------------------------
        // ΚΑΤΩ PANEL (BOTTOM PANEL - Κουμπιά Επιβεβαίωσης/Επιστροφής)
        // ------------------------------------------------------------------
        JPanel bottomPanel = new JPanel(new GridLayout(BOTTOM_ROWS, TWO, TEN, TEN));
        
        bottomPanel.setBorder(
            BorderFactory.createEmptyBorder(TEN, TEN, TEN, TEN));
        bottomPanel.setPreferredSize(new Dimension(10, BOTTOM_PANEL_HEIGHT)); 

        JButton confirmButton = new JButton("Επιβεβαίωση");
        JButton backButton = new JButton("Επιστροφή");

        // Εφαρμογή του ίδιου μεγέθους γραμματοσειράς και στα κουμπιά 
        Font buttonFont = new Font("SansSerif", Font.BOLD, FONT_SIZE);
        confirmButton.setFont(buttonFont);
        backButton.setFont(buttonFont);

        mainFrame.confButtonColors(confirmButton);
        mainFrame.backButtonColors(backButton);

        // Προσθήκη Listeners
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                handleConfirmation();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                mainFrame.switchTo("changesMenu");
            }
        });

        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the confirmation button action, calling the BudgetManager.
     */
    private void handleConfirmation() {
        String code = codeField.getText().trim();
        String amountStr = amountField.getText().trim();
        String justification = justificationField.getText().trim();

        if (code.isEmpty() || amountStr.isEmpty() || justification.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Συμπληρώστε όλα τα πεδία.",
                    "Σφάλμα Εισόδου",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Κλήση της κεντρικής λογικής
            String result = manager.makeAbsoluteChange(code, amountStr, justification);

            // Μήνυμα Επιτυχίας
            JOptionPane.showMessageDialog(this,
                    result,
                    "Επιτυχής Αλλαγή",
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Καθαρισμός των πεδίων
            codeField.setText("");
            amountField.setText("");
            justificationField.setText("");
            
        } catch (AppException e) {
            // Μήνυμα Αποτυχίας
            JOptionPane.showMessageDialog(this,
                    "Αποτυχία Αλλαγής Ποσού: " + e.getMessage(),
                    "Σφάλμα Λογικής Εφαρμογής",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
             JOptionPane.showMessageDialog(this,
                    "Μη αναμενόμενο Σφάλμα: " + e.getMessage(),
                    "Γενικό Σφάλμα",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
