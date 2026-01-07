package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

    /** The main application frame. */
    private final MainFrame mainFrame;
    /** The budget manager instance. */
    private final BudgetManager manager;

    // Input Fields
    /** Field for entering the source code. */
    private JTextField codeField;
    /** Field for entering the amount. */
    private JTextField amountField;
    /** Field for entering the justification. */
    private JTextField justificationField;

    // === Visual Constants ===
    private static final int INPUT_FONT_SIZE = 22;
    private static final Font INPUT_FONT = new Font("SansSerif", Font.PLAIN, INPUT_FONT_SIZE);
    
    private static final int BUTTON_FONT_SIZE = 20;
    /** Font for buttons - Set to BOLD and size 20 to match PercentageChangePanel. */
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, BUTTON_FONT_SIZE);

    private static final int LEFT_EDGE_PADDING = 10;
    private static final int IN_BETWEEN_GAP = 5;

    private static final int TOP_ROW_1 = 20;
    private static final int TOP_ROW_2 = 120;
    private static final int TOP_ROW_3 = 220;
    
    private static final int FIELD_WIDTH = 250;
    private static final int FIELD_HEIGHT_OFFSET = 10;
    private static final int BOTTOM_PANEL_HEIGHT = 70;

    /**
     * Constructor for AbsoluteChangePanel.
     *
     * @param frame   the main application frame
     * @param manager the BudgetManager instance
     */
    public AbsoluteChangePanel(final MainFrame frame, final BudgetManager manager) {
        this.mainFrame = frame;
        this.manager = manager;

        setLayout(new BorderLayout(LEFT_EDGE_PADDING, LEFT_EDGE_PADDING));

        final JPanel formPanel = createFormPanel();
        final JPanel paddingContainer = new JPanel(new BorderLayout());
        paddingContainer.add(formPanel, BorderLayout.NORTH);
        paddingContainer.setBorder(new EmptyBorder(0, 0, 0, 0));

        add(paddingContainer, BorderLayout.CENTER);

        final JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the form panel with input fields for code, amount, and justification.
     *
     * @return the configured form panel
     */
    private JPanel createFormPanel() {
        final JPanel formPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 0.0;

        // ===== 1. ΚΩΔΙΚΟΣ ΠΗΓΗΣ =====
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(TOP_ROW_1, LEFT_EDGE_PADDING, 0, IN_BETWEEN_GAP);

        final JLabel codeLabel = new JLabel("Πληκτρολογήστε κωδικό πηγής:", SwingConstants.LEFT);
        codeLabel.setFont(INPUT_FONT);
        formPanel.add(codeLabel, gbc);

        codeField = new JTextField();
        codeField.setFont(INPUT_FONT);
        codeField.setPreferredSize(new Dimension(FIELD_WIDTH, INPUT_FONT_SIZE + FIELD_HEIGHT_OFFSET));

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

        final JLabel amountLabel = new JLabel("Πληκτρολογήστε ποσό αλλαγής:", SwingConstants.LEFT);
        amountLabel.setFont(INPUT_FONT);
        formPanel.add(amountLabel, gbc);

        amountField = new JTextField();
        amountField.setFont(INPUT_FONT);
        amountField.setPreferredSize(new Dimension(FIELD_WIDTH, INPUT_FONT_SIZE + FIELD_HEIGHT_OFFSET));

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

        final JLabel justificationLabel = new JLabel("Αιτιολογία:", SwingConstants.LEFT);
        justificationLabel.setFont(INPUT_FONT);
        formPanel.add(justificationLabel, gbc);

        justificationField = new JTextField();
        justificationField.setFont(INPUT_FONT);
        justificationField.setPreferredSize(new Dimension(FIELD_WIDTH, INPUT_FONT_SIZE + FIELD_HEIGHT_OFFSET));

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(TOP_ROW_3, LEFT_EDGE_PADDING, 0, IN_BETWEEN_GAP);
        formPanel.add(justificationField, gbc);

        return formPanel;
    }

    /**
     * Creates the bottom panel with confirmation and back buttons.
     *
     * @return the configured bottom panel
     */
    private JPanel createBottomPanel() {
        final JPanel bottomPanel = new JPanel(new GridLayout(1, 2, LEFT_EDGE_PADDING, LEFT_EDGE_PADDING));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(LEFT_EDGE_PADDING, LEFT_EDGE_PADDING, 
                LEFT_EDGE_PADDING, LEFT_EDGE_PADDING));
        bottomPanel.setPreferredSize(new Dimension(LEFT_EDGE_PADDING, BOTTOM_PANEL_HEIGHT));

        final JButton confirmButton = new JButton("Επιβεβαίωση");
        final JButton backButton = new JButton("Επιστροφή");
        
        mainFrame.confButtonColors(confirmButton);
        mainFrame.backButtonColors(backButton);

        confirmButton.setFont(BUTTON_FONT);
        backButton.setFont(BUTTON_FONT);

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

        return bottomPanel;
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
            final String result = manager.makeAbsoluteChange(code, amountStr, justification);

            JOptionPane.showMessageDialog(this,
                    result,
                    "Επιτυχής Αλλαγή",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear fields
            codeField.setText("");
            amountField.setText("");
            justificationField.setText("");

        } catch (AppException e) {
            JOptionPane.showMessageDialog(this,
                    "Αποτυχία Αλλαγής Ποσού: " + e.getMessage(),
                    "Σφάλμα Λογικής Εφαρμογής",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
