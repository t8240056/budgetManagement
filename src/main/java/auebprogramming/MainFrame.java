package auebprogramming;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public final class MainFrame extends JFrame {

    private final  CardLayout cardLayout;
    private final JPanel cardPanel;
    private final RevenuePanel revenuePanel;
    private final Revenue2Panel revenue2Panel;
    private final Revenue3Panel revenue3Panel;
    private final Revenue4Panel revenue4Panel;
    private final BudgetAnalyzer budgetAnalyzer;
    private final ExpenseByAgencySummaryPanel agencySummaryPanel;
    private final AgencyDetailsPanel agencyDetailsPanel;

    public MainFrame() {
        setTitle("Διαχείριση Κρατικού Προϋπολογισμού");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Δημιουργία CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        this.budgetAnalyzer = new BudgetAnalyzer();
        agencySummaryPanel = new ExpenseByAgencySummaryPanel(this, budgetAnalyzer);
        agencyDetailsPanel = new AgencyDetailsPanel(this, budgetAnalyzer);

        // Προσθήκη panels
        cardPanel.add(new MenuPanel(this), "menu");
        cardPanel.add(new ExpensePanel(this), "expensePanel");
        cardPanel.add(agencySummaryPanel, "expenseByAgency");
        cardPanel.add(agencyDetailsPanel, "agencyDetails");
        revenuePanel = new RevenuePanel(this);
        cardPanel.add(revenuePanel, "revenuePanel");
        String revcode2 = revenuePanel.getCode2();
        revenue2Panel= new Revenue2Panel(this, revcode2);
        cardPanel.add(revenue2Panel, "revenue2panel");
        String revcode3 = revenue2Panel.getCode3();
        revenue3Panel = new Revenue3Panel(this, revcode3);
        cardPanel.add(revenue3Panel, "revenue3panel");
        String revcode4 = revenue3Panel.getCode();
        revenue4Panel = new Revenue4Panel(this, revcode4);
        cardPanel.add(revenue4Panel,"revenue4panel");
        cardPanel.add(new ExpenseByCategoryPanel(this),
         "expenseByCategory");


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
    /**
     * Μεταβαίνει στο Panel ανάλυσης και φορτώνει τα στοιχεία του φορέα.
     * @param code ο κωδικός του φορέα.
     */
    public void showAgencyDetails(final int code) {
        agencyDetailsPanel.loadDetails(code);
        switchTo("agencyDetails");
    }
}
