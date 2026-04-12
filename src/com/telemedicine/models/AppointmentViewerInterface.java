package com.telemedicine.models;

/**
 * Interface for appointment management operations.
 * Implemented by user types that can view and manage appointments.
 */
public interface AppointmentViewerInterface {
    
    /**
     * Displays all appointments for the user.
     */
    void viewAppointments();
    
    /**
     * Displays appointments filtered by status.
     * @param status The appointment status to filter by (e.g., "PENDING", "CONFIRMED", "COMPLETED")
     */
    void viewAppointments(String status);
    
    /**
     * Cancels an appointment.
     * @param appointmentId The ID of the appointment to cancel
     */
    void cancelAppointment(String appointmentId);
}
