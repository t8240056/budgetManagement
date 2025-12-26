package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

/**
 * Panel for selecting an organization (ministry) for expense data.
 */
public final class MinistriesSelectionPanel extends JPanel {

    /** Reference to main frame for navigation. */
    private final MainFrame frame;

    /** Budget manager reference. */
    private final BudgetManager manager;

    /** List of ministries. */
    private final JList<String> ministriesList;

    /**
     * Constructs the MinistriesSelectionPanel.
     *
     * @param frame   the main application frame
     * @param manager the budget manager
     */
    public MinistriesSelectionPanel(final MainFrame frame,
                                    final BudgetManager manager) {
        this.frame = frame;
        this.manager = manager;
        setLayout(new BorderLayout());

        // Title
        add(createTitlePanel(), BorderLayout.NORTH);

        // Center list
        final List<String> ministries = loadMinistries();
        this.ministriesList = new JList<>(
                ministries.toArray(new String[0]));
        setupList();

        add(new JScrollPane(ministriesList), BorderLayout.CENTER);

        // Bottom buttons
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates the title panel.
     *
     * @return JPanel containing the title label
     */
    private JPanel createTitlePanel() {
        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        final JLabel title = new JLabel(
                "Επιλέξτε Φορέα για Προβολή Εξόδων",
                SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        panel.add(title);
        return panel;
    }

    /**
     * Loads the ministries list from the BudgetManager.
     *
     * @return list of ministries
     */
    private List<String> loadMinistries() {
        try {
            return manager.getMinistriesList();
        } catch (final AppException ex) {
            AppException.showError(ex.getMessage());
            return List.of();
        }
    }

    /**
     * Configures the ministries JList.
     */
    private void setupList() {
        ministriesList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        ministriesList.setFont(new Font("Arial", Font.PLAIN, 16));
        ministriesList.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Creates the bottom button panel.
     *
     * @return JPanel with confirm and back buttons
     */
    private JPanel createBottomPanel() {
        final JPanel panel = new JPanel(
                new GridLayout(1, 2, 10, 10));
        panel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(200, 70));

        final JButton confirmButton = new JButton("Επιβεβαίωση");
        final JButton backButton = new JButton("Επιστροφή");

        frame.confButtonColors(confirmButton);
        frame.backButtonColors(backButton);

        confirmButton.addActionListener(event -> onConfirm());
        backButton.addActionListener(event ->
                frame.switchTo("menu"));

        panel.add(confirmButton);
        panel.add(backButton);
        return panel;
    }

    /**
     * Handles confirmation logic.
     */
    private void onConfirm() {
        final String selected = ministriesList.getSelectedValue();

        if (selected == null) {
            AppException.showError("Παρακαλώ επιλέξτε έναν φορέα.");
            return;
        }

        final String orgCode = selected.split(" - ")[0];

        try {
            manager.loadOrganizationExpenses(orgCode, null);
            frame.switchTo("changesMenu");
        } catch (final AppException ex) {
            AppException.showError(ex.getMessage());
        }
    }
}
