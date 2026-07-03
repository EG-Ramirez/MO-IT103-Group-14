package MotorPHEmployeeApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class EmployeePortalFrame extends JFrame {
    private JButton exitButton;
    private JTextArea displayArea;

    public EmployeePortalFrame() {
        this(null);
    }

    public EmployeePortalFrame(String username) {
        setTitle("Employee Portal");
        setSize(520, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // MAIN BACKGROUND
        getContentPane().setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout(10, 10));

        // ================= HEADER =================
        JLabel titleLabel = new JLabel("Employee Information", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(25, 118, 210)); // blue
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));

        add(titleLabel, BorderLayout.NORTH);

        // ================= EMPLOYEE INPUT (CUSTOM THEMED) =================
        String empNo = null;

        if (username != null && username.equals("employee")) {

            JTextField empField = new JTextField(15);
            empField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BorderLayout(10, 10));
            cardPanel.setBackground(Color.WHITE);
            cardPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JLabel header = new JLabel("Employee Login", SwingConstants.CENTER);
            header.setFont(new Font("Segoe UI", Font.BOLD, 16));
            header.setOpaque(true);
            header.setBackground(new Color(25, 118, 210));
            header.setForeground(Color.WHITE);
            header.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));

            JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            inputPanel.setBackground(Color.WHITE);

            JLabel label = new JLabel("Enter Employee Number:");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            inputPanel.add(label);
            inputPanel.add(empField);

            cardPanel.add(header, BorderLayout.NORTH);
            cardPanel.add(inputPanel, BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(
                    null,
                    cardPanel,
                    "Login",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION || empField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Employee Number is required to access the portal.",
                        "Access Denied",
                        JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }

            empNo = empField.getText().trim();

            if (MotorPHEmployeeApp.findEmployee(empNo) == null) {
                JOptionPane.showMessageDialog(null,
                        "Employee Number not found. Please contact your administrator.",
                        "Not Found",
                        JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
        }

        // ================= DISPLAY AREA (CARD STYLE) =================
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        displayArea.setBackground(Color.WHITE);
        displayArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);

        // ================= POPULATE DATA =================
        if (empNo != null) {
            MotorPHEmployeeApp.Employee emp = MotorPHEmployeeApp.findEmployee(empNo);
            StringBuilder sb = new StringBuilder();
            sb.append("============================================\n");
            sb.append("            EMPLOYEE INFORMATION           \n");
            sb.append("============================================\n");
            sb.append(" Employee Number : ").append(emp.employeeNumber).append("\n");
            sb.append(" Name            : ").append(emp.name).append("\n");
            sb.append(" Birthday        : ").append(emp.birthday).append("\n");
            sb.append("============================================\n");
            displayArea.setText(sb.toString());
        }

        // ================= FOOTER BUTTON =================
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 247, 250));

        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        exitButton.setBackground(new Color(25, 118, 210));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));

        bottomPanel.add(exitButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // ================= ACTION =================
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}