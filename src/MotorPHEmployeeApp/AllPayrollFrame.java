package MotorPHEmployeeApp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;

public class AllPayrollFrame extends JFrame {
private JTextArea reportArea;
    private JButton generateButton;
    private JButton closeButton;

    public AllPayrollFrame() {
        setTitle("All Employees Payroll");
        setSize(720, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Top label
        JLabel titleLabel = new JLabel("Payroll Reports - All Employees", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // Report area
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setPreferredSize(new Dimension(680, 460));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Buttons
        JPanel bottomPanel = new JPanel();
        generateButton = new JButton("Generate All");
        closeButton = new JButton("Close");
        bottomPanel.add(generateButton);
        bottomPanel.add(closeButton);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Events
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGenerateAll();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void handleGenerateAll() {
        if (MotorPHEmployeeApp.employees == null || MotorPHEmployeeApp.employees.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "No employees loaded.",
                    "Empty Data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ask the user which payroll period to process
        String[] monthOptions = {
            "All Months",
            "June", "July", "August", "September",
            "October", "November", "December"
        };

        JComboBox<String> monthSelector = new JComboBox<>(monthOptions);

        int choice = JOptionPane.showConfirmDialog(
                this,
                monthSelector,
                "Select Payroll Period",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice != JOptionPane.OK_OPTION) {
            return;   // user cancelled
        }
        // Map the selected label back to a month number (0 = all months)
        int selectedIndex = monthSelector.getSelectedIndex();
        int filterMonth = (selectedIndex == 0) ? 0 : (selectedIndex + 5);

        // Generate the report, skipping months that don't match the filter
        String[] monthNames = {"", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

        StringBuilder sb = new StringBuilder();
        for (MotorPHEmployeeApp.Employee emp : MotorPHEmployeeApp.employees) {

            int startMonth = (filterMonth == 0) ? 6 : filterMonth;
            int endMonth = (filterMonth == 0) ? 12 : filterMonth;

            // Build a filtered report for this employee
            StringBuilder empSb = new StringBuilder();
            boolean hasData = false;

            empSb.append("   ==================================================\n");
            empSb.append("                     PAYROLL REPORT                  \n");
            empSb.append("   ==================================================\n");
            empSb.append("      Employee Name : ").append(emp.name).append("\n");
            empSb.append("      Employee ID   : ").append(emp.employeeNumber).append("\n");
            empSb.append("      Birthday      : ").append(emp.birthday).append("\n");
            empSb.append("   ==================================================\n\n");

            for (int m = startMonth; m <= endMonth; m++) {
                double firstHours = MotorPHEmployeeApp.computeHoursWorked(
                        emp.attendanceIn[m][0], emp.attendanceOut[m][0]);
                double secondHours = MotorPHEmployeeApp.computeHoursWorked(
                        emp.attendanceIn[m][1], emp.attendanceOut[m][1]);

                if (firstHours == 0 && secondHours == 0) {
                    continue;
                }
                hasData = true;

                double firstGross = firstHours * emp.hourlyRate;
                double secondGross = secondHours * emp.hourlyRate;
                double combinedGross = firstGross + secondGross;

                double sss = MotorPHEmployeeApp.computeSSS(combinedGross);
                double philHealth = MotorPHEmployeeApp.computePhilHealth(combinedGross);
                double pagIbig = MotorPHEmployeeApp.computePagibig(combinedGross);
                double tax = MotorPHEmployeeApp.computeIncomeTax(
                        combinedGross - (sss + philHealth + pagIbig));
                double totalDeductions = sss + philHealth + pagIbig + tax;

                empSb.append("                --- Summary for: ").append(monthNames[m]).append(" ---\n\n");
                empSb.append("      [ First Cutoff: 1 - 15 ]\n");
                empSb.append("      Hours Worked : ").append(firstHours).append("\n");
                empSb.append("      Gross Pay    : Php ").append(firstGross).append("\n");
                empSb.append("      Net Pay      : Php ").append(firstGross).append("\n");
                empSb.append("      [ Second Cutoff: 16 - 30 ]\n");
                empSb.append("      Hours Worked : ").append(secondHours).append("\n");
                empSb.append("      Gross Pay    : Php ").append(secondGross).append("\n");
                empSb.append("      Net Pay      : Php ").append(secondGross - totalDeductions).append("\n");
                empSb.append("      =========== Monthly Deductions ===========\n");
                empSb.append("      SSS          : Php ").append(sss).append("\n");
                empSb.append("      PhilHealth   : Php ").append(philHealth).append("\n");
                empSb.append("      Pag-IBIG     : Php ").append(pagIbig).append("\n");
                empSb.append("      Tax          : Php ").append(tax).append("\n");
                empSb.append("      Total        : Php ").append(totalDeductions).append("\n\n");
                empSb.append("   ==================================================\n\n");
            }

            if (hasData) {
                sb.append(empSb);
                sb.append("\n");
            }
        }

        reportArea.setText(sb.toString());
        reportArea.setCaretPosition(0);

        // Persist the computed payroll fields, then confirm to the user
        MotorPHEmployeeApp.writeComputedPayrollToCSV();

        String periodLabel = (filterMonth == 0) ? "All Months" : monthNames[filterMonth];
        JOptionPane.showMessageDialog(this,
                "Payroll computed for all employees.\n"
                + "Period: " + periodLabel + "\n"
                + "Results were generated and saved to payroll_computed.csv.",
                "Computation Complete",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
