package com.telemedicine.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Prescription implements Serializable {
    private static final long serialVersionUID = 1L;

    private String prescriptionId;
    private Patient patient;
    private Doctor doctor;
    private LocalDate issuedDate;
    private String diagnosis;
    private ArrayList<Medicine> medicines;
    private String additionalNotes;

    public Prescription(
            String prescriptionId,
            Patient patient,
            Doctor doctor,
            String diagnosis,
            ArrayList<Medicine> medicines,
            String additionalNotes) {
        this.prescriptionId = prescriptionId;
        this.patient = patient;
        this.doctor = doctor;
        this.issuedDate = LocalDate.now();
        this.diagnosis = diagnosis;
        this.medicines = medicines;
        this.additionalNotes = additionalNotes;
    }

    public void addMedicine(Medicine medicine) {
        if (medicine != null) {
            this.medicines.add(medicine);
            System.out.println("✓ Medicine added to prescription.");
        }
    }

    public void displayPrescription() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          PRESCRIPTION                  ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Prescription ID: " + String.format("%-21s", prescriptionId) + "║");
        System.out.println("║ Patient: " + String.format("%-29s", patient.getName()) + "║");
        System.out.println("║ Doctor: Dr. " + String.format("%-27s", doctor.getName()) + "║");
        System.out.println("║ Date: " + String.format("%-32s", issuedDate.toString()) + "║");
        System.out.println("║ Diagnosis: " + String.format("%-28s", diagnosis) + "║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Medicines:                             ║");
        if (medicines.isEmpty()) {
            System.out.println("║   No medicines prescribed              ║");
        } else {
            for (int i = 0; i < medicines.size(); i++) {
                Medicine med = medicines.get(i);
                System.out.println("║ " + (i + 1) + ". " + String.format("%-35s", med.getMedicineName()) + "║");
                System.out.println("║    Dosage: " + String.format("%-28s", med.getDosage()) + "║");
                System.out.println("║    Frequency: " + String.format("%-25s", med.getFrequency()) + "║");
                System.out.println("║    Duration: " + String.format("%-26s", med.getDurationDays() + " days") + "║");
                System.out.println("║    Instructions: " + String.format("%-22s", med.getInstructions()) + "║");
            }
        }
        if (additionalNotes != null && !additionalNotes.isEmpty()) {
            System.out.println("║ Notes: " + String.format("%-31s", additionalNotes) + "║");
        }
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    public void generatePrescriptionReport() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("         PRESCRIPTION REPORT");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("Prescription ID: " + prescriptionId);
        System.out.println("Patient: " + patient.getName() + " (ID: " + patient.getUserId() + ")");
        System.out.println("Doctor: Dr. " + doctor.getName());
        System.out.println("Specialization: " + doctor.getSpecialization());
        System.out.println("Issued Date: " + issuedDate);
        System.out.println("Diagnosis: " + diagnosis);
        System.out.println("\nMedicines Prescribed:");
        if (medicines.isEmpty()) {
            System.out.println("  - No medicines prescribed");
        } else {
            for (int i = 0; i < medicines.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + medicines.get(i).getMedicineDetails());
            }
        }
        if (additionalNotes != null && !additionalNotes.isEmpty()) {
            System.out.println("\nAdditional Notes: " + additionalNotes);
        }
        System.out.println("═══════════════════════════════════════════\n");
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public ArrayList<Medicine> getMedicines() {
        return medicines;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String notes) {
        this.additionalNotes = notes;
    }
}
