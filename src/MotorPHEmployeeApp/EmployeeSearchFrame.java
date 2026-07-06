package MotorPHEmployeeApp;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * EmployeeSearchFrame
 * ─────────────────────────────────────────────────────────────
 * Allows payroll staff to search, view, edit, and delete
 * employee records stored in mph_employees_record.csv.
 *
 * NEW FILE — Feature complement to EmployeeInputFrame.
 */
public class EmployeeSearchFrame extends JFrame {

    private static final String CSV_FILE = "mph_employees_record.csv";
    private static final String[] COLUMNS =
            {"Emp No", "Name", "Rate (₱)", "Hours", "Deductions (₱)", "Gross Pay (₱)", "Net Pay (₱)"};

    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnSearch, btnEdit, btnDelete, btnRefresh, btnClose;
    private JLabel lblStatus;

    // ── Constructor ───────────────────────────────────────────────────────────

    public EmployeeSearchFrame() {
        initUI();
        loadAllRecords();
        setVisible(true);
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    private void initUI() {
        setTitle("Employee Search");
        setSize(820, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(new EmptyBorder(0, 0, 0, 0));
        root.setBackground(new Color(240, 243, 248));

        // ── Header ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 60, 114));
        header.setBorder(new EmptyBorder(12, 18, 12, 18));
        JLabel title = new JLabel("🔍  Employee Search");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        // ── Search Bar ──
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchPanel.setBackground(new Color(240, 243, 248));
        searchPanel.setBorder(new EmptyBorder(0, 12, 0, 12));

        txtSearch = new JTextField(28);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.putClientProperty("JTextField.placeholderText", "Search by name or Emp No...");
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 190, 210), 1, true),
                new EmptyBorder(5, 10, 5, 10)));

        btnSearch  = makeBtn("Search",  new Color(52, 131, 235));
        btnRefresh = makeBtn("Refresh", new Color(100, 116, 139));
        btnEdit    = makeBtn("Edit",    new Color(34, 139, 87));
        btnDelete  = makeBtn("Delete",  new Color(180, 50, 50));
        btnClose   = makeBtn("Close",   new Color(80, 80, 80));

        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(btnEdit);
        searchPanel.add(btnDelete);
        searchPanel.add(btnClose);
        root.add(searchPanel, BorderLayout.PAGE_START);

        // ── Table ──
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(26);
        table.setGridColor(new Color(215, 220, 230));
        table.setSelectionBackground(new Color(52, 131, 235, 80));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(30, 60, 114));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Column widths
        int[] widths = {70, 180, 90, 70, 110, 110, 100};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(0, 12, 0, 12));
        root.add(scroll, BorderLayout.CENTER);

        // ── Status Bar ──
        lblStatus = new JLabel(" Ready");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(80, 90, 110));
        lblStatus.setBorder(new EmptyBorder(5, 14, 5, 14));
        root.add(lblStatus, BorderLayout.SOUTH);

        add(root);

        // ── Listeners ──
        btnSearch.addActionListener(e -> searchRecords());
        btnRefresh.addActionListener(e -> { txtSearch.setText(""); loadAllRecords(); });
        btnClose.addActionListener(e -> dispose());

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) searchRecords();
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            boolean sel = table.getSelectedRow() >= 0;
            btnEdit.setEnabled(sel);
            btnDelete.setEnabled(sel);
        });

        btnEdit.addActionListener(e -> openEditFrame());
        btnDelete.addActionListener(e -> deleteSelectedRecord());
    }

    // ── Data ─────────────────────────────────────────────────────────────────

    private List<String[]> readCSV() {
        List<String[]> rows = new ArrayList<>();
        File f = new File(CSV_FILE);
        if (!f.exists()) return rows;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; } // skip header
                String[] cols = line.split(",", -1);
                if (cols.length >= 7) rows.add(cols);
            }
        } catch (IOException e) {
            showError("Cannot read CSV: " + e.getMessage());
        }
        return rows;
    }

    private void loadAllRecords() {
        populateTable(readCSV());
        lblStatus.setText(" " + tableModel.getRowCount() + " record(s) loaded.");
    }

    private void searchRecords() {
        String query = txtSearch.getText().trim().toLowerCase();
        if (query.isEmpty()) { loadAllRecords(); return; }
        List<String[]> filtered = new ArrayList<>();
        for (String[] row : readCSV()) {
            if (row[0].toLowerCase().contains(query) || row[1].toLowerCase().contains(query))
                filtered.add(row);
        }
        populateTable(filtered);
        lblStatus.setText(" " + filtered.size() + " result(s) found for: \"" + txtSearch.getText().trim() + "\"");
    }

    private void populateTable(List<String[]> data) {
        tableModel.setRowCount(0);
        for (String[] row : data) {
            // Format currency columns
            String[] display = Arrays.copyOf(row, 7);
            for (int i : new int[]{2, 4, 5, 6}) {
                try { display[i] = String.format("%,.2f", Double.parseDouble(row[i].trim())); }
                catch (Exception ignored) {}
            }
            tableModel.addRow(display);
        }
    }

    private void openEditFrame() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        // Fetch raw data from CSV by EmpNo
        String empNo = (String) tableModel.getValueAt(row, 0);
        for (String[] raw : readCSV()) {
            if (raw[0].trim().equalsIgnoreCase(empNo)) {
                new EmployeeInputFrame(raw);
                // Refresh after closing
                SwingUtilities.invokeLater(() -> {
                    try { Thread.sleep(300); } catch (InterruptedException ignored) {}
                    loadAllRecords();
                });
                return;
            }
        }
    }

    private void deleteSelectedRecord() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        String empNo = (String) tableModel.getValueAt(row, 0);
        String name  = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete employee " + empNo + " – " + name + "?\nThis cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        File f = new File(CSV_FILE);
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",", -1);
                if (cols[0].trim().equalsIgnoreCase(empNo)) continue; // skip deleted
                lines.add(line);
            }
        } catch (IOException e) { showError(e.getMessage()); return; }

        try (PrintWriter pw = new PrintWriter(new FileWriter(f, false))) {
            for (String l : lines) pw.println(l);
        } catch (IOException e) { showError(e.getMessage()); return; }

        JOptionPane.showMessageDialog(this, "Record deleted successfully.", "Deleted",
                JOptionPane.INFORMATION_MESSAGE);
        loadAllRecords();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JButton makeBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(82, 30));
        return b;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeSearchFrame::new);
    }
}
