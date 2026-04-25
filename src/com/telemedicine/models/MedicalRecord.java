package com.telemedicine.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class MedicalRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Attributes
    private String recordId;
    private Patient patient;
    private LocalDate recordDate;
    private String diagnosis;
    private String treatment;
    private String doctorName;
    private ArrayList<String> testResults;
    private String notes;
    
    // Constructor
    public MedicalRecord(String recordId, Patient patient, String diagnosis,
                        String treatment, String doctorName) {
        this.recordId = recordId;
        this.patient = patient;
        this.recordDate = LocalDate.now();
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.doctorName = doctorName;
        this.testResults = new ArrayList<>();
        this.notes = "";
    }
    
    public void displayRecord() {
        System.out.println("  Record ID: " + recordId);
        System.out.println("  Date: " + recordDate);
        System.out.println("  Doctor: " + doctorName);
        System.out.println("  Diagnosis: " + diagnosis);
        System.out.println("  Treatment: " + treatment);
        
        if (!testResults.isEmpty()) {
            System.out.println("  Test Results:");
            for (String result : testResults) {
                System.out.println("    • " + result);
            }
        }
        
        if (notes != null && !notes.isEmpty()) {
            System.out.println("  Notes: " + notes);
        }
    }
    
    public void addTestResult(String result) {
        testResults.add(result);
        System.out.println("✓ Test result added: " + result);
    }
    
    public void updateNotes(String newNotes) {
        this.notes = newNotes;
        System.out.println("✓ Notes updated.");
    }
    
    public void displayFullRecord() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       MEDICAL RECORD DETAILS           ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Record ID: " + String.format("%-27s", recordId) + "║");
        System.out.println("║ Patient: " + String.format("%-29s", patient.getName()) + "║");
        System.out.println("║ Date: " + String.format("%-32s", recordDate.toString()) + "║");
        System.out.println("║ Doctor: " + String.format("%-30s", doctorName) + "║");
        System.out.println("║ Diagnosis: " + String.format("%-28s", diagnosis) + "║");
        System.out.println("║ Treatment: " + String.format("%-27s", treatment) + "║");
        
        if (!testResults.isEmpty()) {
            System.out.println("║ Test Results:                          ║");
            for (String result : testResults) {
                System.out.println("║   • " + String.format("%-33s", result) + "║");
            }
        }
        
        if (notes != null && !notes.isEmpty()) {
            System.out.println("║ Notes: " + String.format("%-31s", notes) + "║");
        }
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    public String getRecordId() { 
        return recordId; 
    }
    
    public Patient getPatient() { 
        return patient; 
    }
    
    public LocalDate getRecordDate() { 
        return recordDate; 
    }
    
    public String getDiagnosis() { 
        return diagnosis; 
    }
    
    public String getTreatment() { 
        return treatment; 
    }
    
    public String getDoctorName() { 
        return doctorName; 
    }
    
    public ArrayList<String> getTestResults() { 
        return testResults; 
    }
    
    public String getNotes() { 
        return notes; 
    }
}
