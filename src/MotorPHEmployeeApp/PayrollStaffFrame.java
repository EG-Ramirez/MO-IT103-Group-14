package MotorPHEmployeeApp;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PayrollStaffFrame extends JFrame {
    private JButton inputEmployeeButton;
    private JButton oneEmployeeButton;
    private JButton allEmployeesButton;
    private JButton exitButton;

    public PayrollStaffFrame() {
        setTitle("Payroll Staff Portal");
        setSize(400, 340);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Title
        JLabel titleLabel = new JLabel("Process Payroll", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        titleLabel.setAligmentX(JPanel.CENTER ALIGNMENT) ;

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 60, 25, 60));

        inputEmployeeButton = new JButton("Add New Employee Record");
        oneEmployeeButton = new JButton("Process One Employee");
        allEmployeesButton = new JButton("Process All Employees");
        exitButton = new JButton("Exit");

        buttonPanel.add(inputEmployeeButton);
        buttonPanel.add(oneEmployeeButton);
        buttonPanel.add(allEmployeesButton);
        buttonPanel.add(exitButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(titleLabel);
        mainPanel.add(buttonPanel);
        add(mainPanel);

        // -- Action Events --

        //Opens the Add Employee UI window
        inputEmployeeButton.addActionListener(new ActionListener() {     // NEW
            @Override
            public void actionPerformed(ActionEvent e) {
                new EmployeeInputFrame().setVisible(true);
            }
        });

        //Opens the Single Employee Processing Window
        oneEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SinglePayrollFrame().setVisible(true);
            }
        });

        //Opens the Complete Summary Board Window
        allEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AllPayrollFrame().setVisible(true);
            }
        });

        //Closes the current frame window
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
