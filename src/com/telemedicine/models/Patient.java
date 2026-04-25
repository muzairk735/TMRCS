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
    private ArrayList<MedicalRecord>  medicalHistory;
    private ArrayList<Appointment>    appointments;
    private ArrayList<Prescription>   prescriptions;

    // Validators
    private static void validateAge(int age) {
        if (age < 0 || age > 150)
            throw new IllegalArgumentException("Age must be between 0 and 150.");
    }
    private static void validateGender(String gender) {
        if (gender == null || gender.trim().isEmpty())
            throw new IllegalArgumentException("Gender cannot be empty.");
        String g = gender.trim().toUpperCase();
        if (!g.equals("MALE") && !g.equals("FEMALE") )
            throw new IllegalArgumentException("Gender must be MALE OR FEMALE.");
    }
    private static void validateBloodGroup(String bg) {
        if (bg == null || bg.trim().isEmpty())
            throw new IllegalArgumentException("Blood group cannot be empty.");
        String b = bg.trim().toUpperCase();
        for (String v : VALID_BLOOD_GROUPS) if (v.equals(b)) return;
        throw new IllegalArgumentException(
            "Invalid blood group. Must be one of: A+, A-, B+, B-, AB+, AB-, O+, O-.");
    }
    private static void validateAddress(String a) {
        if (a == null || a.trim().isEmpty())
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
        this.appointments   = new ArrayList<>();
        this.prescriptions  = new ArrayList<>();
    }

    // Display
    @Override
    public void displayProfile() {
        int W = 38;
        System.out.println("\n╔" + "═".repeat(W + 2) + "╗");
        System.out.println("║" + Doctor.center("PATIENT PROFILE", W + 2) + "║");
        System.out.println("╠" + "═".repeat(W + 2) + "╣");
        Doctor.printRow("Patient ID",   userId,                        W);
        Doctor.printRow("Name",         name,                          W);
        Doctor.printRow("Age",          String.valueOf(age),           W);
        Doctor.printRow("Gender",       gender,                        W);
        Doctor.printRow("Blood Group",  bloodGroup,                    W);
        Doctor.printRow("Email",        email,                         W);
        Doctor.printRow("Phone",        phoneNumber,                   W);
        Doctor.printRow("Address",      address,                       W);
        Doctor.printRow("Registered",   registrationDate.toString(),   W);
        Doctor.printRow("Appointments", String.valueOf(appointments.size()), W);
        System.out.println("╚" + "═".repeat(W + 2) + "╝\n");
    }

    // Booking / appointments
    public void bookAppointment(Doctor doctor, LocalDateTime dateTime,
                                String symptoms, String mode) {
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

    public void viewAppointments() {
        if (appointments.isEmpty()) { System.out.println("\n✗ No appointments found."); return; }
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         YOUR APPOINTMENTS              ║");
        System.out.println("╚════════════════════════════════════════╝");
        for (int i = 0; i < appointments.size(); i++) {
            System.out.println("\n" + (i + 1) + ".");
            appointments.get(i).displayAppointmentDetails();
        }
    }

    public void viewAppointments(String status) {
        ArrayList<Appointment> filtered = new ArrayList<>();
        for (Appointment a : appointments)
            if (a.getStatus().equalsIgnoreCase(status)) filtered.add(a);
        if (filtered.isEmpty()) { System.out.println("\n✗ No appointments with status: " + status); return; }
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║    APPOINTMENTS (" + Doctor.padRight(status.toUpperCase() + ")", 22) + "║");
        System.out.println("╚════════════════════════════════════════╝");
        for (int i = 0; i < filtered.size(); i++) {
            System.out.println("\n" + (i + 1) + ".");
            filtered.get(i).displayAppointmentDetails();
        }
    }

    public void cancelAppointment(String appointmentId) {
        for (Appointment a : appointments)
            if (a.getAppointmentId().equals(appointmentId)) {
                a.cancelAppointment("Cancelled by patient"); return;
            }
        System.out.println("\n✗ Appointment not found.");
    }

    public void respondToChat(Appointment appointment, Scanner scanner) {
        int W = 38;
        System.out.println("\n╔" + "═".repeat(W + 2) + "╗");
        System.out.println("║" + Doctor.center("CONSULTATION CHAT", W + 2) + "║");
        System.out.println("╠" + "═".repeat(W + 2) + "╣");
        Doctor.printRow("With", "Dr. " + appointment.getDoctor().getName(), W);
        Doctor.printRow("Mode", appointment.getConsultationMode(), W);
        System.out.println("╚" + "═".repeat(W + 2) + "╝\n");

        System.out.println("--- Conversation History ---\n");
        for (Message m : appointment.getConsultationMessages()) m.displayMessage();

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

    // Medical / prescriptions
    public void viewMedicalHistory() {
        if (medicalHistory.isEmpty()) { System.out.println("\n✗ No medical history available."); return; }
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         MEDICAL HISTORY                ║");
        System.out.println("╚════════════════════════════════════════╝");
        for (MedicalRecord r : medicalHistory) { r.displayRecord(); System.out.println("─".repeat(40)); }
    }

    public void addMedicalRecord(MedicalRecord r)  { medicalHistory.add(r); }
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
            System.out.println("\n✗ No prescriptions found."); return;
        }
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         YOUR PRESCRIPTIONS             ║");
        System.out.println("╚════════════════════════════════════════╝");
        for (int i = 0; i < prescriptions.size(); i++) {
            System.out.println("\n" + (i + 1) + "."); prescriptions.get(i).displayPrescription();
        }
    }

    // Getters / Setters 
    public int getAge()        { return age; }
    public void setAge(int a)  { validateAge(a); this.age = a; }

    public String getGender()  { return gender; }
    public void setGender(String g) { validateGender(g); this.gender = g.trim().toUpperCase(); }

    public String getBloodGroup()    { return bloodGroup; }
    public void setBloodGroup(String b) { validateBloodGroup(b); this.bloodGroup = b.trim().toUpperCase(); }

    public String getAddress()       { return address; }
    public void setAddress(String a) { validateAddress(a); this.address = a.trim(); }

    public ArrayList<Appointment>  getAppointments()  { return appointments; }
    public ArrayList<MedicalRecord> getMedicalHistory() { return medicalHistory; }
    public ArrayList<Prescription> getPrescriptions() { return prescriptions; }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (prescriptions == null) prescriptions = new ArrayList<>();
    }
}