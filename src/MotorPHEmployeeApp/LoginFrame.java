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
        loginCard.setPreferredSize(new Dimension(300, 300));
        loginCard.setBackground(Color.WHITE);
        loginCard.setLayout(new GridBagLayout());
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.gridx = 0;
        cardGbc.weightx = 1.0;
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        cardGbc.anchor = GridBagConstraints.CENTER;

        JLabel loginTitle = new JLabel("Login");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("Enter your credentials");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
               
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameLabel.setHorizontalAlignment(SwingConstants.LEFT);
       
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginButton.setBackground(new Color(0, 128, 215));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension (220,36));
        
        cardGbc.gridy = 0;
        cardGbc.insets = new Insets (0,0,5,0);
        loginCard.add(loginTitle, cardGbc);
        
        cardGbc.gridy = 1;
        cardGbc.insets = new Insets (0,0,15,0);
        loginCard.add(subtitle, cardGbc);
        
        cardGbc.gridy = 2;
        cardGbc.insets = new Insets (0,0,4,0);
        loginCard.add(usernameLabel, cardGbc);
        
        cardGbc.gridy = 3;
        cardGbc.insets = new Insets (0,0,10,0);
        loginCard.add(usernameField, cardGbc);
        
        cardGbc.gridy = 4;
        cardGbc.insets = new Insets (0,0,4,0);
        loginCard.add(passwordLabel, cardGbc);
        
        cardGbc.gridy = 5;
        cardGbc.insets = new Insets (0,0,25,0);
        loginCard.add(passwordField, cardGbc);
        
        cardGbc.gridy = 6;
        cardGbc.insets = new Insets (0,0,0,0);
        loginCard.add(loginButton, cardGbc);

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
            dispose();
            EmployeePortalFrame portal = new EmployeePortalFrame(username);
            if(portal.isAccessGranted()){
                portal.setVisible(true);
            }
        } else {
            dispose();
            new PayrollStaffFrame().setVisible(true);
        }
    }
}
