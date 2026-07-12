package MotorPHEmployeeApp;

import java.awt.*;
import javax.swing.*;

public class PayrollSummaryFrame extends JFrame {

    private JTextArea summaryArea;
    private JButton closeButton;

    public PayrollSummaryFrame(String summary) {
        setTitle("Payroll Summary");
        setSize(650, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("MotorPH Payroll System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel subtitle = new JLabel("Payroll Summary");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel textPanel = new JPanel(new GridLayout(2,1));
        textPanel.setBackground(new Color(0,102,204));
        textPanel.add(titleLabel);
        textPanel.add(subtitle);

        headerPanel.add(textPanel, BorderLayout.WEST);

        summaryArea = new JTextArea(summary);
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        summaryArea.setBackground(new Color(245, 248, 252));
        summaryArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        summaryArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(summaryArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);

        closeButton = new JButton("Close");
        styleButton(closeButton);
        bottomPanel.add(closeButton);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        closeButton.addActionListener(e -> dispose());
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }
}