package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Panel for selecting the budget year at application startup.
 * The selected year is stored in {@link Year}.
 */
public final class YearSelectionPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs the year selection panel.
     *
     * @param mainFrame the main application frame
     */
    public YearSelectionPanel(final MainFrame mainFrame) {
        super(new BorderLayout(10, 10));

        final JLabel titleLabel = createTitleLabel();
        final JComboBox<Integer> yearComboBox = createYearComboBox();
        final JButton confirmButton =
            createConfirmButton(mainFrame, yearComboBox);

        add(titleLabel, BorderLayout.NORTH);
        add(createCenterPanel(yearComboBox), BorderLayout.CENTER);
        add(confirmButton, BorderLayout.SOUTH);
    }

    /**
     * Creates the title label.
     *
     * @return the title label
     */
    private JLabel createTitleLabel() {
        final JLabel label = new JLabel(
            "Επιλέξτε Έτος Κρατικού Προϋπολογισμού",
            SwingConstants.CENTER
        );
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    /**
     * Creates the year selection combo box.
     *
     * @return the year combo box
     */
    private JComboBox<Integer> createYearComboBox() {
        return new JComboBox<>(new Integer[] {2025, 2026});
    }

    /**
     * Creates the confirmation button.
     *
     * @param mainFrame the main frame
     * @param yearComboBox the combo box with years
     * @return the confirm button
     */
    private JButton createConfirmButton(
        final MainFrame mainFrame,
        final JComboBox<Integer> yearComboBox
    ) {
        final JButton button = new JButton("Συνέχεια");

        button.addActionListener(event -> {
            final Integer selectedYear =
                (Integer) yearComboBox.getSelectedItem();

            if (selectedYear != null) {
                Year.SELECTED_YEAR = selectedYear;
                mainFrame.switchTo("menu");
            } else {
                AppException.showError("Επιλέξτε ένα έτος");
            }
        });

        return button;
    }

    /**
     * Creates the center panel containing the combo box.
     *
     * @param yearComboBox the year combo box
     * @return the center panel
     */
    private JPanel createCenterPanel(
        final JComboBox<Integer> yearComboBox
    ) {
        final JPanel panel = new JPanel();
        panel.add(new JLabel("Έτος:"));
        panel.add(yearComboBox);
        return panel;
    }
}
