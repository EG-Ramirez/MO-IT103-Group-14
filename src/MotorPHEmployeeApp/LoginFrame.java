package MotorPHEmployeeApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private static final String PASSWORD = "12345";
    private static final String EMPLOYEE = "employee";
    private static final String STAFF = "payroll_staff";

    public LoginFrame() {
        setTitle("MotorPH Payroll System");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridLayout(1, 2));

        // LEFT PANEL
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
        leftPanel.setBorder(BorderFactory.createEmptyBorder(100, 40, 100, 40));

        JLabel systemLabel = new JLabel("MotorPH Payroll System");
        systemLabel.setForeground(Color.WHITE);
        systemLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        systemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLabel = new JLabel("Secure HR Management");
        subLabel.setForeground(Color.WHITE);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(systemLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(subLabel);

        // RIGHT PANEL
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(245, 245, 245));

        JPanel loginCard = new JPanel();
        loginCard.setPreferredSize(new Dimension(280, 220));
        loginCard.setBackground(Color.WHITE);
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel loginTitle = new JLabel("Login");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Enter your credentials");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginButton.setBackground(new Color(0, 128, 215));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        loginCard.add(loginTitle);
        loginCard.add(Box.createRigidArea(new Dimension(0, 5)));
        loginCard.add(subtitle);
        loginCard.add(Box.createRigidArea(new Dimension(0, 15)));
        loginCard.add(usernameField);
        loginCard.add(Box.createRigidArea(new Dimension(0, 10)));
        loginCard.add(passwordField);
        loginCard.add(Box.createRigidArea(new Dimension(0, 15)));
        loginCard.add(loginButton);

        rightPanel.add(loginCard);

        add(leftPanel);
        add(rightPanel);

        // ENTER key login
        getRootPane().setDefaultButton(loginButton);

        usernameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordField.requestFocusInWindow();
            }
        });

        usernameField.requestFocusInWindow();

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

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both username and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean validUser =
                username.equals(STAFF)
                        || username.equals(EMPLOYEE);

        boolean validPassword =
                password.equals(PASSWORD);

        if (!validUser || !validPassword) {
            JOptionPane.showMessageDialog(this,
                    "Incorrect username and/or password!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocusInWindow();
            return;
        }

        if (username.equals(EMPLOYEE)) {
            new EmployeePortalFrame(username).setVisible(true);
        } else {
            new PayrollStaffFrame().setVisible(true);
        }
        dispose();
    }
}