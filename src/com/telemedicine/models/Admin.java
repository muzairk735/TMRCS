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
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ADMIN PROFILE                  ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Admin ID: " + String.format("%-28s", userId) + "║");
        System.out.println("║ Name: " + String.format("%-32s", name) + "║");
        System.out.println("║ Level: " + String.format("%-31s", adminLevel) + "║");
        System.out.println("║ Email: " + String.format("%-31s", email) + "║");
        System.out.println("║ Phone: " + String.format("%-31s", phoneNumber) + "║");
        System.out.println("║ Registered: " + String.format("%-26s", registrationDate.toString()) + "║");
        System.out.println("╚════════════════════════════════════════╝\n");
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
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           SYSTEM REPORT                ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Total Doctors: " + String.format("%-23s", doctors.size()) + "║");
        System.out.println("║ Total Patients: " + String.format("%-22s", patients.size()) + "║");
        System.out.println("║ Total Appointments: " + String.format("%-17s", appointments.size()) + "║");

        long completedCount = appointments.stream()
            .filter(a -> a.getStatus().equals("COMPLETED")).count();
        System.out.println("║ Completed Appointments: " + String.format("%-13s", completedCount) + "║");

        long pendingCount = appointments.stream()
            .filter(a -> a.getStatus().equals("PENDING")).count();
        System.out.println("║ Pending Appointments: " + String.format("%-15s", pendingCount) + "║");

        long cancelledCount = appointments.stream()
            .filter(a -> a.getStatus().equals("CANCELLED")).count();
        System.out.println("║ Cancelled Appointments: " + String.format("%-13s", cancelledCount) + "║");

        System.out.println("╚════════════════════════════════════════╝\n");
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