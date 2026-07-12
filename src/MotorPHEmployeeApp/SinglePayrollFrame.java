package MotorPHEmployeeApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

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

        JPanel headerPanel = createHeader();
        JPanel topPanel = createTopPanel();
        JScrollPane scrollPane = createReportArea();
        JPanel bottomPanel = createBottomPanel();

        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        mainContentPanel.add(topPanel, BorderLayout.NORTH);
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);
        mainContentPanel.add(bottomPanel, BorderLayout.SOUTH);

        //Frame layout
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);

      //Event
        setupEvents();
    }

    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("MotorPH Payroll System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel subtitle = new JLabel("Single Employee Payroll");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel textPanel = new JPanel(new GridLayout(2,1));
        textPanel.setBackground(new Color(0,102,204));
        textPanel.add(titleLabel);
        textPanel.add(subtitle);

        headerPanel.add(textPanel, BorderLayout.WEST);

        return headerPanel;
    }

    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel label = new JLabel("Input Employee Number:");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        
        empNumberField = new JTextField(12);
        empNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        restrictToDigits(empNumberField, 5);

        generateButton = new JButton("Generate Payroll");
        styleButton(generateButton);

        topPanel.add(label);
        topPanel.add(empNumberField);
        topPanel.add(generateButton);

        return topPanel;
    }

   
    private JScrollPane createReportArea() {
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setLineWrap(true); 
        reportArea.setWrapStyleWord(true); 
        reportArea.setMargin(new Insets(10,10,10,10)); 
        reportArea.setBorder(BorderFactory.createLineBorder(new Color(200,200,200))); 

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setPreferredSize(new Dimension(580, 380));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        return scrollPane;
    }

    ///BUtton panel
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        clearButton = new JButton("Clear");
        closeButton = new JButton("Close");

        styleButton(clearButton);
        styleButton(closeButton);

        bottomPanel.add(clearButton);
        bottomPanel.add(closeButton);

        return bottomPanel;
    }

    //Button style
    private void styleButton(JButton btn) {
        btn.setBackground(new Color(0, 120, 215));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
    }

    // Restricts a text field to numeric digits only, up to maxLen characters
    private void restrictToDigits(JTextField field, int maxLen) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isISOControl(c)) return;
                if (!Character.isDigit(c) || field.getText().length() >= maxLen) {
                    e.consume();
                }
            }
        });
    }

    //Event handlers
    private void setupEvents() {

        // ENTER key triggers generate
        empNumberField.addActionListener(e -> handleGenerate());

        // Generate button
        generateButton.addActionListener(e -> handleGenerate());

        // Clear button
        clearButton.addActionListener(e -> {
            reportArea.setText("");
            empNumberField.setText("");
            empNumberField.requestFocusInWindow();
        });

        // Close button
        closeButton.addActionListener(e -> dispose());
    }

   
    private void handleGenerate() {

        String empNo = empNumberField.getText().trim();

        if (empNo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an Employee Number.",
                    "Missing Input",
                    JOptionPane.WARNING_MESSAGE);
            empNumberField.setText("");
            empNumberField.requestFocusInWindow();
            return;
        }

        MotorPHEmployeeApp.Employee emp = MotorPHEmployeeApp.findEmployee(empNo);

        if (emp == null) {
            JOptionPane.showMessageDialog(this,
                    "Employee Number Does Not Exist!",
                    "Not Found",
                    JOptionPane.ERROR_MESSAGE);
            empNumberField.setText("");
            empNumberField.requestFocusInWindow();
            return;
        }

        String report = MotorPHEmployeeApp.processPayroll(emp);

        reportArea.setText(report);
        reportArea.setCaretPosition(0);
    }
}