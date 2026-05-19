package MotorPHEmployeeApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;

/**
 * EmployeeInputFrame — Add New Employee Record
 *
 * GUI Components   : JTextField for Employee Number, Employee Name, Pay Coverage
 * Event Handling   : ActionListener on Submit, Clear, and Close buttons;
 *                    FocusListener for inline field hints
 * Exception Handling: try-catch for NumberFormatException on numeric fields;
 *                     JOptionPane dialogs for all validation feedback
 */
public class EmployeeInputFrame extends JFrame {

    //  Input Fields 
    private JTextField empNumberField;
    private JTextField empNameField;
    private JTextField payCoverageField;

    //  Inline error hint labels (one per field) 
    private JLabel empNumberHint;
    private JLabel empNameHint;
    private JLabel payCoverageHint;

    //  Action Buttons 
    private JButton submitButton;
    private JButton clearButton;
    private JButton closeButton;

    //  Colours used for hint labels 
    private static final Color HINT_ERROR   = new Color(180, 0, 0);
    private static final Color HINT_SUCCESS = new Color(0, 120, 0);
    private static final Color HINT_NEUTRAL = new Color(100, 100, 100);

    
    public EmployeeInputFrame() {
        setTitle("Add New Employee Record");
        setSize(480, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        buildUI();
        wireEvents();
    }

    //  UI Construction 

    private void buildUI() {
        //  Title bar 
        JLabel titleLabel = new JLabel("New Employee Information Form", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(30, 80, 160));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(14, 0, 6, 0));

        JLabel subtitleLabel = new JLabel(
                "All fields are required. Pay Coverage must be a positive number.",
                SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        subtitleLabel.setForeground(HINT_NEUTRAL);

        JPanel headerPanel = new JPanel(new BorderLayout(0, 2));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 10));
        headerPanel.add(titleLabel,    BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);

        //  Form panel (GridBagLayout for clean alignment) 
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder("Employee Details"),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));

        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.anchor = GridBagConstraints.WEST;
        labelGbc.insets = new Insets(6, 4, 0, 10);

        GridBagConstraints fieldGbc = new GridBagConstraints();
        fieldGbc.fill    = GridBagConstraints.HORIZONTAL;
        fieldGbc.weightx = 1.0;
        fieldGbc.insets  = new Insets(6, 0, 0, 4);

        GridBagConstraints hintGbc = new GridBagConstraints();
        hintGbc.fill    = GridBagConstraints.HORIZONTAL;
        hintGbc.weightx = 1.0;
        hintGbc.insets  = new Insets(0, 0, 2, 4);

        // Row 0 - Employee Number
        labelGbc.gridx = 0; labelGbc.gridy = 0;
        formPanel.add(makeLabel("Employee Number:"), labelGbc);

        empNumberField = new JTextField(18);
        empNumberField.setToolTipText("Enter the unique numeric employee ID (e.g. 10001)");
        fieldGbc.gridx = 1; fieldGbc.gridy = 0;
        formPanel.add(empNumberField, fieldGbc);

        empNumberHint = makeHint("e.g. 10001  —  must be a positive whole number");
        hintGbc.gridx = 1; hintGbc.gridy = 1;
        formPanel.add(empNumberHint, hintGbc);

        // Row 2 - Employee Name
        labelGbc.gridx = 0; labelGbc.gridy = 2;
        formPanel.add(makeLabel("Employee Name:"), labelGbc);

        empNameField = new JTextField(18);
        empNameField.setToolTipText("Full name of the employee (letters only)");
        fieldGbc.gridx = 1; fieldGbc.gridy = 2;
        formPanel.add(empNameField, fieldGbc);

        empNameHint = makeHint("e.g. Juan dela Cruz  —  letters and spaces only");
        hintGbc.gridx = 1; hintGbc.gridy = 3;
        formPanel.add(empNameHint, hintGbc);

        // Row 4 - Pay Coverage
        labelGbc.gridx = 0; labelGbc.gridy = 4;
        formPanel.add(makeLabel("Pay Coverage (₱):"), labelGbc);

        payCoverageField = new JTextField(18);
        payCoverageField.setToolTipText("Monthly pay coverage in Philippine Peso (e.g. 25000.00)");
        fieldGbc.gridx = 1; fieldGbc.gridy = 4;
        formPanel.add(payCoverageField, fieldGbc);

        payCoverageHint = makeHint("e.g. 25000.00  —  must be a positive numeric amount");
        hintGbc.gridx = 1; hintGbc.gridy = 5;
        formPanel.add(payCoverageHint, hintGbc);

        //  Button panel 
        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(30, 120, 60));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 13));
        submitButton.setFocusPainted(false);

        clearButton  = new JButton("Clear");
        closeButton  = new JButton("Close");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 10, 0));
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(closeButton);

        //  Assemble frame 
        add(headerPanel,  BorderLayout.NORTH);
        add(formPanel,    BorderLayout.CENTER);
        add(buttonPanel,  BorderLayout.SOUTH);
    }

    //  Event Wiring 

    private void wireEvents() {

        //  Submit button - validates all fields then persists the record 
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        //  Clear button - resets form to blank state 
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleClear();
            }
        });

        //  Close button - closes this window 
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //  FocusListeners - clear inline hints when user enters a field 
        addFocusReset(empNumberField,   empNumberHint,   "e.g. 10001  —  must be a positive whole number");
        addFocusReset(empNameField,     empNameHint,     "e.g. Juan dela Cruz  —  letters and spaces only");
        addFocusReset(payCoverageField, payCoverageHint, "e.g. 25000.00  —  must be a positive numeric amount");
    }

    //  handleSubmit: full validation with try-catch + JOptionPane feedback 

    private void handleSubmit() {

        boolean valid = true; // track overall form validity

        //  1. Validate Employee Number 
        String empNumberStr = empNumberField.getText().trim();

        if (empNumberStr.isEmpty()) {
            setHint(empNumberHint, "⚠ Employee Number is required.", HINT_ERROR);
            valid = false;
        } else {
            try {
                int empNumber = Integer.parseInt(empNumberStr);

                if (empNumber <= 0) {
                    setHint(empNumberHint, "⚠ Employee Number must be a positive integer.", HINT_ERROR);
                    valid = false;
                } else if (MotorPHEmployeeApp.findEmployee(empNumberStr) != null) {
                    // Duplicate check against the in-memory employee repository
                    setHint(empNumberHint, "⚠ Employee Number already exists in the system.", HINT_ERROR);
                    JOptionPane.showMessageDialog(
                            this,
                            "Employee Number " + empNumber + " is already registered.\n"
                                    + "Please enter a unique Employee Number.",
                            "Duplicate Employee Number",
                            JOptionPane.ERROR_MESSAGE);
                    valid = false;
                } else {
                    setHint(empNumberHint, "✓ Employee Number looks good.", HINT_SUCCESS);
                }

            } catch (NumberFormatException ex) {
                // Non-numeric input caught here
                setHint(empNumberHint, "⚠ Employee Number must be numeric (no letters or symbols).", HINT_ERROR);
                valid = false;
            }
        }

        //  2. Validate Employee Name 
        String empName = empNameField.getText().trim();

        if (empName.isEmpty()) {
            setHint(empNameHint, "⚠ Employee Name is required.", HINT_ERROR);
            valid = false;
        } else if (!empName.matches("[a-zA-ZÀ-ÿ\\s.'-]+")) {
            // Reject names containing digits or special characters
            setHint(empNameHint, "⚠ Employee Name must contain letters and spaces only.", HINT_ERROR);
            valid = false;
        } else if (empName.length() < 2) {
            setHint(empNameHint, "⚠ Employee Name is too short.", HINT_ERROR);
            valid = false;
        } else {
            setHint(empNameHint, "✓ Name accepted.", HINT_SUCCESS);
        }

        //  3. Validate Pay Coverage 
        String payCoverageStr = payCoverageField.getText().trim();

        double payCoverage = 0;
        if (payCoverageStr.isEmpty()) {
            setHint(payCoverageHint, "⚠ Pay Coverage is required.", HINT_ERROR);
            valid = false;
        } else {
            try {
                payCoverage = Double.parseDouble(payCoverageStr.replace(",", ""));

                if (payCoverage <= 0) {
                    setHint(payCoverageHint, "⚠ Pay Coverage must be greater than zero.", HINT_ERROR);
                    valid = false;
                } else if (payCoverage > 9_999_999) {
                    setHint(payCoverageHint, "⚠ Pay Coverage value seems unreasonably large.", HINT_ERROR);
                    valid = false;
                } else {
                    setHint(payCoverageHint,
                            String.format("✓ ₱%,.2f accepted.", payCoverage),
                            HINT_SUCCESS);
                }

            } catch (NumberFormatException ex) {
                // Non-numeric pay coverage caught here
                setHint(payCoverageHint, "⚠ Pay Coverage must be a numeric value (e.g. 25000.00).", HINT_ERROR);
                valid = false;
            }
        }

        //  4. If any field failed, show a summary dialog and stop 
        if (!valid) {
            JOptionPane.showMessageDialog(
                    this,
                    "One or more fields contain invalid input.\n"
                            + "Please review the highlighted hints below each field and try again.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        //  5. All fields valid - add to in-memory employee list 
        int empNumber = Integer.parseInt(empNumberStr);

        MotorPHEmployeeApp.Employee newEmployee =
                new MotorPHEmployeeApp.Employee(
                        empNumberStr,
                        empName,
                        "N/A",          // Birthday not captured in this form
                        payCoverage);   // Pay Coverage stored as hourlyRate placeholder

        // Grow the global employees array by one slot
        MotorPHEmployeeApp.Employee[] updated =
                new MotorPHEmployeeApp.Employee[MotorPHEmployeeApp.employees.length + 1];
        System.arraycopy(MotorPHEmployeeApp.employees, 0, updated, 0,
                MotorPHEmployeeApp.employees.length);
        updated[updated.length - 1] = newEmployee;
        MotorPHEmployeeApp.employees = updated;

        //  6. Confirmation dialog 
        String confirmationMessage = String.format(
                "Employee record added successfully!%n%n"
                        + "  Employee Number : %d%n"
                        + "  Employee Name   : %s%n"
                        + "  Pay Coverage    : ₱%,.2f%n%n"
                        + "The record is now available in the payroll system.",
                empNumber, empName, payCoverage);

        JOptionPane.showMessageDialog(
                this,
                confirmationMessage,
                "Record Saved",
                JOptionPane.INFORMATION_MESSAGE);

        //  7. Reset the form for the next entry 
        handleClear();
    }

    //  handleClear: resets all fields and hints 

    private void handleClear() {
        empNumberField.setText("");
        empNameField.setText("");
        payCoverageField.setText("");

        setHint(empNumberHint,   "e.g. 10001  —  must be a positive whole number",   HINT_NEUTRAL);
        setHint(empNameHint,     "e.g. Juan dela Cruz  —  letters and spaces only",  HINT_NEUTRAL);
        setHint(payCoverageHint, "e.g. 25000.00  —  must be a positive numeric amount", HINT_NEUTRAL);

        empNumberField.requestFocusInWindow();
    }

    //  Helpers 

    /** Creates a bold form label. */
    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        return label;
    }

    /** Creates a small italic hint label shown below each input field. */
    private JLabel makeHint(String text) {
        JLabel hint = new JLabel(text);
        hint.setFont(new Font("Arial", Font.ITALIC, 10));
        hint.setForeground(HINT_NEUTRAL);
        return hint;
    }

    /** Updates a hint label's text and colour at runtime. */
    private void setHint(JLabel hint, String text, Color color) {
        hint.setText(text);
        hint.setForeground(color);
    }

    /**
     * Attaches a FocusListener to a text field so that when the user
     * clicks into the field its hint label reverts to the neutral placeholder text.
     */
    private void addFocusReset(JTextField field, JLabel hint, String neutralText) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // Only reset if the hint currently shows an error (red)
                if (HINT_ERROR.equals(hint.getForeground())) {
                    setHint(hint, neutralText, HINT_NEUTRAL);
                }
            }
        });
    }
}
