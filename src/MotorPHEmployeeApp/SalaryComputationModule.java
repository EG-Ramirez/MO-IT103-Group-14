package MotorPHEmployeeApp;

/*
 * SalaryComputationModule
 * Feature 3 - Salary Computation
 *
 * This class keeps all the payroll computation in one place, separate
 * from the GUI windows. The frames only handle buttons and display;
 * the actual math is done here.
 */
public class SalaryComputationModule {

    // Gross pay = total hours worked for the period * hourly rate.
    // The hours are passed in as an array (e.g. first and second cutoff).
    public static double computeGrossPay(double[] hoursWorked, double hourlyRate) {
        double totalHours = 0;
        if (hoursWorked != null) {
            for (int i = 0; i < hoursWorked.length; i++) {
                totalHours = totalHours + hoursWorked[i];
            }
        }
        return totalHours * hourlyRate;
    }

    // Reuse the existing SSS table so the numbers stay the same.
    public static double computeSSS(double grossPay) {
        return MotorPHEmployeeApp.computeSSS(grossPay);
    }

    // Reuse the existing PhilHealth computation.
    public static double computePhilHealth(double grossPay) {
        return MotorPHEmployeeApp.computePhilHealth(grossPay);
    }

    // Reuse the existing Pag-IBIG computation.
    public static double computePagIBIG(double grossPay) {
        return MotorPHEmployeeApp.computePagibig(grossPay);
    }

    // Reuse the existing income (withholding) tax computation.
    public static double computeWithholdingTax(double taxableIncome) {
        return MotorPHEmployeeApp.computeIncomeTax(taxableIncome);
    }

    // Adds up all the government deductions.
    public static double computeDeductions(double sss, double philHealth,
                                           double pagIbig, double withholdingTax) {
        return sss + philHealth + pagIbig + withholdingTax;
    }

    // Net pay = gross pay minus total deductions.
    public static double computeNetPay(double grossPay, double totalDeductions) {
        return grossPay - totalDeductions;
    }
}