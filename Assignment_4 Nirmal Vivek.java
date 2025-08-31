/**
 * Assignment 4: Student Grade Management System
 * Demonstrates static vs instance members and data processing.
 * 
 * Features:
 * - Student and Subject classes
 * - GPA calculation
 * - Report card generation
 * - School-wide statistics (top performers, averages, etc.)
 * 
 * Author: StudentName
 */

import java.util.*;

/**
 * Subject class to represent course information.
 */
class Subject {
    private String subjectCode;
    private String subjectName;
    private int credits;
    private String instructor;

    public Subject(String subjectCode, String subjectName, int credits, String instructor) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.credits = credits;
        this.instructor = instructor;
    }

    public String getSubjectCode() { return subjectCode; }
    public String getSubjectName() { return subjectName; }
    public int getCredits() { return credits; }
    public String getInstructor() { return instructor; }

    @Override
    public String toString() {
        return subjectCode + " - " + subjectName + " (Credits: " + credits + ", Instructor: " + instructor + ")";
    }
}

/**
 * Student class representing student records and grades.
 */
class Student {
    private String studentId;
    private String studentName;
    private String className;
    private String[] subjects;
    private double[][] marks; // rows -> subjects, cols -> exams/assessments
    private double gpa;

    // Static variables
    private static int totalStudents = 0;
    private static String schoolName = "Default School";
    private static String[] gradingScale = {"A", "B", "C", "D", "F"};
    private static double passPercentage = 40.0;

    public Student(String studentName, String className, String[] subjects) {
        this.studentId = "STU" + String.format("%03d", ++totalStudents);
        this.studentName = studentName;
        this.className = className;
        this.subjects = subjects;
        this.marks = new double[subjects.length][5]; // up to 5 assessments per subject
        this.gpa = 0.0;
    }

    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getClassName() { return className; }
    public double getGpa() { return gpa; }

    /**
     * Add marks for a specific subject.
     */
    public void addMarks(String subject, double score) {
        for (int i = 0; i < subjects.length; i++) {
            if (subjects[i].equals(subject)) {
                for (int j = 0; j < marks[i].length; j++) {
                    if (marks[i][j] == 0) {
                        marks[i][j] = score;
                        return;
                    }
                }
            }
        }
    }

    /**
     * Calculate GPA based on average marks.
     */
    public void calculateGPA() {
        double total = 0;
        int count = 0;

        for (int i = 0; i < subjects.length; i++) {
            double subjectTotal = 0;
            int exams = 0;
            for (int j = 0; j < marks[i].length; j++) {
                if (marks[i][j] > 0) {
                    subjectTotal += marks[i][j];
                    exams++;
                }
            }
            if (exams > 0) {
                total += (subjectTotal / exams);
                count++;
            }
        }
        this.gpa = (count > 0) ? (total / count) / 20 : 0; // GPA on 0-5 scale
    }

    /**
     * Generate a report card for the student.
     */
    public void generateReportCard() {
        System.out.println("\n--- Report Card ---");
        System.out.println("School: " + schoolName);
        System.out.println("Student: " + studentName + " (" + studentId + ")");
        System.out.println("Class: " + className);
        for (int i = 0; i < subjects.length; i++) {
            double avg = Arrays.stream(marks[i]).filter(x -> x > 0).average().orElse(0);
            String grade = getGrade(avg);
            System.out.println("Subject: " + subjects[i] + " | Avg Marks: " + avg + " | Grade: " + grade);
        }
        System.out.println("Final GPA: " + gpa);
        System.out.println("Promotion Status: " + (checkPromotionEligibility() ? "Promoted" : "Not Promoted"));
        System.out.println("-------------------\n");
    }

    /**
     * Check promotion eligibility based on pass percentage.
     */
    public boolean checkPromotionEligibility() {
        for (int i = 0; i < subjects.length; i++) {
            double avg = Arrays.stream(marks[i]).filter(x -> x > 0).average().orElse(0);
            if (avg < passPercentage) {
                return false;
            }
        }
        return true;
    }

    private String getGrade(double percentage) {
        if (percentage >= 90) return "A";
        else if (percentage >= 75) return "B";
        else if (percentage >= 60) return "C";
        else if (percentage >= 40) return "D";
        else return "F";
    }

    // ---------- Static Methods ----------

    public static void setGradingScale(String[] scale) { gradingScale = scale; }

    public static double calculateClassAverage(Student[] students) {
        return Arrays.stream(students)
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0);
    }

    public static Student[] getTopPerformers(Student[] students, int count) {
        return Arrays.stream(students)
                .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
                .limit(count)
                .toArray(Student[]::new);
    }

    public static void generateSchoolReport(Student[] students) {
        System.out.println("\n--- School Report (" + schoolName + ") ---");
        System.out.println("Total Students: " + totalStudents);
        System.out.println("Average GPA: " + calculateClassAverage(students));
        Student[] toppers = getTopPerformers(students, 3);
        System.out.println("Top Performers:");
        for (Student s : toppers) {
            System.out.println(s.getStudentName() + " - GPA: " + s.getGpa());
        }
        System.out.println("------------------------------\n");
    }

    public static void setSchoolName(String name) { schoolName = name; }
}

/**
 * Main driver class
 */
public class Assignment4_StudentName {
    public static void main(String[] args) {
        Student.setSchoolName("Green Valley High School");

        String[] subjects = {"Math", "Science", "English"};

        Student s1 = new Student("Alice", "10A", subjects);
        Student s2 = new Student("Bob", "10A", subjects);
        Student s3 = new Student("Charlie", "10A", subjects);

        // Adding marks
        s1.addMarks("Math", 95);
        s1.addMarks("Science", 88);
        s1.addMarks("English", 92);

        s2.addMarks("Math", 70);
        s2.addMarks("Science", 65);
        s2.addMarks("English", 72);

        s3.addMarks("Math", 55);
        s3.addMarks("Science", 45);
        s3.addMarks("English", 60);

        // Calculate GPA
        s1.calculateGPA();
        s2.calculateGPA();
        s3.calculateGPA();

        // Generate report cards
        s1.generateReportCard();
        s2.generateReportCard();
        s3.generateReportCard();

        // School report
        Student[] allStudents = {s1, s2, s3};
        Student.generateSchoolReport(allStudents);
    }
}
