package com.telemedicine.models;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 Represents a patient in the telemedicine system.
 Inherits common user fields from Person (inheritance).
 Implements AppointmentViewerInterface to view/cancel appointments (abstraction).
*/
public class Patient extends Person implements Serializable, AppointmentViewerInterface {
    private static final long serialVersionUID = 1L;

    // Accepted blood group values
    private static final String[] VALID_BLOOD_GROUPS =
            {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    private int age;
    private String gender;
    private String bloodGroup;
    private String address;

    // Patient-specific records — stored as lists for multiple entries
    private ArrayList<MedicalRecord> medicalHistory;
    private ArrayList<Appointment>   appointments;
    private ArrayList<Prescription>  prescriptions;

    /*
     Constructs a Patient by calling the parent Person constructor first,
     then validating and setting patient-specific fields.
    */
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
        super(userId, name, email, phoneNumber, password); // call Person constructor
        setAge(age);
        setGender(gender);
        setBloodGroup(bloodGroup);
        setAddress(address);
        this.medicalHistory = new ArrayList<>();
        this.appointments   = new ArrayList<>();
        this.prescriptions  = new ArrayList<>();
    }

    /*
     Displays the patient's profile in a formatted box.
     Overrides the abstract displayProfile() from Person — polymorphism.
    */
    @Override
    public void displayProfile() {
        int W = 38;
        System.out.println("\n╔" + "═".repeat(W) + "╗");
        System.out.println("║" + center("PATIENT PROFILE", W) + "║");
        System.out.println("╠" + "═".repeat(W) + "╣");
        printRow("Patient ID",   userId,                           W);
        printRow("Name",         name,                             W);
        printRow("Age",          String.valueOf(age),              W);
        printRow("Gender",       gender,                           W);
        printRow("Blood Group",  bloodGroup,                       W);
        printRow("Email",        email,                            W);
        printRow("Phone",        phoneNumber,                      W);
        printRow("Address",      address,                          W);
        printRow("Registered",   registrationDate.toString(),      W);
        printRow("Appointments", String.valueOf(appointments.size()), W);
        System.out.println("╚" + "═".repeat(W) + "╝\n");
    }

    /*
     Books a new appointment with the given doctor and adds it to both
     the patient's and doctor's appointment lists.
    */
    public void bookAppointment(Doctor doctor, LocalDateTime dateTime, String symptoms, String mode) {
        String id = "APT" + System.currentTimeMillis(); // unique ID based on timestamp
        Appointment appt = new Appointment(id, this, doctor, dateTime, symptoms, mode);
        this.appointments.add(appt);
        doctor.addAppointment(appt); // register with the doctor too
        System.out.println("\n✓ Appointment booked successfully!");
        System.out.println("  Appointment ID: " + id);
        System.out.println("  Doctor: Dr. " + doctor.getName());
        System.out.println("  Date & Time: " + dateTime);
        System.out.println("  Consultation Fee: Rs. " + doctor.getConsultationFee());
    }

    // Shows all appointments — satisfies AppointmentViewerInterface 
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

    // Filters and shows appointments by status (e.g., "PENDING", "CONFIRMED")
    public void viewAppointments(String status) {
        ArrayList<Appointment> filtered = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getStatus().equalsIgnoreCase(status)) filtered.add(a);
        }
        if (filtered.isEmpty()) {
            System.out.println("\n✗ No appointments with status: " + status);
            return;
        }
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║    APPOINTMENTS (" + padRight(status.toUpperCase() + ")", 22) + "║");
        System.out.println("╚════════════════════════════════════════╝");
        for (int i = 0; i < filtered.size(); i++) {
            System.out.println("\n" + (i + 1) + ".");
            filtered.get(i).displayAppointmentDetails();
        }
    }

    // Cancels the appointment matching the given ID 
    public void cancelAppointment(String appointmentId) {
        cancelAppointmentById(appointments, appointmentId, "Cancelled by patient");
    }

    /*
     Lets the patient send replies in an existing consultation chat.
     Reads messages from the scanner until the user types 'done'.
    */
    public void respondToChat(Appointment appointment, Scanner scanner) {
        int W = 38;
        System.out.println("\n╔" + "═".repeat(W) + "╗");
        System.out.println("║" + center("CONSULTATION CHAT", W) + "║");
        System.out.println("╠" + "═".repeat(W) + "╣");
        printRow("With", "Dr. " + appointment.getDoctor().getName(), W);
        printRow("Mode", appointment.getConsultationMode(), W);
        System.out.println("╚" + "═".repeat(W) + "╝\n");

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
            if (msg.isEmpty()) continue; // skip blank input
            appointment.addConsultationMessage(
                    new Message(name, "PATIENT", msg, appointment.getConsultationMode()));
            System.out.println("✓ Message sent\n");
        }
        System.out.println("\n✓ Replies saved. Doctor will see them when they log in.");
    }

    // Displays all medical records on file for this patient
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

    // Adds a medical record to the patient's history 
    public void addMedicalRecord(MedicalRecord r) {
        medicalHistory.add(r);
    }

    // Adds a prescription — avoids duplicates and null entries
    public void addPrescription(Prescription p) {
        if (p != null) {
            if (prescriptions == null) prescriptions = new ArrayList<>();
            if (!prescriptions.contains(p)) prescriptions.add(p);
        }
    }

    public void removePrescription(Prescription p) {
        if (p != null && prescriptions != null) prescriptions.remove(p);
    }

    // Displays all prescriptions issued to this patient
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

    public int getAge() { 
        return age; 
    }
    public void setAge(int a) {
        if (a < 0 || a > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150.");
        }
        this.age = a;
    }

    public String getGender() { 
        return gender; 
    }
    public void setGender(String g) {
        if (g == null || g.trim().isEmpty())
            throw new IllegalArgumentException("Gender cannot be empty.");
        String normalized = g.trim().toUpperCase();
        if (!normalized.equals("MALE") && !normalized.equals("FEMALE"))
            throw new IllegalArgumentException("Gender must be MALE OR FEMALE.");
        this.gender = normalized;
    }

    public String getBloodGroup() { 
        return bloodGroup; 
    }
    public void setBloodGroup(String b) {
        if (b == null || b.trim().isEmpty())
            throw new IllegalArgumentException("Blood group cannot be empty.");
        String normalized = b.trim().toUpperCase();
        for (String v : VALID_BLOOD_GROUPS) if (v.equals(normalized)) {
            this.bloodGroup = normalized;
            return;
        }
        throw new IllegalArgumentException(
                "Invalid blood group. Must be one of: A+, A-, B+, B-, AB+, AB-, O+, O-.");
    }

    public String getAddress() { 
        return address; 
    }
    public void setAddress(String a) {
        if (a == null || a.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty.");
        }
        this.address = a.trim();
    }

    public ArrayList<Appointment>   getAppointments()   { 
        return appointments; 
    }
    public ArrayList<MedicalRecord> getMedicalHistory() { 
        return medicalHistory; 
    }
    public ArrayList<Prescription>  getPrescriptions()  { 
        return prescriptions; 
    }

    /**
     Custom deserialization — ensures prescriptions list is never null
     after loading from a file (handles older serialized objects).
    */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (prescriptions == null) prescriptions = new ArrayList<>();
    }
}