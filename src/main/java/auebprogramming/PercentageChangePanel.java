package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Font;
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

public final class PercentageChangePanel extends JPanel {

    private final MainFrame mainFrame;
    private final BudgetManager manager;

    private JTextField codeField;
    private JTextField percentageField;
    private JTextField justificationField;

    private static final int INPUT_FONT_SIZE = 22;
    private static final Font INPUT_FONT =
            new Font("SansSerif", Font.PLAIN, INPUT_FONT_SIZE);

    // Οριζόντια κενά
    private static final int LEFT_EDGE_PADDING = 10;
    private static final int IN_BETWEEN_GAP = 5;

    // Κάθετα ύψη γραμμών
    private static final int TOP_ROW_1 = 20;
    private static final int TOP_ROW_2 = 120;
    private static final int TOP_ROW_3 = 220;

    // Κουμπιά (όπως αρχικά)
    private static final int BOTTOM_ROWS = 1;
    private static final int TWO = 2;
    private static final int TEN = 10;
    private static final int BOTTOM_PANEL_WIDTH = 200;
    private static final int BOTTOM_PANEL_HEIGHT = 70;

    public PercentageChangePanel(final MainFrame mainFrame,
                                 final BudgetManager manager) {
        this.mainFrame = mainFrame;
        this.manager = manager;

        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 0.0;

        // ===== 1. ΚΩΔΙΚΟΣ ΕΓΓΡΑΦΗΣ =====

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(TOP_ROW_1,
                                LEFT_EDGE_PADDING,
                                0,
                                IN_BETWEEN_GAP);

        JLabel codeLabel =
                new JLabel("Πληκτρολογήστε κωδικό εγγραφής:",
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
        gbc.insets = new Insets(TOP_ROW_1,
                                IN_BETWEEN_GAP,
                                0,
                                LEFT_EDGE_PADDING);
        formPanel.add(codeField, gbc);

        // ===== 2. ΠΟΣΟΣΤΟ =====

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(TOP_ROW_2,
                                LEFT_EDGE_PADDING,
                                0,
                                IN_BETWEEN_GAP);

        JLabel percentageLabel =
                new JLabel("Πληκτρολογήστε ποσοστό (%):",
                           SwingConstants.LEFT);
        percentageLabel.setFont(INPUT_FONT);
        formPanel.add(percentageLabel, gbc);

        percentageField = new JTextField();
        percentageField.setFont(INPUT_FONT);
        percentageField.setPreferredSize(
                new Dimension(250, INPUT_FONT_SIZE + 10));

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(TOP_ROW_2,
                                IN_BETWEEN_GAP,
                                0,
                                LEFT_EDGE_PADDING);
        formPanel.add(percentageField, gbc);

        // ===== 3. ΑΙΤΙΟΛΟΓΗΣΗ =====

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(TOP_ROW_3,
                                LEFT_EDGE_PADDING,
                                0,
                                IN_BETWEEN_GAP);

        JLabel justificationLabel =
                new JLabel("Αιτιολόγηση:",
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
        gbc.insets = new Insets(TOP_ROW_3,
                                IN_BETWEEN_GAP,
                                0,
                                LEFT_EDGE_PADDING);
        formPanel.add(justificationField, gbc);

        JPanel paddingContainer = new JPanel(new BorderLayout());
        paddingContainer.add(formPanel, BorderLayout.NORTH);
        paddingContainer.setBorder(new EmptyBorder(0, 0, 0, 0));

        add(paddingContainer, BorderLayout.CENTER);

        // ===== ΚΟΥΜΠΙΑ (ΑΡΧΙΚΗ ΜΟΡΦΗ) =====

        JPanel bottomPanel =
                new JPanel(new GridLayout(BOTTOM_ROWS, TWO, TEN, TEN));
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(TEN, TEN, TEN, TEN));
        bottomPanel.setPreferredSize(
                new Dimension(BOTTOM_PANEL_WIDTH, BOTTOM_PANEL_HEIGHT));

        JButton confirmButton = new JButton("ΕΠΙΒΕΒΑΙΩΣΗ");
        mainFrame.confButtonColors(confirmButton);

        JButton backButton = new JButton("ΕΠΙΣΤΡΟΦΗ");
        mainFrame.backButtonColors(backButton);

        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);

        confirmButton.addActionListener(new ConfirmButtonListener());
        backButton.addActionListener(
                e -> mainFrame.switchTo("changesMenu"));
    }

    private class ConfirmButtonListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent event) {
            String code = codeField.getText().trim();
            String percentStr = percentageField.getText().trim();
            String justification =
                    justificationField.getText().trim();

            try {
                if (code.isEmpty()
                        || percentStr.isEmpty()
                        || justification.isEmpty()) {
                    throw new AppException(
                            "Παρακαλώ συμπληρώστε όλα τα πεδία.");
                }

                String successMessage =
                        manager.makePercentageChange(
                                code, percentStr, justification);

                JOptionPane.showMessageDialog(
                        mainFrame,
                        successMessage,
                        "Επιτυχής Αλλαγή",
                        JOptionPane.INFORMATION_MESSAGE);

                codeField.setText("");
                percentageField.setText("");
                justificationField.setText("");

            } catch (AppException ex) {
                AppException.showError(ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "Προέκυψε μη αναμενόμενο σφάλμα: "
                                + ex.getMessage(),
                        "Γενικό Σφάλμα",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
