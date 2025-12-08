package auebprogramming;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public final class MainFrame extends JFrame {

    private final  CardLayout cardLayout;
    private final JPanel cardPanel;
    private final RevenuePanel revenuePanel;
    private Revenue2Panel revenue2Panel;
    private Revenue3Panel revenue3Panel;
    private Revenue4Panel revenue4Panel;
    private BudgetAnalyzer budgetAnalyzer;
    private final ExpenseByAgencySummaryPanel agencySummaryPanel;
    private final AgencyDetailsPanel agencyDetailsPanel;
    private final ExpenseByCategoryPanel expenseByCategoryPanel;

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
        expenseByCategoryPanel = new ExpenseByCategoryPanel(this);
        cardPanel.add(expenseByCategoryPanel,
         "expenseByCategory");
        String expcodes = expenseByCategoryPanel.getCodes();


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
    /**
     * Μεταβαίνει στο Panel revenue2 και φορτώνει τα στοιχεία του φορέα.
     * @param code ο κωδικός του φορέα
     */
    public void showRevenue2(String code) {

    // Αν υπάρχει παλιό panel, το αφαιρούμε
    if (revenue2Panel != null) {
        cardPanel.remove(revenue2Panel);
    }

    // Δημιουργούμε νέο panel με τον πραγματικό κωδικό
    revenue2Panel = new Revenue2Panel(this, code);

    cardPanel.add(revenue2Panel, "revenue2panel");
    cardPanel.revalidate();
    cardPanel.repaint();

    switchTo("revenue2panel");
}
/**
     * Μεταβαίνει στο Panel revenue3 και φορτώνει τα στοιχεία του φορέα.
     * @param code ο κωδικός του φορέα
     */
public void showRevenue3(String code) {

    // Αν υπάρχει παλιό panel, το αφαιρούμε
    if (revenue3Panel != null) {
        cardPanel.remove(revenue3Panel);
    }

    // Δημιουργούμε νέο panel με τον πραγματικό κωδικό
    revenue3Panel = new Revenue3Panel(this, code);

    // Προσθήκη στο CardLayout με το σωστό όνομα
    cardPanel.add(revenue3Panel, "revenue3panel");

    // Ανανεώσεις
    cardPanel.revalidate();
    cardPanel.repaint();

    // Εμφάνιση panel
    switchTo("revenue3panel");
}


}
