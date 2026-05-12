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
import javax.swing.SwingConstants;

public class AllPayrollFrame extends JFrame {
private JTextArea reportArea;
    private JButton generateButton;
    private JButton closeButton;

    public AllPayrollFrame() {
        setTitle("All Employees Payroll");
        setSize(720, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Top label
        JLabel titleLabel = new JLabel("Payroll Reports - All Employees", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // Report area
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setPreferredSize(new Dimension(680, 460));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Buttons
        JPanel bottomPanel = new JPanel();
        generateButton = new JButton("Generate All");
        closeButton = new JButton("Close");
        bottomPanel.add(generateButton);
        bottomPanel.add(closeButton);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Events
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGenerateAll();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void handleGenerateAll() {
        if (MotorPHEmployeeApp.employees == null || MotorPHEmployeeApp.employees.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "No employees loaded.",
                    "Empty Data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (MotorPHEmployeeApp.Employee emp : MotorPHEmployeeApp.employees) {
            sb.append(MotorPHEmployeeApp.processPayroll(emp));
            sb.append("\n");
        }

        reportArea.setText(sb.toString());
        reportArea.setCaretPosition(0);
    }
}
