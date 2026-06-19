package MotorPHEmployeeApp;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class PayrollStaffFrame extends JFrame {
    private JButton inputEmployeeButton;
    private JButton oneEmployeeButton;
    private JButton allEmployeesButton;
    private JButton viewRecordsButton;
    private JButton payrollSummaryButton;
    private JButton exitButton;

    public PayrollStaffFrame() {
        setTitle("Payroll Staff Portal");
        setSize(400, 440);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Title
        JLabel titleLabel = new JLabel("Process Payroll", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        titleLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 60, 25, 60));

        inputEmployeeButton = new JButton("Add New Employee Record");
        oneEmployeeButton = new JButton("Process One Employee");
        allEmployeesButton = new JButton("Process All Employees");
        viewRecordsButton = new JButton("View Employee Records");
        payrollSummaryButton = new JButton("Generate Payroll Summary");
        exitButton = new JButton("Exit");

        buttonPanel.add(inputEmployeeButton);
        buttonPanel.add(oneEmployeeButton);
        buttonPanel.add(allEmployeesButton);
        buttonPanel.add(viewRecordsButton);
        buttonPanel.add(payrollSummaryButton);
        buttonPanel.add(exitButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(titleLabel);
        mainPanel.add(buttonPanel);
        add(mainPanel);

        // -- Action Events --

        //Opens the Add Employee UI window
        inputEmployeeButton.addActionListener(new ActionListener() {     // NEW
            @Override
            public void actionPerformed(ActionEvent e) {
                new EmployeeInputFrame().setVisible(true);
            }
        });

        //Opens the Single Employee Processing Window
        oneEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SinglePayrollFrame().setVisible(true);
            }
        });

        //Opens the Complete Summary Board Window
        allEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AllPayrollFrame().setVisible(true);
            }
        });

        
        // Opens the Employee Records table view
        viewRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EmployeeRecordsFrame().setVisible(true);
            }
        });

        //Opens the company-wide Payroll Summary dialog
        payrollSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MotorPHEmployeeApp.employees == null || MotorPHEmployeeApp.employees.length == 0) {
                    JOptionPane.showMessageDialog(PayrollStaffFrame.this,
                            "No employee records are loaded.",
                            "Empty Data",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String summary = MotorPHEmployeeApp.generatePayrollSummary();

                JTextArea summaryArea = new JTextArea(summary);
                summaryArea.setEditable(false);
                summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

                JOptionPane.showMessageDialog(PayrollStaffFrame.this,
                        summaryArea,
                        "Payroll Summary",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //Closes the current frame window
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
