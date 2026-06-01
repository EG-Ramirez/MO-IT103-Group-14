package MotorPHEmployeeApp;

import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class EmployeeUpdateDeleteManager {

    // This is the main list of employees used by the whole system.
    // If this is not the same list used by the table, updates won't show properly.
    private ArrayList<MotorPHEmployeeApp.Employee> employees;
    private String fileName;

    public EmployeeUpdateDeleteManager(ArrayList<MotorPHEmployeeApp.Employee> employees, String fileName) {
        this.employees = employees;
        this.fileName = fileName;
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

        String newSSS = JOptionPane.showInputDialog("Enter new SSS Number:");
        if (newSSS == null) return;

        String newPhil = JOptionPane.showInputDialog("Enter new PhilHealth Number:");
        if (newPhil == null) return;

        String newTIN = JOptionPane.showInputDialog("Enter new TIN:");
        if (newTIN == null) return;

        String newPagIbig = JOptionPane.showInputDialog("Enter new Pag-IBIG Number:");
        if (newPagIbig == null) return;

        String rateStr = JOptionPane.showInputDialog("Enter new Hourly Rate:");
        if (rateStr == null) return;

        double rate;

        try {
            rate = Double.parseDouble(rateStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid rate input!");
            return;
        }

        if (rate <= 0) {
            JOptionPane.showMessageDialog(null, "Rate must be greater than 0!");
            return;
        }

        // Apply changes to the employee object
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
            fileName = "mph_employees_record.csv";

            for (MotorPHEmployeeApp.Employee emp : employees) {

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
                        "0,0,0,0," +
                        emp.hourlyRate;

                bw.write(row);
                bw.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage());
        }
    }
}

/*
 * Tried fixing the logic here.
 * If you notice nothing happens when clicking update/delete, it might not be fully wired yet.
 * The methods are working on the data, but the UI might still need adjustment.
 */