package MotorPHEmployeeApp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * EmployeeRecordsFrame — View and Manage Employee Records
 *
 * Displays all loaded employees in a JTable with columns for
 * Employee Number, Last Name, First Name, SSS No., PhilHealth No.,
 * TIN, and Pag-IBIG No.
 *
 * Selecting a row shows detailed info in the panel below.
 * The Add New Employee button opens EmployeeInputFrame with a callback
 * that refreshes this table immediately after each successful save.
 */
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
        setSize(840, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        loadTableData();
    }

    private void buildUI() {
        // -- Title --
        JLabel titleLabel = new JLabel("Employee Records", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // -- Table setup --
        String[] columns = {
            "Emp. No.", "Last Name", "First Name",
            "SSS No.", "PhilHealth No.", "TIN", "Pag-IBIG No.", "Hourly Rate"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;   // table is read-only
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 12));
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        employeeTable.setRowHeight(20);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScroll = new JScrollPane(employeeTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Employee List"));

        // -- Details area --
        detailsArea = new JTextArea(8, 0);
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Selected Employee Details"));
        detailsScroll.setPreferredSize(new Dimension(820, 165));

        // -- Buttons --
        addButton = new JButton("Add New Employee");
        refreshButton = new JButton("Refresh");
        closeButton = new JButton("Close");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");

        addButton.setFocusable(false);
        refreshButton.setFocusable(false);
        closeButton.setFocusable(false);
        deleteButton.setFocusable(false);
        updateButton.setFocusable(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);

        // -- South section: details + buttons stacked --
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(detailsScroll, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        // -- Frame layout --
        add(titleLabel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        // -- Events --
        // Add New Employee — opens input form; table refreshes after each save
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmployeeInputFrame inputFrame = new EmployeeInputFrame(new Runnable() {
                    @Override
                    public void run() {
                        loadTableData();
                    }
                });
                inputFrame.setVisible(true);
            }
        });

        // Refresh — re-reads in-memory array (picks up any external CSV changes too)
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MotorPHEmployeeApp.loadEmployeesFromCSV("mph_employees_record.csv");
                loadTableData();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    
       updateButton.addActionListener(e -> {
            int row = employeeTable.getSelectedRow();
            if (row < 0 || MotorPHEmployeeApp.employees == null
                    || row >= MotorPHEmployeeApp.employees.length) {
                JOptionPane.showMessageDialog(this,
                        "Please select an employee from the table first.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = MotorPHEmployeeApp.employees[row].employeeNumber;

            EmployeeUpdateDeleteManager manager = buildManager();
            manager.updateRecord(id);

            // Sync the global array with the manager's list, then persist
            MotorPHEmployeeApp.employees =
                    manager.getEmployees().toArray(new MotorPHEmployeeApp.Employee[0]);
            manager.saveAllToCSV();
            loadTableData();
        });

        deleteButton.addActionListener(e -> {
            int row = employeeTable.getSelectedRow();
            if (row < 0 || MotorPHEmployeeApp.employees == null
                    || row >= MotorPHEmployeeApp.employees.length) {
                JOptionPane.showMessageDialog(this,
                        "Please select an employee from the table first.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = MotorPHEmployeeApp.employees[row].employeeNumber;

            EmployeeUpdateDeleteManager manager = buildManager();
            manager.deleteRecord(id);

            // Sync the global array with the manager's list, then persist
            MotorPHEmployeeApp.employees =
                    manager.getEmployees().toArray(new MotorPHEmployeeApp.Employee[0]);
            manager.saveAllToCSV();
            loadTableData();
        });

        // Row selection — populate details area
        employeeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    showSelectedDetails();
                }
            }
        });
    }

    
    // Builds a manager backed by the current in-memory employees.
    // The list holds the same Employee objects as the global array, so an
    // update applies in place; a delete is synced back by the caller.
    private EmployeeUpdateDeleteManager buildManager() {
        ArrayList<MotorPHEmployeeApp.Employee> list = new ArrayList<>();
        if (MotorPHEmployeeApp.employees != null) {
            for (MotorPHEmployeeApp.Employee emp : MotorPHEmployeeApp.employees) {
                list.add(emp);
            }
        }
        return new EmployeeUpdateDeleteManager(list, "mph_employees_record.csv");
    }

// Populate table from the global in-memory employees array
    void loadTableData() {
        tableModel.setRowCount(0);

        if (MotorPHEmployeeApp.employees == null) {
            return;
        }

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

    // Show full details for the selected table row
    private void showSelectedDetails() {
        int row = employeeTable.getSelectedRow();

        if (row < 0 || MotorPHEmployeeApp.employees == null
                || row >= MotorPHEmployeeApp.employees.length) {
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
        sb.append("Hourly Rate     : \u20b1").append(emp.hourlyRate);

        detailsArea.setText(sb.toString());
        detailsArea.setCaretPosition(0);
    }
}


