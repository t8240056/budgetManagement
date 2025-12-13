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
 * Panel responsible for choosing whether the user wants to insert 
 * changes in Revenue (ΕΣΟΔΑ) or Expense (ΕΞΟΔΑ).
 */
public final class InsertChangePanel extends JPanel {

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

    /**
     * Constructs the InsertChangePanel.
     *
     * @param frame the application's main frame used for switching panels
     */
    public InsertChangePanel(final MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        
        // 1. Initialize and add the center panel with options
        final JPanel centerPanel = initializeCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // 2. Initialize and add the bottom buttons
        final JPanel bottomPanel = initializeBottomButtons();
        add(bottomPanel, BorderLayout.SOUTH);

        // 3. Setup event listeners
        setupListeners();
        
        // Initialize the radio buttons here, after the center panel is ready
        this.revenueButton = (JRadioButton) ((JPanel) centerPanel.getComponent(1))
            .getComponent(0);
        this.expenseButton = (JRadioButton) ((JPanel) centerPanel.getComponent(1))
            .getComponent(1);
        this.confirmButton = (JButton) bottomPanel.getComponent(0);
        this.backButton = (JButton) bottomPanel.getComponent(1);

    }
    
    /**
     * Initializes the center area containing the title and the radio buttons.
     *
     * @return the JPanel containing the title and the options
     */
    private JPanel initializeCenterPanel() {
        final JPanel centerContainer = new JPanel(new GridLayout(2, 1));
        centerContainer.setBorder(
            BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Adds padding

        // Title Label
        final JLabel titleLabel = new JLabel(
            "Επιλέξτε τύπο δεδομένων:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        centerContainer.add(titleLabel);

        // Radio Buttons Panel
        final JPanel radioPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        radioPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 100, 20, 100)); // Indentation

        final JRadioButton revButton = new JRadioButton("Έσοδα");
        revButton.setFont(new Font("Arial", Font.PLAIN, 20));

        final JRadioButton expButton = new JRadioButton("Έξοδα");
        expButton.setFont(new Font("Arial", Font.PLAIN, 20));

        final ButtonGroup group = new ButtonGroup();
        group.add(revButton);
        group.add(expButton);

        radioPanel.add(revButton);
        radioPanel.add(expButton);
        
        centerContainer.add(radioPanel);
        return centerContainer;
    }

    /**
     * Initializes the bottom navigation buttons (Confirm and Back).
     *
     * @return the JPanel containing the navigation buttons
     */
    private JPanel initializeBottomButtons() {
        final JPanel bottomPanel =
            new JPanel(new GridLayout(1, 2, 10, 10));
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
        // Find the actual button references after the panels are initialized
        final JButton confButton = (JButton) ((JPanel) getComponent(1))
            .getComponent(0);
        final JButton retButton = (JButton) ((JPanel) getComponent(1))
            .getComponent(1);
        final JRadioButton revButton = (JRadioButton) ((JPanel) 
            ((JPanel) getComponent(0)).getComponent(1)).getComponent(0);
        final JRadioButton expButton = (JRadioButton) ((JPanel) 
            ((JPanel) getComponent(0)).getComponent(1)).getComponent(1);
            
        // Listener for the BACK button
        retButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                // Returns to the MenuPanel, which is mapped to "menu"
                frame.switchTo("menu"); 
            }
        });

        // Listener for the CONFIRM button
        confButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (revButton.isSelected()) {
                    // TODO: Εδώ θα πάει στο επόμενο panel για Εισαγωγή Αλλαγής Εσόδων
                    AppException.showError("Επιλέχθηκε: Έσοδα - Επόμενο βήμα...");
                } else if (expButton.isSelected()) {
                    // TODO: Εδώ θα πάει στο επόμενο panel για Εισαγωγή Αλλαγής Εξόδων
                    AppException.showError("Επιλέχθηκε: Έξοδα - Επόμενο βήμα...");
                } else {
                    AppException.showError("Παρακαλώ επιλέξτε Έσοδα ή Έξοδα.");
                }
            }
        });
    }
}
