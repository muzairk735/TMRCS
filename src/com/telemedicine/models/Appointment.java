package com.telemedicine.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Attributes
    private String appointmentId;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentDateTime;
    private String symptoms;
    private String status; // PENDING, CONFIRMED, COMPLETED, CANCELLED
    private String consultationMode; // VIDEO, PHONE, CHAT
    private LocalDateTime createdAt;
    private Prescription prescription;
    private ArrayList<Message> consultationMessages;  // Consultation session messages/chat history
    
    // Constructor
    public Appointment(String appointmentId, Patient patient, Doctor doctor,
                      LocalDateTime appointmentDateTime, String symptoms,
                      String consultationMode) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.symptoms = symptoms;
        this.consultationMode = consultationMode;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
        this.consultationMessages = new ArrayList<>();
        this.prescription = null;
    }
    
    // Methods
    public void confirmAppointment() {
        if (status.equals("PENDING")) {
            this.status = "CONFIRMED";
            System.out.println("✓ Appointment confirmed.");
        } else {
            System.out.println("✗ Cannot confirm. Current status: " + status);
        }
    }
    
    public void cancelAppointment(String reason) {
        if (!status.equals("COMPLETED")) {
            this.status = "CANCELLED";
            
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
            System.out.println("✗ Cannot cancel completed appointment.");
        }
    }
    
    public void completeAppointment() {
        if (status.equals("CONFIRMED") || status.equals("PENDING")) {
            this.status = "COMPLETED";
        }
    }
    
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
    
    public boolean isWithin24Hours() {
        LocalDateTime now = LocalDateTime.now();
        return appointmentDateTime.minusHours(24).isBefore(now) &&
               appointmentDateTime.isAfter(now);
    }
    
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
    
    public void addConsultationMessage(Message message) {
        if (message != null) {
            this.consultationMessages.add(message);
        }
    }
    

    public ArrayList<Message> getConsultationMessages() {
        return consultationMessages;
    }
    
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
    
    private void readObject(java.io.ObjectInputStream ois) 
            throws java.io.IOException, ClassNotFoundException {
        ois.defaultReadObject();
        
        if (this.consultationMessages == null) {
            this.consultationMessages = new ArrayList<>();
        }
    }
}
