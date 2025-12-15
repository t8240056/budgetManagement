package auebprogramming;

import java.awt.BorderLayout;
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
 * Panel that displays the available change options
 * for the budget modification process.
 */
public final class ChangeMenuPanel extends JPanel {

    /** Reference to the main application frame. */
    private final MainFrame mainFrame;

    /** Group for radio buttons. */
    private final ButtonGroup buttonGroup;

    /** Radio buttons for change options. */
    private final JRadioButton radioButton1;
    private final JRadioButton radioButton2;
    private final JRadioButton radioButton3;
    private final JRadioButton radioButton4;
    private final JRadioButton radioButton5;
    private final JRadioButton radioButton6;
    private final JRadioButton radioButton7;
    private final JRadioButton radioButton8;
    private final JRadioButton radioButton9;

    /**
     * Constructs the change menu panel.
     *
     * @param frame the main application frame
     */
    public ChangeMenuPanel(final MainFrame frame) {
        this.mainFrame = frame;
        this.buttonGroup = new ButtonGroup();

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(
            "Select change operation",
            SwingConstants.CENTER
        );
        titleLabel.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
        add(titleLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(9, 1, 5, 5));
        optionsPanel.setBorder(
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        );

        radioButton1 = new JRadioButton("Increase allocated amount");
        radioButton2 = new JRadioButton("Decrease allocated amount");
        radioButton3 = new JRadioButton("Transfer amount to another code");
        radioButton4 = new JRadioButton("Create new budget code");
        radioButton5 = new JRadioButton("Delete existing budget code");
        radioButton6 = new JRadioButton("Merge two budget codes");
        radioButton7 = new JRadioButton("Split budget code into sub-codes");
        radioButton8 = new JRadioButton("Correct data entry error");
        radioButton9 = new JRadioButton("Reclassify budget code");

        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);
        buttonGroup.add(radioButton3);
        buttonGroup.add(radioButton4);
        buttonGroup.add(radioButton5);
        buttonGroup.add(radioButton6);
        buttonGroup.add(radioButton7);
        buttonGroup.add(radioButton8);
        buttonGroup.add(radioButton9);

        radioButton1.setSelected(true);

        optionsPanel.add(radioButton1);
        optionsPanel.add(radioButton2);
        optionsPanel.add(radioButton3);
        optionsPanel.add(radioButton4);
        optionsPanel.add(radioButton5);
        optionsPanel.add(radioButton6);
        optionsPanel.add(radioButton7);
        optionsPanel.add(radioButton8);
        optionsPanel.add(radioButton9);

        add(optionsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton confirmButton = new JButton("Confirm");
        JButton backButton = new JButton("Back");

        mainFrame.confButtonColors(confirmButton);
        mainFrame.backButtonColors(backButton);

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

        buttonPanel.add(confirmButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the confirmation button action.
     * The implementation will be added later.
     */
    private void handleConfirmation() {
        /* Intentionally left empty */
    }
}
