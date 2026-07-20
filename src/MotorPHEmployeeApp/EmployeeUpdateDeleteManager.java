package MotorPHEmployeeApp;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    
    // Finds an employee by employee number.
    public MotorPHEmployeeApp.Employee findEmployee(String empNo) {

        if (empNo == null) {
            return null;
        }

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
        JTextField birthdayField = new JTextField(emp.birthday);
        JTextField sssField       = new JTextField(emp.sssNumber);
        JTextField philField      = new JTextField(emp.philHealthNumber);
        JTextField tinField       = new JTextField(emp.tin);
        JTextField pagIbigField   = new JTextField(emp.pagIbigNumber);
        JTextField rateField =
            new JTextField(String.format("%.2f", emp.hourlyRate));
        
        // Restrict each field to only the kind of character it accepts,
        // same rule set used when adding a new employee.
        restrictToDigits(empNumberField, 5);
        restrictToLetters(lastNameField);
        restrictToLetters(firstNameField);
        restrictToBirthdayChars(birthdayField, 10); 
        restrictToDigits(sssField, 20);
        restrictToDigits(philField, 12);
        restrictToDigits(tinField, 20);
        restrictToDigits(pagIbigField, 12);
        restrictToDecimal(rateField);

// Auto-insert dashes for SSS and TIN as the user types
        sssField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String formatted = formatSSS(sssField.getText());
                if (!formatted.equals(sssField.getText())) {
                    sssField.setText(formatted);
                    sssField.setCaretPosition(formatted.length());
                }
            }
        });

        tinField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String formatted = formatTIN(tinField.getText());
                if (!formatted.equals(tinField.getText())) {
                    tinField.setText(formatted);
                    tinField.setCaretPosition(formatted.length());
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));

        panel.add(new JLabel("Employee Number:"));
        panel.add(empNumberField);

        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);

        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);

        panel.add(new JLabel("Birthday (MM/DD/YYYY):"));
        panel.add(birthdayField);

        panel.add(new JLabel("SSS Number (##-#######-#):"));
        panel.add(sssField);

        panel.add(new JLabel("PhilHealth Number (12 digits):"));
        panel.add(philField);

        panel.add(new JLabel("TIN (###-###-###-###):"));
        panel.add(tinField);

        panel.add(new JLabel("Pag-IBIG Number (12 digits):"));
        panel.add(pagIbigField);

        panel.add(new JLabel("Hourly Rate:"));
        panel.add(rateField);

        // Keep re-showing the dialog until all inputs are valid or the user cancels.
        // 'continue' re-opens the dialog with the user's existing input intact.
        while (true) {
            int result = JOptionPane.showConfirmDialog(
                    null, panel, "Update Employee Record",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result != JOptionPane.OK_OPTION)
                return;   // user cancelled — exit

            // Read back the edited values
            String newEmpNo = empNumberField.getText().trim();
            String newLast = lastNameField.getText().trim();
            String newFirst = firstNameField.getText().trim();
            String newBirthday = birthdayField.getText().trim();
            String newSSS = sssField.getText().trim();
            String newPhil = philField.getText().trim();
            String newTIN = tinField.getText().trim();
            String newPagIbig = pagIbigField.getText().trim();
            String rateStr = rateField.getText().trim();


        // All fields are required
            if (newEmpNo.isEmpty() || newLast.isEmpty() || newFirst.isEmpty() || newBirthday.isEmpty()
                    || newSSS.isEmpty() || newPhil.isEmpty() || newTIN.isEmpty()
                    || newPagIbig.isEmpty() || rateStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required!\nPlease fill in every field.");
                continue;   // re-open the dialog with values intact
            }


            // Employee Number must be exactly 5 digits and not used by another employee
            if (!newEmpNo.matches("\\d{5}")) {
                JOptionPane.showMessageDialog(null, "Invalid Employee Number format!\nExpected: 10031 (exactly 5 digits)");
                continue;
            }

            // Employee Number must not be used by a different employee
            MotorPHEmployeeApp.Employee existing = findEmployee(newEmpNo);
            if (existing != null && existing != emp) {
                JOptionPane.showMessageDialog(null, "Employee Number already exists!\nPlease use a unique number.");
                continue;
            }

            // Birthday format: MM/DD/YYYY, month 01-12, day 01-31
            if (!isValidBirthday(newBirthday)) {
                JOptionPane.showMessageDialog(null,
                        "Invalid Birthday format!\nExpected: MM/DD/YYYY\n(Month: 01-12, Day: 01-31)");
                continue;
            }

            // SSS format: ##-#######-#  (e.g. 44-4506057-3)
            if (!newSSS.matches("\\d{2}-\\d{7}-\\d")) {
                JOptionPane.showMessageDialog(null,
                        "Invalid SSS Number format!\nExpected: ##-#######-#");
                continue;
            }

            // SSS duplicate check — exclude the employee being edited from the scan
            if (isSssDuplicate(newSSS, emp)) {
                JOptionPane.showMessageDialog(null,
                        "SSS Number already belongs to another employee!");
                continue;
            }


            // PhilHealth format: 12 digits (e.g. 820126853951)
            if (!newPhil.matches("\\d{12}")) {
                JOptionPane.showMessageDialog(null,
                        "Invalid PhilHealth Number format!\n"
                        + "Expected format: ############ (12 digits)\n"
                        + "Please correct and try again.");
                continue;
            }
            
            // PhilHealth duplicate check
            if (isPhilHealthDuplicate(newPhil, emp)) {
                JOptionPane.showMessageDialog(null,
                        "PhilHealth Number already belongs to another employee!");
                continue;
            }


            // TIN format: ###-###-###-### (e.g. 442-605-657-000)
            if (!newTIN.matches("\\d{3}-\\d{3}-\\d{3}-\\d{3}")) {
                JOptionPane.showMessageDialog(null,
                        "Invalid TIN format!\n"
                        + "Expected format: ###-###-###-###\n"
                        + "Please correct and try again.");
                continue;
            }
            
            // TIN duplicate check
            if (isTinDuplicate(newTIN, emp)) {
                JOptionPane.showMessageDialog(null,
                        "TIN already belongs to another employee!");
                continue;
            }


            // Pag-IBIG format: 12 digits (e.g. 691295330870)
            if (!newPagIbig.matches("\\d{12}")) {
                JOptionPane.showMessageDialog(null,
                        "Invalid Pag-IBIG Number format!\n"
                        + "Expected format: ############ (12 digits)\n"
                        + "Please correct and try again.");
                continue;
            }
            
             // Pag-IBIG duplicate check
            if (isPagIbigDuplicate(newPagIbig, emp)) {
                JOptionPane.showMessageDialog(null,
                        "Pag-IBIG Number already belongs to another employee!");
                continue;
            }

            // Hourly Rate must be a positive number
            double rate;
            try {
                rate = Double.parseDouble(rateStr.replace(",", ""));
            } catch (NumberFormatException e) {
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
            emp.birthday = newBirthday;
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
            JOptionPane.showMessageDialog(
                null,
                "Employee " + empNo + " was not found.",
                "Not Found",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete employee "
                + emp.employeeNumber + " (" + emp.name + ")?",
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
    
    // Returns true if any employee OTHER than excludeEmp already has this SSS number
    private boolean isSssDuplicate(String sssNumber, MotorPHEmployeeApp.Employee excludeEmp) {
        for (MotorPHEmployeeApp.Employee emp : employees) {
            if (emp == excludeEmp) {
            continue;
        }
            if (sssNumber.equals(emp.sssNumber)) return true;
        }
        return false;
    }

    // Returns true if any employee OTHER than excludeEmp already has this PhilHealth number
    private boolean isPhilHealthDuplicate(String philHealthNumber, MotorPHEmployeeApp.Employee excludeEmp) {
        for (MotorPHEmployeeApp.Employee emp : employees) {
            if (emp == excludeEmp) continue;
            if (philHealthNumber.equals(emp.philHealthNumber)) return true;
        }
        return false;
    }

    // Returns true if any employee OTHER than excludeEmp already has this TIN
    private boolean isTinDuplicate(String tin, MotorPHEmployeeApp.Employee excludeEmp) {
        for (MotorPHEmployeeApp.Employee emp : employees) {
            if (emp == excludeEmp) continue;
            if (tin.equals(emp.tin)) return true;
        }
        return false;
    }

    // Returns true if any employee OTHER than excludeEmp already has this Pag-IBIG number
    private boolean isPagIbigDuplicate(String pagIbigNumber, MotorPHEmployeeApp.Employee excludeEmp) {
        for (MotorPHEmployeeApp.Employee emp : employees) {
            if (emp == excludeEmp) continue;
            if (pagIbigNumber.equals(emp.pagIbigNumber)) return true;
        }
        return false;
    }
        
        // ── Keystroke Restrictions ───────────────────────────────────────────────

        // Blocks any typed character that isn't a digit, and stops accepting
        // input once the field reaches maxLen characters.
    private void restrictToDigits(JTextField field, int maxLen) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) {
                    return;
                }
                if (!Character.isDigit(c) || field.getText().length() >= maxLen) {
                    e.consume();
                }
            }
        });
    }

    // Blocks any typed character that isn't a digit or a forward slash.
    // No auto-formatting — the user types the slashes themselves, and the
    // strict MM/DD/YYYY format (month 1-12, day 1-31, 4-digit year) is
    // enforced separately in isValidBirthday() when the record is saved.
    private void restrictToBirthdayChars(JTextField field, int maxLen) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) {
                    return;
                }
                if ((!Character.isDigit(c) && c != '/') || field.getText().length() >= maxLen) {
                    e.consume();
                }
            }
        });
    }

    // Blocks any typed character that isn't a letter or a space (for
    // multi-word names like "Dela Cruz").
    private void restrictToLetters(JTextField field) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) {
                    return;
                }
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
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) {
                    return;
                }
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

    // ── Birthday Validation & Formatting ─────────────────────────────────────
    // Checks MM/DD/YYYY: month 1-12, day 1-31, year exactly 4 digits.
    // Month/day may be typed as 1 or 2 digits (e.g. "6/15/1998" or "06/15/1998").
    private boolean isValidBirthday(String birthday) {
        if (!birthday.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
            return false;
        }

        String[] parts = birthday.split("/");
        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        return month >= 1 && month <= 12 && day >= 1 && day <= 31;
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
}
