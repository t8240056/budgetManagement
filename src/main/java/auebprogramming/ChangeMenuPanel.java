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
    private static final int RADIOBUTTON_FONT_SIZE = 20;
    private static final int TITLE_FONT_SIZE = 20;

    private static final int OPTIONS_ROWS = 4;
    private static final int TWO = 2;
    private static final int OPTIONS_H_GAP = 20;
    private static final int TEN = 10;
    private static final int BOTTOM_ROWS = 1;
    private static final int BOTTOM_PANEL_WIDTH = 200;
    private static final int BOTTOM_PANEL_HEIGHT = 70;



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
            "Επιλέξτε λειτουργία",
            SwingConstants.CENTER
        );
        titleLabel.setFont(
            new Font("SansSerif", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setBorder(
            BorderFactory.createEmptyBorder(
                TEN, TEN, TEN, TEN)
        );
        add(titleLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(
            OPTIONS_ROWS, TWO, OPTIONS_H_GAP, TEN));
        optionsPanel.setBorder(
            BorderFactory.createEmptyBorder(
                TEN, OPTIONS_H_GAP, TEN, OPTIONS_H_GAP)
        );

        radioButton1 = new JRadioButton("Προβολή Εγγραφών");
        radioButton1.setFont(new Font(
            "Arial", Font.PLAIN, RADIOBUTTON_FONT_SIZE));

        radioButton2 = new JRadioButton("Αλλαγή Ποσού κατά απόλυτη τιμή");
        radioButton2.setFont(new Font(
            "Arial", Font.PLAIN, RADIOBUTTON_FONT_SIZE));

        radioButton3 = new JRadioButton("Αλλαγή Ποσού κατά ποσοστό");
        radioButton3.setFont(new Font(
            "Arial", Font.PLAIN, RADIOBUTTON_FONT_SIZE));

        radioButton4 = new JRadioButton("Μεταφορά Ποσού");
        radioButton4.setFont(new Font(
            "Arial", Font.PLAIN, RADIOBUTTON_FONT_SIZE));

        radioButton5 = new JRadioButton("Αναίρεση (Undo)");
        radioButton5.setFont(new Font(
            "Arial", Font.PLAIN, RADIOBUTTON_FONT_SIZE));

        radioButton6 = new JRadioButton("Προβολή Ιστορικού");
        radioButton6.setFont(new Font(
            "Arial", Font.PLAIN, RADIOBUTTON_FONT_SIZE));

        radioButton7 = new JRadioButton("Αποθήκευση");
        radioButton7.setFont(new Font(
            "Arial", Font.PLAIN, RADIOBUTTON_FONT_SIZE));

        radioButton8 = new JRadioButton("Φόρτωση από αρχείο");
        radioButton8.setFont(new Font(
            "Arial", Font.PLAIN, RADIOBUTTON_FONT_SIZE));

        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);
        buttonGroup.add(radioButton3);
        buttonGroup.add(radioButton4);
        buttonGroup.add(radioButton5);
        buttonGroup.add(radioButton6);
        buttonGroup.add(radioButton7);
        buttonGroup.add(radioButton8);

        optionsPanel.add(radioButton1);
        optionsPanel.add(radioButton2);
        optionsPanel.add(radioButton3);
        optionsPanel.add(radioButton4);
        optionsPanel.add(radioButton5);
        optionsPanel.add(radioButton6);
        optionsPanel.add(radioButton7);
        optionsPanel.add(radioButton8);

        add(optionsPanel, BorderLayout.CENTER);

        JPanel bottomPanel =
        new JPanel(new GridLayout(BOTTOM_ROWS, TWO, TEN, TEN));
        bottomPanel.setBorder(
            BorderFactory.createEmptyBorder(TEN, TEN,
                 TEN, TEN));
        bottomPanel.setPreferredSize(new Dimension(
            BOTTOM_PANEL_WIDTH, BOTTOM_PANEL_HEIGHT));
        JButton backButton = new JButton("Επιστροφή");
        JButton confirmButton = new JButton("Επιβεβαίωση");
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
     * Handles the confirmation button action.
     * The implementation will be added later.
     */
    private void handleConfirmation() {
        if (radioButton1.isSelected()) {
            // TODO: implement action for radioButton1
        } else if (radioButton2.isSelected()) {
            // TODO: implement action for radioButton2
        } else if (radioButton3.isSelected()) {
            // TODO: implement action for radioButton3
        } else if (radioButton4.isSelected()) {
            // TODO: implement action for radioButton4
        } else if (radioButton5.isSelected()) {
            // TODO: implement action for radioButton5
        } else if (radioButton6.isSelected()) {
            // TODO: implement action for radioButton6
        } else if (radioButton7.isSelected()) {
            // TODO: implement action for radioButton7
        } else if (radioButton8.isSelected()) {
            // TODO: implement action for radioButton8
        }
    }
}
