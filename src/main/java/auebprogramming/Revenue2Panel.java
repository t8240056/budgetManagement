package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
 * Panel responsible for displaying second-level revenue analysis based
 * on a previously selected two-digit revenue code.
 */
public class Revenue2Panel extends JPanel {

    /** Table displaying detailed revenue data (currently placeholder). */
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
    private MainFrame frame;

    /** The two-digit code selected from the previous panel. */
    private String parentCode;

    /**
     * Constructs the Revenue2Panel.
     *
     * @param mainFrame the application's main frame used for switching panels
     * @param code the two-digit revenue code selected in the previous panel
     */
    public Revenue2Panel(final MainFrame mainFrame, final String code) {
        this.frame = mainFrame;
        this.parentCode = code;
        setLayout(new BorderLayout());
        initializeTable();
        initializeCenter();
        initializeBottomButtons();
    }

    /**
     * Initializes the revenue details table.
     * Table content will be filled later by collaborators.
     */
    private void initializeTable() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        String[][] emptyData = { {"-", "-"} };
        String[] columnNames = { "Κωδικός", "Ποσό" };

        revenueTable = new JTable(emptyData, columnNames);
        JScrollPane scrollPane = new JScrollPane(revenueTable);
        scrollPane.setPreferredSize(new Dimension(400, 500));
        topPanel.add(scrollPane);
        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Initializes the center panel containing input controls.
     * Includes button for enabling input and hidden text field.
     */
    private void initializeCenter() {
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        openCodeInputButton = new JButton("Εισάγετε 3ψήφιο κωδικό προς ανάλυση");
        codeField = new JTextField();
        codeField.setVisible(false);

        openCodeInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                codeField.setVisible(true);
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });

        centerPanel.add(openCodeInputButton);
        centerPanel.add(codeField);

        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Initializes the bottom navigation and confirmation buttons.
     */
    private void initializeBottomButtons() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        confirmButton = new JButton("Επιβεβαίωση");
        backButton = new JButton("Επιστροφή");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                frame.switchTo("revenue");
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (codeField.isVisible() && !codeField.getText().trim().isEmpty()) {
                    frame.switchTo("revenue3details");
                } else {
                    AppException.showError("Πληκτρολογήστε κωδικό ή πατήστε Επιστροφή.");
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
    public String getCode() {
        return codeField.getText().trim();
    }
}
