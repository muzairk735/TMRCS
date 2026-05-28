package com.telemedicine.app;

import com.telemedicine.utils.DataManager;
import com.telemedicine.ui.*;
import com.telemedicine.utils.FileHandler;

/**
 * Top-level application controller for the Telemedicine System.
 *
 * <p>This class has exactly three responsibilities:</p>
 * <ol>
 *   <li><strong>Wiring</strong> — creates the shared {@link AppContext} and
 *       instantiates each specialised handler ({@link PatientMenuHandler},
 *       {@link DoctorMenuHandler}, {@link AdminMenuHandler}, {@link DataManager}).</li>
 *   <li><strong>Bootstrapping</strong> — loads persisted data via {@link DataManager},
 *       seeding sample data if the system is run for the first time.</li>
 *   <li><strong>Main loop</strong> — runs the top-level menu and dispatches each
 *       choice to the appropriate handler. All screen rendering happens inside
 *       the handlers, not here.</li>
 * </ol>
 *
 * <p>No business or UI logic belongs in this class. If a new top-level menu
 * option is needed, add a handler class and add one {@code case} line here.</p>
 */
public class TelemedicineSystem {

    // -------------------------------------------------------------------------
    // Collaborators (wired in constructor, immutable after that)
    // -------------------------------------------------------------------------

    /** Shared state passed by reference into every handler. */
    private final AppContext         ctx;

    /** Handles file I/O, sample-data seeding, and the reset flow. */
    private final DataManager        dataManager;

    /** Handles all patient-facing screens. */
    private final PatientMenuHandler patientMenu;

    /** Handles all doctor-facing screens. */
    private final DoctorMenuHandler  doctorMenu;

    /** Handles all admin-facing screens. */
    private final AdminMenuHandler   adminMenu;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Wires all handlers around a single {@link AppContext}, loads saved data,
     * and seeds sample doctors/patients/admin on a fresh install.
     */
    public TelemedicineSystem() {
        ctx         = new AppContext();
        dataManager = new DataManager(ctx, new FileHandler());
        patientMenu = new PatientMenuHandler(ctx);
        doctorMenu  = new DoctorMenuHandler(ctx);
        adminMenu   = new AdminMenuHandler(ctx);

        // Load any previously saved data; seed defaults if nothing is on disk
        dataManager.loadAll();
        if (ctx.doctors.isEmpty()) dataManager.addSampleData();
    }

    // Main loop

    /*
     Runs the top-level menu loop until the user selects Exit (option 6).
     Each iteration shows the main menu, reads a choice, and delegates to
     the relevant handler. Unexpected exceptions are caught and reported
     without crashing the loop.
    */
    public void start() {
        while (true) {
            try {
                displayMainMenu();
                int choice = UIHelper.getMenuInput("Enter your choice: ", ctx.scanner);
                switch (choice) {
                    case 1: patientMenu.login();           break; // Patient login → patient dashboard
                    case 2: doctorMenu.login();            break; // Doctor login  → doctor dashboard
                    case 3: adminMenu.login();             break; // Admin login   → admin dashboard
                    case 4: patientMenu.register();        break; // New patient registration form
                    case 5: dataManager.resetSampleData(); break; // Wipe and reload demo dataset
                    case 6:
                        dataManager.saveAll();
                        System.out.println("\n✓ Thank you for using Telemedicine System!");
                        System.exit(0);
                    default:
                        System.out.println("✗ Invalid choice. Try again.");
                        UIHelper.pauseScreen(ctx.scanner);
                }
            } catch (Exception e) {
                // Catch-all so a single bad input never terminates the program
                System.out.println("Error: " + e.getMessage());
                ctx.scanner.nextLine();
            }
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Renders the top-level menu banner.
     * Kept separate from {@link #start()} so the loop body stays readable.
     */
    private void displayMainMenu() {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  TELEMEDICINE & REMOTE CONSULTATION    ║");
        System.out.println("║              SYSTEM                    ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Patient Login                      ║");
        System.out.println("║  2. Doctor Login                       ║");
        System.out.println("║  3. Admin Login                        ║");
        System.out.println("║  4. New Patient Registration           ║");
        System.out.println("║  5. Reset and View Sample Data         ║");
        System.out.println("║  6. Exit                               ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}
