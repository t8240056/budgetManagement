package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

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
    // === ΟΠΤΙΚΕΣ ΣΤΑΘΕΡΕΣ (ΙΔΙΕΣ ΜΕ PercentageChangePanel) ===
    private static final int INPUT_FONT_SIZE = 22;
    private static final Font INPUT_FONT =
            new Font("SansSerif", Font.PLAIN, INPUT_FONT_SIZE);

    private static final int LEFT_EDGE_PADDING = 10;
    private static final int IN_BETWEEN_GAP = 5;

    private static final int TOP_ROW_1 = 20;
    private static final int TOP_ROW_2 = 120;
    private static final int TOP_ROW_3 = 220;

    private static final int BOTTOM_ROWS = 1;
    private static final int TWO = 2;
    private static final int TEN = 10;
    private static final int BOTTOM_PANEL_WIDTH = 200;
    private static final int BOTTOM_PANEL_HEIGHT = 70;

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
        setLayout(new BorderLayout());
        
        // -------- ΚΕΝΤΡΙΚΗ ΦΟΡΜΑ --------
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 0.0;

        // ===== 1. ΚΩΔΙΚΟΣ ΦΟΡΕΑ =====
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(TOP_ROW_1, LEFT_EDGE_PADDING, 0, IN_BETWEEN_GAP);

        JLabel codeLabel =
                new JLabel("Πληκτρολογήστε κωδικό φορέα:",
                           SwingConstants.LEFT);
        codeLabel.setFont(INPUT_FONT);
        formPanel.add(codeLabel, gbc);

        codeField = new JTextField();
        codeField.setFont(INPUT_FONT);
        codeField.setPreferredSize(
                new Dimension(250, INPUT_FONT_SIZE + 10));

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(TOP_ROW_1, IN_BETWEEN_GAP, 0, LEFT_EDGE_PADDING);
        formPanel.add(codeField, gbc);

        // ===== 2. ΠΟΣΟ ΑΛΛΑΓΗΣ =====
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(TOP_ROW_2, LEFT_EDGE_PADDING, 0, IN_BETWEEN_GAP);

        JLabel amountLabel =
                new JLabel("Πληκτρολογήστε ποσό αλλαγής:",
                           SwingConstants.LEFT);
        amountLabel.setFont(INPUT_FONT);
        formPanel.add(amountLabel, gbc);

        amountField = new JTextField();
        amountField.setFont(INPUT_FONT);
        amountField.setPreferredSize(
                new Dimension(250, INPUT_FONT_SIZE + 10));

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(TOP_ROW_2, IN_BETWEEN_GAP, 0, LEFT_EDGE_PADDING);
        formPanel.add(amountField, gbc);

        // ===== 3. ΑΙΤΙΟΛΟΓΗΣΗ =====
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(TOP_ROW_3, LEFT_EDGE_PADDING, 0, IN_BETWEEN_GAP);

        JLabel justificationLabel =
                new JLabel("Αιτιολογία:",
                           SwingConstants.LEFT);
        justificationLabel.setFont(INPUT_FONT);
        formPanel.add(justificationLabel, gbc);

        justificationField = new JTextField();
        justificationField.setFont(INPUT_FONT);
        justificationField.setPreferredSize(
                new Dimension(250, INPUT_FONT_SIZE + 10));

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(TOP_ROW_3, IN_BETWEEN_GAP, 0, LEFT_EDGE_PADDING);
        formPanel.add(justificationField, gbc);

        JPanel paddingContainer = new JPanel(new BorderLayout());
        paddingContainer.add(formPanel, BorderLayout.NORTH);
        paddingContainer.setBorder(new EmptyBorder(0, 0, 0, 0));

        add(paddingContainer, BorderLayout.CENTER);

        // -------- ΚΟΥΜΠΙΑ (ΙΔΙΑ ΑΚΡΙΒΩΣ) --------
        JPanel bottomPanel =
                new JPanel(new GridLayout(BOTTOM_ROWS, TWO, TEN, TEN));
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(TEN, TEN, TEN, TEN));
        bottomPanel.setPreferredSize(
                new Dimension(BOTTOM_PANEL_WIDTH, BOTTOM_PANEL_HEIGHT));

        JButton confirmButton = new JButton("ΕΠΙΒΕΒΑΙΩΣΗ");
        JButton backButton = new JButton("ΕΠΙΣΤΡΟΦΗ");

        mainFrame.confButtonColors(confirmButton);
        mainFrame.backButtonColors(backButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                handleConfirmation();
            }
        });

        backButton.addActionListener(e ->
                mainFrame.switchTo("changesMenu"));

        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the confirmation button action, calling the BudgetManager.
     */
    private void handleConfirmation() throws AppException, Exception {
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
            String result =
                    manager.makeAbsoluteChange(
                            code, amountStr, justification);

            JOptionPane.showMessageDialog(this,
                    result,
                    "Επιτυχής Αλλαγή",
                    JOptionPane.INFORMATION_MESSAGE);

            // Καθαρισμός πεδίων
            codeField.setText("");
            amountField.setText("");
            justificationField.setText("");

        } catch (final AppException e) {
        } catch (final Exception e) {
        }
    }
}
