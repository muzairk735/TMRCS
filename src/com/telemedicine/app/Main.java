package com.telemedicine.app;

/*
 Entry point for the Telemedicine & Remote Consultation System.

 Responsibilities:
   - Prints the startup banner
   - Catches any unexpected top-level exceptions so the JVM exits cleanly

 Nothing else belongs here; all application logic lives in
*/
public class Main {

    /**
     * JVM entry point. Creates the system and starts the main menu loop.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Startup banner — shown before any data is loaded
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   TELEMEDICINE & REMOTE CONSULTATION   ║");
        System.out.println("║     SYSTEM - Initializing...           ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        try {
            // Wire up the system and hand off control
            TelemedicineSystem system = new TelemedicineSystem();
            system.start();
        } catch (Exception e) {
            // Last-resort handler — normal errors are caught inside start()
            System.out.println("Fatal error starting system: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
