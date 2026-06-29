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
import javax.swing.JTextField;

public class SinglePayrollFrame extends JFrame {
private JTextField empNumberField;
private JButton generateButton;
private JButton clearButton;
private JButton closeButton;
private JTextArea reportArea;

    public SinglePayrollFrame() {
        setTitle("Single Employee Payroll");
        setSize(620, 540);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Top panel: input + generate button
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        topPanel.add(new JLabel("Employee Number:"));
        empNumberField = new JTextField(12);
        topPanel.add(empNumberField);
        generateButton = new JButton("Generate Payroll");
        topPanel.add(generateButton);

        // Center: scrollable report area
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setPreferredSize(new Dimension(580, 380));

        // Bottom: utility buttons
        JPanel bottomPanel = new JPanel();
        clearButton = new JButton("Clear");
        closeButton = new JButton("Close");
        bottomPanel.add(clearButton);
        bottomPanel.add(closeButton);

        generateButton.setFocusable(false);
        clearButton.setFocusable(false);
        closeButton.setFocusable(false);

        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        mainContentPanel.add(topPanel, BorderLayout.NORTH);
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);
        mainContentPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainContentPanel);

        // Events
        
        // Pressing Enter on the employee number field triggers generate
        empNumberField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGenerate();
            }
        });
        
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGenerate();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reportArea.setText("");
                empNumberField.setText("");
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void handleGenerate() {
        String empNo = empNumberField.getText().trim();

        if (empNo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an Employee Number.",
                    "Missing Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        MotorPHEmployeeApp.Employee emp = MotorPHEmployeeApp.findEmployee(empNo);
        if (emp == null) {
            JOptionPane.showMessageDialog(this,
                    "Employee Number Does Not Exist!",
                    "Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String report = MotorPHEmployeeApp.processPayroll(emp);
        reportArea.setText(report);
        reportArea.setCaretPosition(0);
    }
}
