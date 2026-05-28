package com.telemedicine.models;

import java.io.Serializable;
import java.util.ArrayList;

/*
 Represents an admin in the telemedicine system.
 Extends Person (inheritance) and manages doctors, patients, and system reporting.
 Admin level is restricted to "admin" or "super_admin".
*/
public class Admin extends Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String adminLevel; // "admin" or "super_admin"

    /*
     Constructs an Admin by delegating to Person's constructor,
     then setting the admin-specific level field.
    */
    public Admin(
            String userId,
            String name,
            String email,
            String phoneNumber,
            String password,
            String adminLevel) {
        super(userId, name, email, phoneNumber, password);
        setAdminLevel(adminLevel);
    }

    /*
     Displays the admin's profile in a formatted box.
     Overrides displayProfile() from Person — polymorphism.
    */
    @Override
    public void displayProfile() {
        int W = 38;
        System.out.println("\n╔" + "═".repeat(W) + "╗");
        System.out.println("║" + center("ADMIN PROFILE", W) + "║");
        System.out.println("╠" + "═".repeat(W) + "╣");
        printRow("Admin ID",   userId,                      W);
        printRow("Name",       name,                        W);
        printRow("Level",      adminLevel,                  W);
        printRow("Email",      email,                       W);
        printRow("Phone",      phoneNumber,                 W);
        printRow("Registered", registrationDate.toString(), W);
        System.out.println("╚" + "═".repeat(W) + "╝\n");
    }

    // Adds a doctor to the system — checks for duplicate ID first
    public void addDoctor(ArrayList<Doctor> doctorList, Doctor doctor) {
        if (doctorList.stream().anyMatch(d -> d.getUserId().equals(doctor.getUserId()))) {
            System.out.println("✗ Doctor with ID " + doctor.getUserId() + " already exists.");
            return;
        }
        doctorList.add(doctor);
        System.out.println("✓ Doctor " + doctor.getName() + " added successfully.");
    }

    /*
     Removes a patient and cleans up all linked prescriptions
     from both the patient and the issuing doctors.
    */
    public void removePatient(ArrayList<Patient> patientList, String patientId) {
        Patient patientToRemove = patientList.stream()
                .filter(p -> p.getUserId().equals(patientId))
                .findFirst()
                .orElse(null);
        if (patientToRemove == null) {
            System.out.println("✗ Patient not found.");
            return;
        }
        // Remove linked prescriptions from each issuing doctor
        patientToRemove.getPrescriptions().forEach(prescription -> {
            Doctor doctor = prescription.getDoctor();
            if (doctor != null) {
                doctor.removeIssuedPrescription(prescription);
            }
        });
        patientToRemove.getPrescriptions().clear();
        patientList.remove(patientToRemove);
        System.out.println("✓ Patient removed successfully (including all prescriptions).");
    }

    /*
     Removes a doctor and cleans up all linked prescriptions
     from both the doctor and their patients.
    */
    public void removeDoctor(ArrayList<Doctor> doctorList, String doctorId) {
        Doctor doctorToRemove = doctorList.stream()
                .filter(d -> d.getUserId().equals(doctorId))
                .findFirst()
                .orElse(null);
        if (doctorToRemove == null) {
            System.out.println("✗ Doctor not found.");
            return;
        }
        // Remove linked prescriptions from each affected patient
        doctorToRemove.getIssuedPrescriptions().forEach(prescription -> {
            Patient patient = prescription.getPatient();
            if (patient != null) {
                patient.removePrescription(prescription);
            }
        });
        doctorToRemove.getIssuedPrescriptions().clear();
        doctorList.remove(doctorToRemove);
        System.out.println("✓ Doctor removed successfully (including all issued prescriptions).");
    }

    // Lists all appointments in the system
    public void viewAllAppointments(ArrayList<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("\n✗ No appointments in the system.");
            return;
        }
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       ALL SYSTEM APPOINTMENTS          ║");
        System.out.println("╚════════════════════════════════════════╝");
        for (int i = 0; i < appointments.size(); i++) {
            System.out.println("\n" + (i + 1) + ".");
            appointments.get(i).displayAppointmentDetails();
        }
    }

    /*
     Generates a summary report with doctor/patient/appointment counts,
     broken down by appointment status.
    */
    public void generateReport(
            ArrayList<Doctor> doctors,
            ArrayList<Patient> patients,
            ArrayList<Appointment> appointments) {
        int W = 38;
        // Count appointments by status using streams
        long confirmed  = appointments.stream().filter(a -> a.getStatus().equals("CONFIRMED")).count();
        long pending    = appointments.stream().filter(a -> a.getStatus().equals("PENDING")).count();
        long cancelled  = appointments.stream().filter(a -> a.getStatus().equals("CANCELLED")).count();

        System.out.println("\n╔" + "═".repeat(W) + "╗");
        System.out.println("║" + center("SYSTEM REPORT", W) + "║");
        System.out.println("╠" + "═".repeat(W) + "╣");
        printRow("Total Doctors",           String.valueOf(doctors.size()),      W);
        printRow("Total Patients",          String.valueOf(patients.size()),     W);
        printRow("Total Appointments",      String.valueOf(appointments.size()), W);
        printRow("Confirmed Appointments",  String.valueOf(confirmed),           W);
        printRow("Pending Appointments",    String.valueOf(pending),             W);
        printRow("Cancelled Appointments",  String.valueOf(cancelled),           W);
        System.out.println("╚" + "═".repeat(W) + "╝\n");
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        if (adminLevel == null || adminLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("Admin level cannot be empty.");
        }
        String l = adminLevel.trim().toLowerCase();
        if (!l.equals("admin") && !l.equals("super_admin")) {
            throw new IllegalArgumentException(
                    "Admin level must be 'admin' or 'super_admin'.");
        }
        this.adminLevel = l;
    }
}