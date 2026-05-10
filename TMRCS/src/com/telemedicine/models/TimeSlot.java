package com.telemedicine.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimeSlot implements Serializable {
    private static final long serialVersionUID = 1L;

    private String    slotId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean   isAvailable;
    private Doctor    doctor;

    public TimeSlot(
            String slotId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            Doctor doctor) {
        if (date == null) {
            throw new IllegalArgumentException("Slot date cannot be null.");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start and end times cannot be null.");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }
        if (ChronoUnit.MINUTES.between(startTime, endTime) < 15) {
            throw new IllegalArgumentException("Time slot must be at least 15 minutes long.");
        }
        if (doctor == null) {
            throw new IllegalArgumentException("TimeSlot must be associated with a doctor.");
        }
        this.slotId      = slotId;
        this.date        = date;
        this.startTime   = startTime;
        this.endTime     = endTime;
        this.isAvailable = true;
        this.doctor      = doctor;
    }

    public void markAsBooked() {
        this.isAvailable = false;
    }

    public void markAsAvailable() {
        this.isAvailable = true;
    }

    public boolean isSlotAvailable() {
        LocalDate today = LocalDate.now();
        LocalTime now   = LocalTime.now();
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

    public String    getSlotId()     { return slotId; }
    public LocalDate getDate()       { return date; }
    public LocalTime getStartTime()  { return startTime; }
    public LocalTime getEndTime()    { return endTime; }
    public boolean   getIsAvailable(){ return isAvailable; }
    public Doctor    getDoctor()     { return doctor; }
}
