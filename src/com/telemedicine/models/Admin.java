package com.telemedicine.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Admin extends Person implements Serializable {
    private static final long serialVersionUID = 1L;

    // Admin-specific attributes
    private String adminLevel; // super_admin, admin

    // Validator
    private static void validateAdminLevel(String level) {
        if (level == null || level.trim().isEmpty())
            throw new IllegalArgumentException("Admin level cannot be empty.");
        String l = level.trim().toLowerCase();
        if (!l.equals("admin") && !l.equals("super_admin"))
            throw new IllegalArgumentException(
                "Admin level must be 'admin' or 'super_admin'.");
    }

    // Constructor
    public Admin(String userId, String name, String email,
                 String phoneNumber, String password, String adminLevel) {
        super(userId, name, email, phoneNumber, password);
        validateAdminLevel(adminLevel);
        this.adminLevel = adminLevel.trim().toLowerCase();
    }

    // Override abstract method
    @Override
    public void displayProfile() {
        int W = 38;
        System.out.println("\n╔" + "═".repeat(W + 2) + "╗");
        System.out.println("║" + Doctor.center("ADMIN PROFILE", W + 2) + "║");
        System.out.println("╠" + "═".repeat(W + 2) + "╣");
        Doctor.printRow("Admin ID",    userId,                      W);
        Doctor.printRow("Name",        name,                        W);
        Doctor.printRow("Level",       adminLevel,                  W);
        Doctor.printRow("Email",       email,                       W);
        Doctor.printRow("Phone",       phoneNumber,                 W);
        Doctor.printRow("Registered",  registrationDate.toString(), W);
        System.out.println("╚" + "═".repeat(W + 2) + "╝\n");
    }

    // Admin-specific methods
    public void addDoctor(ArrayList<Doctor> doctorList, Doctor doctor) {
        if (doctorList.stream().anyMatch(d -> d.getUserId().equals(doctor.getUserId()))) {
            System.out.println("✗ Doctor with ID " + doctor.getUserId() + " already exists.");
            return;
        }
        doctorList.add(doctor);
        System.out.println("✓ Doctor " + doctor.getName() + " added successfully.");
    }

    public void removePatient(ArrayList<Patient> patientList, String patientId) {
        Patient patientToRemove = patientList.stream()
            .filter(p -> p.getUserId().equals(patientId))
            .findFirst()
            .orElse(null);

        if (patientToRemove == null) {
            System.out.println("✗ Patient not found.");
            return;
        }

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

    public void removeDoctor(ArrayList<Doctor> doctorList, String doctorId) {
        Doctor doctorToRemove = doctorList.stream()
            .filter(d -> d.getUserId().equals(doctorId))
            .findFirst()
            .orElse(null);

        if (doctorToRemove == null) {
            System.out.println("✗ Doctor not found.");
            return;
        }

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

    public void generateReport(ArrayList<Doctor> doctors, ArrayList<Patient> patients,
                              ArrayList<Appointment> appointments) {
        int W = 38;
        long completed  = appointments.stream().filter(a -> a.getStatus().equals("COMPLETED")).count();
        long pending    = appointments.stream().filter(a -> a.getStatus().equals("PENDING")).count();
        long cancelled  = appointments.stream().filter(a -> a.getStatus().equals("CANCELLED")).count();

        System.out.println("\n╔" + "═".repeat(W + 2) + "╗");
        System.out.println("║" + Doctor.center("SYSTEM REPORT", W + 2) + "║");
        System.out.println("╠" + "═".repeat(W + 2) + "╣");
        Doctor.printRow("Total Doctors",          String.valueOf(doctors.size()),      W);
        Doctor.printRow("Total Patients",         String.valueOf(patients.size()),     W);
        Doctor.printRow("Total Appointments",     String.valueOf(appointments.size()), W);
        Doctor.printRow("Completed Appointments", String.valueOf(completed),           W);
        Doctor.printRow("Pending Appointments",   String.valueOf(pending),             W);
        Doctor.printRow("Cancelled Appointments", String.valueOf(cancelled),           W);
        System.out.println("╚" + "═".repeat(W + 2) + "╝\n");
    }

    // Getters and Setters
    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        validateAdminLevel(adminLevel);
        this.adminLevel = adminLevel.trim().toLowerCase();
    }
}