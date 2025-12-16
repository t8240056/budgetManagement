package auebprogramming;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The main application window (Frame).
 * Manages the CardLayout and navigation between different panels.
 */
public final class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    // Fixed Panels
    private final RevenuePanel revenuePanel;
    private final ExpenseByAgencySummaryPanel agencySummaryPanel;
    private final AgencyDetailsPanel agencyDetailsPanel;
    private final ExpenseByCategoryPanel expenseByCategoryPanel;
    private final InsertChangePanel insertChangePanel;
    private AbsoluteChangePanel absoluteChangePanel;

    // Logic Controllers
    private final BudgetAnalyzer budgetAnalyzer;
    private final PercentageChangePanel percentageChangePanel;
    private final BudgetManager budgetManager;

    // Dynamic Panels (re-created on navigation)
    private Revenue2Panel revenue2Panel;
    private Revenue3Panel revenue3Panel;
    private Revenue4Panel revenue4Panel;
    private ExpenseByCategory2Panel expenseByCategory2Panel;

    /**
     * Constructs the MainFrame and initializes all sub-panels.
     */
    public MainFrame() {
        setTitle("Διαχείριση Κρατικού Προϋπολογισμού");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Δημιουργία CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Logic Initialization
        this.budgetAnalyzer = new BudgetAnalyzer();
        this.budgetManager = new BudgetManager();

        // Panel Initialization
        agencySummaryPanel = new ExpenseByAgencySummaryPanel(this,
                budgetAnalyzer);
        agencyDetailsPanel = new AgencyDetailsPanel(this, budgetAnalyzer);
        revenuePanel = new RevenuePanel(this);
        expenseByCategoryPanel = new ExpenseByCategoryPanel(this);
        insertChangePanel = new InsertChangePanel(this, budgetManager);
        absoluteChangePanel = new AbsoluteChangePanel(this, budgetManager);

        // Add panels to CardLayout
        cardPanel.add(new MenuPanel(this), "menu");
        cardPanel.add(new ExpensePanel(this), "expensePanel");
        cardPanel.add(agencySummaryPanel, "expenseByAgency");
        cardPanel.add(agencyDetailsPanel, "agencyDetails");
        cardPanel.add(revenuePanel, "revenuePanel");
        cardPanel.add(expenseByCategoryPanel, "expenseByCategory");

        // Future/Placeholder panels
        cardPanel.add(new BudgetPanel(this), "budget");
        cardPanel.add(insertChangePanel, "insert");
        cardPanel.add(new ChangeMenuPanel(this, budgetManager), "changesMenu");
        cardPanel.add(absoluteChangePanel, "absoluteChange");
        this.absoluteChangePanel = new AbsoluteChangePanel(this, budgetManager);
        cardPanel.add(this.absoluteChangePanel, "absoluteChange");
        this.percentageChangePanel = new PercentageChangePanel(this, budgetManager);
        cardPanel.add(this.percentageChangePanel, "percentageChange");


        add(cardPanel);
        setLocationRelativeTo(null);
    }

    /**
     * Switches the visible panel in the CardLayout.
     *
     * @param panelName the name of the panel to switch to
     */
    public void switchTo(final String panelName) {
        cardLayout.show(cardPanel, panelName);
    }

    /**
     * Switches to the Agency Details panel and loads data.
     *
     * @param code the agency code
     */
    public void showAgencyDetails(final int code) {
        agencyDetailsPanel.loadDetails(code);
        switchTo("agencyDetails");
    }

    /**
     * Switches to the Revenue2 panel and loads data for the code.
     *
     * @param code the revenue code
     */
    public void showRevenue2(final String code) {
        // Remove old panel if exists
        if (revenue2Panel != null) {
            cardPanel.remove(revenue2Panel);
        }

        // Create new panel
        revenue2Panel = new Revenue2Panel(this, code);

        cardPanel.add(revenue2Panel, "revenue2panel");
        cardPanel.revalidate();
        cardPanel.repaint();

        switchTo("revenue2panel");
    }

    /**
     * Switches to the Revenue3 panel and loads data for the code.
     *
     * @param code the revenue code
     */
    public void showRevenue3(final String code) {
        if (revenue3Panel != null) {
            cardPanel.remove(revenue3Panel);
        }

        revenue3Panel = new Revenue3Panel(this, code);

        cardPanel.add(revenue3Panel, "revenue3panel");
        cardPanel.revalidate();
        cardPanel.repaint();

        switchTo("revenue3panel");
    }

    /**
     * Displays the fourth-level revenue analysis panel.
     *
     * @param code the four-digit revenue code to analyze
     */
    public void showRevenue4(final String code) {
        if (revenue4Panel != null) {
            cardPanel.remove(revenue4Panel);
        }

        revenue4Panel = new Revenue4Panel(this, code);

        cardPanel.add(revenue4Panel, "revenue4panel");
        cardPanel.revalidate();
        cardPanel.repaint();

        switchTo("revenue4panel");
    }

    /**
     * Shows the panel with detailed expense reports.
     *
     * @param codesString the codes entered by the user
     */
    public void showExpenseCategory2(final String codesString) {
        if (expenseByCategory2Panel != null) {
            cardPanel.remove(expenseByCategory2Panel);
        }

        expenseByCategory2Panel = new ExpenseByCategory2Panel(this,
                codesString);

        cardPanel.add(expenseByCategory2Panel, "expenseCategory2");
        cardPanel.revalidate();
        cardPanel.repaint();

        switchTo("expenseCategory2");
    }

    /**
     * Shows the panel of ViewEntries.
     */
    public void openViewEntriesPanel() {
        final ViewEntriesPanel panel = new ViewEntriesPanel(this,
                budgetManager);

        cardPanel.add(panel, "viewEntries");
        cardLayout.show(cardPanel, "viewEntries");

        if (budgetManager.getEntriesList().isEmpty()) {
            AppException.showError("Δεν έχουν φορτωθεί δεδομένα.");
        }
    }

    /**
     * Shows the Audit Log panel.
     */
    public void showAuditLogPanel() {
        final ViewAuditLogPanel panel = new ViewAuditLogPanel(this,
                budgetManager);

        cardPanel.add(panel, "auditLog");
        cardLayout.show(cardPanel, "auditLog");
    }

    /**
     * Applies custom styling to a Confirm JButton (Green).
     *
     * @param confirmButton the button to style
     */
    public void confButtonColors(final JButton confirmButton) {
        confirmButton.setBackground(Color.GREEN);
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setOpaque(true);
    }

    /**
     * Applies custom styling to a Back JButton (Yellow).
     *
     * @param backButton the button to style
     */
    public void backButtonColors(final JButton backButton) {
        backButton.setBackground(Color.YELLOW);
        backButton.setForeground(Color.BLACK);
        backButton.setOpaque(true);
    }
}
