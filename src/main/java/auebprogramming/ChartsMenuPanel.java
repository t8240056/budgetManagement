package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

/**
 * Panel that provides options to select different types of budget charts.
 * This panel handles the navigation between different chart visualizations.
 */
public final class ChartsMenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // Constants for Checkstyle compliance (avoiding magic numbers)
    private static final int TITLE_FONT_SIZE = 20;
    private static final int RB_FONT_SIZE = 18;
    private static final int PADDING = 10;
    private static final int GRID_ROWS = 3;
    private static final int GRID_COLS = 1;
    private static final int BOTTOM_PANEL_HEIGHT = 70;
    private static final int BOTTOM_PANEL_WIDTH = 200;

    private final MainFrame mainFrame;
    private final BudgetManager budgetManager;

    private final JRadioButton revenueChartButton;
    private final JRadioButton expenseChartButton;
    private final ButtonGroup group;

    /**
     * Constructs the ChartsMenuPanel.
     *
     * @param frame   the main application frame
     * @param manager the budget manager instance
     */
    public ChartsMenuPanel(final MainFrame frame, final BudgetManager manager) {
        this.mainFrame = frame;
        this.budgetManager = manager;

        setLayout(new BorderLayout());

        // 1. Title Section
        final JLabel titleLabel = new JLabel("Επιλογή Τύπου Γραφήματος",
                SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(
                PADDING, PADDING, PADDING, PADDING));
        add(titleLabel, BorderLayout.NORTH);

        // 2. Options Grid Section
        final JPanel optionsPanel = new JPanel(new GridLayout(
                GRID_ROWS, GRID_COLS, PADDING, PADDING));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(
                PADDING, PADDING * 2, PADDING, PADDING * 2));

        final Font rbFont = new Font("Arial", Font.PLAIN, RB_FONT_SIZE);

        revenueChartButton = new JRadioButton("Γράφημα Εσόδων ανά Κατηγορία");
        revenueChartButton.setFont(rbFont);

        expenseChartButton = new JRadioButton("Γράφημα Εξόδων ανά Φορέα");
        expenseChartButton.setFont(rbFont);

        group = new ButtonGroup();
        group.add(revenueChartButton);
        group.add(expenseChartButton);

        optionsPanel.add(revenueChartButton);
        optionsPanel.add(expenseChartButton);

        add(optionsPanel, BorderLayout.CENTER);

        // 3. Navigation Buttons Section
        final JPanel bottomPanel = new JPanel(new GridLayout(1, 2, PADDING, PADDING));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(
                PADDING, PADDING, PADDING, PADDING));
        bottomPanel.setPreferredSize(new Dimension(
                BOTTOM_PANEL_WIDTH, BOTTOM_PANEL_HEIGHT));

        final JButton confirmButton = new JButton("Επιβεβαίωση");
        final JButton backButton = new JButton("Επιστροφή");

        // Apply global styling from MainFrame
        mainFrame.confButtonColors(confirmButton);
        mainFrame.backButtonColors(backButton);

        // Action Listeners
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                handleConfirmation();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                mainFrame.switchTo("menu");
            }
        });

        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the logic when the confirmation button is pressed.
     */
    private void handleConfirmation() {
        if (revenueChartButton.isSelected()) {
            // Placeholder for the next step
            mainFrame.switchTo("revenueChart");
        } else if (expenseChartButton.isSelected()) {
            // Placeholder for the next step
            mainFrame.switchTo("expenseChart");
        } else {
            AppException.showError("Παρακαλώ επιλέξτε έναν τύπο γραφήματος.");
        }
    }
}
