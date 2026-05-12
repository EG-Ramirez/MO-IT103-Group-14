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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginFrame extends JFrame {private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("MotorPH Payroll - Login");
        setSize(400, 230);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Title at the top
        JLabel titleLabel = new JLabel("MotorPH Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 5, 25));

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        // Button panel
        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Login");
        buttonPanel.add(loginButton);

        add(titleLabel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Login event
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Same rule as CP1: only "payroll_staff" or "employee" with password "12345"
        if (!(password.equals("12345") &&
                (username.equals("payroll_staff") || username.equals("employee")))) {
            JOptionPane.showMessageDialog(this,
                    "Incorrect username and/or password!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            return;
        }

        // Route to the correct portal
        if (username.equals("employee")) {
            new EmployeePortalFrame().setVisible(true);
        } else {
            new PayrollStaffFrame().setVisible(true);
        }
        dispose();
    }
}
