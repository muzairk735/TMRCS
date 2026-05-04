package com.telemedicine.models;

import java.io.Serializable;

// A single medicine entry inside a Prescription
public class Medicine implements Serializable {
    private static final long serialVersionUID = 1L;

    private String medicineName;
    private String dosage;       // e.g. "500mg"
    private String frequency;   // e.g. "Twice daily"
    private int durationDays;
    private String instructions; // e.g. "Take after meals"

    public Medicine(
            String medicineName,
            String dosage,
            String frequency,
            int durationDays,
            String instructions) {
        if (medicineName == null || medicineName.trim().isEmpty()) {
            throw new IllegalArgumentException("Medicine name cannot be empty.");
        }
        if (dosage == null || dosage.trim().isEmpty()) {
            throw new IllegalArgumentException("Dosage cannot be empty.");
        }
        if (frequency == null || frequency.trim().isEmpty()) {
            throw new IllegalArgumentException("Frequency cannot be empty.");
        }
        if (durationDays <= 0 || durationDays > 365) {
            throw new IllegalArgumentException("Duration must be between 1 and 365 days.");
        }
        this.medicineName = medicineName.trim();
        this.dosage = dosage.trim();
        this.frequency = frequency.trim();
        this.durationDays = durationDays;
        this.instructions = (instructions != null) ? instructions.trim() : "";
    }

    // Verbose display used inside prescription printouts
    public void displayMedicineInfo() {
        System.out.println("    • " + medicineName);
        System.out.println("      Dosage: " + dosage);
        System.out.println("      Frequency: " + frequency);
        System.out.println("      Duration: " + durationDays + " days");
        System.out.println("      Instructions: " + instructions);
    }

    // Compact one-liner — useful for lists
    public String getMedicineDetails() {
        return medicineName + " (" + dosage + ") - " + frequency +
                " for " + durationDays + " days";
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public String getInstructions() {
        return instructions;
    }
}
