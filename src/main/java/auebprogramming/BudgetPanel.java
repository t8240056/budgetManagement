package auebprogramming;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class BudgetPanel extends JPanel {

    private final MainFrame frame;
    private final JRadioButton revenueButton;
    private final JRadioButton expenseButton;
    private final JButton confirmButton;
    private final JButton backButton;

    public BudgetPanel(final MainFrame frame) {
        this.frame = frame;

        setLayout(new GridLayout(4, 1, 10, 10));

        // Τίτλος
        final JLabel titleLabel = new JLabel("Επιλέξτε τύπο δεδομένων:", JLabel.CENTER);
        add(titleLabel);

        // RadioButtons για Έσοδα / Έξοδα
        revenueButton = new JRadioButton("Έσοδα");
        expenseButton = new JRadioButton("Έξοδα");

        final ButtonGroup group = new ButtonGroup();
        group.add(revenueButton);
        group.add(expenseButton);

        add(revenueButton);
        add(expenseButton);

        // Κουμπί Επιβεβαίωσης
        confirmButton = new JButton("Επιβεβαίωση");
        add(confirmButton);
        // Κουμπί Επιστροφής στο Menu
        backButton = new JButton("Επιστροφή στο Menu");
        add(backButton);

        // Listener για το κουμπί επιβεβαίωσης
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (revenueButton.isSelected()) {
                    frame.switchTo("budgetRevenue");
                } else if (expenseButton.isSelected()) {
                    frame.switchTo("budgetExpense");
                }
            }
        });
        // Listener για το κουμπί επιστροφής
        backButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent event) {
                frame.switchTo("menu");
            }
        });


    }
}
