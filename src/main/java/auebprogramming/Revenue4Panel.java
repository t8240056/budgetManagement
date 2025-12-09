package auebprogramming;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Panel responsible for displaying fourth-level revenue analysis,
 * based on a previously selected five-digit revenue code.
 * There is no further analysis beyond this level.
 */
public final class Revenue4Panel extends JPanel {

    /** Table displaying final-level revenue data. */
    private JTable revenueTable;

    /** Return button. */
    private JButton backButton;

    /** Reference to main frame for switching panels. */
    private MainFrame frame;

    /** The selected five-digit revenue code. */
    private String parentCode;

    /**
     * Constructs the Revenue4Panel.
     *
     * @param mainFrame reference to main application frame
     * @param code the selected five-digit revenue code
     */
    public Revenue4Panel(final MainFrame mainFrame, final String code) {
        this.frame = mainFrame;
        this.parentCode = code;

        setLayout(new BorderLayout());

        initializeTablePanel();
        initializeBottomPanel();
    }

    /**
     * Initializes the panel that displays the final-level revenue table.
     */
    private void initializeTablePanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        RevenueDataManager revdata = new RevenueDataManager();

        // Fetch final-level data (method name can be adjusted to your implementation)
        String[][] tableData = revdata.get7DigitCodes(parentCode);
        String[] columnNames = { "Κωδικός", "Κατηγορία", "Ποσό" };

        revenueTable = new JTable(tableData, columnNames);
        JScrollPane scrollPane = new JScrollPane(revenueTable);

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Initializes the bottom panel containing only the return button.
     */
    private void initializeBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        backButton = new JButton("Επιστροφή");
        frame.backButtonColors(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                frame.switchTo("revenue3panel");
            }
        });

        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
