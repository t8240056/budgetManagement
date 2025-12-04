package auebprogramming;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

    private final  CardLayout cardLayout;
    private final JPanel cardPanel;
    private final RevenuePanel revenuePanel;
    private final Revenue2Panel revenue2Panel;
    private final Revenue3Panel revenue3Panel;

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
        String revcode2 = revenuePanel.getCode2();
        revenue2Panel= new Revenue2Panel(this, revcode2);
        cardPanel.add(revenue2Panel, "revenue2panel");
        String revcode3 = revenue2Panel.getCode3();
        revenue3Panel = new Revenue3Panel(this, revcode3);
        cardPanel.add(revenue3Panel, "revenue3panel");


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
    public String getcode() {
        return revenuePanel.getCode2();
    }
    public String getcode2() {
        return revenue2Panel.getCode3();
    }


}
