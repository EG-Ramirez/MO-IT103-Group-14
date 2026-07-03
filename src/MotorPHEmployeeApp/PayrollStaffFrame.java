package MotorPHEmployeeApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PayrollStaffFrame extends JFrame {
    private JButton inputEmployeeButton;
    private JButton oneEmployeeButton;
    private JButton allEmployeesButton;
    private JButton viewRecordsButton;
    private JButton payrollSummaryButton;
    private JButton exitButton;

    public PayrollStaffFrame() {
        setTitle("Payroll Staff Portal");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridLayout(1, 2));

        //Left panel
        JPanel leftPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color c1 = new Color(0, 76, 153);
                Color c2 = new Color(0, 128, 215);
                GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(120, 40, 120, 40));

        JLabel titleLeft = new JLabel("Payroll Staff Portal");
        titleLeft.setForeground(Color.WHITE);
        titleLeft.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLeft.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLeft = new JLabel("Manage Payroll System");
        subLeft.setForeground(Color.WHITE);
        subLeft.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subLeft.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(titleLeft);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(subLeft);

        // RIGHT PANEL (BUTTON CARD AREA)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(245, 245, 245));

        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(300, 300));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Process Payroll", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        inputEmployeeButton = new JButton("Add New Employee Record");
        oneEmployeeButton = new JButton("Process One Employee");
        allEmployeesButton = new JButton("Process All Employees");
        viewRecordsButton = new JButton("View Employee Records");
        payrollSummaryButton = new JButton("Generate Payroll Summary");
        exitButton = new JButton("Exit");

        JButton[] buttons = {
            inputEmployeeButton,
            oneEmployeeButton,
            allEmployeesButton,
            viewRecordsButton,
            payrollSummaryButton,
            exitButton
        };

        for (JButton btn : buttons) {
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(0, 128, 215));
            btn.setForeground(Color.WHITE);
            buttonPanel.add(btn);
        }

        // Exit button color
        exitButton.setBackground(new Color(0, 128, 215));

        cardPanel.add(titleLabel, BorderLayout.NORTH);
        cardPanel.add(buttonPanel, BorderLayout.CENTER);

        rightPanel.add(cardPanel);

        add(leftPanel);
        add(rightPanel);

        // ---------------- EVENTS  ----------------

        inputEmployeeButton.addActionListener(new ActionListener() {     
            @Override
            public void actionPerformed(ActionEvent e) {
                new EmployeeInputFrame().setVisible(true);
            }
        });

        oneEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SinglePayrollFrame().setVisible(true);
            }
        });

        allEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AllPayrollFrame().setVisible(true);
            }
        });

        viewRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EmployeeRecordsFrame().setVisible(true);
            }
        });

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
                summaryArea.setLineWrap(true);
                summaryArea.setWrapStyleWord(true);
                summaryArea.setCaretPosition(0);

                JScrollPane scrollPane = new JScrollPane(summaryArea);
                scrollPane.setPreferredSize(new Dimension(600, 400));

                JOptionPane.showMessageDialog(
                        PayrollStaffFrame.this,
                        scrollPane,
                        "Payroll Summary",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}