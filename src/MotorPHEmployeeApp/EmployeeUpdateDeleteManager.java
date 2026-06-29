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

    // Updates employee info — dialog stays open until input is valid or user cancels 
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
        panel.add(new JLabel("  e.g. 44-4506057-3"));panel.add(new JLabel(""));
        panel.add(new JLabel("PhilHealth Number:")); panel.add(philField);
        panel.add(new JLabel("  e.g. 820126853951"));panel.add(new JLabel(""));
        panel.add(new JLabel("TIN:"));               panel.add(tinField);
        panel.add(new JLabel("  e.g. 442-605-657-000"));panel.add(new JLabel(""));
        panel.add(new JLabel("Pag-IBIG Number:"));   panel.add(pagIbigField);
        panel.add(new JLabel("  e.g. 691295330870")); panel.add(new JLabel(""));
        panel.add(new JLabel("Hourly Rate:"));       panel.add(rateField);

        // Keep re-showing the dialog until all inputs are valid or the user cancels
        while (true) {
            int result = JOptionPane.showConfirmDialog(
                    null, panel, "Update Employee Record",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);


            if (result != JOptionPane.OK_OPTION) {
                return;   // user cancelled — exit
            }

            // Read back the edited values
            String newEmpNo = empNumberField.getText().trim();
            String newLast = lastNameField.getText().trim();
            String newFirst = firstNameField.getText().trim();
            String newSSS = sssField.getText().trim();
            String newPhil = philField.getText().trim();
            String newTIN = tinField.getText().trim();
            String newPagIbig = pagIbigField.getText().trim();
            String rateStr = rateField.getText().trim();


        // All fields are required
            // All fields are required
            if (newEmpNo.isEmpty() || newLast.isEmpty() || newFirst.isEmpty()
                    || newSSS.isEmpty() || newPhil.isEmpty() || newTIN.isEmpty()
                    || newPagIbig.isEmpty() || rateStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required!\nPlease fill in every field.");
                continue;   // re-open the dialog with values intact
            }


        // Employee Number must be numeric, positive, and not used by another employee
            int empNumberValue;
            try {
                empNumberValue = Integer.parseInt(newEmpNo);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Employee Number must be numeric!\nPlease enter a valid number.");
                continue;
            }

                    if (empNumberValue <= 0) {
                JOptionPane.showMessageDialog(null, "Employee Number must be greater than 0!\nPlease correct the value.");
                continue;
            }


            // Employee Number must not be used by a different employee
            MotorPHEmployeeApp.Employee existing = findEmployee(newEmpNo);
            if (existing != null && existing != emp) {
                JOptionPane.showMessageDialog(null, "Employee Number already exists!\nPlease use a unique number.");
                continue;
            }


            // SSS format: ##-#######-# (e.g. 44-4506057-3)
            if (!newSSS.matches("\\d{2}-\\d{7}-\\d")) {
                JOptionPane.showMessageDialog(null,
                        "Invalid SSS Number format!\n"
                        + "Expected format: 44-4506057-3\n"
                        + "Please correct and try again.");
                continue;
            }


            // PhilHealth format: 12 digits (e.g. 820126853951)
            if (!newPhil.matches("\\d{12}")) {
                JOptionPane.showMessageDialog(null,
                        "Invalid PhilHealth Number format!\n"
                        + "Expected format: 820126853951 (12 digits)\n"
                        + "Please correct and try again.");
                continue;
            }


            // TIN format: ###-###-###-### (e.g. 442-605-657-000)
            if (!newTIN.matches("\\d{3}-\\d{3}-\\d{3}-\\d{3}")) {
                JOptionPane.showMessageDialog(null,
                        "Invalid TIN format!\n"
                        + "Expected format: 442-605-657-000\n"
                        + "Please correct and try again.");
                continue;
            }


            // Pag-IBIG format: 12 digits (e.g. 691295330870)
            if (!newPagIbig.matches("\\d{12}")) {
                JOptionPane.showMessageDialog(null,
                        "Invalid Pag-IBIG Number format!\n"
                        + "Expected format: 691295330870 (12 digits)\n"
                        + "Please correct and try again.");
                continue;
            }

            // Hourly Rate must be a positive number
            double rate;
            try {
                rate = Double.parseDouble(rateStr.replace(",", ""));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid rate input!\nPlease enter a numeric value (e.g. 133.93).");
                continue;
            }

            if (rate <= 0) {
                JOptionPane.showMessageDialog(null, "Rate must be greater than 0!\nPlease enter a positive value.");
                continue;
            }

            // All validations passed — apply changes to the employee object
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
            break;   // exit the loop — save is complete


        
    }
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

    // Saves everything back to the CSV file — delegates to EmployeeFileManager (centralized I/O)
    public void saveAllToCSV() {

                EmployeeFileManager.saveAllToCSV(employees, fileName);

    }
}