package com.telemedicine.models;

/*
 Common contract for classes that can manage appointments.
 Used by Patient and Doctor through abstraction.
*/
public interface AppointmentViewerInterface {

    // Filter by status (PENDING, CONFIRMED, CANCELLED, ALL)
    void viewAppointments(String status);

    // Cancel a selected appointment
    void cancelAppointment(String appointmentId);
}
