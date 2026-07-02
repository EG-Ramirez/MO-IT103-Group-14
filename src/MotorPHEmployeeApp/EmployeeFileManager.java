package MotorPHEmployeeApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/*
 * EmployeeFileManager — Centralized File Handling
 *
 * All CSV read and write operations for employee records and computed
 * payroll live here. No other class should open, read, or write these
 * files directly. This replaces the scattered I/O that was previously
 * split across MotorPHEmployeeApp and EmployeeUpdateDeleteManager.
 */
public class EmployeeFileManager {

    // The master employee record file
    static final String EMPLOYEE_FILE = "mph_employees_record.csv";

    // The computed payroll output file
    static final String PAYROLL_FILE  = "payroll_computed.csv";

    // ---------------------------------------------------------------
    // READ — load all employees from the master CSV
    // ---------------------------------------------------------------

    /*
     * Reads every data row from the employee CSV and returns the list.
     * The first line is treated as the header (column discovery).
     */
    static ArrayList<MotorPHEmployeeApp.Employee> loadEmployeesFromCSV(String fileName) {
        ArrayList<MotorPHEmployeeApp.Employee> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String headerLine = br.readLine();
            int rateColumnIndex = findColumnIndex(headerLine, "Hourly Rate");

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                MotorPHEmployeeApp.Employee emp = parseEmployee(line, rateColumnIndex);
                list.add(emp);
            }
        } catch (IOException e) {
        
                JOptionPane.showMessageDialog(
                    null,
                    "Unable to read employee records.\n\n"
                    + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return list;
    }

    // Locate a column by header name; fall back to index 18 (Hourly Rate default)
    static int findColumnIndex(String headerLine, String columnName) {
        if (headerLine == null) return 18;
        String[] headers = headerLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return 18;
    }

    // Build one Employee from a single CSV data line
    static MotorPHEmployeeApp.Employee parseEmployee(String line, int rateColumnIndex) {
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        String empNo     = parts[0].trim();
        String lastName  = parts[1].trim();
        String firstName = parts[2].trim();
        String birthday  = parts[3].trim();
        double rate;
     
        try{
        
            rate = Double.parseDouble(parts[rateColumnIndex]
                    .trim()
                    .replace(",",""));
            
        }catch(NumberFormatException ex){
        
            rate = 0;
        }

        // Government ID columns — fall back to empty string if column is absent
        String sssNumber        = (parts.length > 6) ? parts[6].trim() : "";
        String philHealthNumber = (parts.length > 7) ? parts[7].trim() : "";
        String tin              = (parts.length > 8) ? parts[8].trim() : "";
        String pagIbigNumber    = (parts.length > 9) ? parts[9].trim() : "";

        return new MotorPHEmployeeApp.Employee(empNo, lastName, firstName, birthday, rate,
                sssNumber, philHealthNumber, tin, pagIbigNumber);
    }

    // ---------------------------------------------------------------
    // WRITE — append one new employee to the master CSV
    // ---------------------------------------------------------------

    /*
     * Appends a single new employee row to the master employee file.
     * Column order: 0:empNo 1:last 2:first 3:birthday 4:address 5:phone
     *               6:SSS 7:PhilHealth 8:TIN 9:PagIbig 10-12:N/A 13-17:0
     *               18:hourlyRate
     */
    static void writeEmployeeToCSV(MotorPHEmployeeApp.Employee emp) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(EMPLOYEE_FILE, true))) {

            String row = emp.employeeNumber + ","
                    + emp.lastName + ","
                    + emp.firstName + ","
                    + emp.birthday + ","
                    + "N/A" + ","   // address
                    + "N/A" + ","   // phone number
                    + emp.sssNumber + ","
                    + emp.philHealthNumber + ","
                    + emp.tin + ","
                    + emp.pagIbigNumber + ","
                    + "N/A" + ","   // status
                    + "N/A" + ","   // position
                    + "N/A" + ","   // immediate supervisor
                    + "0" + ","     // basic salary
                    + "0" + ","     // rice subsidy
                    + "0" + ","     // phone allowance
                    + "0" + ","     // clothing allowance
                    + "0" + ","     // gross semi-monthly rate
                    + emp.hourlyRate;   // hourly rate (col 18)

            bw.newLine();
            bw.write(row);

        } catch (IOException e) {
        
                JOptionPane.showMessageDialog(
                    null,
                    "Unable to read employee records.\n\n"
                    + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------------------------------------------------
    // WRITE — overwrite the master CSV with the full in-memory list
    // ---------------------------------------------------------------

    /*
     * Saves every employee in the list back to the master CSV file,
     * replacing its entire contents. Used after an update or delete.
     * The header row is written first so the loader never skips the
     * first employee and findColumnIndex always works correctly.
     */
    static void saveAllToCSV(ArrayList<MotorPHEmployeeApp.Employee> employees, String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {

            bw.write("Employee #,Last Name,First Name,Birthday,Address,Phone Number,"
                    + "SSS #,PhilHealth #,TIN #,Pag-IBIG #,Status,Position,"
                    + "Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,"
                    + "Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate");
            bw.newLine();

            for (MotorPHEmployeeApp.Employee emp : employees) {

                // 19-column layout — Hourly Rate at column 18
                String row = emp.employeeNumber + ","
                        + emp.lastName + ","
                        + emp.firstName + ","
                        + emp.birthday + ","
                        + "N/A,N/A,"
                        + emp.sssNumber + ","
                        + emp.philHealthNumber + ","
                        + emp.tin + ","
                        + emp.pagIbigNumber + ","
                        + "N/A,N/A,N/A,"
                        + "0,0,0,0,0,"
                        + emp.hourlyRate;

                bw.write(row);
                bw.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // WRITE — write computed payroll results to the payroll CSV
    // ---------------------------------------------------------------

    /*
     * After salaries are computed this saves gross pay, deductions, and
     * net pay to payroll_computed.csv. A separate file keeps the master
     * employee record in its original format.
     */
    static void writeComputedPayrollToCSV(MotorPHEmployeeApp.Employee[] employees, int month) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(PAYROLL_FILE))) {

            bw.write("Employee #,Last Name,First Name,Month,Gross Pay,Total Deductions,Net Pay");
            bw.newLine();

            for (MotorPHEmployeeApp.Employee emp : employees) {
                double grossPay    = 0;
                double deductions  = 0;
                double netPay      = 0;

                // If month == 0 process all months (6-12), otherwise only the chosen month
                int startMonth = (month == 0) ? 6  : month;
                int endMonth   = (month == 0) ? 12 : month;

                for (int m = startMonth; m <= endMonth; m++) {
                    double firstHours  = MotorPHEmployeeApp.computeHoursWorked(
                            emp.attendanceIn[m][0], emp.attendanceOut[m][0]);
                    double secondHours = MotorPHEmployeeApp.computeHoursWorked(
                            emp.attendanceIn[m][1], emp.attendanceOut[m][1]);

                    if (firstHours == 0 && secondHours == 0) continue;

                    double gross = SalaryComputationModule.computeGrossPay(
                            new double[]{firstHours, secondHours}, emp.hourlyRate);

                    double sss       = SalaryComputationModule.computeSSS(new double[]{gross});
                    double philHealth = SalaryComputationModule.computePhilHealth(new double[]{gross});
                    double pagIbig   = SalaryComputationModule.computePagIBIG(new double[]{gross});
                    double tax       = SalaryComputationModule.computeWithholdingTax(
                            new double[]{gross - (sss + philHealth + pagIbig)});
                    double totalDed  = SalaryComputationModule.computeDeductions(
                            new double[]{sss, philHealth, pagIbig, tax});

                    grossPay   += gross;
                    deductions += totalDed;
                    netPay     += SalaryComputationModule.computeNetPay(
                            new double[]{gross, totalDed});
                }

                String monthLabel = (month == 0) ? "All" : getMonthName(month);

                String row = emp.employeeNumber + ","
                        + emp.lastName + ","
                        + emp.firstName + ","
                        + monthLabel + ","
                        + grossPay + ","
                        + deductions + ","
                        + netPay;

                bw.write(row);
                bw.newLine();
            }
      
                JOptionPane.showMessageDialog(
                    null,
                    "Payroll successfully saved to\n"
                    + PAYROLL_FILE,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            
        } catch (IOException e) {
        
                JOptionPane.showMessageDialog(
                    null,
                    "Unable to read employee records.\n\n"
                    + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper — returns the month name for the given 1-based month index
    private static String getMonthName(int month) {
        String[] names = {"", "January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        if (month >= 1 && month <= 12) return names[month];
        return "Unknown";
    }
}
