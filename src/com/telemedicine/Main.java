package com.telemedicine;

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
