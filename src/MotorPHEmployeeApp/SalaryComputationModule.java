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
    
    private static double sum(double[] values) {
        double total = 0;

        if (values != null) {
            for (double value : values) {
                total += value;
            }
        }

        return total;
    }
    
    // Gross pay = total hours worked for the period * hourly rate.
    // The hours are passed in as an array (e.g. first and second cutoff).
    public static double computeGrossPay(double[] hoursWorked, double hourlyRate) {
        return sum(hoursWorked) * hourlyRate;
    }

    // Uses the existing SSS table so the numbers stay the same.
    public static double computeSSS(double[] grossPay) {
        return MotorPHEmployeeApp.computeSSS(sum(grossPay));
    }

    // Uses the existing PhilHealth computation.
    public static double computePhilHealth(double[] grossPay) {
        return MotorPHEmployeeApp.computePhilHealth(sum(grossPay));
    }
    
    // Uses the existing Pag-IBIG computation.
    public static double computePagIBIG(double[] grossPay) {
        return MotorPHEmployeeApp.computePagibig(sum(grossPay));
    }
    
    // Uses the existing income (withholding) tax computation.
    public static double computeWithholdingTax(double[] grossPay) {
        return MotorPHEmployeeApp.computeIncomeTax(sum(grossPay));
    }
    
    // Adds up all the government deductions passed in as an array.
    public static double computeDeductions(double[] deductions) {
        return sum(deductions);
    }

    // Net pay = gross pay minus total deductions.
    // amounts[0] = gross pay, amounts[1] = total deductions
    public static double computeNetPay(double[] amounts) {
        if (amounts == null || amounts.length < 2) {
            return 0;
        }

        return amounts[0] - amounts[1];
    }
}
