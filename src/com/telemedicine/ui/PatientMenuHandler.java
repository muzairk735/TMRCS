package com.telemedicine.ui;

import com.telemedicine.app.AppContext;
import com.telemedicine.models.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Handles every patient-facing screen in the console UI.
 *
 * <p>This class owns the following user flows:</p>
 * <ul>
 *   <li>{@link #login()} — authenticates a patient and opens the dashboard</li>
 *   <li>{@link #register()} — collects details and creates a new {@link Patient}</li>
 *   <li>{@link #dashboard()} — the patient menu loop (private, entered via login)</li>
 *   <li>Profile update, doctor search, appointment booking/cancellation,
 *       consultation chat, and medical history screens (all private)</li>
 * </ul>
 *
 * <p>All shared state (collections, scanner, session) is accessed through the
 * injected {@link AppContext}. This handler never constructs or owns data —
 * it only reads from and writes into the context.</p>
 */
public class PatientMenuHandler {

    /** Shared application state — collections, scanner, and session info. */
    private final AppContext ctx;

    /**
     * Constructs the handler with access to the shared application context.
     *
     * @param ctx the single {@link AppContext} instance created at startup
     */
    public PatientMenuHandler(AppContext ctx) {
        this.ctx = ctx;
    }

    // -------------------------------------------------------------------------
    // Public entry points (called from TelemedicineSystem)
    // -------------------------------------------------------------------------

    /**
     * Displays the patient login screen, validates credentials against
     * {@code ctx.patients}, sets the session if successful, and enters
     * the patient dashboard loop. Returns immediately on failure.
     */
    public void login() {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         PATIENT LOGIN                  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.print("Email: ");
        String email = ctx.scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = ctx.scanner.nextLine().trim();

        for (Patient p : ctx.patients) {
            if (p.login(email, password)) {
                ctx.currentUser     = p;
                ctx.currentUserType = "PATIENT";
                System.out.println("\n✓ Login successful! Welcome, " + p.getName());
                UIHelper.pauseScreen(ctx.scanner);
                dashboard(); // enters the menu loop for this session
                return;
            }
        }
        System.out.println("\n✗ Invalid credentials.");
        UIHelper.pauseScreen(ctx.scanner);
    }

    /**
     * Displays the patient registration form, collects and validates all
     * required fields, and adds the new {@link Patient} to {@code ctx.patients}.
     * Assigns IDs sequentially starting at P1001.
     */
    public void register() {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      PATIENT REGISTRATION              ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Auto-generate sequential ID: P1001, P1002, ...
        String userId = "P" + (ctx.patients.size() + 1001);

        System.out.print("Name: ");          String name      = ctx.scanner.nextLine().trim();
        System.out.print("Email: ");         String email     = ctx.scanner.nextLine().trim();
        System.out.print("Phone: ");         String phone     = ctx.scanner.nextLine().trim();
        System.out.print("Password: ");      String password  = ctx.scanner.nextLine().trim();
        System.out.print("Age: ");
        Integer age = UIHelper.parseIntInput(ctx.scanner);
        if (age == null) {
            System.out.println("✗ Invalid age — registration cancelled.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }
        System.out.print("Gender (Male/Female): "); String gender     = ctx.scanner.nextLine().trim();
        System.out.print("Blood Group: ");           String bloodGroup = ctx.scanner.nextLine().trim();
        System.out.print("Address: ");               String address    = ctx.scanner.nextLine().trim();

        try {
            Patient p = new Patient(userId, name, email, phone, password, age, gender, bloodGroup, address);
            ctx.patients.add(p);
            System.out.println("\n✓ Patient registered successfully!");
            System.out.println("  Your Patient ID: " + userId);
        } catch (IllegalArgumentException e) {
            // Validation errors from Patient/Person setters are surfaced here
            System.out.println("\n✗ Registration failed: " + e.getMessage());
        }
        UIHelper.pauseScreen(ctx.scanner);
    }

    // -------------------------------------------------------------------------
    // Dashboard loop (private — only entered after a successful login)
    // -------------------------------------------------------------------------

    /**
     * Patient dashboard menu loop. Keeps running while the session type is
     * {@code "PATIENT"}; exits when the patient logs out (case 10) or the
     * session is cleared externally.
     */
    private void dashboard() {
        Patient patient = (Patient) ctx.currentUser;
        while ("PATIENT".equals(ctx.currentUserType)) {
            try {
                UIHelper.clearScreen();
                String welcome = "  Welcome, " + UIHelper.truncate(patient.getName(), 29);
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║       PATIENT DASHBOARD                ║");
                System.out.println("║" + Person.padRight(welcome, 40) + "║");
                System.out.println("╠════════════════════════════════════════╣");
                System.out.println("║  1. View Profile                       ║");
                System.out.println("║  2. Update Profile                     ║");
                System.out.println("║  3. Search Doctors                     ║");
                System.out.println("║  4. Book Appointment                   ║");
                System.out.println("║  5. View My Appointments               ║");
                System.out.println("║  6. Cancel Appointment                 ║");
                System.out.println("║  7. View My Prescriptions              ║");
                System.out.println("║  8. View Consultation Chat             ║");
                System.out.println("║  9. View Medical History               ║");
                System.out.println("║  10. Logout                            ║");
                System.out.println("╚════════════════════════════════════════╝\n");

                int choice = UIHelper.getMenuInput("Enter your choice: ", ctx.scanner);
                switch (choice) {
                    case 1:  patient.displayProfile();         UIHelper.pauseScreen(ctx.scanner); break;
                    case 2:  updateProfile(patient);           break;
                    case 3:  searchDoctors();                  UIHelper.pauseScreen(ctx.scanner); break;
                    case 4:  bookAppointment(patient);         break;
                    case 5:  patient.viewAppointments();       UIHelper.pauseScreen(ctx.scanner); break;
                    case 6:  cancelAppointment(patient);       break;
                    case 7:  patient.viewPrescriptions();      UIHelper.pauseScreen(ctx.scanner); break;
                    case 8:  viewConsultationChat(patient);    break;
                    case 9:  patient.viewMedicalHistory();     UIHelper.pauseScreen(ctx.scanner); break;
                    case 10:
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
     * Sub-menu that lets a patient update one field at a time.
     * Validation is enforced by the respective Person/Patient setters;
     * any {@link IllegalArgumentException} is caught and shown to the user.
     *
     * @param patient the currently logged-in patient
     */
    private void updateProfile(Patient patient) {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      UPDATE PATIENT PROFILE            ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Update Name                        ║");
        System.out.println("║  2. Update Email                       ║");
        System.out.println("║  3. Update Phone                       ║");
        System.out.println("║  4. Update Address                     ║");
        System.out.println("║  5. Cancel                             ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        int choice = UIHelper.getMenuInput("Select option: ", ctx.scanner);
        String field = null; // tracks which field was updated for the success message
        try {
            switch (choice) {
                case 1: System.out.print("New name: ");    patient.setName(ctx.scanner.nextLine().trim());        field = "Name";    break;
                case 2: System.out.print("New email: ");   patient.setEmail(ctx.scanner.nextLine().trim());       field = "Email";   break;
                case 3: System.out.print("New phone: ");   patient.setPhoneNumber(ctx.scanner.nextLine().trim()); field = "Phone";   break;
                case 4: System.out.print("New address: "); patient.setAddress(ctx.scanner.nextLine().trim());     field = "Address"; break;
                case 5: System.out.println("Cancelled."); break;
                default: System.out.println("✗ Invalid choice.");
            }
            if (field != null) System.out.println("✓ " + field + " updated.");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ " + e.getMessage());
        }
        UIHelper.pauseScreen(ctx.scanner);
    }

    /**
     * Filters {@code ctx.doctors} by specialization keyword (case-insensitive).
     * Pressing Enter without typing a keyword returns all doctors.
     */
    private void searchDoctors() {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         SEARCH DOCTORS                 ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.print("Search by specialization (or Enter for all): ");
        String term = ctx.scanner.nextLine().trim().toLowerCase();

        ArrayList<Doctor> results = new ArrayList<>();
        for (Doctor d : ctx.doctors) {
            if (term.isEmpty() || d.getSpecialization().toLowerCase().contains(term))
                results.add(d);
        }
        if (results.isEmpty()) { System.out.println("✗ No doctors found."); return; }

        System.out.println("\nFound " + results.size() + " doctor(s):\n");
        for (int i = 0; i < results.size(); i++) {
            Doctor d = results.get(i);
            System.out.println((i + 1) + ". Dr. " + d.getName());
            System.out.println("   Specialization: " + d.getSpecialization());
            System.out.println("   Experience: "     + d.getExperienceYears() + " years");
            System.out.println("   Fee: Rs. "        + d.getConsultationFee());
            if (d.getTotalRatings() > 0) System.out.printf("   Rating: %.1f/5.0%n", d.getRating());
            System.out.println();
        }
    }

    /**
     * Multi-step appointment booking wizard:
     * <ol>
     *   <li>Patient selects a doctor from the list.</li>
     *   <li>Patient enters a future date (DD-MM-YYYY).</li>
     *   <li>Available time slots for that date are shown; patient picks one.</li>
     *   <li>Patient describes symptoms and chooses a consultation mode.</li>
     *   <li>Appointment is created via {@link Patient#bookAppointment} and
     *       the chosen slot is marked unavailable to prevent double-booking.</li>
     * </ol>
     *
     * @param patient the currently logged-in patient
     */
    private void bookAppointment(Patient patient) {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        BOOK APPOINTMENT                ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        if (ctx.doctors.isEmpty()) {
            System.out.println("✗ No doctors available.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }

        // Step 1 — doctor selection
        for (int i = 0; i < ctx.doctors.size(); i++) {
            Doctor d = ctx.doctors.get(i);
            System.out.println((i + 1) + ". Dr. " + d.getName() +
                    " - " + d.getSpecialization() + " (Rs. " + d.getConsultationFee() + ")");
        }
        int dc = UIHelper.getMenuInput("\nSelect doctor (1-" + ctx.doctors.size() + "): ", ctx.scanner);
        if (dc < 1 || dc > ctx.doctors.size()) {
            System.out.println("✗ Invalid selection.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }
        Doctor selected = ctx.doctors.get(dc - 1);

        // Step 2 — date selection (must be today or in the future)
        System.out.print("\nEnter date (DD-MM-YYYY): ");
        LocalDate date;
        try {
            date = LocalDate.parse(ctx.scanner.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            if (date.isBefore(LocalDate.now())) {
                System.out.println("✗ Cannot book in the past.");
                UIHelper.pauseScreen(ctx.scanner);
                return;
            }
        } catch (Exception e) {
            System.out.println("✗ Invalid date format.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }

        // Step 3 — time slot selection (only shows slots still marked available)
        ArrayList<TimeSlot> slots = selected.getAvailableSlots(date);
        if (slots.isEmpty()) {
            System.out.println("\n✗ No available slots for this date.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }
        System.out.println("\nAvailable Time Slots:");
        for (int i = 0; i < slots.size(); i++)
            System.out.println((i + 1) + ". " + slots.get(i).getStartTime() + " - " + slots.get(i).getEndTime());
        int sc = UIHelper.getMenuInput("\nSelect time slot: ", ctx.scanner);
        if (sc < 1 || sc > slots.size()) {
            System.out.println("✗ Invalid selection.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }
        TimeSlot slot = slots.get(sc - 1);

        // Step 4 — symptoms and consultation mode
        System.out.print("Describe your symptoms: ");
        String symptoms = ctx.scanner.nextLine().trim();
        System.out.println("\nConsultation Mode:\n1. Video Call\n2. Phone Call\n3. Chat");
        int mode = UIHelper.getMenuInput("Select mode: ", ctx.scanner);
        String modeStr = mode == 2 ? "PHONE" : mode == 3 ? "CHAT" : "VIDEO";

        // Step 5 — create appointment and lock the slot
        patient.bookAppointment(selected, LocalDateTime.of(date, slot.getStartTime()), symptoms, modeStr);
        slot.setAvailable(false); // prevent double-booking on this slot

        // Keep the master appointments list in sync with the patient's own list
        ctx.appointments.add(patient.getAppointments().get(patient.getAppointments().size() - 1));
        UIHelper.pauseScreen(ctx.scanner);
    }

    /**
     * Lists the patient's appointments and prompts for an ID to cancel.
     * Cancellation logic (including prescription cleanup) is delegated to
     * {@link Patient#cancelAppointment(String)}.
     *
     * @param patient the currently logged-in patient
     */
    private void cancelAppointment(Patient patient) {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      CANCEL APPOINTMENT                ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        if (patient.getAppointments().isEmpty()) {
            System.out.println("✗ No appointments to cancel.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }
        for (int i = 0; i < patient.getAppointments().size(); i++) {
            Appointment a = patient.getAppointments().get(i);
            System.out.println((i + 1) + ". ID: " + a.getAppointmentId() +
                    " | Dr. " + a.getDoctor().getName() + " | " + a.getStatus());
        }
        System.out.print("\nEnter Appointment ID to cancel: ");
        patient.cancelAppointment(ctx.scanner.nextLine().trim());
        UIHelper.pauseScreen(ctx.scanner);
    }

    /**
     * Shows a list of appointments that have at least one consultation message,
     * then lets the patient select one to read and reply to via
     * {@link Patient#respondToChat(Appointment, java.util.Scanner)}.
     *
     * @param patient the currently logged-in patient
     */
    private void viewConsultationChat(Patient patient) {
        UIHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   VIEW CONSULTATION CHAT               ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Filter to appointments that already have messages
        ArrayList<Appointment> chats = new ArrayList<>();
        for (Appointment a : patient.getAppointments()) {
            if (!a.getConsultationMessages().isEmpty()) chats.add(a);
        }
        if (chats.isEmpty()) {
            System.out.println("✗ No consultation chats yet.");
            UIHelper.pauseScreen(ctx.scanner);
            return;
        }
        for (int i = 0; i < chats.size(); i++) {
            Appointment a = chats.get(i);
            String sym = a.getSymptoms();
            // Show a truncated symptom preview alongside doctor name and mode
            System.out.println((i + 1) + ". Dr. " + a.getDoctor().getName() +
                    " | " + a.getConsultationMode() + " | " +
                    sym.substring(0, Math.min(25, sym.length())) + "...");
        }
        int choice = UIHelper.getMenuInput("\nEnter chat number (or 0 to go back): ", ctx.scanner);
        if (choice < 1 || choice > chats.size()) return;
        patient.respondToChat(chats.get(choice - 1), ctx.scanner);
    }
}
