package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;

/**
 * Panel responsible for displaying second-level revenue analysis based
 * on a previously selected two-digit revenue code.
 */
public final class Revenue2Panel extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Table displaying detailed revenue data. */
    private JTable revenueTable;

    /** Text field for entering a three-digit revenue code. */
    private JTextField codeField;

    /** Button that reveals code input field. */
    private JButton openCodeInputButton;

    /** Confirmation button. */
    private JButton confirmButton;

    /** Return button. */
    private JButton backButton;

    /** Reference to main frame for panel switching. */
    private final MainFrame frame;

    /** The two-digit code selected from the previous panel. */
    private final String parentCode;

    /** Data manager for revenue codes. */
    private final RevenueDataManager revdata;

    /**
     * Constructs the Revenue2Panel.
     *
     * @param mainFrame the application's main frame used for switching panels
     * @param code      the two-digit revenue code selected in the previous panel
     */
    public Revenue2Panel(final MainFrame mainFrame, final String code) {
        this.frame = mainFrame;
        this.parentCode = code;
        this.revdata = new RevenueDataManager();

        setLayout(new BorderLayout());
        initializeTable();
        initializeCenter();
        initializeBottomButtons();
    }

    /**
     * Initializes the revenue details table.
     * Table content will be filled by RevenueDataManager.
     */
    private void initializeTable() {
        final JPanel centerPanel = new JPanel(new FlowLayout(
                FlowLayout.CENTER));
        final String[][] tableData = revdata.get3DigitCodes(parentCode);
        final String[] columnNames = {"Κωδικός", "Κατηγορία", "Ποσό"};

        revenueTable = new JTable(tableData, columnNames);
        // Μεγαλύτερη γραμματοσειρά για τα κελιά
        revenueTable.setFont(new Font("Arial", Font.PLAIN, 16));
        // Μεγαλύτερο ύψος γραμμής
        revenueTable.setRowHeight(24);

        // Μεγαλύτερη γραμματοσειρά για τις επικεφαλίδες
        final JTableHeader header = revenueTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 18));

        final JScrollPane scrollPane = new JScrollPane(revenueTable);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        centerPanel.add(scrollPane);
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Initializes the center panel containing input controls.
     * Includes button for enabling input and hidden text field.
     */
    private void initializeCenter() {
        final JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(
                10, 10, 10, 10));

        openCodeInputButton = new JButton(
                "Εισάγετε 3ψήφιο κωδικό προς ανάλυση");
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
     * Initializes the bottom navigation and confirmation buttons.
     */
    private void initializeBottomButtons() {
        final JPanel bottomPanel = new JPanel(new GridLayout(
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
                frame.switchTo("revenuePanel");
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (codeField.isVisible()
                        && !codeField.getText().trim().isEmpty()) {
                    try {
                        final String code = getCode3();
                        revdata.validateUserInput(parentCode, code, 3);
                        frame.showRevenue3(code);
                    } catch (final AppException e) {
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
     * Returns the trimmed value of the entered three-digit code.
     *
     * @return the entered code
     */
    public String getCode3() {
        return codeField.getText().trim();
    }
}
