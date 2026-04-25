package com.telemedicine.models;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Patient extends Person implements Serializable, AppointmentViewerInterface {
    private static final long serialVersionUID = 1L;

    private static final String[] VALID_BLOOD_GROUPS =
        {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    // Patient-specific attributes
    private int age;
    private String gender;
    private String bloodGroup;
    private String address;
    private ArrayList<MedicalRecord> medicalHistory;
    private ArrayList<Appointment> appointments;
    private ArrayList<Prescription> prescriptions;

    // Validators
    private static void validateAge(int age) {
        if (age < 0 || age > 150)
            throw new IllegalArgumentException("Age must be between 0 and 150.");
    }

    private static void validateGender(String gender) {
        if (gender == null || gender.trim().isEmpty())
            throw new IllegalArgumentException("Gender cannot be empty.");
        String g = gender.trim().toUpperCase();
        if (!g.equals("MALE") && !g.equals("FEMALE"))
            throw new IllegalArgumentException("Gender must be MALE OR FEMALE.");
    }

    private static void validateBloodGroup(String bloodGroup) {
        if (bloodGroup == null || bloodGroup.trim().isEmpty())
            throw new IllegalArgumentException("Blood group cannot be empty.");
        String bg = bloodGroup.trim().toUpperCase();
        for (String valid : VALID_BLOOD_GROUPS) {
            if (valid.equals(bg)) return;
        }
        throw new IllegalArgumentException(
            "Invalid blood group. Must be one of: A+, A-, B+, B-, AB+, AB-, O+, O-.");
    }

    private static void validateAddress(String address) {
        if (address == null || address.trim().isEmpty())
            throw new IllegalArgumentException("Address cannot be empty.");
    }

    // Constructor
    public Patient(String userId, String name, String email,
                   String phoneNumber, String password,
                   int age, String gender, String bloodGroup, String address) {
        super(userId, name, email, phoneNumber, password);
        validateAge(age);
        validateGender(gender);
        validateBloodGroup(bloodGroup);
        validateAddress(address);
        this.age = age;
        this.gender = gender.trim().toUpperCase();
        this.bloodGroup = bloodGroup.trim().toUpperCase();
        this.address = address.trim();
        this.medicalHistory = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
    }

    // Override abstract method from Person
    @Override
    public void displayProfile() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         PATIENT PROFILE                ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Patient ID: " + String.format("%-27s", userId) + "║");
        System.out.println("║ Name: " + String.format("%-32s", name) + "║");
        System.out.println("║ Age: " + String.format("%-33s", age) + "║");
        System.out.println("║ Gender: " + String.format("%-30s", gender) + "║");
        System.out.println("║ Blood Group: " + String.format("%-26s", bloodGroup) + "║");
        System.out.println("║ Email: " + String.format("%-31s", email) + "║");
        System.out.println("║ Phone: " + String.format("%-31s", phoneNumber) + "║");
        System.out.println("║ Address: " + String.format("%-29s", address) + "║");
        System.out.println("║ Registered: " + String.format("%-26s", registrationDate.toString()) + "║");
        System.out.println("║ Appointments: " + String.format("%-24s", appointments.size()) + "║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    public void bookAppointment(Doctor doctor, LocalDateTime dateTime,
                                String symptoms, String mode) {
        String appointmentId = "APT" + System.currentTimeMillis();

        Appointment appointment = new Appointment(
            appointmentId, this, doctor, dateTime, symptoms, mode
        );

        this.appointments.add(appointment);
        doctor.addAppointment(appointment);

        System.out.println("\n✓ Appointment booked successfully!");
        System.out.println("  Appointment ID: " + appointmentId);
        System.out.println("  Doctor: Dr. " + doctor.getName());
        System.out.println("  Specialization: " + doctor.getSpecialization());
        System.out.println("  Date & Time: " + dateTime);
        System.out.println("  Consultation Fee: Rs. " + doctor.getConsultationFee());
    }

    public void viewAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("\n✗ No appointments found.");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         YOUR APPOINTMENTS              ║");
        System.out.println("╚════════════════════════════════════════╝");

        for (int i = 0; i < appointments.size(); i++) {
            System.out.println("\n" + (i + 1) + ".");
            appointments.get(i).displayAppointmentDetails();
        }
    }

    public void viewAppointments(String status) {
        ArrayList<Appointment> filteredAppointments = new ArrayList<>();
        for (Appointment apt : appointments) {
            if (apt.getStatus().equalsIgnoreCase(status)) {
                filteredAppointments.add(apt);
            }
        }

        if (filteredAppointments.isEmpty()) {
            System.out.println("\n✗ No appointments found with status: " + status);
            return;
        }

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║    APPOINTMENTS (" + status.toUpperCase() + ")       ║");
        System.out.println("╚════════════════════════════════════════╝");

        for (int i = 0; i < filteredAppointments.size(); i++) {
            System.out.println("\n" + (i + 1) + ".");
            filteredAppointments.get(i).displayAppointmentDetails();
        }
    }

    public void viewMedicalHistory() {
        if (medicalHistory.isEmpty()) {
            System.out.println("\n✗ No medical history available.");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         MEDICAL HISTORY                ║");
        System.out.println("╚════════════════════════════════════════╝");

        for (MedicalRecord record : medicalHistory) {
            record.displayRecord();
            System.out.println("─────────────────────────────────────────");
        }
    }

    public void cancelAppointment(String appointmentId) {
        for (Appointment apt : appointments) {
            if (apt.getAppointmentId().equals(appointmentId)) {
                apt.cancelAppointment("Cancelled by patient");
                return;
            }
        }
        System.out.println("\n✗ Appointment not found.");
    }

    public void addMedicalRecord(MedicalRecord record) {
        this.medicalHistory.add(record);
    }

    public void addPrescription(Prescription prescription) {
        if (prescription != null) {
            if (this.prescriptions == null) {
                this.prescriptions = new ArrayList<>();
            }
            if (!this.prescriptions.contains(prescription)) {
                this.prescriptions.add(prescription);
            }
        }
    }

    public void removePrescription(Prescription prescription) {
        if (prescription != null && this.prescriptions != null) {
            this.prescriptions.remove(prescription);
        }
    }

    public void viewPrescriptions() {
        if (this.prescriptions == null || this.prescriptions.isEmpty()) {
            System.out.println("\n✗ No prescriptions found.");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         YOUR PRESCRIPTIONS             ║");
        System.out.println("╚════════════════════════════════════════╝");

        for (int i = 0; i < prescriptions.size(); i++) {
            System.out.println("\n" + (i + 1) + ".");
            prescriptions.get(i).displayPrescription();
        }
    }

    // Getters and Setters
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        validateAge(age);
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        validateGender(gender);
        this.gender = gender.trim().toUpperCase();
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        validateBloodGroup(bloodGroup);
        this.bloodGroup = bloodGroup.trim().toUpperCase();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        validateAddress(address);
        this.address = address.trim();
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public ArrayList<MedicalRecord> getMedicalHistory() {
        return medicalHistory;
    }

    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (this.prescriptions == null) {
            this.prescriptions = new ArrayList<>();
        }
    }
}
