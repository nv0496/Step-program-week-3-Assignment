// Assignment8_YourName.java

import java.util.*;

/**
 * Main class to run the Hospital Management System
 */
public class Assignment8_YourName {
    public static void main(String[] args) {
        // Set hospital name
        Appointment.setHospitalName("CityCare Hospital");

        // Create doctors
        Doctor d1 = new Doctor("D101", "Dr. Mehta", "Cardiologist", 
                new String[]{"10:00AM", "11:00AM", "2:00PM"}, 1000);
        Doctor d2 = new Doctor("D102", "Dr. Roy", "Neurologist", 
                new String[]{"9:30AM", "1:30PM"}, 1200);

        // Create patients
        Patient p1 = new Patient("P201", "Ramesh Kumar", 45, "Male", "9876543210");
        Patient p2 = new Patient("P202", "Anita Sharma", 30, "Female", "9123456780");

        // Schedule Appointments
        Appointment a1 = new Appointment("A301", p1, d1, "2025-09-01", "10:00AM", "Consultation");
        Appointment a2 = new Appointment("A302", p2, d2, "2025-09-01", "1:30PM", "Emergency");

        a1.scheduleAppointment();
        a2.scheduleAppointment();

        // Treatments
        p1.updateTreatment("Blood Pressure Monitoring");
        p2.updateTreatment("MRI Scan");

        // Generate Bills
        a1.generateBill();
        a2.generateBill();

        // Hospital Reports
        Appointment.generateHospitalReport();
        Appointment.getDoctorUtilization(new Doctor[]{d1, d2});
        Appointment.getPatientStatistics(new Patient[]{p1, p2});

        // Discharge
        p1.dischargePatient();
        p2.dischargePatient();
    }
}

/**
 * Patient class
 */
class Patient {
    private String patientId;
    private String patientName;
    private int age;
    private String gender;
    private String contactInfo;
    private List<String> medicalHistory;
    private List<String> currentTreatments;

    private static int totalPatients = 0;

    public Patient(String patientId, String patientName, int age, String gender, String contactInfo) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.age = age;
        this.gender = gender;
        this.contactInfo = contactInfo;
        this.medicalHistory = new ArrayList<>();
        this.currentTreatments = new ArrayList<>();
        totalPatients++;
    }

    public void updateTreatment(String treatment) {
        currentTreatments.add(treatment);
        System.out.println("Treatment '" + treatment + "' added for patient: " + patientName);
    }

    public void dischargePatient() {
        System.out.println("Patient " + patientName + " has been discharged.");
        medicalHistory.addAll(currentTreatments);
        currentTreatments.clear();
    }

    public static int getTotalPatients() {
        return totalPatients;
    }

    public String getPatientName() {
        return patientName;
    }
}

/**
 * Doctor class
 */
class Doctor {
    private String doctorId;
    private String doctorName;
    private String specialization;
    private String[] availableSlots;
    private int patientsHandled;
    private double consultationFee;

    public Doctor(String doctorId, String doctorName, String specialization, String[] availableSlots, double consultationFee) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.availableSlots = availableSlots;
        this.consultationFee = consultationFee;
        this.patientsHandled = 0;
    }

    public void incrementPatientsHandled() {
        patientsHandled++;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public int getPatientsHandled() {
        return patientsHandled;
    }
}

/**
 * Appointment class
 */
class Appointment {
    private String appointmentId;
    private Patient patient;
    private Doctor doctor;
    private String appointmentDate;
    private String appointmentTime;
    private String type;  // Consultation, Follow-up, Emergency
    private String status;

    private static int totalAppointments = 0;
    private static String hospitalName;
    private static double totalRevenue = 0;

    public Appointment(String appointmentId, Patient patient, Doctor doctor, String appointmentDate, String appointmentTime, String type) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.type = type;
        this.status = "Scheduled";
        totalAppointments++;
    }

    public void scheduleAppointment() {
        System.out.println("Appointment " + appointmentId + " scheduled for " + patient.getPatientName() +
                " with " + doctor.getDoctorName() + " at " + appointmentTime + " on " + appointmentDate);
        doctor.incrementPatientsHandled();
    }

    public void cancelAppointment() {
        status = "Cancelled";
        System.out.println("Appointment " + appointmentId + " cancelled.");
    }

    public void generateBill() {
        double billAmount = doctor.getConsultationFee();
        if (type.equalsIgnoreCase("Emergency")) {
            billAmount *= 1.5;
        } else if (type.equalsIgnoreCase("Follow-up")) {
            billAmount *= 0.7;
        }
        totalRevenue += billAmount;
        System.out.println("Bill for Appointment " + appointmentId + ": ₹" + billAmount);
    }

    // Static Methods
    public static void setHospitalName(String name) {
        hospitalName = name;
    }

    public static void generateHospitalReport() {
        System.out.println("\n---- Hospital Report ----");
        System.out.println("Hospital: " + hospitalName);
        System.out.println("Total Patients: " + Patient.getTotalPatients());
        System.out.println("Total Appointments: " + totalAppointments);
        System.out.println("Total Revenue: ₹" + totalRevenue);
    }

    public static void getDoctorUtilization(Doctor[] doctors) {
        System.out.println("\n---- Doctor Utilization ----");
        for (Doctor d : doctors) {
            System.out.println(d.getDoctorName() + " handled " + d.getPatientsHandled() + " patients.");
        }
    }

    public static void getPatientStatistics(Patient[] patients) {
        System.out.println("\n---- Patient Statistics ----");
        System.out.println("Total Registered Patients: " + patients.length);
    }
}
