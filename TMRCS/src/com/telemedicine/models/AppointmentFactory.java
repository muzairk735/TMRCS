package com.telemedicine.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AppointmentFactory {

    private static String generateAppointmentId() {
        return "APT" + System.currentTimeMillis();
    }

    public static Appointment create(
            Patient patient,
            Doctor doctor,
            LocalDateTime dateTime,
            String symptoms,
            String mode) {
        String id = generateAppointmentId();
        Appointment appointment = new Appointment(id, patient, doctor, dateTime, symptoms, mode);

        if (patient != null) {
            patient.addAppointment(appointment);
        }
        if (doctor != null) {
            doctor.addAppointment(appointment);
        }

        return appointment;
    }

    public static Appointment createWithPrescription(
            Patient patient,
            Doctor doctor,
            LocalDateTime dateTime,
            String symptoms,
            String mode,
            Prescription prescription) {
        Appointment appointment = create(patient, doctor, dateTime, symptoms, mode);
        if (prescription != null) {
            appointment.setPrescription(prescription);
        }
        return appointment;
    }

    public static ArrayList<Appointment> createBatch(
            Patient patient,
            Doctor doctor,
            ArrayList<LocalDateTime> dateTimes,
            String symptoms,
            String mode) {
        ArrayList<Appointment> appointments = new ArrayList<>();
        for (LocalDateTime dateTime : dateTimes) {
            appointments.add(create(patient, doctor, dateTime, symptoms, mode));
        }
        return appointments;
    }
}