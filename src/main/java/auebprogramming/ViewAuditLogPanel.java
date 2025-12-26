package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * Panel responsible for displaying the audit log history.
 */
public final class ViewAuditLogPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int TITLE_FONT_SIZE = 20;
    private static final int TABLE_ROW_HEIGHT = 24;
    private static final int BOTTOM_PANEL_HEIGHT = 70;
    private static final int TEN = 10;

    private final MainFrame frame;
    private final BudgetManager budgetManager;

    /**
     * Constructs the ViewAuditLogPanel.
     *
     * @param frame         the main application frame
     * @param budgetManager the budget manager
     */
    public ViewAuditLogPanel(final MainFrame frame,
                             final BudgetManager budgetManager) {

        this.frame = frame;
        this.budgetManager = budgetManager;

        setLayout(new BorderLayout());

        add(createTitleLabel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates the title label.
     *
     * @return the formatted title label
     */
    private JLabel createTitleLabel() {
        final JLabel titleLabel =
                new JLabel("Ιστορικό Ενεργειών", SwingConstants.CENTER);
        titleLabel.setFont(
                new Font("SansSerif", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(TEN, TEN, TEN, TEN));
        return titleLabel;
    }

    /**
     * Creates the table panel with the audit log.
     *
     * @return the scroll pane containing the table
     */
    private JScrollPane createTablePanel() {
        final List<String> logs = budgetManager.getAuditLog();

        final Object[][] tableData = new Object[logs.size()][1];
        for (int i = 0; i < logs.size(); i++) {
            tableData[i][0] = logs.get(i);
        }

        final String[] columnNames = {"Ενέργεια"};

        final JTable table = new JTable(tableData, columnNames);
        table.setEnabled(false);
        table.setRowHeight(TABLE_ROW_HEIGHT);

        return new JScrollPane(table);
    }

    /**
     * Creates the bottom navigation panel.
     *
     * @return the panel containing buttons
     */
    private JPanel createBottomPanel() {
        final JPanel bottomPanel =
                new JPanel(new GridLayout(1, 1, TEN, TEN));
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(TEN, TEN, TEN, TEN));
        bottomPanel.setPreferredSize(
                new Dimension(200, BOTTOM_PANEL_HEIGHT));

        final JButton backButton = new JButton("Επιστροφή");
        frame.backButtonColors(backButton);

        backButton.addActionListener((final ActionEvent e) ->
                frame.switchTo("changesMenu"));

        bottomPanel.add(backButton);
        return bottomPanel;
    }
}
