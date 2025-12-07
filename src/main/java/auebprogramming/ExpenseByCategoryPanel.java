package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Panel for selecting expense categories and displaying results.
 */
public final class ExpenseByCategoryPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Padding for borders. */
    private static final int PADDING = 10;

    /** Rows in bottom panel. */
    private static final int GRID_ROWS = 1;

    /** Columns in bottom panel. */
    private static final int GRID_COLS = 2;

    /** Gap size in grid. */
    private static final int GRID_GAP = 10;

    /** Font size for text area. */
    private static final int FONT_SIZE = 14;

    /** Main frame reference. */
    private final MainFrame frame;

    /** Area to display reports. */
    private final JTextArea displayArea;

    /** Field for inputting codes. */
    private final JTextField codeField;

    /** Button to reveal the input field. */
    private final JButton openInputButton;

    /** Button confirming the selection. */
    private final JButton confirmButton;

    /** Button to go back. */
    private final JButton backButton;

    /**
     * Constructs an ExpenseByCategoryPanel.
     *
     * @param ownerFrame the main frame
     */
    public ExpenseByCategoryPanel(final MainFrame ownerFrame) {
        this.frame = ownerFrame;
        setLayout(new BorderLayout());

        displayArea = new JTextArea();
        codeField = new JTextField();
        openInputButton = new JButton("Εισάγετε έναν η περισσότερους κωδικούς δαπανών για περαιτέρω ανάλυση");
        confirmButton = new JButton("Επιβεβαίωση");
        backButton = new JButton("Επιστροφή");

        initializeTopPanel();
        initializeCenterPanel();
        initializeBottomPanel();
    }

    /**
     * Initializes the top panel containing the input button and text field.
     */
    private void initializeTopPanel() {
        final JPanel topPanel =
            new JPanel(new GridLayout(2, 1, GRID_GAP, GRID_GAP));
        topPanel.setBorder(
            BorderFactory.createEmptyBorder(
                PADDING, PADDING, PADDING, PADDING));

        codeField.setVisible(false);

        openInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                codeField.setVisible(true);
                topPanel.revalidate();
                topPanel.repaint();
            }
        });

        topPanel.add(openInputButton);
        topPanel.add(codeField);

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Initializes the center panel containing the display area.
     */
    private void initializeCenterPanel() {
        displayArea.setEditable(false);
        displayArea.setFont(
            new Font("Monospaced", Font.PLAIN, FONT_SIZE));

        final JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Initializes the bottom panel containing Back and Confirm buttons.
     */
    private void initializeBottomPanel() {
        final JPanel bottomPanel =
            new JPanel(new GridLayout(GRID_ROWS, GRID_COLS,
                                      GRID_GAP, GRID_GAP));

        bottomPanel.setBorder(
            BorderFactory.createEmptyBorder(
                PADDING, PADDING, PADDING, PADDING));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                frame.switchTo("expensePanel");
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                final String text = codeField.getText().trim();
                if (!text.isEmpty()) {
                    frame.switchTo("displayCategory");
                }
                else {
                    AppException.showError("Πατήστε το κουμπί επιστροφής ή πληκτρολογήστε κωδικό");
                }
            }
        });

        bottomPanel.add(backButton);
        bottomPanel.add(confirmButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Returns the user-entered codes.
     *
     * @return a string containing the codes
     */
    public String getCodes() {
        return codeField.getText().trim();
    }

    /**
     * Sets the display text in the output area.
     *
     * @param text the text to display
     */
    public void setDisplayText(final String text) {
        displayArea.setText(text);
    }
}
