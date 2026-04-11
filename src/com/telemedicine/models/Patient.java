package com.telemedicine.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Represents a Patient in the Telemedicine System.
 * Extends Person and adds patient-specific attributes and methods.
 * 
 * @author Telemedicine Team
 * @version 1.0
 */
public class Patient extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Patient-specific attributes
    private int age;
    private String gender;
    private String bloodGroup;
    private String address;
    private ArrayList<MedicalRecord> medicalHistory;
    private ArrayList<Appointment> appointments;
    
    // Constructor
    public Patient(String userId, String name, String email, 
                   String phoneNumber, String password,
                   int age, String gender, String bloodGroup, String address) {
        super(userId, name, email, phoneNumber, password);
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.medicalHistory = new ArrayList<>();
        this.appointments = new ArrayList<>();
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
    
    // Patient-specific methods
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
                System.out.println("\n✓ Appointment cancelled successfully.");
                return;
            }
        }
        System.out.println("\n✗ Appointment not found.");
    }
    
    public void addMedicalRecord(MedicalRecord record) {
        this.medicalHistory.add(record);
    }
    
    // Getters and Setters
    public int getAge() { 
        return age; 
    }
    
    public void setAge(int age) { 
        this.age = age; 
    }
    
    public String getGender() { 
        return gender; 
    }
    
    public String getBloodGroup() { 
        return bloodGroup; 
    }
    
    public String getAddress() { 
        return address; 
    }
    
    public void setAddress(String address) { 
        this.address = address; 
    }
    
    public ArrayList<Appointment> getAppointments() { 
        return appointments; 
    }
    
    public ArrayList<MedicalRecord> getMedicalHistory() { 
        return medicalHistory; 
    }
}
