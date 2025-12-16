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
 * Panel responsible for displaying third-level revenue analysis
 * based on a previously selected three-digit revenue code.
 */
public final  class Revenue3Panel extends JPanel {

    /** Table displaying detailed revenue data (currently placeholder). */
    private JTable revenueTable;

    /** Text field for entering a four-digit revenue code. */
    private JTextField codeField;

    /** Button that reveals code input field. */
    private JButton openCodeInputButton;

    /** Confirmation button. */
    private JButton confirmButton;

    /** Return button. */
    private JButton backButton;

    /** Reference to main frame for panel switching. */
    private final MainFrame frame;

    /** The three-digit code selected from the previous panel. */
    private final String parentCode;

    RevenueDataManager revdata;

    /**
     * Constructs the Revenue3Panel.
     *
     * @param mainFrame the application's main frame used for switching panels
     * @param code the three-digit revenue code selected in the previous panel
     */
    public Revenue3Panel(final MainFrame mainFrame, final String code) {
        this.frame = mainFrame;
        this.parentCode = code;
        setLayout(new BorderLayout());
        initializeTablePanel();
        initializeCenterPanel();
        initializeBottomPanel();
    }

    /**
     * Initializes the table panel containing the revenue analysis table.
     */
    private void initializeTablePanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        revdata = new RevenueDataManager();
        String[][] emptyData = revdata.get5DigitCodes(parentCode);
        String[] columnNames = { "Κωδικός", "Κατηγορία", "Ποσό" };

        revenueTable = new JTable(emptyData, columnNames);
        JScrollPane scrollPane = new JScrollPane(revenueTable);
        topPanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.CENTER);
    }

    /**
     * Initializes the center panel containing the input controls.
     */
    private void initializeCenterPanel() {
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        openCodeInputButton = new JButton(
            "Εισάγετε 5ψήφιο κωδικό προς ανάλυση");
        codeField = new JTextField();
        codeField.setVisible(false);

        openCodeInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                codeField.setVisible(true);
                topPanel.revalidate();
                topPanel.repaint();
            }
        });

        topPanel.add(openCodeInputButton);
        topPanel.add(codeField);

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Initializes the bottom panel with navigation and confirmation buttons.
     */
    private void initializeBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(
            1, 2, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(
            10, 10, 10, 10));
        bottomPanel.setPreferredSize(new Dimension(200, 70));

        confirmButton = new JButton("Επιβεβαίωση");
        frame.confButtonColors(confirmButton);
        backButton = new JButton("Επιστροφή");
        frame.backButtonColors(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                frame.switchTo("revenue2panel");
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if
                (codeField.isVisible()
                 && !codeField.getText().trim().isEmpty()) {
                    try {
                        revdata.validateUserInput(
                            parentCode, getCode(), 5);
                        frame.showRevenue4(getCode());
                    } catch (AppException e) {
                        AppException.showError(e.getMessage());
                    }
                } else {
                    AppException.showError(
                        "Πληκτρολογήστε κωδικό ή πατήστε Επιστροφή.");
                }
            }
        });

        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Returns the trimmed value of the entered four-digit code.
     *
     * @return the entered code
     */
    public String getCode() {
        return codeField.getText().trim();
    }
}
