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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class EmployeePortalFrame extends JFrame {
private JTextField empNumberField;
    private JButton searchButton;
    private JButton exitButton;
    private JTextArea displayArea;

    public EmployeePortalFrame() {
        setTitle("Employee Portal");
        setSize(450, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Title
        JLabel titleLabel = new JLabel("Employee Information Lookup", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // Top input area
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topPanel.add(new JLabel("Employee Number:"));
        empNumberField = new JTextField();
        topPanel.add(empNumberField);
        searchButton = new JButton("Search");
        topPanel.add(searchButton);

        // Display area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        displayArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Bottom button panel
        JPanel bottomPanel = new JPanel();
        exitButton = new JButton("Exit");
        bottomPanel.add(exitButton);

        // Layout assembly
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(titleLabel, BorderLayout.NORTH);
        northPanel.add(topPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);
        add(displayArea, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Events
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void handleSearch() {
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
            displayArea.setText("");
            JOptionPane.showMessageDialog(this,
                    "Employee Number Does Not Exist!",
                    "Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=========== Employee Information ===========\n");
        sb.append("Employee Number : ").append(emp.employeeNumber).append("\n");
        sb.append("Name            : ").append(emp.name).append("\n");
        sb.append("Birthday        : ").append(emp.birthday).append("\n");
        sb.append("============================================\n");
        displayArea.setText(sb.toString());
    }
}