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
 * Panel for applying a percentage change to a budget entry.
 */
public final class PercentageChangePanel extends JPanel {

    /** The main application frame. */
    private final MainFrame mainFrame;
    /** The budget manager instance. */
    private final BudgetManager manager;

    /** Field for entering the source code. */
    private JTextField codeField;
    /** Field for entering the percentage value. */
    private JTextField percentageField;
    /** Field for entering the justification. */
    private JTextField justificationField;

    /** Font size for input labels and fields. */
    private static final int INPUT_FONT_SIZE = 22;
    /** Font for input labels and fields. */
    private static final Font INPUT_FONT =
            new Font("SansSerif", Font.PLAIN, INPUT_FONT_SIZE);

    /** Padding for the left edge. */
    private static final int LEFT_EDGE_PADDING = 10;
    /** Gap between components. */
    private static final int IN_BETWEEN_GAP = 5;

    /** Vertical position for the first row. */
    private static final int TOP_ROW_1 = 20;
    /** Vertical position for the second row. */
    private static final int TOP_ROW_2 = 120;
    /** Vertical position for the third row. */
    private static final int TOP_ROW_3 = 220;

    /** Number of rows in the bottom panel grid. */
    private static final int BOTTOM_ROWS = 1;
    /** Number of columns in the bottom panel grid. */
    private static final int TWO = 2;
    /** Padding value for the bottom panel. */
    private static final int TEN = 10;
    /** Width for the bottom panel. */
    private static final int BOTTOM_PANEL_WIDTH = 200;
    /** Height for the bottom panel. */
    private static final int BOTTOM_PANEL_HEIGHT = 70;
    /** Width for the text fields. */
    private static final int FIELD_WIDTH = 250;
    /** Height offset for text fields. */
    private static final int FIELD_HEIGHT_OFFSET = 10;

    /**
     * Constructor for PercentageChangePanel.
     * @param mainFrame the main application frame
     * @param manager the budget manager instance
     */
    public PercentageChangePanel(final MainFrame mainFrame,
                                 final BudgetManager manager) {
        this.mainFrame = mainFrame;
        this.manager = manager;

        setLayout(new BorderLayout());
        initializeComponents();
    }

    /**
     * Initializes the UI components of the panel.
     */
    private void initializeComponents() {
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

        // ===== 2. ΠΟΣΟΣΤΟ =====
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(TOP_ROW_2, LEFT_EDGE_PADDING, 0, IN_BETWEEN_GAP);

        final JLabel percentageLabel = new JLabel("Πληκτρολογήστε ποσοστό (%):", SwingConstants.LEFT);
        percentageLabel.setFont(INPUT_FONT);
        formPanel.add(percentageLabel, gbc);

        percentageField = new JTextField();
        percentageField.setFont(INPUT_FONT);
        percentageField.setPreferredSize(new Dimension(FIELD_WIDTH, INPUT_FONT_SIZE + FIELD_HEIGHT_OFFSET));

        gbc.gridx = 1;
        gbc.insets = new Insets(TOP_ROW_2, IN_BETWEEN_GAP, 0, LEFT_EDGE_PADDING);
        formPanel.add(percentageField, gbc);

        // ===== 3. ΑΙΤΙΟΛΟΓΗΣΗ =====
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(TOP_ROW_3, LEFT_EDGE_PADDING, 0, IN_BETWEEN_GAP);

        final JLabel justificationLabel = new JLabel("Αιτιολόγηση:", SwingConstants.LEFT);
        justificationLabel.setFont(INPUT_FONT);
        formPanel.add(justificationLabel, gbc);

        justificationField = new JTextField();
        justificationField.setFont(INPUT_FONT);
        justificationField.setPreferredSize(new Dimension(FIELD_WIDTH, INPUT_FONT_SIZE + FIELD_HEIGHT_OFFSET));

        gbc.gridx = 1;
        gbc.insets = new Insets(TOP_ROW_3, IN_BETWEEN_GAP, 0, LEFT_EDGE_PADDING);
        formPanel.add(justificationField, gbc);

        final JPanel paddingContainer = new JPanel(new BorderLayout());
        paddingContainer.add(formPanel, BorderLayout.NORTH);
        paddingContainer.setBorder(new EmptyBorder(0, 0, 0, 0));

        add(paddingContainer, BorderLayout.CENTER);

        // ===== ΚΟΥΜΠΙΑ =====
        final JPanel bottomPanel = new JPanel(new GridLayout(BOTTOM_ROWS, TWO, TEN, TEN));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(TEN, TEN, TEN, TEN));
        bottomPanel.setPreferredSize(new Dimension(BOTTOM_PANEL_WIDTH, BOTTOM_PANEL_HEIGHT));

        final JButton confirmButton = new JButton("Επιβεβαίωση");
        mainFrame.confButtonColors(confirmButton);

        final JButton backButton = new JButton("Επιστροφή");
        mainFrame.backButtonColors(backButton);

        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);

        confirmButton.addActionListener(new ConfirmButtonListener());
        backButton.addActionListener(e -> mainFrame.switchTo("changesMenu"));
    }

    /**
     * Listener for the confirmation button.
     */
    private class ConfirmButtonListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent event) {
            final String code = codeField.getText().trim();
            final String percentStr = percentageField.getText().trim();
            final String justification = justificationField.getText().trim();

            try {
                if (code.isEmpty() || percentStr.isEmpty() || justification.isEmpty()) {
                    throw new AppException("Παρακαλώ συμπληρώστε όλα τα πεδία.");
                }

                final String successMessage = manager.makePercentageChange(code, percentStr, justification);

                JOptionPane.showMessageDialog(mainFrame, successMessage, "Επιτυχής Αλλαγή",
                        JOptionPane.INFORMATION_MESSAGE);

                codeField.setText("");
                percentageField.setText("");
                justificationField.setText("");

            } catch (AppException ex) {
                AppException.showError(ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, 
                        "Προέκυψε μη αναμενόμενο σφάλμα: " + ex.getMessage(),
                        "Γενικό Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
