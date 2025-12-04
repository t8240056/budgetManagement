package auebprogramming;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
//*Class for demonstrating expenses */

public class ExpensePanel extends JPanel {

    private final JRadioButton byAgencyButton;
    private final JRadioButton byCategoryButton;
    private final JButton backButton;
    private final JButton confirmButton;
    private final JTable expenseTable;
//*Constructor with a MainFrame parameter */
    public ExpensePanel(MainFrame frame) {

        setLayout(new BorderLayout());

        // ============================
        // 1) Top Panel (Radio Buttons)
        // ============================
        final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        byAgencyButton = new JRadioButton("Έξοδα ανά φορέα");
        byCategoryButton = new JRadioButton("Έξοδα ανά δαπάνη");

        final ButtonGroup group = new ButtonGroup();
        group.add(byAgencyButton);
        group.add(byCategoryButton);

        topPanel.add(byAgencyButton);
        topPanel.add(byCategoryButton);

        add(topPanel, BorderLayout.NORTH);

        final String[] columns = { "Κωδικός", "Περιγραφή", "Ποσό" };
        final Object[][] data = {
                { "-", "Δεν υπάρχουν δεδομένα ακόμα", "-" }
        };

        expenseTable = new JTable(data, columns);
        final JScrollPane scrollPane = new JScrollPane(expenseTable);

        add(scrollPane, BorderLayout.CENTER);

        // ============================
        // 3) Bottom Panel (Buttons)
        // ============================
        final JPanel bottomPanel =
        new JPanel(new FlowLayout(FlowLayout.CENTER));

        backButton = new JButton("Επιστροφή");
        confirmButton = new JButton("Επιβεβαίωση");
        //*BackButton's method */
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                frame.switchTo("budget"); // πίσω στο BudgetPanel
            }
        });
        //*ConfirmButton's method */
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {

                if (byAgencyButton.isSelected()) {
                    frame.switchTo("expenseByAgency");
                } else if (byCategoryButton.isSelected()) {
                    frame.switchTo("expenseByCategory");
                } else {
                    // GUI-friendly μήνυμα
                    JOptionPane.showMessageDialog(
                            ExpensePanel.this,
                            "Παρακαλώ επιλέξτε τύπο προβολής.",
                            "Σφάλμα",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
//*method for getting the JTable */
    public JTable getExpenseTable() {
        return expenseTable;
    }
}
