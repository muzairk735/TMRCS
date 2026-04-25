package com.telemedicine.models;

public interface AppointmentViewerInterface {
    
    void viewAppointments();
    
    void viewAppointments(String status);
    
    void cancelAppointment(String appointmentId);
}
