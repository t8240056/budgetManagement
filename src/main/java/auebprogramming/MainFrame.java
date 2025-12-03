package auebprogramming;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private RevenuePanel revenuePanel;

    public MainFrame() {
        setTitle("Διαχείριση Κρατικού Προϋπολογισμού");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Δημιουργία CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Προσθήκη panels
        cardPanel.add(new MenuPanel(this), "menu");
        cardPanel.add(new ExpensePanel(this), "expensePanel");
        revenuePanel = new RevenuePanel(this);
        cardPanel.add(revenuePanel, "revenuePanel");


        // Εδώ αργότερα θα προσθέσεις κι άλλες οθόνες, πχ:
        cardPanel.add(new BudgetPanel(this), "budget");
        // cardPanel.add(new ChangesPanel(this), "changes");

        add(cardPanel);
        setLocationRelativeTo(null);
    }

    // Μέθοδος για εναλλαγή panels
    public void switchTo(String panelName) {
        cardLayout.show(cardPanel, panelName);
    }

    public String getRevenueCode() {
        return revenuePanel.getCode2(); // παίρνω τον κωδικό από το panel
    }
}
