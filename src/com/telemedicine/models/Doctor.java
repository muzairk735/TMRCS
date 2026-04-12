package com.telemedicine.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Represents a Doctor in the Telemedicine System.
 * Extends Person and adds doctor-specific attributes and methods.
 * 
 * @author Telemedicine Team
 * @version 1.0
 */
public class Doctor extends Person implements Serializable, IAppointmentViewer {
    private static final long serialVersionUID = 1L;
    
    // Doctor-specific attributes
    private String specialization;
    private String licenseNumber;
    private int experienceYears;
    private double consultationFee;
    private ArrayList<TimeSlot> availability;
    private ArrayList<Appointment> appointments;
    private double rating;
    private int totalRatings;
    
    // Constructor
    public Doctor(String userId, String name, String email, 
                  String phoneNumber, String password,
                  String specialization, String licenseNumber,
                  int experienceYears, double consultationFee) {
        super(userId, name, email, phoneNumber, password);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.experienceYears = experienceYears;
        this.consultationFee = consultationFee;
        this.availability = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.rating = 0.0;
        this.totalRatings = 0;
    }
    
    // Override abstract method
    @Override
    public void displayProfile() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         DOCTOR PROFILE                 ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Doctor ID: " + String.format("%-27s", userId) + "║");
        System.out.println("║ Name: Dr. " + String.format("%-28s", name) + "║");
        System.out.println("║ Specialization: " + String.format("%-22s", specialization) + "║");
        System.out.println("║ License: " + String.format("%-29s", licenseNumber) + "║");
        System.out.println("║ Experience: " + String.format("%-26s", experienceYears + " years") + "║");
        System.out.println("║ Fee: Rs. " + String.format("%-29s", consultationFee) + "║");
        System.out.println("║ Email: " + String.format("%-31s", email) + "║");
        System.out.println("║ Phone: " + String.format("%-31s", phoneNumber) + "║");
        if (totalRatings > 0) {
            System.out.println("║ Rating: " + String.format("%-29s", 
                    String.format("%.1f/5.0 (%d reviews)", rating, totalRatings)) + "║");
        }
        System.out.println("║ Appointments: " + String.format("%-24s", appointments.size()) + "║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    // Set availability for a date range
    public void setAvailability(LocalDate date, LocalTime startTime, 
                               LocalTime endTime) {
        String slotId = "SLOT" + System.currentTimeMillis();
        TimeSlot slot = new TimeSlot(slotId, date, startTime, endTime, this);
        availability.add(slot);
        System.out.println("✓ Availability set for " + date + " (" + startTime + " - " + endTime + ")");
    }
    
    // Get available slots for a specific date
    public ArrayList<TimeSlot> getAvailableSlots(LocalDate date) {
        ArrayList<TimeSlot> availableSlots = new ArrayList<>();
        for (TimeSlot slot : availability) {
            if (slot.getDate().equals(date) && slot.isSlotAvailable()) {
                availableSlots.add(slot);
            }
        }
        return availableSlots;
    }
    
    // View appointments by status
    public void viewAppointments(String status) {
        ArrayList<Appointment> filteredAppointments = new ArrayList<>();
        
        if (status.equalsIgnoreCase("ALL")) {
            filteredAppointments = appointments;
        } else {
            for (Appointment apt : appointments) {
                if (apt.getStatus().equalsIgnoreCase(status)) {
                    filteredAppointments.add(apt);
                }
            }
        }
        
        if (filteredAppointments.isEmpty()) {
            System.out.println("\n✗ No " + status + " appointments found.");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  " + String.format("%-37s", status.toUpperCase() + " APPOINTMENTS") + "║");
        System.out.println("╚════════════════════════════════════════╝");
        
        for (int i = 0; i < filteredAppointments.size(); i++) {
            System.out.println("\n" + (i + 1) + ".");
            filteredAppointments.get(i).displayAppointmentDetails();
        }
    }
    
    // Method overloading
    public void viewAppointments() {
        viewAppointments("ALL");
    }
    
    // Cancel appointment
    public void cancelAppointment(String appointmentId) {
        for (Appointment apt : appointments) {
            if (apt.getAppointmentId().equals(appointmentId)) {
                apt.cancelAppointment("Cancelled by doctor");
                System.out.println("\n✓ Appointment cancelled successfully.");
                return;
            }
        }
        System.out.println("\n✗ Appointment not found.");
    }
    
    // Conduct consultation
    public void conductConsultation(Appointment appointment) {
        if (appointment.getStatus().equals("PENDING") || 
            appointment.getStatus().equals("CONFIRMED")) {
            appointment.completeAppointment();
            System.out.println("✓ Consultation completed.");
        } else {
            System.out.println("✗ Cannot conduct consultation. " +
                             "Appointment status: " + appointment.getStatus());
        }
    }
    
    // Issue prescription
    public void issuePrescription(Patient patient, String diagnosis, 
                                 ArrayList<Medicine> medicines, 
                                 String notes) {
        String prescriptionId = "PRE" + System.currentTimeMillis();
        Prescription prescription = new Prescription(
            prescriptionId, patient, this, diagnosis, medicines, notes
        );
        
        System.out.println("✓ Prescription issued successfully.");
        prescription.displayPrescription();
    }
    
    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }
    
    // Update rating
    public void addRating(double newRating) {
        if (newRating < 0 || newRating > 5) {
            System.out.println("Rating must be between 0 and 5.");
            return;
        }
        double totalScore = rating * totalRatings;
        totalRatings++;
        rating = (totalScore + newRating) / totalRatings;
    }
    
    // Getters
    public String getSpecialization() { 
        return specialization; 
    }
    
    public String getLicenseNumber() { 
        return licenseNumber; 
    }
    
    public int getExperienceYears() { 
        return experienceYears; 
    }
    
    public double getConsultationFee() { 
        return consultationFee; 
    }
    
    public void setConsultationFee(double fee) { 
        this.consultationFee = fee; 
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
}
