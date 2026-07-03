package MotorPHEmployeeApp;

import java.awt.*;
import javax.swing.*;

public class AllPayrollFrame extends JFrame {

    private JTextArea reportArea;
    private JButton generateButton;
    private JButton closeButton;

    public AllPayrollFrame() {
        setTitle("All Employees Payroll");
        setSize(750, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("MotorPH Payroll System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel subtitle = new JLabel("All Employees Payroll Report");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel textPanel = new JPanel(new GridLayout(2,1));
        textPanel.setBackground(new Color(0,102,204));
        textPanel.add(titleLabel);
        textPanel.add(subtitle);

        headerPanel.add(textPanel, BorderLayout.WEST);

        
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setBackground(new Color(245, 248, 252));
        reportArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

      
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);

        generateButton = new JButton("Generate All");
        closeButton = new JButton("Close");

        styleButton(generateButton);
        styleButton(closeButton);

        bottomPanel.add(generateButton);
        bottomPanel.add(closeButton);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        generateButton.addActionListener(e -> handleGenerateAll());
        closeButton.addActionListener(e -> dispose());
    }
   private void styleButton(JButton btn) {
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void handleGenerateAll() {

        if (MotorPHEmployeeApp.employees == null || MotorPHEmployeeApp.employees.length == 0) {
            JOptionPane.showMessageDialog(this, "No employees loaded.");
            return;
        }

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
                JOptionPane.OK_CANCEL_OPTION);

        if (choice != JOptionPane.OK_OPTION) return;

        int selectedIndex = monthSelector.getSelectedIndex();
        int filterMonth = (selectedIndex == 0) ? 0 : (selectedIndex + 5);

        String[] monthNames = {"", "January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};

        StringBuilder sb = new StringBuilder();

        for (MotorPHEmployeeApp.Employee emp : MotorPHEmployeeApp.employees) {

            int startMonth = (filterMonth == 0) ? 6 : filterMonth;
            int endMonth = (filterMonth == 0) ? 12 : filterMonth;

            boolean hasData = false;

            
            sb.append("\n====================================================\n");
            sb.append(" EMPLOYEE: ").append(emp.name.toUpperCase()).append("\n");
            sb.append(" ID       : ").append(emp.employeeNumber).append("\n");
            sb.append(" BIRTHDAY : ").append(emp.birthday).append("\n");
            sb.append("====================================================\n");

            for (int m = startMonth; m <= endMonth; m++) {

                double firstHours = MotorPHEmployeeApp.computeHoursWorked(
                        emp.attendanceIn[m][0], emp.attendanceOut[m][0]);

                double secondHours = MotorPHEmployeeApp.computeHoursWorked(
                        emp.attendanceIn[m][1], emp.attendanceOut[m][1]);

                if (firstHours == 0 && secondHours == 0) continue;

                hasData = true;

                double firstGross = firstHours * emp.hourlyRate;
                double secondGross = secondHours * emp.hourlyRate;
                double combinedGross = firstGross + secondGross;

                double sss = MotorPHEmployeeApp.computeSSS(combinedGross);
                double philHealth = MotorPHEmployeeApp.computePhilHealth(combinedGross);
                double pagIbig = MotorPHEmployeeApp.computePagibig(combinedGross);

                double taxableIncome = combinedGross - (sss + philHealth + pagIbig);
                double tax = MotorPHEmployeeApp.computeIncomeTax(taxableIncome);

                double totalDeductions = sss + philHealth + pagIbig + tax;
                double netPay = combinedGross - totalDeductions;

                // 🔵 MONTH SECTION
                sb.append("\n>> ").append(monthNames[m]).append("\n");
                sb.append("----------------------------------------------\n");

                sb.append(String.format("  First Cutoff  : %6.2f hrs | Php %10.2f%n", firstHours, firstGross));
                sb.append(String.format("  Second Cutoff : %6.2f hrs | Php %10.2f%n", secondHours, secondGross));

                sb.append("\n  DEDUCTIONS\n");
                sb.append(String.format("    SSS        : Php %,.2f%n", sss));
                sb.append(String.format("    PhilHealth : Php %,.2f%n", philHealth));
                sb.append(String.format("    Pag-IBIG   : Php %,.2f%n", pagIbig));
                sb.append(String.format("    Tax        : Php %,.2f%n", tax));

                sb.append("----------------------------------------------\n");
                sb.append(String.format("  NET PAY      : Php %,.2f%n", netPay));
                sb.append("==============================================\n");
            }

            if (!hasData) {
                sb.append("\n  No records found.\n");
            }

            sb.append("\n");
        }

        reportArea.setText(sb.toString());
        reportArea.setCaretPosition(0);

        EmployeeFileManager.writeComputedPayrollToCSV(
                MotorPHEmployeeApp.employees,
                filterMonth);

        String periodLabel = (filterMonth == 0) ? "All Months" : monthNames[filterMonth];

        JOptionPane.showMessageDialog(this,
                "Payroll computed successfully.\n"
                + "Period: " + periodLabel + "\n"
                + "Saved to payroll_computed.csv.",
                "Done",
                JOptionPane.INFORMATION_MESSAGE);
    }
}