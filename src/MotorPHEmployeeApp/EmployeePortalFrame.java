package MotorPHEmployeeApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class EmployeePortalFrame extends JFrame {
    private JButton exitButton;
    private JButton returnButton;
    private JButton logoutButton;
    private JTextArea displayArea;
    private boolean accessGranted = true;
    private String username;

    public EmployeePortalFrame() {
        this(null);
    }
    
    public boolean isAccessGranted(){
        return accessGranted;
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
            
            boolean validEntry = false;
            while(!validEntry) {

            JTextField empField = new JTextField ();
            empField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            
            JPanel cardPanel = new JPanel();
            cardPanel.setPreferredSize(new Dimension(340, 220));
            cardPanel.setLayout(new BorderLayout(10, 10));
            cardPanel.setBackground(Color.WHITE);
            cardPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(0, 0, 15, 0)
            ));
            
            JLabel header = new JLabel("Employee Login", SwingConstants.CENTER);
            header.setFont(new Font("Segoe UI", Font.BOLD, 16));
            header.setOpaque(true);
            header.setBackground(new Color(25, 118, 210));
            header.setForeground(Color.WHITE);
            header.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
            
            JPanel inputPanel = new JPanel (new GridBagLayout());
            inputPanel.setBackground(Color.WHITE);
            inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
            
            GridBagConstraints dGbc = new GridBagConstraints();
            dGbc.gridx = 0;
            dGbc.weightx = 1.0;
            dGbc.fill = GridBagConstraints.HORIZONTAL;
            dGbc.anchor = GridBagConstraints.CENTER;      
            
            JLabel label = new JLabel ("Employee Number");
            label.setFont(new Font ("Segoe UI", Font.BOLD, 12));
                    
            empField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
           
            dGbc.gridy = 0; dGbc.insets = new Insets (0, 0, 6, 0);
            inputPanel.add(label, dGbc);
            
            dGbc.gridy = 1; dGbc.insets = new Insets (0, 0, 0, 0);
            inputPanel.add(empField, dGbc);
            
            cardPanel.add(header, BorderLayout.NORTH);
            cardPanel.add(inputPanel, BorderLayout.CENTER);
            
            SwingUtilities.invokeLater(() -> empField.requestFocusInWindow());
                                      
            int result = JOptionPane.showConfirmDialog(
                    null,
                    cardPanel,
                    "Login", 
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                    );                                
         
            if (result != JOptionPane.OK_OPTION) {
                accessGranted = false;
                dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
                return;
            }
            
            accessGranted = true;
            String enteredEmpNo = empField.getText().trim();
            
            if (enteredEmpNo.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                        "Employee Number is required to access the portal.",
                        "Access Denied", 
                        JOptionPane.ERROR_MESSAGE);
                accessGranted = false;
                continue;
            }

            

            if (MotorPHEmployeeApp.findEmployee(enteredEmpNo) == null) {
                JOptionPane.showMessageDialog(null,
                        "Employee Number not found. Please enter a valid Employee Number.",
                        "Not Found",
                        JOptionPane.ERROR_MESSAGE);
                accessGranted = false;
                continue;
            }
            
            empNo = enteredEmpNo;
            validEntry = true;
            
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
        
        returnButton = new JButton("Return");
        returnButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        returnButton.setBackground(new Color(25, 118, 210));
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);
        returnButton.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutButton.setBackground(new Color(25, 118, 210));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        

        bottomPanel.add(exitButton);
        bottomPanel.add(returnButton);
        bottomPanel.add(logoutButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // ================= ACTION =================
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                EmployeePortalFrame portal = new EmployeePortalFrame(username);
                if(portal.isAccessGranted()){
                    portal.setVisible(true);
                }
                
            }
        });   
    }
}
