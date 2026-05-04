package com.telemedicine.models;

// Shared appointment-viewing contract for Doctor and Patient
public interface AppointmentViewerInterface {

    void viewAppointments();

    // Filter by status (PENDING, CONFIRMED, CANCELLED, ALL)
    void viewAppointments(String status);

    void cancelAppointment(String appointmentId);
}
