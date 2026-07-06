package MotorPHEmployeeApp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * EmployeeInputFrame - Add or Edit an Employee Record
 * Supports both "Add New" and "Edit Existing" modes.
 */
public class EmployeeInputFrame extends JFrame {

    // ── Fields ────────────────────────────────────────────────────────────────
    private JTextField txtEmpNo, txtName, txtRate, txtHours, txtDeductions;
    private JLabel lblGrossPay, lblNetPay;
    private JButton btnCompute, btnSave, btnCancel, btnClear;

    private static final String CSV_FILE = "mph_employees_record.csv";
    private boolean editMode = false;
    private String originalEmpNo = "";

    // ── Constructors ──────────────────────────────────────────────────────────

    /** Open in Add-New mode */
    public EmployeeInputFrame() {
        this(null);
    }

    /** Open in Edit mode pre-filled with an existing employee */
    public EmployeeInputFrame(String[] employeeData) {
        if (employeeData != null && employeeData.length >= 5) {
            this.editMode = true;
            this.originalEmpNo = employeeData[0].trim();
        }
        initComponents();
        if (editMode) populateFields(employeeData);
        setVisible(true);
    }

    // ── UI Setup ──────────────────────────────────────────────────────────────

    private void initComponents() {
        setTitle(editMode ? "Edit Employee Record" : "Add New Employee");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(480, 540);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(new Color(245, 247, 250));

        // ── Header ──
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 60, 114));
        headerPanel.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblTitle = new JLabel(editMode ? "✎  Edit Employee" : "＋  Add New Employee");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // ── Form Panel ──
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 215, 225), 1, true),
                new EmptyBorder(18, 22, 18, 22)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 6, 7, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Employee No
        txtEmpNo = new JTextField(20);
        addFormRow(formPanel, gbc, 0, "Employee No. *", txtEmpNo);

        // Full Name
        txtName = new JTextField(20);
        addFormRow(formPanel, gbc, 1, "Full Name *", txtName);

        // Hourly Rate
        txtRate = new JTextField(20);
        addFormRow(formPanel, gbc, 2, "Hourly Rate (₱) *", txtRate);

        // Hours Worked
        txtHours = new JTextField(20);
        addFormRow(formPanel, gbc, 3, "Hours Worked *", txtHours);

        // Deductions
        txtDeductions = new JTextField(20);
        txtDeductions.setText("0.00");
        addFormRow(formPanel, gbc, 4, "Deductions (₱)", txtDeductions);

        // Separator
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 4, 0);
        formPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(7, 6, 7, 6);

        // Gross Pay (read-only)
        lblGrossPay = makeResultLabel("—");
        addFormRow(formPanel, gbc, 6, "Gross Pay (₱)", lblGrossPay);

        // Net Pay (read-only)
        lblNetPay = makeResultLabel("—");
        addFormRow(formPanel, gbc, 7, "Net Pay (₱)", lblNetPay);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // ── Button Panel ──
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setBackground(new Color(245, 247, 250));
        btnPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        btnCompute = makeButton("Compute", new Color(52, 131, 235));
        btnSave    = makeButton("Save",    new Color(34, 139, 87));
        btnClear   = makeButton("Clear",   new Color(160, 105, 30));
        btnCancel  = makeButton("Cancel",  new Color(180, 50, 50));

        btnPanel.add(btnCompute);
        btnPanel.add(btnSave);
        btnPanel.add(btnClear);
        btnPanel.add(btnCancel);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // ── Listeners ──
        btnCompute.addActionListener(e -> computeSalary());
        btnSave.addActionListener(e -> saveRecord());
        btnClear.addActionListener(e -> clearFields());
        btnCancel.addActionListener(e -> dispose());

        // Auto-compute on field changes
        KeyAdapter autoCompute = new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { tryAutoCompute(); }
        };
        txtRate.addKeyListener(autoCompute);
        txtHours.addKeyListener(autoCompute);
        txtDeductions.addKeyListener(autoCompute);
    }

    // ── Helper: add a label + component row ──────────────────────────────────

    private void addFormRow(JPanel panel, GridBagConstraints gbc,
                            int row, String labelText, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.32;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(55, 65, 90));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.68;
        if (field instanceof JTextField) {
            JTextField tf = (JTextField) field;
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tf.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(180, 190, 210), 1, true),
                    new EmptyBorder(4, 8, 4, 8)));
        }
        panel.add(field, gbc);
    }

    private JLabel makeResultLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(30, 100, 60));
        lbl.setBorder(new EmptyBorder(2, 8, 2, 8));
        return lbl;
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(90, 34));
        return btn;
    }

    // ── Business Logic ────────────────────────────────────────────────────────

    private void tryAutoCompute() {
        try {
            double rate = Double.parseDouble(txtRate.getText().trim());
            double hours = Double.parseDouble(txtHours.getText().trim());
            double deductions = txtDeductions.getText().trim().isEmpty()
                    ? 0 : Double.parseDouble(txtDeductions.getText().trim());
            double gross = rate * hours;
            double net = gross - deductions;
            lblGrossPay.setText(String.format("₱ %,.2f", gross));
            lblNetPay.setText(String.format("₱ %,.2f", net));
            lblNetPay.setForeground(net < 0 ? Color.RED : new Color(30, 100, 60));
        } catch (NumberFormatException ignored) {
            lblGrossPay.setText("—");
            lblNetPay.setText("—");
        }
    }

    private void computeSalary() {
        try {
            double rate       = parsePositive(txtRate, "Hourly Rate");
            double hours      = parsePositive(txtHours, "Hours Worked");
            double deductions = txtDeductions.getText().trim().isEmpty()
                    ? 0 : parseNonNegative(txtDeductions, "Deductions");
            double gross = rate * hours;
            double net   = gross - deductions;
            lblGrossPay.setText(String.format("₱ %,.2f", gross));
            lblNetPay.setText(String.format("₱ %,.2f", net));
            lblNetPay.setForeground(net < 0 ? Color.RED : new Color(30, 100, 60));
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void saveRecord() {
        // Validate required fields
        String empNo = txtEmpNo.getText().trim();
        String name  = txtName.getText().trim();
        if (empNo.isEmpty()) { showError("Employee No. is required."); txtEmpNo.requestFocus(); return; }
        if (name.isEmpty())  { showError("Full Name is required.");    txtName.requestFocus();  return; }

        double rate, hours, deductions, gross, net;
        try {
            rate       = parsePositive(txtRate, "Hourly Rate");
            hours      = parsePositive(txtHours, "Hours Worked");
            deductions = txtDeductions.getText().trim().isEmpty()
                    ? 0 : parseNonNegative(txtDeductions, "Deductions");
        } catch (IllegalArgumentException e) { showError(e.getMessage()); return; }

        gross = rate * hours;
        net   = gross - deductions;

        // Duplicate check (only in Add mode)
        if (!editMode && employeeExists(empNo)) {
            showError("Employee No. " + empNo + " already exists.\nUse Edit mode to update.");
            return;
        }

        String newLine = String.join(",",
                empNo, name,
                String.format("%.2f", rate),
                String.format("%.2f", hours),
                String.format("%.2f", deductions),
                String.format("%.2f", gross),
                String.format("%.2f", net));

        try {
            if (editMode) {
                rewriteCSV(originalEmpNo, newLine);
            } else {
                appendToCSV(newLine);
            }
            JOptionPane.showMessageDialog(this,
                    (editMode ? "Record updated" : "Employee added") + " successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IOException e) {
            showError("Could not save to CSV:\n" + e.getMessage());
        }
    }

    private boolean employeeExists(String empNo) {
        File f = new File(CSV_FILE);
        if (!f.exists()) return false;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",");
                if (cols.length > 0 && cols[0].trim().equalsIgnoreCase(empNo)) return true;
            }
        } catch (IOException ignored) {}
        return false;
    }

    private void appendToCSV(String line) throws IOException {
        File f = new File(CSV_FILE);
        boolean needHeader = !f.exists() || f.length() == 0;
        try (PrintWriter pw = new PrintWriter(new FileWriter(f, true))) {
            if (needHeader)
                pw.println("EmpNo,Name,Rate,HoursWorked,Deductions,GrossPay,NetPay");
            pw.println(line);
        }
    }

    private void rewriteCSV(String targetEmpNo, String replacement) throws IOException {
        File f = new File(CSV_FILE);
        List<String> lines = new ArrayList<>();
        if (f.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String l;
                while ((l = br.readLine()) != null) lines.add(l);
            }
        }
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] cols = lines.get(i).split(",");
            if (cols.length > 0 && cols[0].trim().equalsIgnoreCase(targetEmpNo)) {
                lines.set(i, replacement);
                found = true;
                break;
            }
        }
        if (!found) lines.add(replacement);
        try (PrintWriter pw = new PrintWriter(new FileWriter(f, false))) {
            for (String l : lines) pw.println(l);
        }
    }

    private void clearFields() {
        txtEmpNo.setText(""); txtName.setText("");
        txtRate.setText(""); txtHours.setText("");
        txtDeductions.setText("0.00");
        lblGrossPay.setText("—"); lblNetPay.setText("—");
        txtEmpNo.requestFocus();
    }

    private void populateFields(String[] data) {
        // data: EmpNo, Name, Rate, HoursWorked, Deductions, GrossPay, NetPay
        if (data.length > 0) txtEmpNo.setText(data[0].trim());
        if (data.length > 1) txtName.setText(data[1].trim());
        if (data.length > 2) txtRate.setText(data[2].trim());
        if (data.length > 3) txtHours.setText(data[3].trim());
        if (data.length > 4) txtDeductions.setText(data[4].trim());
        if (data.length > 5) lblGrossPay.setText("₱ " + data[5].trim());
        if (data.length > 6) lblNetPay.setText("₱ " + data[6].trim());
        txtEmpNo.setEditable(false); // Can't change primary key in edit mode
        txtEmpNo.setBackground(new Color(235, 237, 242));
    }

    // ── Validation helpers ────────────────────────────────────────────────────

    private double parsePositive(JTextField f, String label) {
        try {
            double v = Double.parseDouble(f.getText().trim());
            if (v <= 0) throw new IllegalArgumentException(label + " must be > 0.");
            return v;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(label + " must be a valid number.");
        }
    }

    private double parseNonNegative(JTextField f, String label) {
        try {
            double v = Double.parseDouble(f.getText().trim());
            if (v < 0) throw new IllegalArgumentException(label + " cannot be negative.");
            return v;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(label + " must be a valid number.");
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    // ── Quick test ────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeInputFrame::new);
    }
}
