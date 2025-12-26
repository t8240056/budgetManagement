package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

/**
 * Panel responsible for choosing whether the user wants to insert
 * changes in Revenue (ΕΣΟΔΑ) or Expense (ΕΞΟΔΑ).
 */
public final class InsertChangePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Reference to main frame for panel switching. */
    private final MainFrame frame;

    /** Radio button for Revenue (Έσοδα) selection. */
    private final JRadioButton revenueButton;

    /** Radio button for Expense (Έξοδα) selection. */
    private final JRadioButton expenseButton;

    /** Button for confirmation. */
    private final JButton confirmButton;

    /** Button for returning to the main menu. */
    private final JButton backButton;

    private final BudgetManager budgetManager;

    /**
     * Constructs the InsertChangePanel.
     *
     * @param frame   the application's main frame used for switching panels
     * @param manager the budget manager instance
     */
    public InsertChangePanel(final MainFrame frame,
                             final BudgetManager manager) {
        this.frame = frame;
        setLayout(new BorderLayout());
        this.budgetManager = manager;

        // 1. Initialize and add the North panel with options and title
        final JPanel northPanel = initializeNorthPanel();
        add(northPanel, BorderLayout.NORTH);

        // 2. Initialize and add the bottom buttons
        final JPanel bottomPanel = initializeBottomButtons();
        add(bottomPanel, BorderLayout.SOUTH);

        // 3. Setup event listeners
        setupListeners();

        // --- ΑΡΧΙΚΟΠΟΙΗΣΗ FIELDS ---
        // northPanel (BoxLayout) -> contentWrapper (0) [BorderLayout]
        final JPanel contentWrapper = (JPanel) northPanel.getComponent(0);

        // radioAligner είναι το component(2) του contentWrapper
        final JPanel radioAligner = (JPanel) contentWrapper.getComponent(2);
        final JPanel radioGroupPanel = (JPanel) radioAligner.getComponent(0);

        this.revenueButton = (JRadioButton) ((JPanel) radioGroupPanel
                .getComponent(0)).getComponent(0);
        this.expenseButton = (JRadioButton) ((JPanel) radioGroupPanel
                .getComponent(1)).getComponent(0);

        // Accessing the JButtons:
        this.confirmButton = (JButton) bottomPanel.getComponent(0);
        this.backButton = (JButton) bottomPanel.getComponent(1);
    }

    /**
     * Initializes the north area containing the title and the radio buttons.
     * Uses BorderLayout for overall structure and EmptyBorder in the center
     * to push the content down.
     *
     * @return the JPanel containing the title and the options
     */
    private JPanel initializeNorthPanel() {
        // Main container (northContentPanel) returned to MainFrame
        final JPanel northContentPanel = new JPanel();
        northContentPanel.setLayout(new BoxLayout(northContentPanel,
                BoxLayout.Y_AXIS));

        // Wrapper για περιεχόμενο (τίτλος, κενό, radio buttons).
        final JPanel contentWrapper = new JPanel(new BorderLayout());

        contentWrapper.setBorder(BorderFactory.createEmptyBorder(
                10, 10, 0, 10));

        // 1. Title Label (Centered)
        final JLabel titleLabel = new JLabel("Επιλέξτε τύπο δεδομένων:",
                SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        contentWrapper.add(titleLabel, BorderLayout.NORTH);

        // 2. ΜΕΓΑΛΟ ΚΕΝΟ ΤΙΤΛΟΥ/ΕΠΙΛΟΓΩΝ
        final JPanel spacingPanel = new JPanel();
        // 150px κάθετο padding
        spacingPanel.setBorder(BorderFactory.createEmptyBorder(
                150, 0, 0, 0));
        contentWrapper.add(spacingPanel, BorderLayout.CENTER);

        // 3. Panel για τα Radio Buttons
        final JRadioButton revButton = new JRadioButton("Έσοδα");
        revButton.setFont(new Font("Arial", Font.PLAIN, 20));

        final JRadioButton expButton = new JRadioButton("Έξοδα");
        expButton.setFont(new Font("Arial", Font.PLAIN, 20));

        final ButtonGroup group = new ButtonGroup();
        group.add(revButton);
        group.add(expButton);

        // Panel to hold the stacked buttons.
        final JPanel radioGroupPanel = new JPanel(new GridLayout(2, 1, 0, 0));

        // Wrap radio buttons in FlowLayout.LEFT panels
        final JPanel revPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        revPanel.add(revButton);

        // 4. ΑΥΞΗΣΗ ΚΕΝΟΥ ΜΕΤΑΞΥ ΕΣΟΔΑ / ΕΞΟΔΑ
        revPanel.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 70, 0));

        final JPanel expPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        expPanel.add(expButton);

        radioGroupPanel.add(revPanel);
        radioGroupPanel.add(expPanel);

        // Wrapper to align radio buttons left
        final JPanel radioAligner = new JPanel(new FlowLayout(
                FlowLayout.LEFT, 0, 0));
        radioAligner.add(radioGroupPanel);

        // Προσθήκη των radio buttons κάτω από το μεγάλο κενό.
        contentWrapper.add(radioAligner, BorderLayout.SOUTH);

        // northContentPanel περιέχει μόνο το contentWrapper.
        northContentPanel.add(contentWrapper);

        return northContentPanel;
    }

    /**
     * Initializes the bottom navigation buttons (Confirm and Back).
     *
     * @return the JPanel containing the navigation buttons
     */
    private JPanel initializeBottomButtons() {
        final JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.setPreferredSize(new Dimension(200, 70));

        final JButton confButton = new JButton("Επιβεβαίωση");
        final JButton retButton = new JButton("Επιστροφή στο Menu");

        // Apply colors using MainFrame utility methods
        frame.confButtonColors(confButton);
        frame.backButtonColors(retButton);

        bottomPanel.add(confButton);
        bottomPanel.add(retButton);
        return bottomPanel;
    }

    /**
     * Sets up the ActionListeners for the buttons.
     */
    private void setupListeners() {
        // Find the actual button references
        final JButton confButton = (JButton) ((JPanel) getComponent(1))
                .getComponent(0);
        final JButton retButton = (JButton) ((JPanel) getComponent(1))
                .getComponent(1);

        // Listener for the BACK button
        retButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                frame.switchTo("menu");
            }
        });

        // Listener for the CONFIRM button
        confButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                // --- ΠΡΟΣΒΑΣΗ ΣΤΑ RADIO BUTTONS ---
                final JPanel northPanel = (JPanel) getComponent(0);
                final JPanel contentWrapper = (JPanel) northPanel
                        .getComponent(0);

                // contentWrapper: NORTH=0, CENTER=1, SOUTH=2
                final JPanel radioAligner = (JPanel) contentWrapper
                        .getComponent(2);
                final JPanel radioGroupPanel = (JPanel) radioAligner
                        .getComponent(0);

                final JRadioButton revButton = (JRadioButton) ((JPanel)
                        radioGroupPanel.getComponent(0)).getComponent(0);
                final JRadioButton expButton = (JRadioButton) ((JPanel)
                        radioGroupPanel.getComponent(1)).getComponent(0);
                // ----------------------------------------------------
                try {
                    if (revButton.isSelected()) {
                        budgetManager.setBudgetType(0);
                        frame.switchTo("changesMenu");
                    } else if (expButton.isSelected()) {
                        budgetManager.setBudgetType(1);
                        frame.showMinistriesSelectionPanel();
                    } else {
                        AppException.showError(
                                "Παρακαλώ επιλέξτε Έσοδα ή Έξοδα.");
                    }
                } catch (final AppException ex) {
                    AppException.showError(ex.getMessage());
                }
            }
        });
    }
}
