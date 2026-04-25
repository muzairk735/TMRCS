package com.telemedicine.models;

import java.io.Serializable;

public class Medicine implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String medicineName;
    private String dosage;
    private String frequency; // Once daily, Twice daily, etc.
    private int durationDays;
    private String instructions; // After meals, Before sleep, etc.
    
    public Medicine(String medicineName, String dosage, String frequency,
                   int durationDays, String instructions) {
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.durationDays = durationDays;
        this.instructions = instructions;
    }
    
    public void displayMedicineInfo() {
        System.out.println("    • " + medicineName);
        System.out.println("      Dosage: " + dosage);
        System.out.println("      Frequency: " + frequency);
        System.out.println("      Duration: " + durationDays + " days");
        System.out.println("      Instructions: " + instructions);
    }
    
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
