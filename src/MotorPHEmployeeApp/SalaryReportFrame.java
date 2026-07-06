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
 * SalaryReportFrame
 * ─────────────────────────────────────────────────────────────
 * Generates a summary salary report from mph_employees_record.csv.
 * Displays total gross pay, total net pay, total deductions,
 * highest/lowest earner, and average salary statistics.
 *
 * NEW FILE — Analytics / reporting module.
 */
public class SalaryReportFrame extends JFrame {

    private static final String CSV_FILE = "mph_employees_record.csv";

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblTotalGross, lblTotalNet, lblTotalDeductions;
    private JLabel lblHighest, lblLowest, lblAvgNet, lblHeadCount;
    private JButton btnExport, btnClose, btnRefresh;

    private static final String[] COLUMNS =
            {"Emp No", "Name", "Rate (₱)", "Hours", "Gross Pay (₱)", "Deductions (₱)", "Net Pay (₱)", "Status"};

    // ── Constructor ───────────────────────────────────────────────────────────

    public SalaryReportFrame() {
        initUI();
        loadReport();
        setVisible(true);
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    private void initUI() {
        setTitle("Salary Report — MotorPH");
        setSize(900, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(new Color(240, 243, 248));

        // ── Header ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 60, 114));
        header.setBorder(new EmptyBorder(13, 18, 13, 18));
        JLabel title = new JLabel("📊  Salary Report");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        JLabel subtitle = new JLabel("MotorPH Employee Payroll Summary");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(180, 200, 230));
        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setOpaque(false);
        titleGroup.add(title);
        titleGroup.add(subtitle);
        header.add(titleGroup, BorderLayout.WEST);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        toolbar.setOpaque(false);
        btnRefresh = makeBtn("↺ Refresh", new Color(52, 100, 180));
        btnExport  = makeBtn("⬇ Export CSV", new Color(34, 139, 87));
        btnClose   = makeBtn("✕ Close",   new Color(160, 50, 50));
        toolbar.add(btnRefresh); toolbar.add(btnExport); toolbar.add(btnClose);
        header.add(toolbar, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // ── Stats Cards ──
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 10, 0));
        statsRow.setBackground(new Color(240, 243, 248));
        statsRow.setBorder(new EmptyBorder(12, 14, 6, 14));

        lblHeadCount      = new JLabel("—", SwingConstants.CENTER);
        lblTotalGross     = new JLabel("—", SwingConstants.CENTER);
        lblTotalDeductions= new JLabel("—", SwingConstants.CENTER);
        lblTotalNet       = new JLabel("—", SwingConstants.CENTER);

        statsRow.add(makeCard("Employees", lblHeadCount,      new Color(30, 60, 114)));
        statsRow.add(makeCard("Total Gross Pay", lblTotalGross,      new Color(34, 100, 60)));
        statsRow.add(makeCard("Total Deductions", lblTotalDeductions, new Color(160, 80, 30)));
        statsRow.add(makeCard("Total Net Pay",   lblTotalNet,        new Color(100, 30, 130)));
        root.add(statsRow, BorderLayout.PAGE_START);

        // ── Table ──
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(26);
        table.setGridColor(new Color(215, 220, 230));
        table.setSelectionBackground(new Color(52, 131, 235, 70));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(50, 80, 140));
        table.getTableHeader().setForeground(Color.WHITE);

        // Column widths
        int[] widths = {65, 175, 80, 65, 110, 110, 110, 75};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Color "Status" column
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int r, int c) {
                super.getTableCellRendererComponent(t, val, sel, focus, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                String v = val == null ? "" : val.toString();
                setForeground("Regular".equals(v) ? new Color(30, 130, 60) : new Color(160, 50, 50));
                setFont(getFont().deriveFont(Font.BOLD));
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(0, 14, 0, 14));
        root.add(scroll, BorderLayout.CENTER);

        // ── Bottom Stats ──
        JPanel bottomStats = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 8));
        bottomStats.setBackground(new Color(228, 232, 240));
        bottomStats.setBorder(new EmptyBorder(4, 14, 4, 14));

        lblHighest = makeStatLabel("Highest: —");
        lblLowest  = makeStatLabel("Lowest: —");
        lblAvgNet  = makeStatLabel("Avg Net Pay: —");

        bottomStats.add(lblHighest);
        bottomStats.add(new JLabel("|"));
        bottomStats.add(lblLowest);
        bottomStats.add(new JLabel("|"));
        bottomStats.add(lblAvgNet);
        root.add(bottomStats, BorderLayout.SOUTH);

        add(root);

        // ── Listeners ──
        btnRefresh.addActionListener(e -> loadReport());
        btnClose.addActionListener(e -> dispose());
        btnExport.addActionListener(e -> exportToCSV());
    }

    private JPanel makeCard(String label, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 2));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(accent, 2, true),
                new EmptyBorder(10, 14, 10, 14)));

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(90, 100, 120));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(accent);

        card.add(lbl);
        card.add(valueLabel);
        return card;
    }

    private JLabel makeStatLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(new Color(50, 60, 80));
        return l;
    }

    // ── Data ─────────────────────────────────────────────────────────────────

    private void loadReport() {
        tableModel.setRowCount(0);
        double sumGross = 0, sumNet = 0, sumDed = 0;
        double maxNet = Double.NEGATIVE_INFINITY, minNet = Double.POSITIVE_INFINITY;
        String maxName = "—", minName = "—";
        int count = 0;

        File f = new File(CSV_FILE);
        if (!f.exists()) {
            JOptionPane.showMessageDialog(this,
                    "CSV file not found: " + CSV_FILE,
                    "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] c = line.split(",", -1);
                if (c.length < 7) continue;
                try {
                    double rate  = Double.parseDouble(c[2].trim());
                    double hours = Double.parseDouble(c[3].trim());
                    double ded   = Double.parseDouble(c[4].trim());
                    double gross = Double.parseDouble(c[5].trim());
                    double net   = Double.parseDouble(c[6].trim());

                    sumGross += gross; sumNet += net; sumDed += ded;

                    if (net > maxNet) { maxNet = net; maxName = c[1].trim(); }
                    if (net < minNet) { minNet = net; minName = c[1].trim(); }
                    count++;

                    String status = hours >= 160 ? "Regular" : "Part-Time";

                    tableModel.addRow(new Object[]{
                            c[0].trim(), c[1].trim(),
                            String.format("%,.2f", rate),
                            String.format("%.1f", hours),
                            String.format("%,.2f", gross),
                            String.format("%,.2f", ded),
                            String.format("%,.2f", net),
                            status
                    });
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        lblHeadCount.setText(String.valueOf(count));
        lblTotalGross.setText(String.format("₱ %,.2f", sumGross));
        lblTotalDeductions.setText(String.format("₱ %,.2f", sumDed));
        lblTotalNet.setText(String.format("₱ %,.2f", sumNet));

        double avg = count > 0 ? sumNet / count : 0;
        lblHighest.setText("Highest Net: ₱" + String.format("%,.2f", maxNet) + " (" + maxName + ")");
        lblLowest.setText("Lowest Net: ₱"  + String.format("%,.2f", minNet)  + " (" + minName + ")");
        lblAvgNet.setText("Avg Net Pay: ₱" + String.format("%,.2f", avg));
    }

    private void exportToCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("salary_report.csv"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        try (PrintWriter pw = new PrintWriter(new FileWriter(fc.getSelectedFile()))) {
            pw.println(String.join(",", COLUMNS));
            for (int r = 0; r < tableModel.getRowCount(); r++) {
                List<String> row = new ArrayList<>();
                for (int c = 0; c < tableModel.getColumnCount(); c++)
                    row.add(tableModel.getValueAt(r, c).toString());
                pw.println(String.join(",", row));
            }
            JOptionPane.showMessageDialog(this, "Report exported to:\n" + fc.getSelectedFile().getAbsolutePath(),
                    "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Export failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        b.setPreferredSize(new Dimension(115, 30));
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SalaryReportFrame::new);
    }
}
