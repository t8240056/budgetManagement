package auebprogramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter; // ΝΕΟ
import java.io.FileWriter;     // ΝΕΟ
import java.io.IOException;    // ΝΕΟ

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane; // ΝΕΟ
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel; // ΝΕΟ

public class RevenuePanelSaveChanges extends JPanel {

    private JTable revenueTable;
    private JTextField codeField;
    private JButton openCodeInputButton;
    private JButton confirmButton;
    private JButton backButton;
    private final MainFrame frame;
    
    // ΝΕΟ: Ορίζουμε το μονοπάτι του αρχείου σου εδώ (άλλαξέ το στο σωστό)
    private final String CSV_FILE_PATH = "revenue_categories2_2025.csv"; 

    public RevenuePanelSaveChanges(final MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        initializeTable();
        initializeCenter();
        initializeBottomButtons();
    }

    private void initializeTable() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        RevenueDataManager revdata = new RevenueDataManager();
        String[][] Data = revdata.get2DigitCodes();
        String[] columnNames = { "Κωδικός", "Κατηγορία" ,"Ποσό"};

        // ΣΗΜΕΙΩΣΗ: Από προεπιλογή αυτός ο πίνακας επιτρέπει την επεξεργασία (Edit).
        revenueTable = new JTable(Data, columnNames);
        
        JScrollPane scrollPane = new JScrollPane(revenueTable);
        scrollPane.setPreferredSize(new Dimension(400,500));
        topPanel.add(scrollPane);
        add(topPanel, BorderLayout.CENTER);
    }

    private void initializeCenter() {
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        openCodeInputButton = new JButton("Εισάγετε 2ψηφίο κωδικό προς περαιτέρω ανάλυση");
        codeField = new JTextField();
        codeField.setVisible(false);

        openCodeInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                codeField.setVisible(true);
                centerPanel.revalidate();
                centerPanel.repaint();
            }
        });

        centerPanel.add(openCodeInputButton);
        centerPanel.add(codeField);

        add(centerPanel, BorderLayout.NORTH);
    }

    private void initializeBottomButtons() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        backButton = new JButton("Επιστροφή");
        confirmButton = new JButton("Επιβεβαίωση");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                frame.switchTo("budget");
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                // ΝΕΟ: Πρώτα αποθηκεύουμε τις αλλαγές του πίνακα
                saveTableData(); 

                if (codeField.isVisible() && !codeField.getText().trim().isEmpty()) {
                     frame.switchTo("revenue2panel");
                } else {
                     // Προσοχή: Εδώ υποθέτω ότι υπάρχει η κλάση AppException
                     AppException.showError("Πληκτρολογήστε κωδικό ή πατήστε το κουμί επιστροφής");
                }
            }
        });

        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public String getCode2() {
        return codeField.getText().trim();
    }

    // --- ΝΕΑ ΜΕΘΟΔΟΣ ΓΙΑ ΑΠΟΘΗΚΕΥΣΗ ---
    private void saveTableData() {
        // Σταματάμε τυχόν ενεργό editing για να μην χάσουμε την τελευταία τιμή
        if (revenueTable.isEditing()) {
            revenueTable.getCellEditor().stopCellEditing();
        }

        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            TableModel model = revenueTable.getModel();

            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder rowString = new StringBuilder();
                for (int j = 0; j < model.getColumnCount(); j++) {
                    rowString.append(model.getValueAt(i, j));
                    if (j < model.getColumnCount() - 1) {
                        rowString.append(","); // Χωρίζουμε με κόμμα
                    }
                }
                bWriter.write(rowString.toString());
                bWriter.newLine();
            }
            // Προαιρετικό μήνυμα επιτυχίας
            JOptionPane.showMessageDialog(this, "Οι αλλαγές αποθηκεύτηκαν!"); 
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Σφάλμα αποθήκευσης: " + e.getMessage());
        }
    }
}
