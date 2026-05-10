package com.telemedicine.models;

import java.util.ArrayList;

public class PrescriptionBuilder {
    private String prescriptionId;
    private Patient patient;
    private Doctor doctor;
    private String diagnosis;
    private ArrayList<Medicine> medicines;
    private String additionalNotes;

    public PrescriptionBuilder() {
        this.medicines = new ArrayList<>();
        this.prescriptionId = "PRE" + System.currentTimeMillis();
    }

    public PrescriptionBuilder withPatient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public PrescriptionBuilder withDoctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public PrescriptionBuilder withDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
        return this;
    }

    public PrescriptionBuilder addMedicine(Medicine medicine) {
        if (medicine != null) {
            this.medicines.add(medicine);
        }
        return this;
    }

    public PrescriptionBuilder withNotes(String notes) {
        this.additionalNotes = notes;
        return this;
    }

    public Prescription build() {
        if (patient == null) {
            throw new IllegalStateException("Patient is required for prescription");
        }
        if (doctor == null) {
            throw new IllegalStateException("Doctor is required for prescription");
        }
        if (diagnosis == null || diagnosis.trim().isEmpty()) {
            throw new IllegalStateException("Diagnosis is required for prescription");
        }

        Prescription prescription = new Prescription(
                prescriptionId,
                patient,
                doctor,
                diagnosis,
                medicines,
                additionalNotes
        );

        if (patient != null) {
            patient.addPrescription(prescription);
        }

        return prescription;
    }
}