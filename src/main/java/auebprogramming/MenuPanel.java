package auebprogramming;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class MenuPanel extends JPanel {

    private final MainFrame frame;

    public MenuPanel(final MainFrame frame) {
        this.frame = frame;

        setLayout(new GridLayout(6, 1, 10, 10));

        final JLabel titleLabel = new JLabel(
                "=== Πρωθυπουργός για μια μέρα ===",
                SwingConstants.CENTER
        );
        add(titleLabel);

        final JRadioButton viewBudgetButton =
                new JRadioButton("1. Προβολή προϋπολογισμού");

        final JRadioButton insertChangeButton =
                new JRadioButton("2. Εισαγωγή αλλαγής");

        final JRadioButton viewChangesButton =
                new JRadioButton("3. Εμφάνιση αλλαγών");

        final JRadioButton exitButton =
                new JRadioButton("4. Έξοδος");

        final ButtonGroup group = new ButtonGroup();
        group.add(viewBudgetButton);
        group.add(insertChangeButton);
        group.add(viewChangesButton);
        group.add(exitButton);

        add(viewBudgetButton);
        add(insertChangeButton);
        add(viewChangesButton);
        add(exitButton);

        final JButton confirmButton = new JButton("Επιβεβαίωση");
        add(confirmButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (viewBudgetButton.isSelected()) {
                    frame.switchTo("budget");
                } else if (insertChangeButton.isSelected()) {
                    frame.switchTo("insert");
                } else if (viewChangesButton.isSelected()) {
                    frame.switchTo("changes");
                } else if (exitButton.isSelected()) {
                    System.exit(0);
                }
            }
        });
    }
}
