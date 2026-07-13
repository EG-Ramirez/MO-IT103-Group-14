package MotorPHEmployeeApp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.JSplitPane;
import javax.swing.table.DefaultTableModel;

public class EmployeeRecordsFrame extends JFrame {

    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextArea detailsArea;
    private JButton addButton;
    private JButton refreshButton;
    private JButton closeButton;
    private JButton deleteButton;
    private JButton updateButton;

    public EmployeeRecordsFrame() {
        setTitle("Employee Records");
        setSize(900, 580); // slight adjust for layout
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        loadTableData();
    }
    
    private void clearSelection() {
        employeeTable.clearSelection();
        detailsArea.setText("");
    }
    
    private void buildUI() {

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(10, 40, 120));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("EMPLOYEE MANAGEMENT DASHBOARD");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel moduleLabel = new JLabel("HR Records Module");
        moduleLabel.setForeground(Color.WHITE);
        moduleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(moduleLabel, BorderLayout.EAST);


        // ===== TABLE =====
        String[] columns = {
            "Emp. No.", "Last Name", "First Name",
            "SSS No.", "PhilHealth No.", "TIN", "Pag-IBIG No.", "Hourly Rate"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        employeeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        employeeTable.setRowHeight(22);
        employeeTable.setAutoCreateRowSorter(true);
        employeeTable.setFillsViewportHeight(true);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScroll = new JScrollPane(employeeTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Employee Directory"));


        // ===== DETAILS =====
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setMargin(new Insets(10,10,10,10));

        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Employee Profile"));
        detailsScroll.setPreferredSize(new Dimension(300, 0));


        // ===== SPLIT =====
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScroll, detailsScroll);
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.7);


        // ===== BUTTONS =====
        addButton = new JButton("Add");
        refreshButton = new JButton("Refresh");
        closeButton = new JButton("Close");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");

        styleButton(addButton);
        styleButton(refreshButton);
        styleButton(closeButton);
        styleButton(deleteButton);
        styleButton(updateButton);

        addButton.setFocusable(false);
        refreshButton.setFocusable(false);
        closeButton.setFocusable(false);
        deleteButton.setFocusable(false);
        updateButton.setFocusable(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        // ===== FRAME =====
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ===== EVENTS (UNCHANGED) =====

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmployeeInputFrame inputFrame = new EmployeeInputFrame(new Runnable() {
                    @Override
                    public void run() {
                        loadTableData();
                    }
                });
                
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MotorPHEmployeeApp.loadEmployeesFromCSV("mph_employees_record.csv");
                MotorPHEmployeeApp.loadAttendanceFromCSV("attendance_record.csv");
                loadTableData();
                clearSelection();
                
                JOptionPane.showMessageDialog(
                    EmployeeRecordsFrame.this,
                    "Employee records have been refreshed.",
                    "Refresh Complete",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });

        closeButton.addActionListener(e -> dispose());

        updateButton.addActionListener(e -> {
            int viewRow = employeeTable.getSelectedRow();

            if (MotorPHEmployeeApp.employees == null || viewRow < 0) {
                JOptionPane.showMessageDialog(this,
                        "Please select an employee from the table first.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int row = employeeTable.convertRowIndexToModel(viewRow);
            if (row < 0 || row >= MotorPHEmployeeApp.employees.length) return;

            String id = MotorPHEmployeeApp.employees[row].employeeNumber;

            EmployeeUpdateDeleteManager manager = buildManager();
            manager.updateRecord(id);

            MotorPHEmployeeApp.employees =
            manager.getEmployees().toArray(new MotorPHEmployeeApp.Employee[0]);
            manager.saveAllToCSV();
            loadTableData();
            clearSelection();
        });

        deleteButton.addActionListener(e -> {
            int viewRow = employeeTable.getSelectedRow();

            if (MotorPHEmployeeApp.employees == null || viewRow < 0) {
                detailsArea.setText("");
                JOptionPane.showMessageDialog(this,
                        "Please select an employee from the table first.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int row = employeeTable.convertRowIndexToModel(viewRow);
            if (row < 0 || row >= MotorPHEmployeeApp.employees.length) return;

            String id = MotorPHEmployeeApp.employees[row].employeeNumber;

            EmployeeUpdateDeleteManager manager = buildManager();
            manager.deleteRecord(id);

            MotorPHEmployeeApp.employees =
            manager.getEmployees().toArray(new MotorPHEmployeeApp.Employee[0]);
            manager.saveAllToCSV();
            loadTableData();
            clearSelection();
        });

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedDetails();
            }
        });
    }

    // ===== UI STYLE ONLY =====
    private void styleButton(JButton btn) {
        btn.setBackground(new Color(0, 120, 215));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setFocusPainted(false);
    }

    private EmployeeUpdateDeleteManager buildManager() {
        ArrayList<MotorPHEmployeeApp.Employee> list = new ArrayList<>();
        if (MotorPHEmployeeApp.employees != null) {
            for (MotorPHEmployeeApp.Employee emp : MotorPHEmployeeApp.employees) {
                list.add(emp);
            }
        }
        return new EmployeeUpdateDeleteManager(list, "mph_employees_record.csv");
    }

    void loadTableData() {
        tableModel.setRowCount(0);

        if (MotorPHEmployeeApp.employees == null) return;

        for (MotorPHEmployeeApp.Employee emp : MotorPHEmployeeApp.employees) {
            tableModel.addRow(new Object[]{
                emp.employeeNumber,
                emp.lastName,
                emp.firstName,
                emp.sssNumber,
                emp.philHealthNumber,
                emp.tin,
                emp.pagIbigNumber,
                String.format("\u20b1%,.2f", emp.hourlyRate)
            });
        }
    }

    private void showSelectedDetails() {
        int viewRow = employeeTable.getSelectedRow();

        if (MotorPHEmployeeApp.employees == null || viewRow < 0) {
            detailsArea.setText("");
            return;
        }

        int row = employeeTable.convertRowIndexToModel(viewRow);
        if (row < 0 || row >= MotorPHEmployeeApp.employees.length) {
            detailsArea.setText("");
            return;
        }

        MotorPHEmployeeApp.Employee emp = MotorPHEmployeeApp.employees[row];

        StringBuilder sb = new StringBuilder();
        sb.append("Employee Number : ").append(emp.employeeNumber).append("\n");
        sb.append("Full Name       : ").append(emp.name).append("\n");
        sb.append("Birthday        : ").append(emp.birthday).append("\n");
        sb.append("SSS Number      : ").append(emp.sssNumber).append("\n");
        sb.append("PhilHealth No.  : ").append(emp.philHealthNumber).append("\n");
        sb.append("TIN             : ").append(emp.tin).append("\n");
        sb.append("Pag-IBIG No.    : ").append(emp.pagIbigNumber).append("\n");
        sb.append(String.format("Hourly Rate     : ₱%,.2f", emp.hourlyRate));

        detailsArea.setText(sb.toString());
        detailsArea.setCaretPosition(0);
    }
}
