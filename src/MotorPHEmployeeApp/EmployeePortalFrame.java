package MotorPHEmployeeApp;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class EmployeePortalFrame extends JFrame {
    private JButton exitButton;
    private JTextArea displayArea;

    // No-arg constructor kept so any existing call sites continue to compile
    public EmployeePortalFrame() {
        this(null);
    }

    public EmployeePortalFrame(String username) {
        setTitle("Employee Portal");
        setSize(450, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Ask the employee for their number before the portal opens
        String empNo = null;
        if (username != null && username.equals("employee")) {
            String input = JOptionPane.showInputDialog(
                    null,
                    "Please enter your Employee Number to continue:",
                    "Employee Login",
                    JOptionPane.QUESTION_MESSAGE);

            if (input == null || input.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Employee Number is required to access the portal.",
                        "Access Denied",
                        JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }

            empNo = input.trim();

            if (MotorPHEmployeeApp.findEmployee(empNo) == null) {
                JOptionPane.showMessageDialog(null,
                        "Employee Number not found. Please contact your administrator.",
                        "Not Found",
                        JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
        }

        // Title
        JLabel titleLabel = new JLabel("Employee Information", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // Display area — shows the employee's info immediately, no search needed
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        displayArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Populate the display immediately using the verified employee number
        if (empNo != null) {
            MotorPHEmployeeApp.Employee emp = MotorPHEmployeeApp.findEmployee(empNo);
            StringBuilder sb = new StringBuilder();
            sb.append("    ============================================\n");
            sb.append("                EMPLOYEE INFORMATION           \n");
            sb.append("    ============================================\n");
            sb.append("       Employee Number : ").append(emp.employeeNumber).append("\n");
            sb.append("       Name            : ").append(emp.name).append("\n");
            sb.append("       Birthday        : ").append(emp.birthday).append("\n");
            sb.append("    ============================================\n");
            displayArea.setText(sb.toString());
        }

        // Bottom button panel
        JPanel bottomPanel = new JPanel();
        exitButton = new JButton("Exit");
        bottomPanel.add(exitButton);

        add(titleLabel, BorderLayout.NORTH);
        add(displayArea, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}