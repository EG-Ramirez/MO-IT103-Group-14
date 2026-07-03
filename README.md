# MotorPH Employee App (Computer Programming 2 - MOIT103 Group 14)

A Java Swing desktop application that manages employee records and 
automates payroll computation for MotorPH employees using attendance 
records and government-mandated deductions.

---

## Features

- User login authentication for **Payroll Staff** and **Employee** accounts
- Employee information lookup (employee portal)
- Add, update, and delete employee records
- View employee records in a sortable and interactive table
- Generate payroll for a single employee
- Generate payroll for all employees (batch processing)
- Generate payroll summary reports
- Load employee and attendance data from CSV files
- Save computed payroll results to CSV file
- Payroll computation includes:
  - Gross Pay
  - SSS Contribution (2024 table)
  - PhilHealth Contribution
  - Pag-IBIG Contribution
  - Withholding Tax
  - Net Pay

---

## Default Login Credentials

| User Type      | Username         | Password |
|----------------|------------------|----------|
| Payroll Staff  | `payroll_staff`  | `12345`  |
| Employee       | `employee`       | `12345`  |

---

## Required Files

Make sure the following CSV files are placed in the project root directory:

- `mph_employees_record.csv`
- `attendance_record.csv`

---

## How to Run

1. Open the project in **Apache NetBeans IDE**
2. Ensure all required CSV files are placed in the project root folder
3. Build and run the project (Press **F6**)
4. Log in using the credentials above

---

## Tech Stack

- Java Development Kit (JDK 25)
- Java Swing (GUI)
- Apache NetBeans IDE

---

## Project Structure

The system is divided into the following modules:

- **GUI Layer** – All JFrame windows and user interfaces
- **Employee Management** – Add, update, delete, and view employee records
- **Payroll Processing** – Payroll computation and report generation
- **File Management** – CSV reading and writing operations
- **Salary Computation Module** – Handles all government deductions and payroll calculations

---

## Development Team
**Group 14**
Carreon, Rey Lorenz<br>
Gadil, Ysabelle<br>
Layson, John Mervin<br>
Montejo, Kera Froilan<br>
Ramirez, Enzo Gabriel<br>
