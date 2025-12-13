package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
        
        // 1. Initialize and add the North panel with options and title
        final JPanel northPanel = initializeNorthPanel();
        add(northPanel, BorderLayout.NORTH); // Τοποθέτηση στο NORTH

        // 2. Initialize and add the bottom buttons
        final JPanel bottomPanel = initializeBottomButtons();
        add(bottomPanel, BorderLayout.SOUTH);

        // 3. Setup event listeners
        setupListeners();
        
        // The rest of the initialization remains the same but adjusted for new structure
        this.revenueButton = (JRadioButton) ((JPanel) ((JPanel) northPanel.getComponent(1))
            .getComponent(0)).getComponent(0);
        this.expenseButton = (JRadioButton) ((JPanel) ((JPanel) northPanel.getComponent(1))
            .getComponent(1)).getComponent(0);
        this.confirmButton = (JButton) bottomPanel.getComponent(0);
        this.backButton = (JButton) bottomPanel.getComponent(1);

    }
    
    /**
     * Initializes the north area containing the title and the radio buttons.
     * This panel uses BorderLayout and is added to BorderLayout.NORTH of the main panel.
     *
     * @return the JPanel containing the title and the options
     */
    private JPanel initializeNorthPanel() {
        // Use a container with FlowLayout or BoxLayout to pack content to the top
        final JPanel topContainer = new JPanel(new GridLayout(2, 1));
        topContainer.setBorder(
            BorderFactory.createEmptyBorder(50, 50, 20, 50)); 

        // Title Label
        final JLabel titleLabel = new JLabel(
            "Επιλέξτε τύπο δεδομένων:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        topContainer.add(titleLabel);

        // Radio Buttons Panel Container (FlowLayout for grouping)
        final JPanel radioContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));

        final JRadioButton revButton = new JRadioButton("Έσοδα");
        revButton.setFont(new Font("Arial", Font.PLAIN, 20));

        final JRadioButton expButton = new JRadioButton("Έξοδα");
        expButton.setFont(new Font("Arial", Font.PLAIN, 20));
        
        // Use a simple panel for each radio button to keep them left-aligned relative to the center
        final JPanel revPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        revPanel.add(revButton);
        final JPanel expPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        expPanel.add(expButton);


        final ButtonGroup group = new ButtonGroup();
        group.add(revButton);
        group.add(expButton);

        // Adding radio buttons to a panel with GridLayout for proper spacing/alignment
        final JPanel radioPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        radioPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        radioPanel.add(revPanel); 
        radioPanel.add(expPanel);

        topContainer.add(radioPanel);
        return topContainer;
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
                // Need to use the correct references now based on the new North Panel structure
                final JRadioButton revButton = (JRadioButton) ((JPanel) 
                    ((JPanel) ((JPanel) getComponent(0)).getComponent(1)).getComponent(0)).getComponent(0);
                final JRadioButton expButton = (JRadioButton) ((JPanel) 
                    ((JPanel) ((JPanel) getComponent(0)).getComponent(1)).getComponent(1)).getComponent(0);

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
