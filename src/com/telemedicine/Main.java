package com.telemedicine;

/**
 * Entry point for the Telemedicine & Remote Consultation System.
 * 
 * @author Telemedicine Team
 * @version 1.0
 * @since 2026-04-06
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   TELEMEDICINE & REMOTE CONSULTATION   ║");
        System.out.println("║     SYSTEM - Initializing...           ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        try {
            // Create and start the system
            TelemedicineSystem system = new TelemedicineSystem();
            system.start();
        } catch (Exception e) {
            System.out.println("Error starting system: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
