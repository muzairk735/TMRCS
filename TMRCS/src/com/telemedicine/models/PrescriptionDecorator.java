package com.telemedicine.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PrescriptionDecorator extends Prescription {
    protected LocalDateTime decoratedAt;

    public PrescriptionDecorator(Prescription prescription) {
        super(
            prescription.getPrescriptionId(),
            prescription.getPatient(),
            prescription.getDoctor(),
            prescription.getDiagnosis(),
            prescription.getMedicines(),
            prescription.getAdditionalNotes()
        );
        this.decoratedAt = LocalDateTime.now();
    }

    @Override
    public void addMedicine(Medicine medicine) {
        super.addMedicine(medicine);
    }

    @Override
    public void displayPrescription() {
        super.displayPrescription();
        System.out.println("  [Enhanced Prescription - Decorated at: " + decoratedAt + "]");
    }

    @Override
    public void generatePrescriptionReport() {
        super.generatePrescriptionReport();
        System.out.println("  [Enhanced Report - Decorated at: " + decoratedAt + "]");
    }

    public LocalDateTime getDecoratedAt() { return decoratedAt; }
}

class UrgentPrescriptionDecorator extends PrescriptionDecorator {
    private String urgencyLevel;
    private String reason;

    public UrgentPrescriptionDecorator(Prescription prescription, String reason) {
        super(prescription);
        this.urgencyLevel = "HIGH";
        this.reason = reason;
    }

    @Override
    public void displayPrescription() {
        System.out.println("\n⚠ URGENT PRESCRIPTION ⚠");
        super.displayPrescription();
        System.out.println("  Urgency Level: " + urgencyLevel);
        System.out.println("  Reason: " + reason);
        System.out.println("═══════════════════════════════════════════\n");
    }

    public String getUrgencyLevel() { return urgencyLevel; }
    public String getReason() { return reason; }
}

class SignedPrescriptionDecorator extends PrescriptionDecorator {
    private String digitalSignature;
    private LocalDateTime signedAt;

    public SignedPrescriptionDecorator(Prescription prescription) {
        super(prescription);
        this.signedAt = LocalDateTime.now();
    }

    public void sign(String signature) {
        this.digitalSignature = signature;
        System.out.println("✓ Prescription digitally signed at: " + signedAt);
    }

    public String getDigitalSignature() { return digitalSignature; }
    public LocalDateTime getSignedAt() { return signedAt; }

    @Override
    public void displayPrescription() {
        super.displayPrescription();
        if (digitalSignature != null) {
            System.out.println("  [Digitally Signed: " + digitalSignature + "]");
        }
    }
}