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
    public static double computeSSS(double[] grossPay) {
        double total = 0;
        for (int i = 0; i < grossPay.length; i++) {
            total = total + grossPay[i];
        }
        return MotorPHEmployeeApp.computeSSS(total);
    }

    // Reuse the existing PhilHealth computation.
    public static double computePhilHealth(double[] grossPay) {
        double total = 0;
        for (int i = 0; i < grossPay.length; i++) {
            total = total + grossPay[i];
        }
        return MotorPHEmployeeApp.computePhilHealth(total);
    }

    // Reuse the existing Pag-IBIG computation.
    public static double computePagIBIG(double[] grossPay) {
        double total = 0;
        for (int i = 0; i < grossPay.length; i++) {
            total = total + grossPay[i];
        }
        return MotorPHEmployeeApp.computePagibig(total);
    }

    // Reuse the existing income (withholding) tax computation.
    public static double computeWithholdingTax(double[] taxableIncome) {
        double total = 0;
        for (int i = 0; i < taxableIncome.length; i++) {
            total = total + taxableIncome[i];
        }
        return MotorPHEmployeeApp.computeIncomeTax(total);
    }

    // Adds up all the government deductions passed in as an array.
    public static double computeDeductions(double[] deductions) {
        double total = 0;
        for (int i = 0; i < deductions.length; i++) {
            total = total + deductions[i];
        }
        return total;
    }

    // Net pay = gross pay minus total deductions.
    // amounts[0] = gross pay, amounts[1] = total deductions
    public static double computeNetPay(double[] amounts) {
        return amounts[0] - amounts[1];
    }
}