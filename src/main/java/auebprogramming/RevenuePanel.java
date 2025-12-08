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
/**
 * Panel responsible for displaying revenue information.
 * This panel contains a table, a field for entering a revenue code,
 * and buttons for navigation and confirmation.
 */
public final class RevenuePanel extends JPanel {

    /** Table displaying revenue data (currently placeholder). */
    private JTable revenueTable;

    /** Text field for entering revenue code. */
    private JTextField codeField;

    /** Button that reveals code input field. */
    private JButton openCodeInputButton;

    /** Confirmation button. */
    private JButton confirmButton;

    /** Return button. */
    private JButton backButton;

    /** Reference to main frame for panel switching. */
    private final MainFrame frame;

    /**
     * Constructs the RevenuePanel.
     *
     * @param frame the application's main frame used for switching panels
     */
    public RevenuePanel(final MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        initializeTable();
        initializeCenter();
        initializeBottomButtons();
    }

    /**
     * Initializes the revenue table.
     * Table content will be filled later by collaborators.
     */
    private void initializeTable() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        RevenueDataManager revdata = new RevenueDataManager();
        String[][] Data = revdata.get2DigitCodes();
        String[] columnNames = { "Κωδικός", "Κατηγορία" ,"Ποσό"};

        revenueTable = new JTable(Data, columnNames);
        revenueTable.setFont(new Font("Arial", Font.PLAIN, 16));
        revenueTable.setRowHeight(22);

    // 🔹 Μεγαλύτερη γραμματοσειρά στην επικεφαλίδα
        revenueTable.getTableHeader().setFont(
        new Font("Arial", Font.BOLD, 16));
        JScrollPane scrollPane = new JScrollPane(revenueTable);
        scrollPane.setPreferredSize(new Dimension(550,335));
        topPanel.add(scrollPane);
        add(topPanel, BorderLayout.CENTER);
    }

    /**
     * Initializes the center area containing code entry controls.
     */
    private void initializeCenter() {
        JPanel centerPanel = new JPanel(new GridLayout(
            2, 1, 5, 5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(
            10, 10, 10, 10));

        openCodeInputButton = new JButton(
            "Εισάγετε 2ψηφίο κωδικό προς περαιτέρω ανάλυση");
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

        add(centerPanel, BorderLayout.NORTH);
    }

    /**
     * Initializes the bottom navigation buttons.
     */
    private void initializeBottomButtons() {
        JPanel bottomPanel =
        new JPanel(new GridLayout(1, 2, 10, 10));
        bottomPanel.setBorder(
            BorderFactory.createEmptyBorder(10, 10,
                 10, 10));

        backButton = new JButton("Επιστροφή");
        confirmButton = new JButton("Επιβεβαίωση");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                frame.switchTo("budget");
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (codeField.isVisible() && !codeField.getText().trim().isEmpty()){
                //try {
                    // Εδώ θα μπε η μέθοδος του Θανάση if (codeField.isVisible() && !codeField.getText().trim().isEmpty()) {
                    String code = getCode2();
                    frame.showRevenue2(code);
                    //}
                //} catch (AppException ex) {
                    //AppException.showError(ex.getMessage());
                //}
                } else {
                    AppException.showError(
                    "Πληκτρολογήστε κωδικό ή πατήστε Επιστροφή");
                }


            }
        });


        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    //*Method for getting the inputed code */
    public  String getCode2() {
        return codeField.getText().trim();
    }
}
