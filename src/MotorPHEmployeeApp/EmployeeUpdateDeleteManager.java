package MotorPHEmployeeApp;

import java.awt.GridLayout;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EmployeeUpdateDeleteManager {

    // This is the main list of employees used by the whole system.
    // If this is not the same list used by the table, updates won't show properly.
    private ArrayList<MotorPHEmployeeApp.Employee> employees;
    private String fileName;

    public EmployeeUpdateDeleteManager(ArrayList<MotorPHEmployeeApp.Employee> employees, String fileName) {
        this.employees = employees;
        this.fileName = fileName;
    }

    
    // Exposes the working list so EmployeeRecordsFrame can sync the
    // global employees array after an update or delete.
    public ArrayList<MotorPHEmployeeApp.Employee> getEmployees() {
        return employees;
    }
    
     // Just finds an employee using their ID
    public MotorPHEmployeeApp.Employee findEmployee(String empNo) {

        if (empNo == null) return null;

        for (MotorPHEmployeeApp.Employee emp : employees) {

            if (emp.employeeNumber != null &&
                emp.employeeNumber.trim().equals(empNo.trim())) {
                return emp;
            }
        }
        return null;
    }

     // Updates employee info 
    public void updateRecord(String empNo) {

        MotorPHEmployeeApp.Employee emp = findEmployee(empNo);

        if (emp == null) {
            JOptionPane.showMessageDialog(null, "Employee not found!");
            return;
        }

        // Build an editable form pre-filled with the employee's current details.
        // Every field can be changed; anything left as-is keeps its current value.
        JTextField empNumberField = new JTextField(emp.employeeNumber);
        JTextField lastNameField  = new JTextField(emp.lastName);
        JTextField firstNameField = new JTextField(emp.firstName);
        JTextField sssField       = new JTextField(emp.sssNumber);
        JTextField philField      = new JTextField(emp.philHealthNumber);
        JTextField tinField       = new JTextField(emp.tin);
        JTextField pagIbigField   = new JTextField(emp.pagIbigNumber);
        JTextField rateField      = new JTextField(String.valueOf(emp.hourlyRate));

        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));
        panel.add(new JLabel("Employee Number:"));   panel.add(empNumberField);
        panel.add(new JLabel("Last Name:"));         panel.add(lastNameField);
        panel.add(new JLabel("First Name:"));        panel.add(firstNameField);
        panel.add(new JLabel("SSS Number:"));        panel.add(sssField);
        panel.add(new JLabel("PhilHealth Number:")); panel.add(philField);
        panel.add(new JLabel("TIN:"));               panel.add(tinField);
        panel.add(new JLabel("Pag-IBIG Number:"));   panel.add(pagIbigField);
        panel.add(new JLabel("Hourly Rate:"));       panel.add(rateField);

        int result = JOptionPane.showConfirmDialog(
                null, panel, "Update Employee Record",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        // Read back the edited values
        String newEmpNo   = empNumberField.getText().trim();
        String newLast    = lastNameField.getText().trim();
        String newFirst   = firstNameField.getText().trim();
        String newSSS     = sssField.getText().trim();
        String newPhil    = philField.getText().trim();
        String newTIN     = tinField.getText().trim();
        String newPagIbig = pagIbigField.getText().trim();
        String rateStr    = rateField.getText().trim();

        // All fields are required
        if (newEmpNo.isEmpty() || newLast.isEmpty() || newFirst.isEmpty()
                || newSSS.isEmpty() || newPhil.isEmpty() || newTIN.isEmpty()
                || newPagIbig.isEmpty() || rateStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!");
            return;
        }

        // Employee Number must be numeric, positive, and not used by another employee
        int empNumberValue;

        try {
            empNumberValue = Integer.parseInt(newEmpNo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Employee Number must be numeric!");
            return;
        }

        if (empNumberValue <= 0) {
            JOptionPane.showMessageDialog(null, "Employee Number must be greater than 0!");
            return;
        }

        MotorPHEmployeeApp.Employee existing = findEmployee(newEmpNo);
        if (existing != null && existing != emp) {
            JOptionPane.showMessageDialog(null, "Employee Number already exists!");
            return;
        }

        double rate;

        try {
            rate = Double.parseDouble(rateStr.replace(",", ""));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid rate input!");
            return;
        }

        if (rate <= 0) {
            JOptionPane.showMessageDialog(null, "Rate must be greater than 0!");
            return;
        }

        // Apply changes to the employee object
        emp.employeeNumber = newEmpNo;
        emp.lastName = newLast;
        emp.firstName = newFirst;
        emp.name = newFirst + " " + newLast;   // keep the combined name in sync
        emp.sssNumber = newSSS;
        emp.philHealthNumber = newPhil;
        emp.tin = newTIN;
        emp.pagIbigNumber = newPagIbig;
        emp.hourlyRate = rate;

        JOptionPane.showMessageDialog(null, "Employee updated successfully!");
    }

      // Deletes an employee after confirmation
    public void deleteRecord(String empNo) {

        MotorPHEmployeeApp.Employee emp = findEmployee(empNo);

        if (emp == null) {
            JOptionPane.showMessageDialog(null, "Employee not found!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this employee?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            employees.remove(emp);

            JOptionPane.showMessageDialog(null, "Employee deleted successfully!");
        }
    }

    // Saves everything back to the CSV file
    public void saveAllToCSV() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            
            // Write the header row first. loadEmployeesFromCSV always treats the
            // first line as the header, so this keeps the first employee from
            // being skipped and lets findColumnIndex locate the Hourly Rate column.
            bw.write("Employee #,Last Name,First Name,Birthday,Address,Phone Number,"
                    + "SSS #,PhilHealth #,TIN #,Pag-IBIG #,Status,Position,"
                    + "Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,"
                    + "Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate");
            bw.newLine();

            for (MotorPHEmployeeApp.Employee emp : employees) {

                // Same 19-column layout as writeEmployeeToCSV (Hourly Rate at column 18)
                String row =
                        emp.employeeNumber + "," +
                        emp.lastName + "," +
                        emp.firstName + "," +
                        emp.birthday + "," +
                        "N/A,N/A," +
                        emp.sssNumber + "," +
                        emp.philHealthNumber + "," +
                        emp.tin + "," +
                        emp.pagIbigNumber + "," +
                        "N/A,N/A,N/A," +
                        "0,0,0,0,0," +
                        emp.hourlyRate;

                bw.write(row);
                bw.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage());
        }
    }
}
