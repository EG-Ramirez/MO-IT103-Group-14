package MotorPHEmployeeApp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * EmployeeInputFrame - Add or Edit an Employee Record
 * Collects the employee master data (Employee No., Name, government
 * numbers, Hourly Rate) and saves it through EmployeeFileManager, so
 * every record ends up in the same CSV format the rest of the system
 * already relies on. Payroll figures (hours, gross/net pay) are NOT
 * part of an employee record — those belong to payroll processing.
 */
public class EmployeeInputFrame extends JFrame {

    // ── Fields ────────────────────────────────────────────────────────────────
    private JTextField txtEmpNo, txtFirstName, txtLastName, txtRate;
    private JTextField txtSSS, txtPhilHealth, txtTIN, txtPagIbig;
    private JButton btnSave, btnCancel, btnClear;
    private Runnable onSaveCallback;

    private boolean editMode = false;
    private String originalEmpNo = "";

    // ── Constructors ──────────────────────────────────────────────────────────

    /** Open in Add-New mode */
    public EmployeeInputFrame() {
        this((String[]) null);
    }

    public EmployeeInputFrame(Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback ;
        initComponents();
        setVisible(true);
    }    

    /** Open in Edit mode pre-filled with an existing employee */
    public EmployeeInputFrame(String[] employeeData) {
        if (employeeData != null && employeeData.length >= 1) {
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
        setSize(500, 720);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 25, 10, 25));
        mainPanel.setBackground(new Color(245, 247, 250));

        // ── Header ──
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 60, 114));
        headerPanel.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblTitle = new JLabel(editMode ? "✎  Edit Employee" : "＋ Add New Employee");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // ── Form Panel ──
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 215, 225), 1, true),
                new EmptyBorder(16, 22, 12, 22)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 2, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Employee No
        txtEmpNo = new JTextField(20);
        addFormRow(formPanel, gbc, 0, "Employee No.", txtEmpNo);
        addHintRow(formPanel, gbc, 1, "Required Format: ##### (5 digits)");

        // First Name
        txtFirstName = new JTextField(20);
        addFormRow(formPanel, gbc, 2, "First Name", txtFirstName);

        // Last Name
        txtLastName = new JTextField(20);
        addFormRow(formPanel, gbc, 3, "Last Name", txtLastName);

        // SSS Number
        txtSSS = new JTextField(20);
        addFormRow(formPanel, gbc, 4, "SSS Number", txtSSS);
        addHintRow(formPanel, gbc, 5, "Required Format: ##-#######-#");

        // PhilHealth Number
        txtPhilHealth = new JTextField(20);
        addFormRow(formPanel, gbc, 6, "PhilHealth Number", txtPhilHealth);
        addHintRow(formPanel, gbc, 7, "Required Format: ############ (12 digits)");

        // TIN
        txtTIN = new JTextField(20);
        addFormRow(formPanel, gbc, 8, "TIN", txtTIN);
        addHintRow(formPanel, gbc, 9, "Required Format: ###-###-###-###");

        // Pag-IBIG Number
        txtPagIbig = new JTextField(20);
        addFormRow(formPanel, gbc, 10, "Pag-IBIG Number", txtPagIbig);
        addHintRow(formPanel, gbc, 11, "Required Format: ############ (12 digits)");

        // Hourly Rate
        txtRate = new JTextField(20);
        addFormRow(formPanel, gbc, 12, "Hourly Rate (₱)", txtRate);
        addHintRow(formPanel, gbc, 13, "Required Format: ###.## (2 decimal places)");

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // ── Button Panel ──
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setBackground(new Color(245, 247, 250));
        btnPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        btnSave    = makeButton("Save",    new Color(34, 139, 87));
        btnClear   = makeButton("Clear",   new Color(160, 105, 30));
        btnCancel  = makeButton("Cancel",  new Color(180, 50, 50));

        btnPanel.add(btnSave);
        btnPanel.add(btnClear);
        btnPanel.add(btnCancel);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // ── Listeners ──
        btnSave.addActionListener(e -> saveRecord());
        btnClear.addActionListener(e -> clearFields());
        btnCancel.addActionListener(e -> dispose());

        // Auto-insert dashes for SSS and TIN as the user types
        txtSSS.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String formatted = formatSSS(txtSSS.getText());
                if (!formatted.equals(txtSSS.getText())) {
                    txtSSS.setText(formatted);
                    txtSSS.setCaretPosition(formatted.length());
                }
            }
        });

        txtTIN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String formatted = formatTIN(txtTIN.getText());
                if (!formatted.equals(txtTIN.getText())) {
                    txtTIN.setText(formatted);
                    txtTIN.setCaretPosition(formatted.length());
                }
            }
        });
        // Restrict each field to only the kind of character it accepts
        restrictToDigits(txtEmpNo, 5);
        restrictToLetters(txtFirstName);
        restrictToLetters(txtLastName);
        restrictToDigits(txtSSS, 20);
        restrictToDigits(txtPhilHealth, 12);
        restrictToDigits(txtTIN, 20);
        restrictToDigits(txtPagIbig, 12);
        restrictToDecimal(txtRate);
    }

    // ── Helper: add a label + component row ──────────────────────────────────

    private void addFormRow(JPanel panel, GridBagConstraints gbc,
            int row, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.32;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(55, 65, 90));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.68;
        if (field instanceof JTextField) {
            JTextField tf = (JTextField) field;
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tf.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(180, 190, 210), 1, true),
                    new EmptyBorder(4, 8, 4, 8)));
        }
        panel.add(field, gbc);
    }

    // Small gray format guide shown right under a field (e.g. "Format: ##-#######-#")
    private void addHintRow(JPanel panel, GridBagConstraints gbc, int row, String hintText) {
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.68;
        gbc.insets = new Insets(0, 6, 12, 6);
        JLabel hint = new JLabel(hintText);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(140, 148, 165));
        panel.add(hint, gbc);
        gbc.insets = new Insets(8, 6, 2, 6);
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

    // ── Keystroke Restrictions ───────────────────────────────────────────────

    // Blocks any typed character that isn't a digit, and stops accepting
    // input once the field reaches maxLen characters.
    private void restrictToDigits(JTextField field, int maxLen) {
        field.addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) return;
                if (!Character.isDigit(c) || field.getText().length() >= maxLen) {
                    e.consume();
                }
            }
        });
    }

    // Blocks any typed character that isn't a letter or a space (for
    // multi-word names like "Dela Cruz").
    private void restrictToLetters(JTextField field) {
        field.addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) return;
                if (!Character.isLetter(c) && c != ' ') {
                    e.consume();
                }
            }
        });
    }

    // Blocks any typed character that isn't a digit or a decimal point,
    // and only allows one decimal point per field.
    private void restrictToDecimal(JTextField field) {
        field.addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) return;
                if (c == '.' && field.getText().contains(".")) {
                    e.consume();
                    return;
                }
                if (!Character.isDigit(c) && c != '.') {
                    e.consume();
                }
            }
        });
    }

    // ── Business Logic ────────────────────────────────────────────────────────

    private void saveRecord() {
        String empNo       = txtEmpNo.getText().trim();
        String firstName   = txtFirstName.getText().trim();
        String lastName    = txtLastName.getText().trim();
        String sss         = txtSSS.getText().trim();
        String philHealth  = txtPhilHealth.getText().trim();
        String tin         = txtTIN.getText().trim();
        String pagIbig     = txtPagIbig.getText().trim();
        String rateStr     = txtRate.getText().trim();

        // All fields are required
        if (empNo.isEmpty() || firstName.isEmpty() || lastName.isEmpty()
                || sss.isEmpty() || philHealth.isEmpty() || tin.isEmpty()
                || pagIbig.isEmpty() || rateStr.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        // Employee No: exactly 5 digits (e.g. 10031)
        if (!empNo.matches("\\d{5}")) {
            showError("Invalid Employee No. format!\nExpected: ##### (exactly 5 digits)");
            txtEmpNo.setText("");
            txtEmpNo.requestFocus();
            return;
        }

        // SSS: ##-#######-#  (e.g. 52-1859253-1)
        if (!sss.matches("\\d{2}-\\d{7}-\\d")) {
            showError("Invalid SSS Number format!\nExpected: ##-#######-#");
            txtSSS.setText("");
            txtSSS.requestFocus();
            return;
        }

        // PhilHealth: 12 digits (e.g. 136451303068)
        if (!philHealth.matches("\\d{12}")) {
            showError("Invalid PhilHealth Number format!\nExpected: ############ (12 digits)");
            txtPhilHealth.setText("");
            txtPhilHealth.requestFocus();
            return;
        }

        // TIN: ###-###-###-###  (e.g. 599-312-588-000)
        if (!tin.matches("\\d{3}-\\d{3}-\\d{3}-\\d{3}")) {
            showError("Invalid TIN format!\nExpected: ###-###-###-###");
            txtTIN.setText("");
            txtTIN.requestFocus();
            return;
        }

        // Pag-IBIG: 12 digits (e.g. 110018813465)
        if (!pagIbig.matches("\\d{12}")) {
            showError("Invalid Pag-IBIG Number format!\nExpected: ############ (12 digits)");
            txtPagIbig.setText("");
            txtPagIbig.requestFocus();
            return;
        }

        // Hourly Rate: positive number, exactly two decimal digits (e.g. 133.93)
        if (!rateStr.matches("\\d+\\.\\d{2}")) {
            showError("Invalid Hourly Rate format!\nMust have exactly two decimal places (e.g. 133.93).");
            txtRate.setText("");
            txtRate.requestFocus();
            return;
        }

        double rate = Double.parseDouble(rateStr);
        if (rate <= 0) {
            showError("Hourly Rate must be greater than 0.");
            txtRate.setText("");
            txtRate.requestFocus();
            return;
        }

        // Duplicate check against every employee already in memory.
        // In Edit mode the employee's own Employee No. is excluded from the scan.
        String duplicateField = findDuplicateField(empNo, sss, philHealth, tin, pagIbig,
                editMode ? originalEmpNo : null);
        if (duplicateField != null) {
            showError(duplicateField + " already belongs to another employee.");
            return;
        }

        MotorPHEmployeeApp.Employee emp = new MotorPHEmployeeApp.Employee(
                empNo, lastName, firstName, "N/A", rate,
                sss, philHealth, tin, pagIbig);

        if (editMode) {
            updateExistingEmployee(emp);
        } else {
            EmployeeFileManager.writeEmployeeToCSV(emp);
            appendToMemory(emp);
        }

        JOptionPane.showMessageDialog(this,
                (editMode ? "Record updated" : "Employee added") + " successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
        dispose();
    }

    // Checks every employee already loaded in memory for a matching
    // identifier. Returns the name of the field that is duplicated, or
    // null if everything is unique. excludeEmpNo lets Edit mode skip the
    // record being edited when checking Employee No.
    private String findDuplicateField(String empNo, String sss, String philHealth,
                                       String tin, String pagIbig, String excludeEmpNo) {
        if (MotorPHEmployeeApp.employees == null) return null;

        for (MotorPHEmployeeApp.Employee emp : MotorPHEmployeeApp.employees) {
            if (excludeEmpNo != null && emp.employeeNumber != null
                    && emp.employeeNumber.trim().equals(excludeEmpNo.trim())) {
                continue;
            }
            if (emp.employeeNumber != null && emp.employeeNumber.trim().equals(empNo)) return "Employee Number";
            if (emp.sssNumber != null && emp.sssNumber.trim().equals(sss)) return "SSS Number";
            if (emp.philHealthNumber != null && emp.philHealthNumber.trim().equals(philHealth)) return "PhilHealth Number";
            if (emp.tin != null && emp.tin.trim().equals(tin)) return "TIN";
            if (emp.pagIbigNumber != null && emp.pagIbigNumber.trim().equals(pagIbig)) return "Pag-IBIG Number";
        }
        return null;
    }

    // Appends the new employee to the in-memory array so it is searchable
    // and available for payroll processing right away, without a restart.
    private void appendToMemory(MotorPHEmployeeApp.Employee emp) {
        MotorPHEmployeeApp.Employee[] current = MotorPHEmployeeApp.employees;
        MotorPHEmployeeApp.Employee[] updated;

        if (current == null) {
            updated = new MotorPHEmployeeApp.Employee[]{emp};
        } else {
            updated = new MotorPHEmployeeApp.Employee[current.length + 1];
            System.arraycopy(current, 0, updated, 0, current.length);
            updated[current.length] = emp;
        }

        MotorPHEmployeeApp.employees = updated;
    }

    // Edit mode: finds the employee matching originalEmpNo in memory,
    // applies the new field values, and saves the whole list back to CSV.
    private void updateExistingEmployee(MotorPHEmployeeApp.Employee updated) {
        if (MotorPHEmployeeApp.employees == null) return;

        ArrayList<MotorPHEmployeeApp.Employee> list = new ArrayList<>();
        for (MotorPHEmployeeApp.Employee emp : MotorPHEmployeeApp.employees) {
            if (emp.employeeNumber != null && emp.employeeNumber.trim().equals(originalEmpNo)) {
                emp.employeeNumber = updated.employeeNumber;
                emp.lastName = updated.lastName;
                emp.firstName = updated.firstName;
                emp.name = updated.name;
                emp.sssNumber = updated.sssNumber;
                emp.philHealthNumber = updated.philHealthNumber;
                emp.tin = updated.tin;
                emp.pagIbigNumber = updated.pagIbigNumber;
                emp.hourlyRate = updated.hourlyRate;
            }
            list.add(emp);
        }
        EmployeeFileManager.saveAllToCSV(list, EmployeeFileManager.EMPLOYEE_FILE);
    }

    private void clearFields() {
        txtEmpNo.setText(""); txtFirstName.setText(""); txtLastName.setText("");
        txtSSS.setText(""); txtPhilHealth.setText(""); txtTIN.setText("");
        txtPagIbig.setText(""); txtRate.setText("");
        txtEmpNo.requestFocus();
    }

    private void populateFields(String[] data) {
        // data: EmpNo, LastName, FirstName, Birthday, Address, Phone,
        //       SSS, PhilHealth, TIN, PagIbig, ... , HourlyRate (col 18)
        if (data.length > 0) txtEmpNo.setText(data[0].trim());
        if (data.length > 1) txtLastName.setText(data[1].trim());
        if (data.length > 2) txtFirstName.setText(data[2].trim());
        if (data.length > 6) txtSSS.setText(data[6].trim());
        if (data.length > 7) txtPhilHealth.setText(data[7].trim());
        if (data.length > 8) txtTIN.setText(data[8].trim());
        if (data.length > 9) txtPagIbig.setText(data[9].trim());
        if (data.length > 18) txtRate.setText(data[18].trim());
        txtEmpNo.setEditable(false); // Can't change primary key in edit mode
        txtEmpNo.setBackground(new Color(235, 237, 242));
    }

    // Strips non-digits and rebuilds as ##-#######-#  (e.g. 52-1859253-1)
    private String formatSSS(String raw) {
        String digits = raw.replaceAll("[^0-9]", "");
        if (digits.length() > 10) {
            digits = digits.substring(0, 10);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            if (i == 2 || i == 9) {
                sb.append("-");
            }
            sb.append(digits.charAt(i));
        }
        return sb.toString();
    }

    // Strips non-digits and rebuilds as ###-###-###-###  (e.g. 599-312-588-000)
    private String formatTIN(String raw) {
        String digits = raw.replaceAll("[^0-9]", "");
        if (digits.length() > 12) {
            digits = digits.substring(0, 12);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            if (i == 3 || i == 6 || i == 9) {
                sb.append("-");
            }
            sb.append(digits.charAt(i));
        }
        return sb.toString();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    // ── Quick test ────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeInputFrame::new);
    }
}