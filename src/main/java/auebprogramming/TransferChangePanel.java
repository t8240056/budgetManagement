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

public final class TransferChangePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final MainFrame mainFrame;
    private final BudgetManager manager;

    private final JTextField sourceCodeField;
    private final JTextField targetCodeField;
    private final JTextField amountField;
    private final JTextField justificationField;

    // === Οπτικές σταθερές όπως πριν ===
    private static final int INPUT_FONT_SIZE = 22;
    private static final Font INPUT_FONT =
            new Font("SansSerif", Font.PLAIN, INPUT_FONT_SIZE);

    private static final int LEFT_EDGE_PADDING = 10;
    private static final int IN_BETWEEN_GAP = 5;

    // Θα υπολογίσουμε συμμετρικά insets για τέλεια κατανομή
    private static final int TOP_PADDING = 20;
    private static final int BOTTOM_PADDING = 20;

    // Κουμπιά
    private static final int BOTTOM_ROWS = 1;
    private static final int TWO = 2;
    private static final int TEN = 10;
    private static final int BOTTOM_PANEL_WIDTH = 200;
    private static final int BOTTOM_PANEL_HEIGHT = 70;

    public TransferChangePanel(final MainFrame frame,
                               final BudgetManager manager) {
        this.mainFrame = frame;
        this.manager = manager;

        // ---- Αρχικοποίηση των final πεδίων ----
        sourceCodeField = new JTextField();
        targetCodeField = new JTextField();
        amountField = new JTextField();
        justificationField = new JTextField();

        // ---- Layout κύριου panel ----
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        // ---- Κεντρική φόρμα με GridBagLayout ----
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 0.0;

        // Υπολογίζουμε συμμετρικές αποστάσεις
        int totalHeight = 300; // συνολικό διαθέσιμο ύψος για τα 4 πεδία
        int interval = totalHeight / 3; // 3 ίσα διαστήματα μεταξύ 4 πεδίων

        // 1. Κωδικός πηγής
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        gbc.insets = new Insets(TOP_PADDING, LEFT_EDGE_PADDING, 0, IN_BETWEEN_GAP);
        JLabel sourceLabel = new JLabel("Πληκτρολογήστε κωδικό πηγής:", SwingConstants.LEFT);
        sourceLabel.setFont(INPUT_FONT);
        formPanel.add(sourceLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(TOP_PADDING, IN_BETWEEN_GAP, 0, LEFT_EDGE_PADDING);
        sourceCodeField.setFont(INPUT_FONT);
        sourceCodeField.setPreferredSize(new Dimension(250, INPUT_FONT_SIZE+10));
        formPanel.add(sourceCodeField, gbc);

        // 2. Κωδικός προορισμού
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        gbc.insets = new Insets(interval, LEFT_EDGE_PADDING, 0, IN_BETWEEN_GAP);
        JLabel targetLabel = new JLabel("Πληκτρολογήστε κωδικό προορισμού:", SwingConstants.LEFT);
        targetLabel.setFont(INPUT_FONT);
        formPanel.add(targetLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(interval, IN_BETWEEN_GAP, 0, LEFT_EDGE_PADDING);
        targetCodeField.setFont(INPUT_FONT);
        targetCodeField.setPreferredSize(new Dimension(250, INPUT_FONT_SIZE+10));
        formPanel.add(targetCodeField, gbc);

        // 3. Ποσό μεταφοράς
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        gbc.insets = new Insets(interval, LEFT_EDGE_PADDING, 0, IN_BETWEEN_GAP);
        JLabel amountLabel = new JLabel("Πληκτρολογήστε ποσό μεταφοράς:", SwingConstants.LEFT);
        amountLabel.setFont(INPUT_FONT);
        formPanel.add(amountLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(interval, IN_BETWEEN_GAP, 0, LEFT_EDGE_PADDING);
        amountField.setFont(INPUT_FONT);
        amountField.setPreferredSize(new Dimension(250, INPUT_FONT_SIZE+10));
        formPanel.add(amountField, gbc);

        // 4. Αιτιολόγηση
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        gbc.insets = new Insets(interval, LEFT_EDGE_PADDING, BOTTOM_PADDING, IN_BETWEEN_GAP);
        JLabel justificationLabel = new JLabel("Αιτιολογία:", SwingConstants.LEFT);
        justificationLabel.setFont(INPUT_FONT);
        formPanel.add(justificationLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(interval, IN_BETWEEN_GAP, BOTTOM_PADDING, LEFT_EDGE_PADDING);
        justificationField.setFont(INPUT_FONT);
        justificationField.setPreferredSize(new Dimension(250, INPUT_FONT_SIZE+10));
        formPanel.add(justificationField, gbc);

        JPanel paddingContainer = new JPanel(new BorderLayout());
        paddingContainer.add(formPanel, BorderLayout.NORTH);
        paddingContainer.setBorder(new EmptyBorder(0,0,0,0));
        add(paddingContainer, BorderLayout.CENTER);

        // ---- Κουμπιά ----
        JPanel bottomPanel = new JPanel(new GridLayout(BOTTOM_ROWS, TWO, TEN, TEN));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(TEN, TEN, TEN, TEN));
        bottomPanel.setPreferredSize(new Dimension(BOTTOM_PANEL_WIDTH, BOTTOM_PANEL_HEIGHT));

        JButton confirmButton = new JButton("ΕΠΙΒΕΒΑΙΩΣΗ");
        JButton backButton = new JButton("ΕΠΙΣΤΡΟΦΗ");

        mainFrame.confButtonColors(confirmButton);
        mainFrame.backButtonColors(backButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleTransfer();
            }
        });

        backButton.addActionListener(e -> mainFrame.switchTo("changesMenu"));

        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleTransfer() {
        final String sourceCode = sourceCodeField.getText().trim();
        final String targetCode = targetCodeField.getText().trim();
        final String amountStr = amountField.getText().trim();
        final String justification = justificationField.getText().trim();

        try {
            final String result = manager.makeTransfer(sourceCode, targetCode, amountStr, justification);

            JOptionPane.showMessageDialog(
                    mainFrame,
                    result,
                    "Επιτυχία Μεταφοράς",
                    JOptionPane.INFORMATION_MESSAGE
            );

            sourceCodeField.setText("");
            targetCodeField.setText("");
            amountField.setText("");
            justificationField.setText("");

        } catch (final AppException ex) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    ex.getMessage(),
                    "Σφάλμα Μεταφοράς",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
