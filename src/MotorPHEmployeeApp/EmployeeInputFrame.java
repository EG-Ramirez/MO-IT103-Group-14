package MotorPHEmployeeApp;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class EmployeeInputFrame extends JFrame {

    private JTextField empNumberField;
    private JTextField empNameField;
    private JTextField payCoverageField;
    private JButton submitButton;
    private JButton clearButton;
    private JButton closeButton;

    public EmployeeInputFrame() {
        setTitle("Employee Information Form");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Title
        JLabel titleLabel = new JLabel("Enter New Employee Information", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        // Input panel (3 labels + 3 text fields)
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        inputPanel.add(new JLabel("Employee Number:"));
        empNumberField = new JTextField();
        inputPanel.add(empNumberField);

        inputPanel.add(new JLabel("Employee Name:"));
        empNameField = new JTextField();
        inputPanel.add(empNameField);

        inputPanel.add(new JLabel("Pay Coverage:"));
        payCoverageField = new JTextField();
        inputPanel.add(payCoverageField);

        // Button panel
        JPanel buttonPanel = new JPanel();
        submitButton = new JButton("Submit");
        clearButton = new JButton("Clear");
        closeButton = new JButton("Close");
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(closeButton);

        add(titleLabel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event handling
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleClear();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    // Handles the Submit button - validates input with exception handling
    private void handleSubmit() {
        String empNumberStr = empNumberField.getText().trim();
        String empName = empNameField.getText().trim();
        String payCoverageStr = payCoverageField.getText().trim();

        // --- Validate Employee Number ---
        if (empNumberStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Employee Number is required.",
                    "Missing Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int empNumber;
        try {
            empNumber = Integer.parseInt(empNumberStr);
            if (empNumber < 0) {
                JOptionPane.showMessageDialog(this,
                        "Employee Number cannot be negative.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Employee Number must be numeric.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Validate Employee Name ---
        if (empName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Employee Name is required.",
                    "Missing Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- Validate Pay Coverage ---
        if (payCoverageStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Pay Coverage is required.",
                    "Missing Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double payCoverage;
        try {
            payCoverage = Double.parseDouble(payCoverageStr);
            if (payCoverage < 0) {
                JOptionPane.showMessageDialog(this,
                        "Pay Coverage cannot be negative.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Pay Coverage must be numeric.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Success feedback ---
        StringBuilder sb = new StringBuilder();
        sb.append("Employee information submitted successfully!\n\n");
        sb.append("Employee Number : ").append(empNumber).append("\n");
        sb.append("Employee Name   : ").append(empName).append("\n");
        sb.append("Pay Coverage    : ").append(payCoverage).append("\n");

        JOptionPane.showMessageDialog(this,
                sb.toString(),
                "Submission Successful",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Handles the Clear button - resets all input fields
    private void handleClear() {
        empNumberField.setText("");
        empNameField.setText("");
        payCoverageField.setText("");
    }
}
