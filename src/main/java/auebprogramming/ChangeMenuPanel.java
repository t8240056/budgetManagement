package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

/**
 * Panel that displays the available change options
 * for the budget modification process.
 */
public final class ChangeMenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // Layout Constants
    private static final int RADIOBUTTON_FONT_SIZE = 20;
    private static final int TITLE_FONT_SIZE = 20;
    private static final int OPTIONS_ROWS = 4;
    private static final int TWO = 2; // Columns
    private static final int OPTIONS_H_GAP = 20;
    private static final int TEN = 10; // Padding/Gap
    private static final int BOTTOM_ROWS = 1;
    private static final int BOTTOM_PANEL_WIDTH = 200;
    private static final int BOTTOM_PANEL_HEIGHT = 70;

    /** Reference to the main application frame. */
    private final MainFrame mainFrame;
    private final BudgetManager manager;

    /** Group for radio buttons. */
    private final ButtonGroup buttonGroup;

    /** Radio buttons for change options. */
    private final JRadioButton viewButton;
    private final JRadioButton absChangeButton;
    private final JRadioButton percChangeButton;
    private final JRadioButton transferButton;
    private final JRadioButton undoButton;
    private final JRadioButton historyButton;
    private final JRadioButton saveButton;
    private final JRadioButton loadButton;

    /**
     * Constructs the change menu panel.
     *
     * @param frame   the main application frame
     * @param manager the budget manager instance
     */
    public ChangeMenuPanel(final MainFrame frame, final BudgetManager manager) {
        this.mainFrame = frame;
        this.manager = manager;
        this.buttonGroup = new ButtonGroup();

        setLayout(new BorderLayout());

        // 1. Title
        final JLabel titleLabel = new JLabel("Επιλέξτε λειτουργία",
                SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(
                TEN, TEN, TEN, TEN));
        add(titleLabel, BorderLayout.NORTH);

        // 2. Options Grid
        final JPanel optionsPanel = new JPanel(new GridLayout(
                OPTIONS_ROWS, TWO, OPTIONS_H_GAP, TEN));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(
                TEN, OPTIONS_H_GAP, TEN, OPTIONS_H_GAP));

        // Initialize Radio Buttons
        final Font rbFont = new Font("Arial", Font.PLAIN,
                RADIOBUTTON_FONT_SIZE);

        viewButton = new JRadioButton("Προβολή Εγγραφών");
        viewButton.setFont(rbFont);

        absChangeButton = new JRadioButton("Αλλαγή Ποσού (Απόλυτη)");
        absChangeButton.setFont(rbFont);

        percChangeButton = new JRadioButton("Αλλαγή Ποσού (Ποσοστό)");
        percChangeButton.setFont(rbFont);

        transferButton = new JRadioButton("Μεταφορά Ποσού");
        transferButton.setFont(rbFont);

        undoButton = new JRadioButton("Αναίρεση (Undo)");
        undoButton.setFont(rbFont);

        historyButton = new JRadioButton("Προβολή Ιστορικού");
        historyButton.setFont(rbFont);

        saveButton = new JRadioButton("Αποθήκευση");
        saveButton.setFont(rbFont);

        loadButton = new JRadioButton("Φόρτωση από αρχείο");
        loadButton.setFont(rbFont);

        // Add to Group
        buttonGroup.add(viewButton);
        buttonGroup.add(absChangeButton);
        buttonGroup.add(percChangeButton);
        buttonGroup.add(transferButton);
        buttonGroup.add(undoButton);
        buttonGroup.add(historyButton);
        buttonGroup.add(saveButton);
        buttonGroup.add(loadButton);

        // Add to Panel
        optionsPanel.add(viewButton);
        optionsPanel.add(absChangeButton);
        optionsPanel.add(percChangeButton);
        optionsPanel.add(transferButton);
        optionsPanel.add(undoButton);
        optionsPanel.add(historyButton);
        optionsPanel.add(saveButton);
        optionsPanel.add(loadButton);

        add(optionsPanel, BorderLayout.CENTER);

        // 3. Bottom Buttons
        final JPanel bottomPanel = new JPanel(new GridLayout(
                BOTTOM_ROWS, TWO, TEN, TEN));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(
                TEN, TEN, TEN, TEN));
        bottomPanel.setPreferredSize(new Dimension(
                BOTTOM_PANEL_WIDTH, BOTTOM_PANEL_HEIGHT));

        final JButton backButton = new JButton("Επιστροφή");
        final JButton confirmButton = new JButton("Επιβεβαίωση");

        frame.confButtonColors(confirmButton);
        frame.backButtonColors(backButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                handleConfirmation();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                mainFrame.switchTo("insert");
            }
        });

        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the confirmation button action based on selection.
     */
    private void handleConfirmation() {
        if (viewButton.isSelected()) {
            mainFrame.openViewEntriesPanel();

        } else if (absChangeButton.isSelected()) {
            mainFrame.switchTo("absoluteChange");

        } else if (percChangeButton.isSelected()) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Η λειτουργία ποσοστιαίας αλλαγής δεν είναι "
                    + "διαθέσιμη ακόμα.");

        } else if (transferButton.isSelected()) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Η λειτουργία μεταφοράς ποσού δεν είναι "
                    + "διαθέσιμη ακόμα.");

        } else if (undoButton.isSelected()) {
            try {
                final String msg = manager.undoLastAction();
                JOptionPane.showMessageDialog(mainFrame, msg);
            } catch (final AppException ex) {
                AppException.showError(ex.getMessage());
            }

        } else if (historyButton.isSelected()) {
            mainFrame.showAuditLogPanel();

        } else if (saveButton.isSelected()) {
            handleSaveAction();

        } else if (loadButton.isSelected()) {
            handleLoadAction();
        }
    }

    /**
     * Helper method to handle the save operation.
     */
    private void handleSaveAction() {
        final String filename = JOptionPane.showInputDialog(
                this,
                "Δώστε όνομα αρχείου (ή αφήστε κενό για αυτόματο):",
                "Αποθήκευση",
                JOptionPane.QUESTION_MESSAGE
        );

        try {
            // Αν πατηθεί Cancel -> filename == null
            if (filename != null) {
                final String savedPath = manager.saveWork(filename);
                JOptionPane.showMessageDialog(
                        this,
                        "Η αποθήκευση ολοκληρώθηκε επιτυχώς:\n" + savedPath,
                        "Επιτυχία",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (final AppException ex) {
            AppException.showError(ex.getMessage());
        }
    }

    /**
     * Helper method to handle the load operation.
     */
    private void handleLoadAction() {
        try {
            // Παίρνουμε όλα τα διαθέσιμα αρχεία για τον τρέχοντα φορέα
            final List<String> files = manager.getAvailableSavedFiles();

            if (files.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Δεν υπάρχουν αποθηκευμένα σενάρια για "
                        + "τον τρέχοντα φορέα.",
                        "Φόρτωση",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // Δημιουργία dropdown για επιλογή αρχείου
            final String selectedFile = (String) JOptionPane.showInputDialog(
                    this,
                    "Επιλέξτε αρχείο σεναρίου για φόρτωση:",
                    "Φόρτωση Σεναρίου",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    files.toArray(new String[0]),
                    files.get(0)
            );

            if (selectedFile != null) {
                manager.loadSavedScenario(selectedFile);
                JOptionPane.showMessageDialog(
                        this,
                        "Το σενάριο φορτώθηκε επιτυχώς: " + selectedFile,
                        "Επιτυχία",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (final AppException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Σφάλμα Φόρτωσης",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
