package com.telemedicine.utils;

import com.telemedicine.app.AppContext;
import com.telemedicine.models.*;
import com.telemedicine.ui.UIHelper;
import com.telemedicine.utils.FileHandler;
import java.time.*;

/**
 * Manages all data persistence and sample-data initialization for the system.
 *
 * <p>This class has two distinct responsibilities:</p>
 * <ol>
 *   <li><strong>Persistence</strong> — {@link #saveAll()} and {@link #loadAll()}
 *       delegate to {@link FileHandler} to serialise/deserialise the four main
 *       collections (patients, doctors, appointments, admins) to and from disk.</li>
 *   <li><strong>Seeding</strong> — {@link #addSampleData()} populates the context
 *       with three doctors, one admin, and one patient for demo/testing purposes.
 *       {@link #resetSampleData()} wraps this with a confirmation prompt and a
 *       credential printout so testers know how to log in.</li>
 * </ol>
 *
 * <p>All data is written into and read from the shared {@link AppContext}; this
 * class never owns its own copies of the collections.</p>
 */
public class DataManager {

    /** Shared application state — the collections this manager reads and writes. */
    private final AppContext  ctx;

    /** Low-level file I/O helper that handles serialization details. */
    private final FileHandler fileHandler;

    /**
     * Constructs the manager with the shared context and file handler.
     *
     * @param ctx         the single {@link AppContext} instance created at startup
     * @param fileHandler the {@link FileHandler} used to read/write disk files
     */
    public DataManager(AppContext ctx, FileHandler fileHandler) {
        this.ctx         = ctx;
        this.fileHandler = fileHandler;
    }

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------

    /**
     * Saves all four collections to disk via {@link FileHandler}.
     * Called on clean exit (option 6 in the main menu) and after a data reset.
     */
    public void saveAll() {
        System.out.println("\nSaving data...");
        fileHandler.savePatients(ctx.patients);
        fileHandler.saveDoctors(ctx.doctors);
        fileHandler.saveAppointments(ctx.appointments);
        fileHandler.saveAdmins(ctx.admins);
        System.out.println("✓ Data saved successfully.");
    }

    /**
     * Loads all four collections from disk into the shared context.
     * Called once at startup before the main menu is shown.
     * If no saved files exist, {@link FileHandler} returns empty lists,
     * and {@link TelemedicineSystem} will then call {@link #addSampleData()}.
     */
    public void loadAll() {
        ctx.patients     = fileHandler.loadPatients();
        ctx.doctors      = fileHandler.loadDoctors();
        ctx.appointments = fileHandler.loadAppointments();
        ctx.admins       = fileHandler.loadAdmins();
    }

    // -------------------------------------------------------------------------
    // Sample data
    // -------------------------------------------------------------------------

    /**
     * Interactive reset flow: asks the admin to confirm, clears all four
     * collections, calls {@link #addSampleData()}, saves to disk, and
     * prints the sample credentials so testers can log in immediately.
     */
    public void resetSampleData() {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      RESET SAMPLE DATA                 ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.println("This will clear all data and reload fresh sample data.\n");
        System.out.print("Are you sure? (yes/no): ");

        if (ctx.scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            // Wipe all existing data before re-seeding
            ctx.doctors.clear();
            ctx.patients.clear();
            ctx.appointments.clear();
            ctx.admins.clear();
            addSampleData();
            saveAll();
            System.out.println("\n✓ Sample data reloaded successfully!");
            System.out.println("  - 3 sample doctors  - 1 admin  - 1 patient");
        } else {
            System.out.println("\n✗ Cancelled.");
        }

        // Always print credentials so testers know how to log in
        printSampleCredentials();
        UIHelper.pauseScreen(ctx.scanner);
    }

    /**
     * Seeds three doctors (Cardiologist, Dermatologist, Pediatrician),
     * one super_admin, and one patient into the shared context. Each doctor
     * gets 7 days of availability starting tomorrow.
     *
     * <p>Called on first run (when no saved data exists) and after a reset.</p>
     */
    public void addSampleData() {
        Doctor d1 = new Doctor("D001", "Fatima Khan",  "fatima@hospital.com", "0301-1111111", "doc123", "Cardiologist",  "PMC-12345", 10, 2000.0);
        Doctor d2 = new Doctor("D002", "Ali Raza",     "ali@hospital.com",    "0302-2222222", "doc123", "Dermatologist", "PMC-12346",  8, 1500.0);
        Doctor d3 = new Doctor("D003", "Sara Ahmed",   "sara@hospital.com",   "0303-3333333", "doc123", "Pediatrician",  "PMC-12347", 12, 1800.0);

        // Populate a full week of availability so patients can book on any of the next 7 days
        for (int i = 1; i <= 7; i++) {
            d1.setAvailability(LocalDate.now().plusDays(i), LocalTime.of(9,  0), LocalTime.of(17, 0));
            d2.setAvailability(LocalDate.now().plusDays(i), LocalTime.of(10, 0), LocalTime.of(16, 0));
            d3.setAvailability(LocalDate.now().plusDays(i), LocalTime.of(8,  0), LocalTime.of(14, 0));
        }

        ctx.doctors.add(d1);
        ctx.doctors.add(d2);
        ctx.doctors.add(d3);
        ctx.admins.add(new Admin(
                "A001", "Admin User", "admin@system.com", "0300-0000000", "admin123", "super_admin"));
        ctx.patients.add(new Patient(
                "P001", "Ahmed Ali", "ahmed@email.com", "0300-1234567", "pass123", 28, "Male", "B+", "Rawalpindi"));
        System.out.println("✓ Sample data loaded.");
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Prints a formatted block of all sample login credentials.
     * Only called after a successful reset so testers don't have to look
     * up passwords elsewhere.
     */
    private void printSampleCredentials() {
        System.out.println("\n========== SAMPLE DATA LOADED ==========\n");

        System.out.println("---- Doctors ----");
        System.out.println("ID: D001 | Name: Fatima Khan | Email: fatima@hospital.com | Phone: 0301-1111111");
        System.out.println("Specialization: Cardiologist | PMC: PMC-12345 | Experience: 10 years | Fee: Rs. 2000");
        System.out.println("Availability: Next 7 days | 09:00 - 17:00\n");

        System.out.println("ID: D002 | Name: Ali Raza | Email: ali@hospital.com | Phone: 0302-2222222");
        System.out.println("Specialization: Dermatologist | PMC: PMC-12346 | Experience: 8 years | Fee: Rs. 1500");
        System.out.println("Availability: Next 7 days | 10:00 - 16:00\n");

        System.out.println("ID: D003 | Name: Sara Ahmed | Email: sara@hospital.com | Phone: 0303-3333333");
        System.out.println("Specialization: Pediatrician | PMC: PMC-12347 | Experience: 12 years | Fee: Rs. 1800");
        System.out.println("Availability: Next 7 days | 08:00 - 14:00\n");

        System.out.println("---- Admin ----");
        System.out.println("ID: A001 | Name: Admin User | Email: admin@system.com | Phone: 0300-0000000");
        System.out.println("Role: super_admin\n");

        System.out.println("---- Patient ----");
        System.out.println("ID: P001 | Name: Ahmed Ali | Email: ahmed@email.com | Phone: 0300-1234567");
        System.out.println("Age: 28 | Gender: Male | Blood Group: B+ | Address: Rawalpindi\n");

        System.out.println("========================================");
    }
}
