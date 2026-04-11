package com.telemedicine.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Represents a time slot for doctor availability.
 * Manages booking and availability status of appointment slots.
 * 
 * @author Telemedicine Team
 * @version 1.0
 */
public class TimeSlot implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Attributes
    private String slotId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isAvailable;
    private Doctor doctor;
    
    // Constructor
    public TimeSlot(String slotId, LocalDate date, LocalTime startTime,
                   LocalTime endTime, Doctor doctor) {
        this.slotId = slotId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = true;
        this.doctor = doctor;
    }
    
    // Methods
    public void markAsBooked() {
        this.isAvailable = false;
    }
    
    public void markAsAvailable() {
        this.isAvailable = true;
    }
    
    public boolean isSlotAvailable() {
        // Check if slot is in the future
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        
        if (date.isBefore(today)) {
            return false;
        }
        
        if (date.equals(today) && startTime.isBefore(now)) {
            return false;
        }
        
        return isAvailable;
    }
    
    public boolean isConflict(TimeSlot otherSlot) {
        if (!this.date.equals(otherSlot.date)) {
            return false;
        }
        
        // Check time overlap
        return !(this.endTime.isBefore(otherSlot.startTime) || 
                this.startTime.isAfter(otherSlot.endTime));
    }
    
    public int getDurationInMinutes() {
        return (int) ChronoUnit.MINUTES.between(startTime, endTime);
    }
    
    public void displaySlotInfo() {
        System.out.println("  Slot: " + date + " | " + startTime + 
                         " - " + endTime + 
                         " | Status: " + (isAvailable ? "Available" : "Booked"));
    }
    
    // Getters
    public String getSlotId() { 
        return slotId; 
    }
    
    public LocalDate getDate() { 
        return date; 
    }
    
    public LocalTime getStartTime() { 
        return startTime; 
    }
    
    public LocalTime getEndTime() { 
        return endTime; 
    }
    
    public boolean getIsAvailable() { 
        return isAvailable; 
    }
    
    public Doctor getDoctor() { 
        return doctor; 
    }
}
