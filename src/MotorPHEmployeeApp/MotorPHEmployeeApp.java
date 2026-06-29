package MotorPHEmployeeApp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MotorPHEmployeeApp {
// Employee class kept as a static nested class (same as CP1)
    static class Employee {
        String employeeNumber;
        String name;
        String lastName;
        String firstName;
        String birthday;
        double hourlyRate;
        String sssNumber;
        String philHealthNumber;
        String tin;
        String pagIbigNumber;

        /*
        3D Attendance Structure:
        > Dimension 1: Month index (1–12)
        > Dimension 2: Cutoff index (0 = 1st cutoff, 1 = 2nd cutoff)
        > Dimension 3: Dynamic time entries
        */
        double[][][] attendanceIn = new double[13][2][];
        double[][][] attendanceOut = new double[13][2][];

        Employee(String employeeNumber, String name, String birthday, double hourlyRate) {
            this.employeeNumber = employeeNumber;
            this.name = name;
            this.birthday = birthday;
            this.hourlyRate = hourlyRate;
            this.sssNumber = "";
            this.philHealthNumber = "";
            this.tin = "";
            this.pagIbigNumber = "";
        }
        
        // Full constructor used by parseEmployee and the updated EmployeeInputFrame
        Employee(String employeeNumber, String lastName, String firstName, String birthday,
                double hourlyRate, String sssNumber, String philHealthNumber,
                String tin, String pagIbigNumber) {
            this.employeeNumber = employeeNumber;
            this.lastName = lastName;
            this.firstName = firstName;
            this.name = firstName + " " + lastName;
            this.birthday = birthday;
            this.hourlyRate = hourlyRate;
            this.sssNumber = sssNumber;
            this.philHealthNumber = philHealthNumber;
            this.tin = tin;
            this.pagIbigNumber = pagIbigNumber;
        }
    }

    // Global in-memory Employee Repository
    static Employee[] employees;

    public static void main(String[] args) {
        // Data Ingestion Phase (same as CP1)
        loadEmployeesFromCSV("mph_employees_record.csv");
        loadAttendanceFromCSV("attendance_record.csv");

        // Launch Login Window on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }

    /*
     * CSV Employees Loader — delegates to EmployeeFileManager (centralized I/O)
     */
    
    static void loadEmployeesFromCSV(String mph_employees_record) {
        ArrayList<Employee> list = EmployeeFileManager.loadEmployeesFromCSV(mph_employees_record);

        employees = list.toArray(new Employee[0]);
    }

    // CSV Attendance Loader
    static void loadAttendanceFromCSV(String attendance_record) {
        try (BufferedReader br = new BufferedReader(new FileReader(attendance_record))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");

                String empNo = parts[0].trim();
                String date = parts[3].trim();
                double timeIn = Double.parseDouble(parts[4].trim().replace(":", "."));
                double timeOut = Double.parseDouble(parts[5].trim().replace(":", "."));

                Employee emp = findEmployee(empNo);
                if (emp == null) continue;

                int month = Integer.parseInt(date.substring(0, 2));
                int day = Integer.parseInt(date.substring(3, 5));
                int cutoff = (day <= 15) ? 0 : 1;

                emp.attendanceIn[month][cutoff] = append(emp.attendanceIn[month][cutoff], timeIn);
                emp.attendanceOut[month][cutoff] = append(emp.attendanceOut[month][cutoff], timeOut);
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
        }
    }

    // Manually grow a fixed-size Java array
    static double[] append(double[] array, double value) {
        if (array == null) return new double[]{value};
        double[] newArr = new double[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = value;
        return newArr;
    }

    /*
     * Appends a new employee record to the CSV file.
     * Delegates to EmployeeFileManager (centralized I/O).
     */
    static void writeEmployeeToCSV(Employee emp) {
                EmployeeFileManager.writeEmployeeToCSV(emp);
    }
    
    
    /*
     * Payroll Report Builder
     * NOTE: In CP1 this method used System.out.println().
     * For CP2 it builds the same text using StringBuilder
     * so it can be shown inside a JTextArea.
     */
    static String processPayroll(Employee emp) {
        String[] monthNames = {"", "January", "February", "March", "April", "May", "June",
                               "July", "August", "September", "October", "November", "December"};

        StringBuilder sb = new StringBuilder();

       sb.append("   ==================================================\n");
       sb.append("                     PAYROLL REPORT                  \n");
       sb.append("   ==================================================\n");
       sb.append("      Employee Name : ").append(emp.name).append("\n");
       sb.append("      Employee ID   : ").append(emp.employeeNumber).append("\n");
       sb.append("      Birthday      : ").append(emp.birthday).append("\n");
       sb.append("   ==================================================\n\n");

        for (int m = 6; m <= 12; m++) {
            double firstHours = computeHoursWorked(emp.attendanceIn[m][0], emp.attendanceOut[m][0]);
            double secondHours = computeHoursWorked(emp.attendanceIn[m][1], emp.attendanceOut[m][1]);

            if (firstHours == 0 && secondHours == 0) continue;

            double firstGross = firstHours * emp.hourlyRate;
            double secondGross = secondHours * emp.hourlyRate;
            double combinedGross = firstGross + secondGross;

            double sss = computeSSS(combinedGross);
            double philHealth = computePhilHealth(combinedGross);
            double pagIbig = computePagibig(combinedGross);
            double tax = computeIncomeTax(combinedGross - (sss + philHealth + pagIbig));
            double totalDeductions = sss + philHealth + pagIbig + tax;

            sb.append("                --- Summary for: ").append(monthNames[m]).append(" ---\n\n");

            sb.append("      [ First Cutoff: 1 - 15 ]\n");
            sb.append("      Hours Worked : ").append(firstHours).append("\n");
            sb.append("      Gross Pay    : Php ").append(firstGross).append("\n");
            sb.append("      Net Pay      : Php ").append(firstGross).append("\n");

            sb.append("      [ Second Cutoff: 16 - 30 ]\n");
            sb.append("      Hours Worked : ").append(secondHours).append("\n");
            sb.append("      Gross Pay    : Php ").append(secondGross).append("\n");
            sb.append("      Net Pay      : Php ").append(secondGross - totalDeductions).append("\n");

            sb.append("      =========== Monthly Deductions ===========\n");
            sb.append("      SSS          : Php ").append(sss).append("\n");
            sb.append("      PhilHealth   : Php ").append(philHealth).append("\n");
            sb.append("      Pag-IBIG     : Php ").append(pagIbig).append("\n");
            sb.append("      Tax          : Php ").append(tax).append("\n");
            sb.append("      Total        : Php ").append(totalDeductions).append("\n\n");
            sb.append("   ==================================================\n\n");
        }

        return sb.toString();
    }

    /*
     * Payroll Summary Builder
     * Loops through every employee, reuses the Feature 3 computations in
     * SalaryComputationModule, and accumulates company-wide totals. Returns
     * the same StringBuilder text style as processPayroll so it can be shown
     * inside a JOptionPane or JTextArea.
     */
    static String generatePayrollSummary() {
        int totalEmployees = employees.length;
        double totalGrossPay = 0;
        double totalDeductions = 0;
        double totalNetPay = 0;

        for (Employee emp : employees) {
            for (int m = 6; m <= 12; m++) {
                double firstHours = computeHoursWorked(emp.attendanceIn[m][0], emp.attendanceOut[m][0]);
                double secondHours = computeHoursWorked(emp.attendanceIn[m][1], emp.attendanceOut[m][1]);

                if (firstHours == 0 && secondHours == 0) continue;

                double gross = SalaryComputationModule.computeGrossPay(
                        new double[]{firstHours, secondHours}, emp.hourlyRate);

                double sss = SalaryComputationModule.computeSSS(new double[]{gross});
                double philHealth = SalaryComputationModule.computePhilHealth(new double[]{gross});
                double pagIbig = SalaryComputationModule.computePagIBIG(new double[]{gross});
                double tax = SalaryComputationModule.computeWithholdingTax(
                        new double[]{gross - (sss + philHealth + pagIbig)});

                double deductions = SalaryComputationModule.computeDeductions(new double[]{sss, philHealth, pagIbig, tax});
                double net = SalaryComputationModule.computeNetPay(new double[]{gross, deductions});

                totalGrossPay += gross;
                totalDeductions += deductions;
                totalNetPay += net;
            }
        }

        double averageNetPay = totalNetPay / totalEmployees;

        StringBuilder sb = new StringBuilder();
        sb.append("   ==================================================\n");
        sb.append("                   PAYROLL SUMMARY                   \n");
        sb.append("   ==================================================\n");
        sb.append("      Total Employees   : ").append(totalEmployees).append("\n");
        sb.append("      Total Gross Pay   : Php ").append(String.format("%,.2f", totalGrossPay)).append("\n");
        sb.append("      Total Deductions  : Php ").append(String.format("%,.2f", totalDeductions)).append("\n");
        sb.append("      Average Net Pay   : Php ").append(String.format("%,.2f", averageNetPay)).append("\n");
        sb.append("   ==================================================\n");

        return sb.toString();
    }
    
    /*
     * Computed Payroll Writer (Feature 3)
     * Delegates to EmployeeFileManager (centralized I/O).
     * Passing month=0 means all months (6-12) are included.
     */
    static void writeComputedPayrollToCSV() {
                EmployeeFileManager.writeComputedPayrollToCSV(employees, 0);
    }
    
    // Convert HH.MM into decimal hours
    static double convertToHours(double timeValue) {
        int hour = (int) timeValue;
        double decimal = timeValue - hour;
        int minutes = (int) (decimal * 100);
        return hour + (minutes / 60.0);
    }

    static double computeHoursWorked(double[] timeIn, double[] timeOut) {
        double hoursWorked = 0;
        if (timeIn == null || timeOut == null) return 0;
        for (int i = 0; i < timeIn.length; i++) {
            double in = timeIn[i];
            double out = timeOut[i];
            if (in <= 8.05) in = 8.0;          // 5-minute grace period
            if (in < 8.0) in = 8.0;
            if (out > 17.0) out = 17.0;
            double decimalIn = convertToHours(in);
            double decimalOut = convertToHours(out);
            double daily = decimalOut - decimalIn;
            if (decimalOut < decimalIn || daily < 0) daily = 0;
            hoursWorked += daily;
        }
        return hoursWorked;
    }

    static Employee findEmployee(String empNo) {
        if (employees == null) return null;
        for (Employee emp : employees) {
            if (emp.employeeNumber.equals(empNo)) return emp;
        }
        return null;
    }

    // SSS Monthly Deduction Table (2024)
    static double computeSSS(double monthlyGross) {
        double monthlyContribution = 0;

        if (monthlyGross < 3250) monthlyContribution = 135;
        else if (monthlyGross <= 3750) monthlyContribution = 157.5;
        else if (monthlyGross <= 4250) monthlyContribution = 180;
        else if (monthlyGross <= 4750) monthlyContribution = 202.5;
        else if (monthlyGross <= 5250) monthlyContribution = 225;
        else if (monthlyGross <= 5750) monthlyContribution = 247.5;
        else if (monthlyGross <= 6250) monthlyContribution = 270;
        else if (monthlyGross <= 6750) monthlyContribution = 292.5;
        else if (monthlyGross <= 7250) monthlyContribution = 315;
        else if (monthlyGross <= 7750) monthlyContribution = 337.5;
        else if (monthlyGross <= 8250) monthlyContribution = 360;
        else if (monthlyGross <= 8750) monthlyContribution = 382.5;
        else if (monthlyGross <= 9250) monthlyContribution = 405;
        else if (monthlyGross <= 9750) monthlyContribution = 427.5;
        else if (monthlyGross <= 10250) monthlyContribution = 450;
        else if (monthlyGross <= 10750) monthlyContribution = 472.5;
        else if (monthlyGross <= 11250) monthlyContribution = 495;
        else if (monthlyGross <= 11750) monthlyContribution = 517.5;
        else if (monthlyGross <= 12250) monthlyContribution = 540;
        else if (monthlyGross <= 12750) monthlyContribution = 562.5;
        else if (monthlyGross <= 13250) monthlyContribution = 585;
        else if (monthlyGross <= 13750) monthlyContribution = 607.5;
        else if (monthlyGross <= 14250) monthlyContribution = 630;
        else if (monthlyGross <= 14750) monthlyContribution = 652.5;
        else if (monthlyGross <= 15250) monthlyContribution = 675;
        else if (monthlyGross <= 15750) monthlyContribution = 697.5;
        else if (monthlyGross <= 16250) monthlyContribution = 720;
        else if (monthlyGross <= 16750) monthlyContribution = 742.5;
        else if (monthlyGross <= 17250) monthlyContribution = 765;
        else if (monthlyGross <= 17750) monthlyContribution = 787.5;
        else if (monthlyGross <= 18250) monthlyContribution = 810;
        else if (monthlyGross <= 18750) monthlyContribution = 832.5;
        else if (monthlyGross <= 19250) monthlyContribution = 855;
        else if (monthlyGross <= 19750) monthlyContribution = 877.5;
        else if (monthlyGross <= 20250) monthlyContribution = 900;
        else if (monthlyGross <= 20750) monthlyContribution = 922.5;
        else if (monthlyGross <= 21250) monthlyContribution = 945;
        else if (monthlyGross <= 21750) monthlyContribution = 967.5;
        else if (monthlyGross <= 22250) monthlyContribution = 990;
        else if (monthlyGross <= 22750) monthlyContribution = 1012.5;
        else if (monthlyGross <= 23250) monthlyContribution = 1035;
        else if (monthlyGross <= 23750) monthlyContribution = 1057.5;
        else if (monthlyGross <= 24250) monthlyContribution = 1080;
        else monthlyContribution = 1125;

        return monthlyContribution;
    }

    // PhilHealth Monthly Deduction (employee share)
    static double computePhilHealth(double monthlySalary) {
        double monthlyPremium = monthlySalary * 0.03;
        if (monthlyPremium > 1800) monthlyPremium = 1800;
        return monthlyPremium * 0.5;
    }

    // Pag-IBIG Monthly Deduction
    static double computePagibig(double monthlySalary) {
        double rate = (monthlySalary > 1500) ? 0.04 : (monthlySalary >= 1000 ? 0.03 : 0);
        double contribution = monthlySalary * rate;
        return Math.min(contribution, 100);
    }

    // Income Tax Monthly Deduction
    static double computeIncomeTax(double taxableIncome) {
        if (taxableIncome <= 20832) return 0;
        else if (taxableIncome <= 33332) return (taxableIncome - 20833) * 0.20;
        else if (taxableIncome <= 66666) return 2500 + (taxableIncome - 33333) * 0.25;
        else if (taxableIncome <= 166666) return 10833 + (taxableIncome - 66667) * 0.30;
        else if (taxableIncome <= 666666) return 40833.33 + (taxableIncome - 166667) * 0.32;
        else return 200833.33 + (taxableIncome - 666667) * 0.35;
    }
}
