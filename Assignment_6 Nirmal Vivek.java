/**
 * Assignment 6: Employee Payroll and Attendance System
 * Demonstrates Payroll, Attendance, Bonus Calculation, and Department-wise reporting
 */

import java.util.*;

/**
 * Employee class representing different types of employees
 */
class Employee {
    private String empId;
    private String empName;
    private String department;
    private String designation;
    private double baseSalary;
    private String joinDate;
    private boolean[] attendanceRecord; // 30 days attendance

    // Static variables
    public static int totalEmployees = 0;
    public static String companyName = "TechCorp Pvt Ltd";
    public static double totalSalaryExpense = 0;
    public static int workingDaysPerMonth = 30;

    // Constructor
    public Employee(String empId, String empName, String department, String designation,
                    double baseSalary, String joinDate) {
        this.empId = empId;
        this.empName = empName;
        this.department = department;
        this.designation = designation;
        this.baseSalary = baseSalary;
        this.joinDate = joinDate;
        this.attendanceRecord = new boolean[workingDaysPerMonth];
        totalEmployees++;
    }

    // Mark attendance
    public void markAttendance(int day, boolean present) {
        if (day >= 1 && day <= workingDaysPerMonth) {
            attendanceRecord[day - 1] = present;
        } else {
            System.out.println("Invalid day entered!");
        }
    }

    // Calculate monthly salary
    public double calculateSalary() {
        int presentDays = 0;
        for (boolean day : attendanceRecord) {
            if (day) presentDays++;
        }
        double salary = (baseSalary / workingDaysPerMonth) * presentDays;
        totalSalaryExpense += salary;
        return salary;
    }

    // Performance-based bonus
    public double calculateBonus() {
        int presentDays = 0;
        for (boolean day : attendanceRecord) {
            if (day) presentDays++;
        }
        double attendanceRate = (presentDays * 100.0) / workingDaysPerMonth;
        if (attendanceRate >= 95) {
            return baseSalary * 0.20; // 20% bonus
        } else if (attendanceRate >= 85) {
            return baseSalary * 0.10; // 10% bonus
        }
        return 0.0;
    }

    // Generate payslip
    public void generatePaySlip() {
        double salary = calculateSalary();
        double bonus = calculateBonus();
        System.out.println("----- Pay Slip for " + empName + " -----");
        System.out.println("Employee ID: " + empId);
        System.out.println("Designation: " + designation);
        System.out.println("Base Salary: " + baseSalary);
        System.out.println("Net Salary (with attendance): " + salary);
        System.out.println("Bonus: " + bonus);
        System.out.println("Total Payable: " + (salary + bonus));
        System.out.println("--------------------------------------");
    }

    // Leave request
    public void requestLeave(int days) {
        if (days <= 5) {
            System.out.println(empName + " has been granted " + days + " days leave.");
        } else {
            System.out.println(empName + " leave request denied (exceeds max allowed).");
        }
    }

    public String getDepartment() {
        return department;
    }

    public String getEmpName() {
        return empName;
    }
}

/**
 * Department class to manage employees and budget
 */
class Department {
    private String deptId;
    private String deptName;
    private Employee manager;
    private Employee[] employees;
    private double budget;

    public Department(String deptId, String deptName, Employee manager, Employee[] employees, double budget) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.manager = manager;
        this.employees = employees;
        this.budget = budget;
    }

    // Department-wise expenses
    public double calculateDepartmentExpense() {
        double expense = 0;
        for (Employee e : employees) {
            if (e != null) {
                expense += e.calculateSalary() + e.calculateBonus();
            }
        }
        return expense;
    }

    public String getDeptName() {
        return deptName;
    }
}

/**
 * Main system class
 */
public class Assignment6_YourName {

    // Static method: calculate total payroll
    public static double calculateCompanyPayroll(Employee[] employees) {
        double total = 0;
        for (Employee e : employees) {
            total += e.calculateSalary() + e.calculateBonus();
        }
        return total;
    }

    // Static method: department-wise expenses
    public static void getDepartmentWiseExpenses(Department[] departments) {
        System.out.println("----- Department-wise Expenses -----");
        for (Department d : departments) {
            System.out.println(d.getDeptName() + " Expense: " + d.calculateDepartmentExpense());
        }
    }

    // Static method: attendance report
    public static void getAttendanceReport(Employee[] employees) {
        System.out.println("----- Attendance Report -----");
        for (Employee e : employees) {
            System.out.println(e.getEmpName() + " - Salary based on attendance: " + e.calculateSalary());
        }
    }

    public static void main(String[] args) {
        // Create employees
        Employee e1 = new Employee("E101", "Alice", "IT", "Developer", 50000, "2023-01-01");
        Employee e2 = new Employee("E102", "Bob", "HR", "HR Manager", 60000, "2022-05-15");
        Employee e3 = new Employee("E103", "Charlie", "Finance", "Accountant", 55000, "2021-07-20");

        // Mark attendance
        for (int i = 1; i <= 28; i++) e1.markAttendance(i, true);
        for (int i = 1; i <= 25; i++) e2.markAttendance(i, true);
        for (int i = 1; i <= 20; i++) e3.markAttendance(i, true);

        // Generate payslips
        e1.generatePaySlip();
        e2.generatePaySlip();
        e3.generatePaySlip();

        // Request leave
        e1.requestLeave(3);
        e2.requestLeave(6);

        // Create departments
        Department d1 = new Department("D01", "IT", e1, new Employee[]{e1}, 1000000);
        Department d2 = new Department("D02", "HR", e2, new Employee[]{e2}, 500000);
        Department d3 = new Department("D03", "Finance", e3, new Employee[]{e3}, 700000);

        // Company payroll and reports
        Employee[] employees = {e1, e2, e3};
        Department[] departments = {d1, d2, d3};

        System.out.println("Total Company Payroll: " + calculateCompanyPayroll(employees));
        getDepartmentWiseExpenses(departments);
        getAttendanceReport(employees);
    }
}
