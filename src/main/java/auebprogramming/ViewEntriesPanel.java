package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Panel that displays the budget entries returned by the BudgetManager.
 */
public final class ViewEntriesPanel extends JPanel {

    /** Table column names. */
    private static final String[] COLUMN_NAMES = {
        "Code", "Description", "Amount (€)"
    };

    /** Layout constants. */
    private static final int TABLE_COLUMNS = 3;
    private static final int BOTTOM_ROWS = 1;
    private static final int BOTTOM_COLUMNS = 1;
    private static final int PANEL_HEIGHT = 70;
    private static final int BORDER_PADDING = 10;

    /** Reference to the main application frame. */
    private final MainFrame mainFrame;

    /** Reference to the budget manager backend. */
    private final BudgetManager manager;

    /**
     * Constructs the panel that displays budget entries.
     *
     * @param frame the main application frame
     * @param manager the budget manager backend instance
     */
    public ViewEntriesPanel(final MainFrame frame,
                            final BudgetManager manager) {
        this.mainFrame = frame;
        this.manager = manager;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(
            BORDER_PADDING, BORDER_PADDING,
            BORDER_PADDING, BORDER_PADDING));

        add(createTablePanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates the panel containing the entries table.
     *
     * @return the table panel
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        List<BudgetChangesEntry> entries =
            manager.getEntriesList();

        Object[][] data =
            new Object[entries.size()][TABLE_COLUMNS];

        for (int i = 0; i < entries.size(); i++) {
            BudgetChangesEntry entry = entries.get(i);
            data[i][0] = entry.getCode();
            data[i][1] = entry.getDescription();
            data[i][2] = entry.getAmount();
        }

        JTable table = new JTable(data, COLUMN_NAMES);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the bottom panel containing the back button.
     *
     * @return the bottom panel
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(
            new GridLayout(BOTTOM_ROWS, BOTTOM_COLUMNS));
        bottomPanel.setPreferredSize(
            new Dimension(0, PANEL_HEIGHT));

        JButton backButton = new JButton("Επιστροφή");
        mainFrame.backButtonColors(backButton);

        backButton.addActionListener(event ->
            mainFrame.switchTo("changesMenu")
        );

        bottomPanel.add(backButton);
        return bottomPanel;
    }
}
