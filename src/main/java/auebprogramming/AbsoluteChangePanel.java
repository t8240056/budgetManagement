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
public final class AbsoluteChangePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final MainFrame mainFrame;
    private final BudgetManager manager;

    // Input Fields
    private final JTextField codeField;
    private final JTextField amountField;
    private final JTextField justificationField;

    /**
     * Constructor for AbsoluteChangePanel.
     *
     * @param frame   the main application frame
     * @param manager the BudgetManager instance
     */
    public AbsoluteChangePanel(final MainFrame frame,
                               final BudgetManager manager) {
        this.mainFrame = frame;
        this.manager = manager;

        setLayout(new BorderLayout(10, 10));

        // Δημιουργία Font για ετικέτες και πεδία
        final Font inputFont = new Font("SansSerif", Font.PLAIN, 20);

        // ------------------------------------------------------------------
        // ΚΕΝΤΡΙΚΟ PANEL ΕΙΣΟΔΩΝ
        // ------------------------------------------------------------------

        // Grid Panel για στοίχιση
        final JPanel gridPanel = new JPanel(new GridLayout(3, 2, 10, 30));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(60, 20, 20, 20));

        // Κωδικός Φορέα
        final JLabel label1 = new JLabel("Πληκτρολογήστε κωδικό φορέα:",
                SwingConstants.RIGHT);
        label1.setFont(inputFont);
        gridPanel.add(label1);

        codeField = new JTextField(10);
        codeField.setFont(inputFont);
        gridPanel.add(codeField);

        // Ποσό Αλλαγής
        final JLabel label2 = new JLabel("Πληκτρολογήστε ποσό αλλαγής:",
                SwingConstants.RIGHT);
        label2.setFont(inputFont);
        gridPanel.add(label2);

        amountField = new JTextField(10);
        amountField.setFont(inputFont);
        gridPanel.add(amountField);

        // Αιτιολογία
        final JLabel label3 = new JLabel("Αιτιολογία:", SwingConstants.RIGHT);
        label3.setFont(inputFont);
        gridPanel.add(label3);

        justificationField = new JTextField(10);
        justificationField.setFont(inputFont);
        gridPanel.add(justificationField);

        // Wrapper για στοίχιση αριστερά
        final JPanel alignWrapper = new JPanel(new BorderLayout());
        alignWrapper.add(gridPanel, BorderLayout.WEST);

        add(alignWrapper, BorderLayout.CENTER);

        // ------------------------------------------------------------------
        // ΚΑΤΩ PANEL (Κουμπιά)
        // ------------------------------------------------------------------
        final JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.setPreferredSize(new Dimension(10, 70));

        final JButton confirmButton = new JButton("Επιβεβαίωση");
        final JButton backButton = new JButton("Επιστροφή");

        final Font buttonFont = new Font("SansSerif", Font.BOLD, 20);
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
        final String code = codeField.getText().trim();
        final String amountStr = amountField.getText().trim();
        final String justification = justificationField.getText().trim();

        if (code.isEmpty() || amountStr.isEmpty() || justification.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Συμπληρώστε όλα τα πεδία.",
                    "Σφάλμα Εισόδου",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            final String result = manager.makeAbsoluteChange(code,
                    amountStr, justification);

            JOptionPane.showMessageDialog(this,
                    result,
                    "Επιτυχής Αλλαγή",
                    JOptionPane.INFORMATION_MESSAGE);

            // Καθαρισμός πεδίων
            codeField.setText("");
            amountField.setText("");
            justificationField.setText("");

        } catch (final AppException e) {
            JOptionPane.showMessageDialog(this,
                    "Αποτυχία Αλλαγής Ποσού: " + e.getMessage(),
                    "Σφάλμα Λογικής Εφαρμογής",
                    JOptionPane.ERROR_MESSAGE);
        } catch (final Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Μη αναμενόμενο Σφάλμα: " + e.getMessage(),
                    "Γενικό Σφάλμα",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
