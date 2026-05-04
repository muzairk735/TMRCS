package com.telemedicine;

/**
 * Entry point for the telemedicine application.
 * Creates the main system object and starts the program flow.
 */
public class Main {
    /** Starts the console-based system. */
    public static void main(String[] args) {
        // Startup banner
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   TELEMEDICINE & REMOTE CONSULTATION   ║");
        System.out.println("║     SYSTEM - Initializing...           ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        try {
            // Main controller object
            TelemedicineSystem system = new TelemedicineSystem();
            system.start();
        } catch (Exception e) {
            // Fallback error output
            System.out.println("Error starting system: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
