package com.telemedicine.ui;

import com.telemedicine.app.AppContext;
import com.telemedicine.models.*;
import java.util.ArrayList;

/**
 * Handles every doctor-facing screen in the console UI.
 *
 * <p>This class owns the following user flows:</p>
 * <ul>
 *   <li>{@link #login()} — authenticates a doctor and opens the dashboard</li>
 *   <li>{@link #dashboard()} — the doctor menu loop (private, entered via login)</li>
 *   <li>{@link #conductConsultation(Doctor)} — selects an appointment and starts
 *       the consultation session (private, entered from the dashboard)</li>
 * </ul>
 *
 * <p>Prescription issuance and availability management are delegated to
 * methods on {@link Doctor} itself; this handler only collects user input
 * and routes it to the right model method.</p>
 */
public class DoctorMenuHandler {

    /** Shared application state — collections, scanner, and session info. */
    private final AppContext ctx;

    /**
     * Constructs the handler with access to the shared application context.
     *
     * @param ctx the single {@link AppContext} instance created at startup
     */
    public DoctorMenuHandler(AppContext ctx) {
        this.ctx = ctx;
    }

    // -------------------------------------------------------------------------
    // Public entry point (called from TelemedicineSystem)
    // -------------------------------------------------------------------------

    /**
     * Displays the doctor login screen, validates credentials against
     * {@code ctx.doctors}, sets the session if successful, and enters the
     * doctor dashboard loop. Returns immediately on failure.
     */
    public void login() {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         DOCTOR LOGIN                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.print("Email: ");
        String email = ctx.scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = ctx.scanner.nextLine().trim();

        for (Doctor d : ctx.doctors) {
            if (d.login(email, password)) {
                ctx.currentUser     = d;
                ctx.currentUserType = "DOCTOR";
                System.out.println("\n✓ Login successful! Welcome, Dr. " + d.getName());
                UIHelper.pauseScreen(ctx.scanner);
                dashboard(); // enters the menu loop for this session
                return;
            }
        }
        System.out.println("\n✗ Invalid credentials.");
        UIHelper.pauseScreen(ctx.scanner);
    }

    // -------------------------------------------------------------------------
    // Dashboard loop (private — only entered after a successful login)
    // -------------------------------------------------------------------------

    /**
     * Doctor dashboard menu loop. Keeps running while the session type is
     * {@code "DOCTOR"}; exits when the doctor logs out (case 9) or the
     * session is cleared externally.
     */
    private void dashboard() {
        Doctor doctor = (Doctor) ctx.currentUser;
        while ("DOCTOR".equals(ctx.currentUserType)) {
            try {
                UIHelper.clearScreen();
                String welcome = "  Welcome, Dr. " + UIHelper.truncate(doctor.getName(), 25);
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║       DOCTOR DASHBOARD                 ║");
                System.out.println("║" + Person.padRight(welcome, 40) + "║");
                System.out.println("╠════════════════════════════════════════╣");
                System.out.println("║  1. View Profile                       ║");
                System.out.println("║  2. Set Availability                   ║");
                System.out.println("║  3. View All Appointments              ║");
                System.out.println("║  4. View Pending Appointments          ║");
                System.out.println("║  5. View Confirmed Appointments        ║");
                System.out.println("║  6. Conduct Consultation / Open Chat   ║");
                System.out.println("║  7. Issue Prescription                 ║");
                System.out.println("║  8. View My Issued Prescriptions       ║");
                System.out.println("║  9. Logout                             ║");
                System.out.println("╚════════════════════════════════════════╝\n");

                int choice = UIHelper.getMenuInput("Enter your choice: ", ctx.scanner);
                switch (choice) {
                    case 1:
                        doctor.displayProfile();
                        UIHelper.pauseScreen(ctx.scanner);
                        break;
                    case 2:
                        // clearScreen here so the availability prompt has a clean console
                        UIHelper.clearScreen();
                        doctor.promptSetAvailability(ctx.scanner);
                        UIHelper.pauseScreen(ctx.scanner);
                        break;
                    case 3:
                        doctor.viewAppointments("ALL");
                        UIHelper.pauseScreen(ctx.scanner);
                        break;
                    case 4:
                        doctor.viewAppointments("PENDING");
                        UIHelper.pauseScreen(ctx.scanner);
                        break;
                    case 5:
                        doctor.viewAppointments("CONFIRMED");
                        UIHelper.pauseScreen(ctx.scanner);
                        break;
                    case 6:
                        conductConsultation(doctor);
                        break;
                    case 7:
                        UIHelper.clearScreen();
                        System.out.println("\n╔════════════════════════════════════════╗");
                        System.out.println("║      ISSUE PRESCRIPTION                ║");
                        System.out.println("╚════════════════════════════════════════╝\n");
                        // ctx.patients passed so the doctor can search by patient name
                        doctor.promptIssuePrescription(ctx.patients, ctx.scanner);
                        UIHelper.pauseScreen(ctx.scanner);
                        break;
                    case 8:
                        doctor.viewIssuedPrescriptions();
                        UIHelper.pauseScreen(ctx.scanner);
                        break;
                    case 9:
                        ctx.clearSession();
                        System.out.println("\n✓ Logged out successfully.");
                        UIHelper.pauseScreen(ctx.scanner);
                        return; // exits the dashboard loop
                    default:
                        System.out.println("✗ Invalid choice.");
                        UIHelper.pauseScreen(ctx.scanner);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                ctx.scanner.nextLine();
                UIHelper.pauseScreen(ctx.scanner);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Private screen methods
    // -------------------------------------------------------------------------

    /**
     * Shows PENDING and CONFIRMED appointments, prompts the doctor to enter
     * an appointment ID, and starts the consultation session via
     * {@link Doctor#runConsultationSession(Appointment, java.util.Scanner)}.
     *
     * @param doctor the currently logged-in doctor
     */
    private void conductConsultation(Doctor doctor) {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      CONDUCT CONSULTATION              ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Collect appointments the doctor can currently act on
        ArrayList<Appointment> pending = new ArrayList<>();
        for (Appointment a : doctor.getAppointments()) {
            if (a.getStatus().equals("PENDING") || a.getStatus().equals("CONFIRMED"))
                pending.add(a);
        }
        if (pending.isEmpty()) {
            System.out.println("✗ No pending appointments.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }
        for (int i = 0; i < pending.size(); i++) {
            Appointment a = pending.get(i);
            System.out.println((i + 1) + ". " + a.getPatient().getName() + " | ID: " + a.getAppointmentId());
        }

        // Look up the chosen appointment by ID in the doctor's full list
        System.out.print("\nEnter Appointment ID: ");
        String id = ctx.scanner.nextLine().trim();
        for (Appointment a : doctor.getAppointments()) {
            if (a.getAppointmentId().equals(id)) {
                doctor.runConsultationSession(a, ctx.scanner);
                return;
            }
        }
        System.out.println("✗ Appointment not found.");
        UIHelper.pauseScreen(ctx.scanner);
    }
}
