package com.telemedicine.ui;

import com.telemedicine.app.AppContext;
import com.telemedicine.models.*;

/**
 * Handles every admin-facing screen in the console UI.
 *
 * <p>This class owns the following user flows:</p>
 * <ul>
 *   <li>{@link #login()} — authenticates an admin and opens the dashboard</li>
 *   <li>{@link #dashboard()} — the admin menu loop (private, entered via login)</li>
 *   <li>Doctor/patient listing, addition, and removal screens (all private)</li>
 * </ul>
 *
 * <p>The actual add/remove operations are delegated to {@link Admin#addDoctor},
 * {@link Admin#removeDoctor}, and {@link Admin#removePatient}. This handler
 * is only responsible for collecting input and confirming destructive actions
 * before calling those methods.</p>
 */
public class AdminMenuHandler {

    /** Shared application state — collections, scanner, and session info. */
    private final AppContext ctx;

    /**
     * Constructs the handler with access to the shared application context.
     *
     * @param ctx the single {@link AppContext} instance created at startup
     */
    public AdminMenuHandler(AppContext ctx) {
        this.ctx = ctx;
    }

    // -------------------------------------------------------------------------
    // Public entry point (called from TelemedicineSystem)
    // -------------------------------------------------------------------------

    /**
     * Displays the admin login screen, validates credentials against
     * {@code ctx.admins}, sets the session if successful, and enters the
     * admin dashboard loop. Returns immediately on failure.
     */
    public void login() {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ADMIN LOGIN                    ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.print("Email: ");
        String email = ctx.scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = ctx.scanner.nextLine().trim();

        for (Admin a : ctx.admins) {
            if (a.login(email, password)) {
                ctx.currentUser     = a;
                ctx.currentUserType = "ADMIN";
                System.out.println("\n✓ Login successful! Welcome, Admin");
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
     * Admin dashboard menu loop. Keeps running while the session type is
     * {@code "ADMIN"}; exits when the admin logs out (case 9) or the
     * session is cleared externally.
     */
    private void dashboard() {
        Admin admin = (Admin) ctx.currentUser;
        while ("ADMIN".equals(ctx.currentUserType)) {
            try {
                UIHelper.clearScreen();
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║       ADMIN DASHBOARD                  ║");
                System.out.println("╠════════════════════════════════════════╣");
                System.out.println("║  1. View My Profile                    ║");
                System.out.println("║  2. View All Appointments              ║");
                System.out.println("║  3. Generate System Report             ║");
                System.out.println("║  4. View All Doctors                   ║");
                System.out.println("║  5. View All Patients                  ║");
                System.out.println("║  6. Add Doctor                         ║");
                System.out.println("║  7. Remove Doctor                      ║");
                System.out.println("║  8. Remove Patient                     ║");
                System.out.println("║  9. Logout                             ║");
                System.out.println("╚════════════════════════════════════════╝\n");

                int choice = UIHelper.getMenuInput("Enter your choice: ", ctx.scanner);
                switch (choice) {
                    case 1:
                        admin.displayProfile();
                        UIHelper.pauseScreen(ctx.scanner);
                        break;
                    case 2:
                        admin.viewAllAppointments(ctx.appointments);
                        UIHelper.pauseScreen(ctx.scanner);
                        break;
                    case 3:
                        // Passes all three collections so the report can compute cross-list stats
                        admin.generateReport(ctx.doctors, ctx.patients, ctx.appointments);
                        UIHelper.pauseScreen(ctx.scanner);
                        break;
                    case 4:
                        viewAllDoctors();
                        UIHelper.pauseScreen(ctx.scanner);
                        break;
                    case 5:
                        viewAllPatients();
                        UIHelper.pauseScreen(ctx.scanner);
                        break;
                    case 6:
                        addDoctor(admin);
                        break;
                    case 7:
                        removeDoctor(admin);
                        break;
                    case 8:
                        removePatient(admin);
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
    // Private screen methods (one per dashboard menu option)
    // -------------------------------------------------------------------------

    /**
     * Collects all required doctor details, constructs a {@link Doctor} object,
     * and adds it to {@code ctx.doctors} via {@link Admin#addDoctor}.
     * IDs are assigned sequentially starting at D1001.
     * Aborts and notifies the user if any input cannot be parsed.
     *
     * @param admin the currently logged-in admin
     */
    private void addDoctor(Admin admin) {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ADD NEW DOCTOR                 ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Auto-generate sequential ID: D1001, D1002, ...
        String userId = "D" + (ctx.doctors.size() + 1001);

        System.out.print("Name: ");              String name  = ctx.scanner.nextLine().trim();
        System.out.print("Email: ");             String email = ctx.scanner.nextLine().trim();
        System.out.print("Phone: ");             String phone = ctx.scanner.nextLine().trim();
        System.out.print("Password: ");          String pass  = ctx.scanner.nextLine().trim();
        System.out.print("Specialization: ");    String spec  = ctx.scanner.nextLine().trim();
        System.out.print("License Number: ");    String lic   = ctx.scanner.nextLine().trim();

        System.out.print("Years of Experience: ");
        Integer exp = UIHelper.parseIntInput(ctx.scanner);
        if (exp == null) {
            System.out.println("✗ Invalid experience — doctor not added.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }

        System.out.print("Consultation Fee (Rs.): ");
        double fee;
        try {
            fee = Double.parseDouble(ctx.scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid fee — doctor not added.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }

        try {
            Doctor d = new Doctor(userId, name, email, phone, pass, spec, lic, exp, fee);
            admin.addDoctor(ctx.doctors, d);
            System.out.println("  Doctor ID: " + userId);
        } catch (IllegalArgumentException e) {
            // Validation errors from Doctor/Person setters are surfaced here
            System.out.println("✗ Failed to add doctor: " + e.getMessage());
        }
        UIHelper.pauseScreen(ctx.scanner);
    }

    /**
     * Lists every doctor in {@code ctx.doctors} with their ID, name,
     * specialization, and years of experience.
     */
    private void viewAllDoctors() {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ALL DOCTORS                    ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        if (ctx.doctors.isEmpty()) { System.out.println("✗ No doctors registered."); return; }
        for (int i = 0; i < ctx.doctors.size(); i++) {
            Doctor d = ctx.doctors.get(i);
            System.out.println((i + 1) + ". Dr. " + d.getName());
            System.out.println("   ID: "             + d.getUserId());
            System.out.println("   Specialization: " + d.getSpecialization());
            System.out.println("   Experience: "     + d.getExperienceYears() + " years\n");
        }
    }

    /**
     * Lists every patient in {@code ctx.patients} with their ID, name,
     * email, and total appointment count.
     */
    private void viewAllPatients() {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ALL PATIENTS                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        if (ctx.patients.isEmpty()) { System.out.println("✗ No patients registered."); return; }
        for (int i = 0; i < ctx.patients.size(); i++) {
            Patient p = ctx.patients.get(i);
            System.out.println((i + 1) + ". " + p.getName());
            System.out.println("   ID: "           + p.getUserId());
            System.out.println("   Email: "        + p.getEmail());
            System.out.println("   Appointments: " + p.getAppointments().size() + "\n");
        }
    }

    /**
     * Lists all doctors, prompts the admin to select one by number, asks for
     * confirmation, then delegates removal to {@link Admin#removeDoctor}.
     * Cancels without changes if the admin types anything other than "yes".
     *
     * @param admin the currently logged-in admin
     */
    private void removeDoctor(Admin admin) {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      REMOVE DOCTOR                     ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        if (ctx.doctors.isEmpty()) {
            System.out.println("✗ No doctors to remove.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }
        for (int i = 0; i < ctx.doctors.size(); i++)
            System.out.println((i + 1) + ". Dr. " + ctx.doctors.get(i).getName() +
                    " (ID: " + ctx.doctors.get(i).getUserId() + ")");

        int choice = UIHelper.getMenuInput("\nSelect doctor (1-" + ctx.doctors.size() + ") or 0 to cancel: ", ctx.scanner);
        if (choice == 0) { System.out.println("Cancelled."); UIHelper.pauseScreen(ctx.scanner); return; }
        if (choice < 1 || choice > ctx.doctors.size()) {
            System.out.println("✗ Invalid selection.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }

        Doctor d = ctx.doctors.get(choice - 1);
        System.out.print("\nRemove Dr. " + d.getName() + "? (yes/no): ");
        if (ctx.scanner.nextLine().trim().equalsIgnoreCase("yes"))
            admin.removeDoctor(ctx.doctors, d.getUserId());
        else
            System.out.println("Cancelled.");
        UIHelper.pauseScreen(ctx.scanner);
    }

    /**
     * Lists all patients, prompts the admin to select one by number, asks for
     * confirmation, then delegates removal to {@link Admin#removePatient}.
     * Cancels without changes if the admin types anything other than "yes".
     *
     * @param admin the currently logged-in admin
     */
    private void removePatient(Admin admin) {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      REMOVE PATIENT                    ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        if (ctx.patients.isEmpty()) {
            System.out.println("✗ No patients to remove.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }
        for (int i = 0; i < ctx.patients.size(); i++)
            System.out.println((i + 1) + ". " + ctx.patients.get(i).getName() +
                    " (ID: " + ctx.patients.get(i).getUserId() + ")");

        int choice = UIHelper.getMenuInput("\nSelect patient (1-" + ctx.patients.size() + ") or 0 to cancel: ", ctx.scanner);
        if (choice == 0) { System.out.println("Cancelled."); UIHelper.pauseScreen(ctx.scanner); return; }
        if (choice < 1 || choice > ctx.patients.size()) {
            System.out.println("✗ Invalid selection.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }

        Patient p = ctx.patients.get(choice - 1);
        System.out.print("\nRemove " + p.getName() + "? (yes/no): ");
        if (ctx.scanner.nextLine().trim().equalsIgnoreCase("yes"))
            admin.removePatient(ctx.patients, p.getUserId());
        else
            System.out.println("Cancelled.");
        UIHelper.pauseScreen(ctx.scanner);
    }
}
