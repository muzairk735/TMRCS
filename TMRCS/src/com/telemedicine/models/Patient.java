package com.telemedicine.models;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Patient extends Person implements Serializable, AppointmentViewerInterface {
    private static final long serialVersionUID = 1L;

    private static final String[] VALID_BLOOD_GROUPS =
            {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    private int age;
    private String gender;
    private String bloodGroup;
    private String address;
    private ArrayList<MedicalRecord> medicalHistory;
    private ArrayList<Appointment>   appointments;
    private ArrayList<Prescription>  prescriptions;

    public Patient(
            String userId,
            String name,
            String email,
            String phoneNumber,
            String password,
            int age,
            String gender,
            String bloodGroup,
            String address) {
        super(userId, name, email, phoneNumber, password);
        this.age             = validateAge(age);
        this.gender          = normalizeGender(gender);
        this.bloodGroup       = normalizeBloodGroup(bloodGroup);
        this.address         = validateAndNormalizeAddress(address);
        this.medicalHistory  = new ArrayList<>();
        this.appointments    = new ArrayList<>();
        this.prescriptions   = new ArrayList<>();
    }

    private static int validateAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150.");
        }
        return age;
    }

    private static String normalizeGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be empty.");
        }
        String normalized = gender.trim().toUpperCase();
        if (!normalized.equals("MALE") && !normalized.equals("FEMALE")) {
            throw new IllegalArgumentException("Gender must be MALE OR FEMALE.");
        }
        return normalized;
    }

    private static String normalizeBloodGroup(String bg) {
        if (bg == null || bg.trim().isEmpty()) {
            throw new IllegalArgumentException("Blood group cannot be empty.");
        }
        String normalized = bg.trim().toUpperCase();
        for (String v : VALID_BLOOD_GROUPS) {
            if (v.equals(normalized)) return normalized;
        }
        throw new IllegalArgumentException(
                "Invalid blood group. Must be one of: A+, A-, B+, B-, AB+, AB-, O+, O-.");
    }

    private static String validateAndNormalizeAddress(String a) {
        if (a == null || a.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty.");
        }
        return a.trim();
    }

    @Override
    public void displayProfile() {
        int W = 38;
        System.out.println("\n╔" + "═".repeat(W + 2) + "╗");
        System.out.println("║" + UIHelper.center("PATIENT PROFILE", W + 2) + "║");
        System.out.println("╠" + "═".repeat(W + 2) + "╣");
        UIHelper.printRow("Patient ID",   userId,                           W);
        UIHelper.printRow("Name",         name,                             W);
        UIHelper.printRow("Age",          String.valueOf(age),              W);
        UIHelper.printRow("Gender",       gender,                           W);
        UIHelper.printRow("Blood Group",  bloodGroup,                      W);
        UIHelper.printRow("Email",        email,                            W);
        UIHelper.printRow("Phone",        phoneNumber,                      W);
        UIHelper.printRow("Address",      address,                          W);
        UIHelper.printRow("Registered",   registrationDate.toString(),      W);
        UIHelper.printRow("Appointments", String.valueOf(appointments.size()), W);
        System.out.println("╚" + "═".repeat(W + 2) + "╝\n");
    }

    public void bookAppointment(Doctor doctor, LocalDateTime dateTime, String symptoms, String mode) {
        String id = "APT" + System.currentTimeMillis();
        Appointment appt = new Appointment(id, this, doctor, dateTime, symptoms, mode);
        this.appointments.add(appt);
        doctor.addAppointment(appt);
        System.out.println("\n✓ Appointment booked successfully!");
        System.out.println("  Appointment ID: " + id);
        System.out.println("  Doctor: Dr. " + doctor.getName());
        System.out.println("  Date & Time: " + dateTime);
        System.out.println("  Consultation Fee: Rs. " + doctor.getConsultationFee());
    }

    @Override
    public void viewAppointments() {
        viewAppointments("ALL");
    }

    @Override
    public void viewAppointments(String status) {
        ArrayList<Appointment> filtered = new ArrayList<>();
        for (Appointment a : appointments) {
            if (status.equalsIgnoreCase("ALL") || a.getStatus().equalsIgnoreCase(status)) {
                filtered.add(a);
            }
        }
        if (filtered.isEmpty()) {
            System.out.println("\n✗ No appointments with status: " + status);
            return;
        }
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║    APPOINTMENTS (" + UIHelper.padRight(status.toUpperCase() + ")", 22) + "║");
        System.out.println("╚════════════════════════════════════════╝");
        for (int i = 0; i < filtered.size(); i++) {
            System.out.println("\n" + (i + 1) + ".");
            filtered.get(i).displayAppointmentDetails();
        }
    }

    @Override
    public void cancelAppointment(String appointmentId) {
        for (Appointment a : appointments) {
            if (a.getAppointmentId().equals(appointmentId)) {
                a.cancelAppointment("Cancelled by patient");
                return;
            }
        }
        System.out.println("\n✗ Appointment not found.");
    }

    public void respondToChat(Appointment appointment, Scanner scanner) {
        int W = 38;
        System.out.println("\n╔" + "═".repeat(W + 2) + "╗");
        System.out.println("║" + UIHelper.center("CONSULTATION CHAT", W + 2) + "║");
        System.out.println("╠" + "═".repeat(W + 2) + "╣");
        UIHelper.printRow("With", "Dr. " + appointment.getDoctor().getName(), W);
        UIHelper.printRow("Mode", appointment.getConsultationMode(), W);
        System.out.println("╚" + "═".repeat(W + 2) + "╝\n");

        System.out.println("--- Conversation History ---\n");
        for (Message m : appointment.getConsultationMessages()) {
            m.displayMessage();
        }

        System.out.println("\n--- Your Reply ---");
        System.out.println("Type your messages (type 'done' when finished)\n");

        while (true) {
            System.out.print(name + ": ");
            String msg = scanner.nextLine().trim();
            if (msg.equalsIgnoreCase("done")) break;
            if (msg.isEmpty()) continue;
            appointment.addConsultationMessage(
                    new Message(name, "PATIENT", msg, appointment.getConsultationMode()));
            System.out.println("✓ Message sent\n");
        }
        System.out.println("\n✓ Replies saved. Doctor will see them when they log in.");
    }

    public void viewMedicalHistory() {
        if (medicalHistory.isEmpty()) {
            System.out.println("\n✗ No medical history available.");
            return;
        }
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         MEDICAL HISTORY                ║");
        System.out.println("╚════════════════════════════════════════╝");
        for (MedicalRecord r : medicalHistory) {
            r.displayRecord();
            System.out.println("─".repeat(40));
        }
    }

    public void addMedicalRecord(MedicalRecord r) {
        medicalHistory.add(r);
    }

    public void addPrescription(Prescription p) {
        if (p != null) {
            if (prescriptions == null) prescriptions = new ArrayList<>();
            if (!prescriptions.contains(p)) prescriptions.add(p);
        }
    }

    public void removePrescription(Prescription p) {
        if (p != null && prescriptions != null) prescriptions.remove(p);
    }

    public void viewPrescriptions() {
        if (prescriptions == null || prescriptions.isEmpty()) {
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

    public void addAppointment(Appointment a) {
        if (a != null && !appointments.contains(a)) {
            appointments.add(a);
        }
    }

    public int getAge() { return age; }
    public void setAge(int a) { this.age = validateAge(a); }

    public String getGender() { return gender; }
    public void setGender(String g) { this.gender = normalizeGender(g); }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String b) { this.bloodGroup = normalizeBloodGroup(b); }

    public String getAddress() { return address; }
    public void setAddress(String a) { this.address = validateAndNormalizeAddress(a); }

    public ArrayList<Appointment>   getAppointments()   { return appointments; }
    public ArrayList<MedicalRecord> getMedicalHistory() { return medicalHistory; }
    public ArrayList<Prescription>  getPrescriptions()  { return prescriptions; }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (prescriptions == null) prescriptions = new ArrayList<>();
    }
}