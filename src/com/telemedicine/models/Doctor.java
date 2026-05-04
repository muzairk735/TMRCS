package com.telemedicine.models;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a doctor in the telemedicine system.
 * Extends Person (inheritance) and implements AppointmentViewerInterface (abstraction).
 * Also provides shared display utility methods used by Patient and Admin (printRow, center, padRight).
 */
public class Doctor extends Person implements Serializable, AppointmentViewerInterface {
    private static final long serialVersionUID = 1L;

    private String specialization;
    private String licenseNumber;
    private int experienceYears;
    private double consultationFee;

    // Availability slots set by the doctor
    private ArrayList<TimeSlot> availability;
    private ArrayList<Appointment> appointments;
    private ArrayList<Prescription> issuedPrescriptions;

    // Rating fields for cumulative average calculation
    private double rating;
    private int totalRatings;

    // --- Input validation helpers ---

    private static void validateSpecialization(String s) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization cannot be empty.");
        }
        if (s.trim().length() < 3) {
            throw new IllegalArgumentException("Specialization must be at least 3 characters.");
        }
    }

    // Alphanumeric, hyphens allowed, 5-20 chars — matches PMC license format
    private static void validateLicenseNumber(String l) {
        if (l == null || l.trim().isEmpty()) {
            throw new IllegalArgumentException("License number cannot be empty.");
        }
        if (!l.trim().matches("^[A-Za-z0-9\\-]{5,20}$")) {
            throw new IllegalArgumentException(
                    "License number must be 5-20 alphanumeric characters (hyphens allowed).");
        }
    }

    private static void validateExperienceYears(int y) {
        if (y < 0 || y > 70) {
            throw new IllegalArgumentException("Experience years must be between 0 and 70.");
        }
    }

    private static void validateConsultationFee(double f) {
        if (f < 0) {
            throw new IllegalArgumentException("Consultation fee cannot be negative.");
        }
        if (f > 1_000_000) {
            throw new IllegalArgumentException("Consultation fee is unrealistically high.");
        }
    }

    /**
     * Constructs a Doctor by first calling Person's constructor,
     * then validating and assigning doctor-specific fields.
     */
    public Doctor(
            String userId,
            String name,
            String email,
            String phoneNumber,
            String password,
            String specialization,
            String licenseNumber,
            int experienceYears,
            double consultationFee) {
        super(userId, name, email, phoneNumber, password);
        validateSpecialization(specialization);
        validateLicenseNumber(licenseNumber);
        validateExperienceYears(experienceYears);
        validateConsultationFee(consultationFee);
        this.specialization = specialization.trim();
        this.licenseNumber = licenseNumber.trim().toUpperCase();
        this.experienceYears = experienceYears;
        this.consultationFee = consultationFee;
        this.availability = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.issuedPrescriptions = new ArrayList<>();
        this.rating = 0.0;
        this.totalRatings = 0;
    }

    /**
     * Displays the doctor's profile in a formatted box.
     * Overrides the abstract displayProfile() from Person — polymorphism.
     */
    @Override
    public void displayProfile() {
        int W = 38;
        System.out.println("\n╔" + "═".repeat(W) + "╗");
        System.out.println("║" + center("DOCTOR PROFILE", W) + "║");
        System.out.println("╠" + "═".repeat(W) + "╣");
        printRow("Doctor ID",      userId,                     W);
        printRow("Name",           "Dr. " + name,              W);
        printRow("Specialization", specialization,             W);
        printRow("License",        licenseNumber,              W);
        printRow("Experience",     experienceYears + " years", W);
        printRow("Fee",            "Rs. " + consultationFee,  W);
        printRow("Email",          email,                      W);
        printRow("Phone",          phoneNumber,                W);
        if (totalRatings > 0) {
            printRow("Rating", String.format("%.1f/5.0 (%d reviews)", rating, totalRatings), W);
        }
        printRow("Appointments", String.valueOf(appointments.size()), W);
        System.out.println("╚" + "═".repeat(W) + "╝\n");
    }

    // --- Shared display utility methods (used by Patient and Admin too) ---

    /** Prints a single labeled row inside the bordered profile box */
    public static void printRow(String label, String value, int innerWidth) {
        String prefix = " " + label + ": ";
        int valueWidth = innerWidth - prefix.length();
        if (valueWidth < 1) valueWidth = 1;
        String v = value != null ? value : "";
        if (v.length() > valueWidth) v = v.substring(0, valueWidth - 1) + "…"; // truncate if too long
        System.out.println("║" + prefix + padRight(v, valueWidth) + "║");
    }

    /** Pads a string with spaces on the right to fill the given width */
    public static String padRight(String s, int width) {
        if (s.length() >= width) return s;
        return s + " ".repeat(width - s.length());
    }

    /** Centers a string within the given width using spaces */
    public static String center(String s, int width) {
        int pad = width - s.length();
        int left = pad / 2;
        int right = pad - left;
        return " ".repeat(left) + s + " ".repeat(right);
    }

    /**
     * Adds an availability time slot for the given date and time range.
     * Creates a unique slot ID using the current timestamp.
     */
    public void setAvailability(LocalDate date, LocalTime startTime, LocalTime endTime) {
        String slotId = "SLOT" + System.currentTimeMillis();
        TimeSlot slot = new TimeSlot(slotId, date, startTime, endTime, this);
        availability.add(slot);
        System.out.println("✓ Availability set for " + date +
                " (" + startTime + " - " + endTime + ")");
    }

    /** Prompts the doctor to enter a date and time range via the console */
    public void promptSetAvailability(Scanner scanner) {
        System.out.print("Enter date (DD-MM-YYYY): ");
        String dateStr = scanner.nextLine();
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date = LocalDate.parse(dateStr, fmt);
            System.out.print("Enter start time (HH:MM): ");
            LocalTime start = LocalTime.parse(scanner.nextLine());
            System.out.print("Enter end time (HH:MM): ");
            LocalTime end = LocalTime.parse(scanner.nextLine());
            setAvailability(date, start, end);
        } catch (Exception e) {
            System.out.println("✗ Invalid input format.");
        }
    }

    /** Returns all available (unbooked, future) slots for a specific date */
    public ArrayList<TimeSlot> getAvailableSlots(LocalDate date) {
        ArrayList<TimeSlot> result = new ArrayList<>();
        for (TimeSlot slot : availability) {
            if (slot.getDate().equals(date) && slot.isSlotAvailable()) {
                result.add(slot);
            }
        }
        return result;
    }

    /**
     * Displays appointments filtered by status.
     * Pass "ALL" to show everything — satisfies AppointmentViewerInterface.
     */
    public void viewAppointments(String status) {
        ArrayList<Appointment> list = new ArrayList<>();
        if (status.equalsIgnoreCase("ALL")) {
            list = appointments;
        } else {
            for (Appointment a : appointments) {
                if (a.getStatus().equalsIgnoreCase(status)) list.add(a);
            }
        }
        if (list.isEmpty()) {
            System.out.println("\n✗ No " + status + " appointments.");
            return;
        }
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  " + padRight(status.toUpperCase() + " APPOINTMENTS", 37) + "║");
        System.out.println("╚════════════════════════════════════════╝");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("\n" + (i + 1) + ".");
            list.get(i).displayAppointmentDetails();
        }
    }

    /** Shows all appointments regardless of status */
    public void viewAppointments() {
        viewAppointments("ALL");
    }

    public void addAppointment(Appointment a) {
        this.appointments.add(a);
    }

    /** Cancels the appointment with the given ID */
    public void cancelAppointment(String id) {
        for (Appointment a : appointments) {
            if (a.getAppointmentId().equals(id)) {
                a.cancelAppointment("Cancelled by doctor");
                return;
            }
        }
        System.out.println("\n✗ Appointment not found.");
    }

    /**
     * Runs the appropriate consultation session based on mode (VIDEO, PHONE, CHAT).
     * Marks the appointment as confirmed before starting.
     */
    public void runConsultationSession(Appointment appointment, Scanner scanner) {
        Patient patient = appointment.getPatient();
        String mode = appointment.getConsultationMode();

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   CONSULTATION SESSION - " + padRight(mode, 14) + "║");
        System.out.println("╠════════════════════════════════════════╣");
        printRow("Patient", patient.getName(), 38);
        printRow("Doctor", "Dr. " + name, 38);
        String sym = appointment.getSymptoms();
        printRow("Symptoms", sym.length() > 28 ? sym.substring(0, 27) + "…" : sym, 38);
        System.out.println("╚════════════════════════════════════════╝\n");

        appointment.confirmAppointment(); // auto-confirm when session starts

        // Delegate to the appropriate session handler
        switch (mode) {
            case "VIDEO": runVideoConsultation(appointment, scanner); break;
            case "PHONE": runPhoneConsultation(appointment, scanner); break;
            default:      runChatSession(appointment, scanner);       break;
        }
        System.out.println("\n✓ Messages sent. Patient will see them when they log in.");
    }

    private void runChatSession(Appointment appointment, Scanner scanner) {
        printSessionHeader("CHAT", appointment.getPatient().getName());
        showPreviousMessages(appointment);
        collectDoctorMessages(appointment, "CHAT", scanner);
    }

    private void runVideoConsultation(Appointment appointment, Scanner scanner) {
        printSessionHeader("VIDEO", appointment.getPatient().getName());
        showPreviousMessages(appointment);
        // Log session start event as a message
        appointment.addConsultationMessage(
                new Message(name, "DOCTOR", "[Doctor initiated video consultation session]", "VIDEO"));
        System.out.println("✓ Video consultation session started\n");
        collectDoctorMessages(appointment, "VIDEO", scanner);
        // Log session end event
        appointment.addConsultationMessage(
                new Message(name, "DOCTOR", "[Doctor ended video consultation session]", "VIDEO"));
    }

    private void runPhoneConsultation(Appointment appointment, Scanner scanner) {
        printSessionHeader("PHONE", appointment.getPatient().getName());
        showPreviousMessages(appointment);
        appointment.addConsultationMessage(
                new Message(name, "DOCTOR", "[Doctor initiated phone consultation]", "PHONE"));
        System.out.println("✓ Phone consultation session started\n");
        collectDoctorMessages(appointment, "PHONE", scanner);
        appointment.addConsultationMessage(
                new Message(name, "DOCTOR", "[Doctor ended phone consultation]", "PHONE"));
    }

    private void printSessionHeader(String mode, String patientName) {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("        APPOINTMENT MESSAGING (" + mode + ")");
        System.out.println("═══════════════════════════════════════════\n");
        System.out.println("Send messages to " + patientName + " (type 'done' when finished)\n");
    }

    /** Displays previous messages if any exist in this appointment */
    private void showPreviousMessages(Appointment appointment) {
        if (!appointment.getConsultationMessages().isEmpty()) {
            System.out.println("--- Previous Messages ---");
            for (Message m : appointment.getConsultationMessages()) {
                m.displayMessage();
            }
            System.out.println("--- New Messages ---\n");
        }
    }

    /** Reads messages from the doctor until they type 'done' */
    private void collectDoctorMessages(Appointment appointment, String mode, Scanner scanner) {
        while (true) {
            System.out.print("Dr. " + name + ": ");
            String msg = scanner.nextLine().trim();
            if (msg.equalsIgnoreCase("done")) break;
            if (msg.isEmpty()) continue; // skip blank lines
            appointment.addConsultationMessage(new Message(name, "DOCTOR", msg, mode));
            System.out.println("✓ Message saved\n");
        }
    }

    /**
     * Creates and issues a prescription for a patient.
     * Also links it to both the patient's and doctor's prescription lists.
     */
    public void issuePrescription(
            Patient patient,
            String diagnosis,
            ArrayList<Medicine> medicines,
            String notes) {
        String id = "PRE" + System.currentTimeMillis();
        Prescription prescription = new Prescription(id, patient, this, diagnosis, medicines, notes);
        if (patient != null) patient.addPrescription(prescription);
        addIssuedPrescription(prescription);
        System.out.println("✓ Prescription issued successfully.");
        prescription.displayPrescription();
    }

    /**
     * Prompts the doctor to enter prescription details via the console.
     * Collects medicines one by one until 'done' is entered.
     */
    public void promptIssuePrescription(ArrayList<Patient> patients, Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine().trim();

        // Search for the patient by ID
        Patient selectedPatient = null;
        for (Patient p : patients) {
            if (p.getUserId().equals(patientId)) {
                selectedPatient = p;
                break;
            }
        }

        if (selectedPatient == null) {
            System.out.println("✗ Patient not found.");
            return;
        }

        System.out.print("Diagnosis: ");
        String diagnosis = scanner.nextLine().trim();

        ArrayList<Medicine> medicines = new ArrayList<>();
        while (true) {
            System.out.print("\nMedicine name (or 'done' to finish): ");
            String mName = scanner.nextLine().trim();
            if (mName.equalsIgnoreCase("done")) break;
            System.out.print("Dosage: ");
            String dosage = scanner.nextLine().trim();
            System.out.print("Frequency: ");
            String freq = scanner.nextLine().trim();
            System.out.print("Duration (days): ");
            String durStr = scanner.nextLine().trim();
            int dur;
            try {
                dur = Integer.parseInt(durStr);
            } catch (NumberFormatException e) {
                System.out.println("✗ Invalid duration — medicine skipped.");
                continue;
            }
            System.out.print("Instructions: ");
            String instr = scanner.nextLine().trim();
            try {
                medicines.add(new Medicine(mName, dosage, freq, dur, instr));
                System.out.println("✓ Medicine added.");
            } catch (IllegalArgumentException e) {
                System.out.println("✗ Invalid medicine data: " + e.getMessage());
            }
        }

        System.out.print("\nAdditional notes: ");
        String notes = scanner.nextLine().trim();
        issuePrescription(selectedPatient, diagnosis, medicines, notes);
    }

    /** Adds a prescription to the doctor's issued list — avoids nulls and duplicates */
    public void addIssuedPrescription(Prescription p) {
        if (p != null) {
            if (issuedPrescriptions == null) issuedPrescriptions = new ArrayList<>();
            if (!issuedPrescriptions.contains(p)) issuedPrescriptions.add(p);
        }
    }

    public void removeIssuedPrescription(Prescription p) {
        if (p != null && issuedPrescriptions != null) issuedPrescriptions.remove(p);
    }

    /** Displays all prescriptions this doctor has issued */
    public void viewIssuedPrescriptions() {
        if (issuedPrescriptions == null || issuedPrescriptions.isEmpty()) {
            System.out.println("\n✗ No prescriptions issued.");
            return;
        }
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      PRESCRIBED BY YOU                 ║");
        System.out.println("╚════════════════════════════════════════╝");
        for (int i = 0; i < issuedPrescriptions.size(); i++) {
            System.out.println("\n" + (i + 1) + ".");
            issuedPrescriptions.get(i).displayPrescription();
        }
    }

    /** Marks appointment as complete if it's in a valid state */
    public void conductConsultation(Appointment a) {
        if (a.getStatus().equals("PENDING") || a.getStatus().equals("CONFIRMED")) {
            a.completeAppointment();
            System.out.println("✓ Consultation completed.");
        } else {
            System.out.println("✗ Cannot conduct. Status: " + a.getStatus());
        }
    }

    /**
     * Updates the doctor's running average rating.
     * Uses cumulative average formula: (old_avg * count + new_rating) / (count + 1)
     */
    public void addRating(double r) {
        if (r < 0 || r > 5) {
            System.out.println("Rating must be between 0 and 5.");
            return;
        }
        rating = (rating * totalRatings + r) / ++totalRatings;
    }

    // --- Getters and validated setters ---

    public String getSpecialization() { 
        return specialization; 
    }
    public void setSpecialization(String s) { 
        validateSpecialization(s); 
        this.specialization = s.trim(); 
    }

    public String getLicenseNumber() {
        return licenseNumber; 
    }
    public void setLicenseNumber(String l) { 
        validateLicenseNumber(l); 
        this.licenseNumber = l.trim().toUpperCase(); 
    }

    public int getExperienceYears() { 
        return experienceYears; 
    }
    public void setExperienceYears(int y) { 
        validateExperienceYears(y); 
        this.experienceYears = y; 
    }

    public double getConsultationFee() { 
        return consultationFee; 
    }
    public void setConsultationFee(double f) { 
        validateConsultationFee(f); 
        this.consultationFee = f; 
    }

    public double getRating() { 
        return rating; 
    }
    public int getTotalRatings() { 
        return totalRatings; 
    }

    public ArrayList<Appointment> getAppointments() { 
        return appointments; 
    }
    public ArrayList<TimeSlot> getAvailability() { 
        return availability; 
    }
    public ArrayList<Prescription> getIssuedPrescriptions() { 
        return issuedPrescriptions; 
    }

    /**
     * Custom deserialization — ensures issuedPrescriptions is never null
     * after loading from file (handles older serialized objects).
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (issuedPrescriptions == null) issuedPrescriptions = new ArrayList<>();
    }
}