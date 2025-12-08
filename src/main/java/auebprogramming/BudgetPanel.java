package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**Class for the second appeared panel */
public final class BudgetPanel extends JPanel {

    private final MainFrame frame;
    private final JRadioButton revenueButton;
    private final JRadioButton expenseButton;
    private final JButton confirmButton;
    private final JButton backButton;
//*Constructor for the second appeared class */
    public BudgetPanel(final MainFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout(10, 10));

        // Τίτλος + RadioButtons:NORTH
        final JPanel topPanel = new JPanel(
        new GridLayout(3, 1, 5, 5));
        final JLabel titleLabel = new JLabel(
        "Επιλέξτε τύπο δεδομένων:", JLabel.CENTER);
        titleLabel.setFont(
            new Font("SansSerif", Font.BOLD | Font.ITALIC, 20));
        topPanel.add(titleLabel);

        // RadioButtons για Έσοδα / Έξοδα
        revenueButton = new JRadioButton("Έσοδα");
        expenseButton = new JRadioButton("Έξοδα");
        revenueButton.setFont(new Font("Arial", Font.PLAIN, 20));
        expenseButton.setFont(new Font("Arial", Font.PLAIN, 20));


        final ButtonGroup group = new ButtonGroup();
        group.add(revenueButton);
        group.add(expenseButton);

        topPanel.add(revenueButton);
        topPanel.add(expenseButton);
        topPanel.setPreferredSize(new java.awt.Dimension(
        300, 400));
        add(topPanel, BorderLayout.NORTH);

        final JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(
        1, 2, 10, 0));
        bottomPanel.setPreferredSize(new Dimension(0, 80));


        // Κουμπί Επιβεβαίωσης
        confirmButton = new JButton("Επιβεβαίωση");
        confirmButton.setBackground(Color.GREEN);
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setOpaque(true);
        bottomPanel.add(confirmButton);
        // Κουμπί Επιστροφής στο Menu
        backButton = new JButton("Επιστροφή στο Menu");
        backButton.setBackground(Color.YELLOW);
        backButton.setForeground(Color.BLACK);
        backButton.setOpaque(true);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        //*  Listener για το κουμπί επιβεβαίωσης*/
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (revenueButton.isSelected()) {
                    frame.switchTo("revenuePanel");
                } else if (expenseButton.isSelected()) {
                    frame.switchTo("expensePanel");
                } else {
                    AppException.showError(
                        "Επιλέξτε τύπο δεδομένων ή πατήστε Επιστροφή");
                }
            }
        });
        //* Listener για το κουμπί επιστροφής*/
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                frame.switchTo("menu");
            }
        });

    }
}
