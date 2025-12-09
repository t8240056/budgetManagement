package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Panel for displaying detailed expense reports for selected categories.
 */
public final class ExpenseByCategory2Panel extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Padding for borders. */
    private static final int PADDING = 10;

    /** Rows in bottom panel. */
    private static final int GRID_ROWS = 1;

    /** Columns in bottom panel. */
    private static final int GRID_COLS = 1;

    /** Gap size in grid. */
    private static final int GRID_GAP = 10;

    /** Font size for text area. */
    private static final int FONT_SIZE = 15;

    /** Height for bottom panel. */
    private static final int BOTTOM_HEIGHT = 70;

    /** Main frame reference. */
    private final MainFrame frame;

    /** Display area for the details report. */
    private final JTextArea displayArea;

    /** Button to return to the previous panel. */
    private final JButton backButton;

    /**
     * Constructs an ExpenseByCategory2Panel.
     *
     * @param ownerFrame the main frame
     * @param codesString the raw code string entered by the user
     */
    public ExpenseByCategory2Panel(final MainFrame ownerFrame,
                                   final String codesString) {

        this.frame = ownerFrame;
        setLayout(new BorderLayout());

        displayArea = new JTextArea();
        backButton = new JButton("Επιστροφή");
        frame.backButtonColors(backButton);

        initializeCenterPanel(codesString);
        initializeBottomPanel();
    }

    /**
     * Initializes the center panel by creating and filling the text area.
     *
     * @param codesString the comma-separated codes given by the user
     */
    private void initializeCenterPanel(final String codesString) {
        displayArea.setEditable(false);
        displayArea.setFont(
            new Font("Monospaced", Font.PLAIN, FONT_SIZE));

        // Convert user input to array
        String[] codes = codesString.split("[, ]+");

        // Get formatted report
        ExpenseManager manager =
            new ExpenseManager("expense_categories_2025.csv");

        String report = manager.getExpenseDetailsReport(codes);

        displayArea.setText(report);

        final JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(500, 500));

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Initializes the bottom panel containing a single Back button.
     */
    private void initializeBottomPanel() {
        final JPanel bottomPanel =
            new JPanel(new GridLayout(GRID_ROWS, GRID_COLS,
                                      GRID_GAP, GRID_GAP));

        bottomPanel.setBorder(
            BorderFactory.createEmptyBorder(
                PADDING, PADDING, PADDING, PADDING));

        bottomPanel.setPreferredSize(new Dimension(0, BOTTOM_HEIGHT));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                frame.switchTo("expenseByCategory");
            }
        });

        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
