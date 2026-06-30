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
import javax.swing.JScrollPane;

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
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField birthdayField;
    private JTextField sssNumberField;
    private JTextField philHealthField;
    private JTextField tinField;
    private JTextField pagIbigField;
    private JTextField hourlyRateField;

    //  Inline error hint labels (one per field) 
    private JLabel empNumberHint;
    private JLabel lastNameHint;
    private JLabel firstNameHint;
    private JLabel birthdayHint;
    private JLabel sssNumberHint;
    private JLabel philHealthHint;
    private JLabel tinHint;
    private JLabel pagIbigHint;
    private JLabel hourlyRateHint;

    //  Action Buttons 
    private JButton submitButton;
    private JButton clearButton;
    private JButton closeButton;

    //  Colours used for hint labels 
    private static final Color HINT_ERROR   = new Color(180, 0, 0);
    private static final Color HINT_SUCCESS = new Color(0, 120, 0);
    private static final Color HINT_NEUTRAL = new Color(100, 100, 100);

    // Optional callback — invoked after each successful record save
    // Used by EmployeeRecordsFrame to refresh the JTable in real time
    private Runnable onRecordAdded;

    // No-arg constructor preserves Feature 1 call site in PayrollStaffFrame
    public EmployeeInputFrame() {
        this(null);
    }

    public EmployeeInputFrame(Runnable onRecordAdded) {
        this.onRecordAdded = onRecordAdded;
        setTitle("Add New Employee Record");
        setSize(520, 680);
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
                "All fields are required. Hourly Rate must be a positive number.",
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

        // -- Row 2 / Row 3 : Last Name --
        labelGbc.gridx = 0;
        labelGbc.gridy = 2;
        formPanel.add(makeLabel("Last Name:"), labelGbc);

        lastNameField = new JTextField(18);
        lastNameField.setToolTipText("Employee's last name (letters only)");
        fieldGbc.gridx = 1;
        fieldGbc.gridy = 2;
        formPanel.add(lastNameField, fieldGbc);

        lastNameHint = makeHint("e.g. dela Cruz  —  letters and spaces only");
        hintGbc.gridx = 1;
        hintGbc.gridy = 3;
        formPanel.add(lastNameHint, hintGbc);

        // -- Row 4 / Row 5 : First Name --
        labelGbc.gridx = 0;
        labelGbc.gridy = 4;
        formPanel.add(makeLabel("First Name:"), labelGbc);

        firstNameField = new JTextField(18);
        firstNameField.setToolTipText("Employee's first name (letters only)");
        fieldGbc.gridx = 1;
        fieldGbc.gridy = 4;
        formPanel.add(firstNameField, fieldGbc);

        firstNameHint = makeHint("e.g. Juan  —  letters and spaces only");
        hintGbc.gridx = 1;
        hintGbc.gridy = 5;
        formPanel.add(firstNameHint, hintGbc);

        // -- Row 6 / Row 7 : Birthday --
        labelGbc.gridx = 0;
        labelGbc.gridy = 6;
        formPanel.add(makeLabel("Birthday:"), labelGbc);

        birthdayField = new JTextField(18);
        birthdayField.setToolTipText("Date of birth in MM/DD/YYYY format (e.g. 06/19/1988)");
        fieldGbc.gridx = 1;
        fieldGbc.gridy = 6;
        formPanel.add(birthdayField, fieldGbc);

        birthdayHint = makeHint("e.g. 06/19/1988  —  MM/DD/YYYY format");
        hintGbc.gridx = 1;
        hintGbc.gridy = 7;
        formPanel.add(birthdayHint, hintGbc);

        // -- Row 8 / Row 9 : SSS Number --
        labelGbc.gridx = 0;
        labelGbc.gridy = 8;
        formPanel.add(makeLabel("SSS Number:"), labelGbc);

        sssNumberField = new JTextField(18);
        sssNumberField.setToolTipText("SSS membership number (e.g. 33-1234567-8)");
        fieldGbc.gridx = 1;
        fieldGbc.gridy = 8;
        formPanel.add(sssNumberField, fieldGbc);

        sssNumberHint = makeHint("e.g. 44-4506057-3  —  format: ##-#######-#");
        hintGbc.gridx = 1;
        hintGbc.gridy = 9;
        formPanel.add(sssNumberHint, hintGbc);

        // -- Row 10 / Row 11 : PhilHealth Number --
        labelGbc.gridx = 0;
        labelGbc.gridy = 10;
        formPanel.add(makeLabel("PhilHealth Number:"), labelGbc);

        philHealthField = new JTextField(18);
        philHealthField.setToolTipText("PhilHealth identification number (e.g. 123456789012)");
        fieldGbc.gridx = 1;
        fieldGbc.gridy = 10;
        formPanel.add(philHealthField, fieldGbc);

        philHealthHint = makeHint("e.g. 820126853951  —  12 digits");
        hintGbc.gridx = 1;
        hintGbc.gridy = 11;
        formPanel.add(philHealthHint, hintGbc);

        // -- Row 12 / Row 13 : TIN --
        labelGbc.gridx = 0;
        labelGbc.gridy = 12;
        formPanel.add(makeLabel("TIN:"), labelGbc);

        tinField = new JTextField(18);
        tinField.setToolTipText("Tax Identification Number (e.g. 123-456-789-000)");
        fieldGbc.gridx = 1;
        fieldGbc.gridy = 12;
        formPanel.add(tinField, fieldGbc);

        tinHint = makeHint("e.g. 442-605-657-000  —  format: ###-###-###-###");
        hintGbc.gridx = 1;
        hintGbc.gridy = 13;
        formPanel.add(tinHint, hintGbc);

        // -- Row 14 / Row 15 : Pag-IBIG Number --
        labelGbc.gridx = 0;
        labelGbc.gridy = 14;
        formPanel.add(makeLabel("Pag-IBIG Number:"), labelGbc);

        pagIbigField = new JTextField(18);
        pagIbigField.setToolTipText("Pag-IBIG (HDMF) membership number (e.g. 121212121212)");
        fieldGbc.gridx = 1;
        fieldGbc.gridy = 14;
        formPanel.add(pagIbigField, fieldGbc);

        pagIbigHint = makeHint("e.g. 691295330870  —  12 digits");
        hintGbc.gridx = 1;
        hintGbc.gridy = 15;
        formPanel.add(pagIbigHint, hintGbc);

        // -- Row 16 / Row 17 : Hourly Rate (renamed from Pay Coverage) --
        labelGbc.gridx = 0;
        labelGbc.gridy = 16;
        formPanel.add(makeLabel("Hourly Rate (₱):"), labelGbc);

        hourlyRateField = new JTextField(18);
        hourlyRateField.setToolTipText("Hourly wage in Philippine Peso (e.g. 133.93)");
        fieldGbc.gridx = 1;
        fieldGbc.gridy = 16;
        formPanel.add(hourlyRateField, fieldGbc);

        hourlyRateHint = makeHint("e.g. 133.93  —  must be a positive numeric amount");
        hintGbc.gridx = 1;
        hintGbc.gridy = 17;
        formPanel.add(hourlyRateHint, hintGbc);

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
        
        //  Wrap form in a scroll pane to handle variable screen heights gracefully
        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        formScroll.getVerticalScrollBar().setUnitIncrement(16);

        //  Assemble frame 
        add(headerPanel,  BorderLayout.NORTH);
        add(formScroll, BorderLayout.CENTER);
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
        addFocusReset(empNumberField, empNumberHint, "e.g. 10001  —  must be a positive whole number");
        addFocusReset(lastNameField, lastNameHint, "e.g. dela Cruz  —  letters and spaces only");
        addFocusReset(firstNameField, firstNameHint, "e.g. Juan  —  letters and spaces only");
        addFocusReset(birthdayField, birthdayHint, "e.g. 06/19/1988  —  MM/DD/YYYY format");
        addFocusReset(sssNumberField, sssNumberHint, "e.g. 44-4506057-3  —  format: ##-#######-#");
        addFocusReset(philHealthField, philHealthHint, "e.g. 820126853951  —  12 digits");
        addFocusReset(tinField, tinHint, "e.g. 442-605-657-000  —  format: ###-###-###-###");
        addFocusReset(pagIbigField, pagIbigHint, "e.g. 691295330870  —  12 digits");
        addFocusReset(hourlyRateField, hourlyRateHint, "e.g. 133.93  —  must be a positive numeric amount");
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

        //  2. Validate Last Name
        String lastName = lastNameField.getText().trim();

        if (lastName.isEmpty()) {
            setHint(lastNameHint, "⚠ Last Name is required.", HINT_ERROR);
            valid = false;
        } else if (!lastName.matches("[a-zA-ZÀ-ÿ\\s.'-]+")) {
            setHint(lastNameHint, "⚠ Last Name must contain letters and spaces only.", HINT_ERROR);
            valid = false;
        } else if (lastName.length() < 2) {
            setHint(lastNameHint, "⚠ Last Name is too short.", HINT_ERROR);
            valid = false;
        } else {
            setHint(lastNameHint, "✓ Last Name accepted.", HINT_SUCCESS);
        }

        //  3. Validate First Name
        String firstName = firstNameField.getText().trim();

        if (firstName.isEmpty()) {
            setHint(firstNameHint, "⚠ First Name is required.", HINT_ERROR);
            valid = false;
        } else if (!firstName.matches("[a-zA-ZÀ-ÿ\\s.'-]+")) {
            setHint(firstNameHint, "⚠ First Name must contain letters and spaces only.", HINT_ERROR);
            valid = false;
        } else if (firstName.length() < 2) {
            setHint(firstNameHint, "⚠ First Name is too short.", HINT_ERROR);
            valid = false;
        } else {
            setHint(firstNameHint, "✓ First Name accepted.", HINT_SUCCESS);
        }

        //  4. Validate Birthday
        String birthday = birthdayField.getText().trim();

        if (birthday.isEmpty()) {
            setHint(birthdayHint, "⚠ Birthday is required.", HINT_ERROR);
            valid = false;
        } else if (!birthday.matches("\\d{2}/\\d{2}/\\d{4}")) {
            setHint(birthdayHint, "⚠ Birthday must be in MM/DD/YYYY format (e.g. 06/19/1988).", HINT_ERROR);
            valid = false;
        } else {
            setHint(birthdayHint, "✓ Birthday accepted.", HINT_SUCCESS);
        }

        //  5. Validate SSS Number
        String sssNumber = sssNumberField.getText().trim();

        if (sssNumber.isEmpty()) {
            setHint(sssNumberHint, "⚠ SSS Number is required.", HINT_ERROR);
            valid = false;
        } else if (!sssNumber.matches("\\d{2}-\\d{7}-\\d")) {
            setHint(sssNumberHint, "⚠ Invalid format. Expected: 44-4506057-3", HINT_ERROR);
            valid = false;
        } else if (isSssDuplicate(sssNumber, null)) {
            setHint(sssNumberHint, "⚠ SSS Number already exists in the system.", HINT_ERROR);
            valid = false;
        } else {
            setHint(sssNumberHint, "✓ SSS Number accepted.", HINT_SUCCESS);
        }

        //  6. Validate PhilHealth Number
        String philHealthNumber = philHealthField.getText().trim();

        if (philHealthNumber.isEmpty()) {
            setHint(philHealthHint, "⚠ PhilHealth Number is required.", HINT_ERROR);
            valid = false;
        } else if (!philHealthNumber.matches("\\d{12}")) {
            setHint(philHealthHint, "⚠ Invalid format. Expected: 820126853951 (12 digits)", HINT_ERROR);
            valid = false;
        } else if (isPhilHealthDuplicate(philHealthNumber, null)) {
            setHint(philHealthHint, "⚠ PhilHealth Number already exists in the system.", HINT_ERROR);
            valid = false;
        } else {
            setHint(philHealthHint, "✓ PhilHealth Number accepted.", HINT_SUCCESS);
        }

        //  7. Validate TIN
        String tin = tinField.getText().trim();

        if (tin.isEmpty()) {
            setHint(tinHint, "⚠ TIN is required.", HINT_ERROR);
            valid = false;
        } else if (!tin.matches("\\d{3}-\\d{3}-\\d{3}-\\d{3}")) {
            setHint(tinHint, "⚠ Invalid format. Expected: 442-605-657-000", HINT_ERROR);
            valid = false;
        } else if (isTinDuplicate(tin, null)) {
            setHint(tinHint, "⚠ TIN already exists in the system.", HINT_ERROR);
            valid = false;
        } else {
            setHint(tinHint, "✓ TIN accepted.", HINT_SUCCESS);
        }

        //  8. Validate Pag-IBIG Number
        String pagIbigNumber = pagIbigField.getText().trim();

        if (pagIbigNumber.isEmpty()) {
            setHint(pagIbigHint, "⚠ Pag-IBIG Number is required.", HINT_ERROR);
            valid = false;
        } else if (!pagIbigNumber.matches("\\d{12}")) {
            setHint(pagIbigHint, "⚠ Invalid format. Expected: 691295330870 (12 digits)", HINT_ERROR);
            valid = false;
        } else if (isPagIbigDuplicate(pagIbigNumber, null)) {
            setHint(pagIbigHint, "⚠ Pag-IBIG Number already exists in the system.", HINT_ERROR);
            valid = false;
        } else {
            setHint(pagIbigHint, "✓ Pag-IBIG Number accepted.", HINT_SUCCESS);
        }

        //  9. Validate Hourly Rate
        String hourlyRateStr = hourlyRateField.getText().trim();
        double hourlyRate = 0;

        if (hourlyRateStr.isEmpty()) {
            setHint(hourlyRateHint, "⚠ Hourly Rate is required.", HINT_ERROR);
            valid = false;
        } else {
            try {
                hourlyRate = Double.parseDouble(hourlyRateStr.replace(",", ""));

                if (hourlyRate <= 0) {
                    setHint(hourlyRateHint, "⚠ Hourly Rate must be greater than zero.", HINT_ERROR);
                    valid = false;
                } else if (hourlyRate > 9_999_999) {
                    setHint(hourlyRateHint, "⚠ Hourly Rate value seems unreasonably large.", HINT_ERROR);
                    valid = false;
                } else {
                    setHint(hourlyRateHint,
                            String.format("✓ ₱%,.2f accepted.", hourlyRate),
                            HINT_SUCCESS);
                }

            } catch (NumberFormatException ex) {
                // Non-numeric hourly rate caught here
                setHint(hourlyRateHint, "⚠ Hourly Rate must be a numeric value (e.g. 133.93).", HINT_ERROR);
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

        MotorPHEmployeeApp.Employee newEmployee
                = new MotorPHEmployeeApp.Employee(
                        empNumberStr,
                        lastName,
                        firstName,
                        birthday,
                        hourlyRate,
                        sssNumber,
                        philHealthNumber,
                        tin,
                        pagIbigNumber);

        // Grow the global employees array by one slot
        MotorPHEmployeeApp.Employee[] updated =
                new MotorPHEmployeeApp.Employee[MotorPHEmployeeApp.employees.length + 1];
        System.arraycopy(MotorPHEmployeeApp.employees, 0, updated, 0,
                MotorPHEmployeeApp.employees.length);
        updated[updated.length - 1] = newEmployee;
        MotorPHEmployeeApp.employees = updated;

        // Append the new record to the CSV file for persistence
        MotorPHEmployeeApp.writeEmployeeToCSV(newEmployee);

        //  Confirmation dialog
        String confirmationMessage = String.format(
                "Employee record added successfully!%n%n"
                + "  Employee Number : %d%n"
                + "  Name            : %s %s%n"
                + "  Hourly Rate     : ₱%,.2f%n%n"
                + "The record is now available in the payroll system.",
                empNumber, firstName, lastName, hourlyRate);

        JOptionPane.showMessageDialog(
                this,
                confirmationMessage,
                "Record Saved",
                JOptionPane.INFORMATION_MESSAGE);

        //  7. Reset the form for the next entry 
        handleClear(); //  handleClear: resets all fields and hints
    
        if (onRecordAdded != null) {
            onRecordAdded.run();
        }
    }

    private void handleClear() {
        empNumberField.setText("");
        lastNameField.setText("");
        firstNameField.setText("");
        birthdayField.setText("");
        sssNumberField.setText("");
        philHealthField.setText("");
        tinField.setText("");
        pagIbigField.setText("");
        hourlyRateField.setText("");

        setHint(empNumberHint,   "e.g. 10001  —  must be a positive whole number",   HINT_NEUTRAL);
        setHint(lastNameHint, "e.g. dela Cruz  —  letters and spaces only", HINT_NEUTRAL);
        setHint(firstNameHint, "e.g. Juan  —  letters and spaces only", HINT_NEUTRAL);
        setHint(birthdayHint, "e.g. 06/19/1988  —  MM/DD/YYYY format", HINT_NEUTRAL);
        setHint(sssNumberHint, "e.g. 44-4506057-3  —  format: ##-#######-#", HINT_NEUTRAL);
        setHint(philHealthHint, "e.g. 820126853951  —  12 digits", HINT_NEUTRAL);
        setHint(tinHint, "e.g. 442-605-657-000  —  format: ###-###-###-###", HINT_NEUTRAL);
        setHint(pagIbigHint, "e.g. 691295330870  —  12 digits", HINT_NEUTRAL);
        setHint(hourlyRateHint, "e.g. 133.93  —  must be a positive numeric amount", HINT_NEUTRAL);

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
    
    // Returns true if any employee OTHER than excludeEmp already has this SSS number
    private boolean isSssDuplicate(String sssNumber, MotorPHEmployeeApp.Employee excludeEmp) {
        if (MotorPHEmployeeApp.employees == null) return false;
        for (MotorPHEmployeeApp.Employee emp : MotorPHEmployeeApp.employees) {
            if (emp == excludeEmp) continue;
            if (sssNumber.equals(emp.sssNumber)) return true;
        }
        return false;
    }

    // Returns true if any employee OTHER than excludeEmp already has this PhilHealth number
    private boolean isPhilHealthDuplicate(String philHealthNumber, MotorPHEmployeeApp.Employee excludeEmp) {
        if (MotorPHEmployeeApp.employees == null) return false;
        for (MotorPHEmployeeApp.Employee emp : MotorPHEmployeeApp.employees) {
            if (emp == excludeEmp) continue;
            if (philHealthNumber.equals(emp.philHealthNumber)) return true;
        }
        return false;
    }

    // Returns true if any employee OTHER than excludeEmp already has this TIN
    private boolean isTinDuplicate(String tin, MotorPHEmployeeApp.Employee excludeEmp) {
        if (MotorPHEmployeeApp.employees == null) return false;
        for (MotorPHEmployeeApp.Employee emp : MotorPHEmployeeApp.employees) {
            if (emp == excludeEmp) continue;
            if (tin.equals(emp.tin)) return true;
        }
        return false;
    }

    // Returns true if any employee OTHER than excludeEmp already has this Pag-IBIG number
    private boolean isPagIbigDuplicate(String pagIbigNumber, MotorPHEmployeeApp.Employee excludeEmp) {
        if (MotorPHEmployeeApp.employees == null) return false;
        for (MotorPHEmployeeApp.Employee emp : MotorPHEmployeeApp.employees) {
            if (emp == excludeEmp) continue;
            if (pagIbigNumber.equals(emp.pagIbigNumber)) return true;
        }
        return false;
    }
}
