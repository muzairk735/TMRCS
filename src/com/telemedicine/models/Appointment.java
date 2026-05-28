package com.telemedicine.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/*
 Represents a consultation appointment between a patient and a doctor.
 Tracks status, messages, consultation mode, and any associated prescription.
*/
public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;

    private String appointmentId;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentDateTime;
    private String symptoms;
    private String status;               // PENDING, CONFIRMED, CANCELLED
    private String consultationMode;     // VIDEO, PHONE, or CHAT
    private LocalDateTime createdAt;
    private Prescription prescription;  // set after the consultation
    private ArrayList<Message> consultationMessages;

    /*
     Creates a new appointment in PENDING status.
     Initializes consultation messages list and records creation time.
    */
    public Appointment(
            String appointmentId,
            Patient patient,
            Doctor doctor,
            LocalDateTime appointmentDateTime,
            String symptoms,
            String consultationMode) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.symptoms = symptoms;
        this.consultationMode = consultationMode;
        this.status = "PENDING"; // default status on creation
        this.createdAt = LocalDateTime.now();
        this.consultationMessages = new ArrayList<>();
        this.prescription = null;
    }

    // Confirms a pending appointment — only valid if status is PENDING 
    public void confirmAppointment() {
        if (status.equals("PENDING")) {
            this.status = "CONFIRMED";
            System.out.println("✓ Appointment confirmed.");
        } else {
            System.out.println("✗ Cannot confirm. Current status: " + status);
        }
    }

    /*
     Cancels the appointment if not already confirmed.
     Also removes any linked prescription from both patient and doctor.
    */
    public void cancelAppointment(String reason) {
        if (!status.equals("CONFIRMED")) {
            this.status = "CANCELLED";
            // Clean up prescription if one was linked
            if (this.prescription != null) {
                if (patient != null) {
                    patient.removePrescription(this.prescription);
                }
                if (doctor != null) {
                    doctor.removeIssuedPrescription(this.prescription);
                }
                this.prescription = null;
            }
            System.out.println("✓ Appointment cancelled. Reason: " + reason);
        } else {
            System.out.println("✗ Cannot cancel confirmed appointment.");
        }
    }

    // Sets appointment status to CONFIRMED if it's PENDING or CONFIRMED 
    public void completeAppointment() {
        if (status.equals("CONFIRMED") || status.equals("PENDING")) {
            this.status = "CONFIRMED";
        }
    }

    // Prints a summary of appointment details to the console
    public void displayAppointmentDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        System.out.println("  Appointment ID: " + appointmentId);
        System.out.println("  Patient: " + patient.getName());
        System.out.println("  Doctor: Dr. " + doctor.getName() +
                " (" + doctor.getSpecialization() + ")");
        System.out.println("  Date & Time: " + appointmentDateTime.format(formatter));
        System.out.println("  Mode: " + consultationMode);
        System.out.println("  Status: " + status);
        System.out.println("  Symptoms: " + symptoms);
        System.out.println("  Fee: Rs. " + doctor.getConsultationFee());
    }

    // Returns true if the appointment is within the next 24 hours
    public boolean isWithin24Hours() {
        LocalDateTime now = LocalDateTime.now();
        return appointmentDateTime.minusHours(24).isBefore(now) &&
                appointmentDateTime.isAfter(now);
    }

    // --- Getters and setters ---

    public String getAppointmentId() {
        return appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getConsultationMode() {
        return consultationMode;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    /*
     Links a prescription to this appointment and registers it
     with both the patient and doctor automatically.
    */
    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
        if (prescription != null) {
            if (patient != null) {
                patient.addPrescription(prescription);
            }
            if (doctor != null) {
                doctor.addIssuedPrescription(prescription);
            }
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Adds a message to the consultation chat — ignores null messages 
    public void addConsultationMessage(Message message) {
        if (message != null) {
            this.consultationMessages.add(message);
        }
    }

    public ArrayList<Message> getConsultationMessages() {
        return consultationMessages;
    }

    // Displays the full chat history for this appointment 
    public void displayConsultationHistory() {
        if (consultationMessages.isEmpty()) {
            System.out.println("\n✗ No consultation messages recorded.");
            return;
        }
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      CONSULTATION SESSION HISTORY      ║");
        System.out.println("╚════════════════════════════════════════╝");
        for (Message msg : consultationMessages) {
            msg.displayMessage();
        }
    }


    // Custom deserialization — ensures consultationMessages is never null
    // after loading from file (backward compatibility with older saves).

    private void readObject(java.io.ObjectInputStream ois)
            throws java.io.IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (this.consultationMessages == null) {
            this.consultationMessages = new ArrayList<>();
        }
    }
}