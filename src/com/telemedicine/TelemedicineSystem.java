package com.telemedicine;

import com.telemedicine.models.*;
import com.telemedicine.utils.FileHandler;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class TelemedicineSystem {

    private ArrayList<Patient>     patients;
    private ArrayList<Doctor>      doctors;
    private ArrayList<Admin>       admins;
    private ArrayList<Appointment> appointments;
    private FileHandler fileHandler;
    private Scanner     scanner;
    private Person      currentUser;
    private String      currentUserType;

    //  Constructor 
    public TelemedicineSystem() {
        patients     = new ArrayList<>();
        doctors      = new ArrayList<>();
        admins       = new ArrayList<>();
        appointments = new ArrayList<>();
        fileHandler  = new FileHandler();
        scanner      = new Scanner(System.in);
        currentUser  = null;
        currentUserType = null;
        loadAllData();
        if (doctors.isEmpty()) addSampleData();
    }

    // MAIN MENU 
    
    public void start() {
        while (true) {
            try {
                displayMainMenu();
                int choice = getMenuInput("Enter your choice: ");
                switch (choice) {
                    case 1: 
                        patientLogin();
                        break;
                    case 2: 
                        doctorLogin();    
                        break;
                    case 3: 
                        adminLogin();     
                        break;
                    case 4: 
                        registerPatient();
                        break;
                    case 5: 
                        resetSampleData();
                        break;
                    case 6:
                        saveAllData();
                        System.out.println("\n✓ Thank you for using Telemedicine System!");
                        System.exit(0);
                    default:
                        System.out.println("✗ Invalid choice. Try again.");
                        pauseScreen();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private void displayMainMenu() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  TELEMEDICINE & REMOTE CONSULTATION    ║");
        System.out.println("║              SYSTEM                    ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Patient Login                      ║");
        System.out.println("║  2. Doctor Login                       ║");
        System.out.println("║  3. Admin Login                        ║");
        System.out.println("║  4. New Patient Registration           ║");
        System.out.println("║  5. Reset Sample Data                  ║");
        System.out.println("║  6. Exit                               ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    // PATIENT SECTION 
    
    private void patientLogin() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         PATIENT LOGIN                  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.print("Email: ");
        String email    = scanner.nextLine().trim();
        System.out.print("Password: "); 
        String password = scanner.nextLine().trim();
        for (Patient p : patients) {
            if (p.login(email, password)) {
                currentUser = p; currentUserType = "PATIENT";
                System.out.println("\n✓ Login successful! Welcome, " + p.getName());
                pauseScreen(); patientDashboard(); return;
            }
        }
        System.out.println("\n✗ Invalid credentials."); pauseScreen();
    }

    private void patientDashboard() {
        Patient patient = (Patient) currentUser;
        while (currentUserType != null && currentUserType.equals("PATIENT")) {
            try {
                clearScreen();
                String welcome = "  Welcome, " + truncate(patient.getName(), 29);
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║       PATIENT DASHBOARD                ║");
                System.out.println("║" + Doctor.padRight(welcome, 40) + "║");
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
                int choice = getMenuInput("Enter your choice: ");
                switch (choice) {
                    case 1:  
                        patient.displayProfile(); 
                        pauseScreen(); 
                        break;
                    case 2:  
                        updatePatientProfile(patient); 
                        break;
                    case 3:  
                        searchDoctors(); 
                        pauseScreen(); 
                        break;
                    case 4:  
                        bookAppointment(patient); 
                        break;
                    case 5:  
                        patient.viewAppointments(); 
                        pauseScreen(); 
                        break;
                    case 6:  
                        cancelAppointment(patient); 
                        break;
                    case 7:  
                        patient.viewPrescriptions(); 
                        pauseScreen(); 
                        break;
                    case 8:  
                        viewConsultationChat(patient); 
                        break;
                    case 9:  
                        patient.viewMedicalHistory(); 
                        pauseScreen(); 
                        break;
                    case 10:
                        currentUser = null; currentUserType = null;
                        System.out.println("\n✓ Logged out successfully."); pauseScreen(); return;
                    default: System.out.println("✗ Invalid choice."); pauseScreen();
                }
            } catch (Exception e) { System.out.println("Error: " + e.getMessage()); scanner.nextLine(); pauseScreen(); }
        }
    }

    private void registerPatient() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      PATIENT REGISTRATION              ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        String userId = "P" + (patients.size() + 1001);
        System.out.print("Name: ");         String name     = scanner.nextLine().trim();
        System.out.print("Email: ");        String email    = scanner.nextLine().trim();
        System.out.print("Phone: ");        String phone    = scanner.nextLine().trim();
        System.out.print("Password: ");     String password = scanner.nextLine().trim();
        System.out.print("Age: ");
        Integer age = parseIntInput();
        if (age == null) { System.out.println("✗ Invalid age — registration cancelled."); pauseScreen(); return; }
        System.out.print("Gender (Male/Female): "); String gender     = scanner.nextLine().trim();
        System.out.print("Blood Group: ");                String bloodGroup = scanner.nextLine().trim();
        System.out.print("Address: ");                    String address    = scanner.nextLine().trim();
        try {
            Patient p = new Patient(userId, name, email, phone, password, age, gender, bloodGroup, address);
            patients.add(p);
            System.out.println("\n✓ Patient registered successfully!");
            System.out.println("  Your Patient ID: " + userId);
        } catch (IllegalArgumentException e) {
            System.out.println("\n✗ Registration failed: " + e.getMessage());
        }
        pauseScreen();
    }

    private void updatePatientProfile(Patient patient) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      UPDATE PATIENT PROFILE            ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Update Name                        ║");
        System.out.println("║  2. Update Email                       ║");
        System.out.println("║  3. Update Phone                       ║");
        System.out.println("║  4. Update Address                     ║");
        System.out.println("║  5. Cancel                             ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        int choice = getMenuInput("Select option: ");
        switch (choice) {
            case 1: System.out.print("New name: ");
                try { patient.setName(scanner.nextLine().trim()); System.out.println("✓ Name updated."); }
                catch (IllegalArgumentException e) { System.out.println("✗ " + e.getMessage()); } break;
            case 2: System.out.print("New email: ");
                try { patient.setEmail(scanner.nextLine().trim()); System.out.println("✓ Email updated."); }
                catch (IllegalArgumentException e) { System.out.println("✗ " + e.getMessage()); } break;
            case 3: System.out.print("New phone: ");
                try { patient.setPhoneNumber(scanner.nextLine().trim()); System.out.println("✓ Phone updated."); }
                catch (IllegalArgumentException e) { System.out.println("✗ " + e.getMessage()); } break;
            case 4: System.out.print("New address: ");
                try { patient.setAddress(scanner.nextLine().trim()); System.out.println("✓ Address updated."); }
                catch (IllegalArgumentException e) { System.out.println("✗ " + e.getMessage()); } break;
            case 5: System.out.println("Cancelled."); break;
            default: System.out.println("✗ Invalid choice.");
        }
        pauseScreen();
    }

    private void searchDoctors() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         SEARCH DOCTORS                 ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.print("Search by specialization (or Enter for all): ");
        String term = scanner.nextLine().trim().toLowerCase();
        ArrayList<Doctor> results = new ArrayList<>();
        for (Doctor d : doctors)
            if (term.isEmpty() || d.getSpecialization().toLowerCase().contains(term))
                results.add(d);
        if (results.isEmpty()) { System.out.println("✗ No doctors found."); return; }
        System.out.println("\nFound " + results.size() + " doctor(s):\n");
        for (int i = 0; i < results.size(); i++) {
            Doctor d = results.get(i);
            System.out.println((i + 1) + ". Dr. " + d.getName());
            System.out.println("   Specialization: " + d.getSpecialization());
            System.out.println("   Experience: " + d.getExperienceYears() + " years");
            System.out.println("   Fee: Rs. " + d.getConsultationFee());
            if (d.getTotalRatings() > 0) System.out.printf("   Rating: %.1f/5.0%n", d.getRating());
            System.out.println();
        }
    }

    private void bookAppointment(Patient patient) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        BOOK APPOINTMENT                ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        if (doctors.isEmpty()) { System.out.println("✗ No doctors available."); pauseScreen(); return; }
        System.out.println("Available Doctors:\n");
        for (int i = 0; i < doctors.size(); i++) {
            Doctor d = doctors.get(i);
            System.out.println((i + 1) + ". Dr. " + d.getName() +
                " - " + d.getSpecialization() + " (Rs. " + d.getConsultationFee() + ")");
        }
        int dc = getMenuInput("\nSelect doctor (1-" + doctors.size() + "): ");
        if (dc < 1 || dc > doctors.size()) { System.out.println("✗ Invalid selection."); pauseScreen(); return; }
        Doctor selected = doctors.get(dc - 1);

        System.out.print("\nEnter date (DD-MM-YYYY): ");
        String dateStr = scanner.nextLine();
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            if (date.isBefore(LocalDate.now())) { System.out.println("✗ Cannot book in the past."); pauseScreen(); return; }
        } catch (Exception e) { System.out.println("✗ Invalid date format."); pauseScreen(); return; }

        ArrayList<TimeSlot> slots = selected.getAvailableSlots(date);
        if (slots.isEmpty()) { System.out.println("\n✗ No available slots for this date."); pauseScreen(); return; }
        System.out.println("\nAvailable Time Slots:");
        for (int i = 0; i < slots.size(); i++)
            System.out.println((i + 1) + ". " + slots.get(i).getStartTime() + " - " + slots.get(i).getEndTime());
        int sc = getMenuInput("\nSelect time slot: ");
        if (sc < 1 || sc > slots.size()) { System.out.println("✗ Invalid selection."); pauseScreen(); return; }
        TimeSlot slot = slots.get(sc - 1);

        System.out.print("Describe your symptoms: "); String symptoms = scanner.nextLine().trim();
        System.out.println("\nConsultation Mode:\n1. Video Call\n2. Phone Call\n3. Chat");
        int mode = getMenuInput("Select mode: ");
        String modeStr = mode == 2 ? "PHONE" : mode == 3 ? "CHAT" : "VIDEO";

        patient.bookAppointment(selected, LocalDateTime.of(date, slot.getStartTime()), symptoms, modeStr);
        slot.markAsBooked();
        appointments.add(patient.getAppointments().get(patient.getAppointments().size() - 1));
        pauseScreen();
    }

    private void cancelAppointment(Patient patient) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      CANCEL APPOINTMENT                ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        if (patient.getAppointments().isEmpty()) { System.out.println("✗ No appointments to cancel."); pauseScreen(); return; }
        for (int i = 0; i < patient.getAppointments().size(); i++) {
            Appointment a = patient.getAppointments().get(i);
            System.out.println((i + 1) + ". ID: " + a.getAppointmentId() +
                " | Dr. " + a.getDoctor().getName() + " | " + a.getStatus());
        }
        System.out.print("\nEnter Appointment ID to cancel: ");
        patient.cancelAppointment(scanner.nextLine().trim());
        pauseScreen();
    }

    private void viewConsultationChat(Patient patient) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   VIEW CONSULTATION CHAT               ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        ArrayList<Appointment> chats = new ArrayList<>();
        for (Appointment a : patient.getAppointments())
            if (!a.getConsultationMessages().isEmpty()) chats.add(a);
        if (chats.isEmpty()) { System.out.println("✗ No consultation chats yet."); pauseScreen(); return; }
        for (int i = 0; i < chats.size(); i++) {
            Appointment a = chats.get(i);
            String sym = a.getSymptoms();
            System.out.println((i + 1) + ". Dr. " + a.getDoctor().getName() +
                " | " + a.getConsultationMode() + " | " +
                sym.substring(0, Math.min(25, sym.length())) + "...");
        }
        int choice = getMenuInput("\nEnter chat number (or 0 to go back): ");
        if (choice < 1 || choice > chats.size()) return;
        patient.respondToChat(chats.get(choice - 1), scanner);
    }

    // Doctor section
    private void doctorLogin() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         DOCTOR LOGIN                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.print("Email: ");    String email    = scanner.nextLine().trim();
        System.out.print("Password: "); String password = scanner.nextLine().trim();
        for (Doctor d : doctors) {
            if (d.login(email, password)) {
                currentUser = d; currentUserType = "DOCTOR";
                System.out.println("\n✓ Login successful! Welcome, Dr. " + d.getName());
                pauseScreen(); doctorDashboard(); return;
            }
        }
        System.out.println("\n✗ Invalid credentials."); pauseScreen();
    }

    private void doctorDashboard() {
        Doctor doctor = (Doctor) currentUser;
        while (currentUserType != null && currentUserType.equals("DOCTOR")) {
            try {
                clearScreen();
                String welcome = "  Welcome, Dr. " + truncate(doctor.getName(), 25);
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║       DOCTOR DASHBOARD                 ║");
                System.out.println("║" + Doctor.padRight(welcome, 40) + "║");
                System.out.println("╠════════════════════════════════════════╣");
                System.out.println("║  1. View Profile                       ║");
                System.out.println("║  2. Set Availability                   ║");
                System.out.println("║  3. View All Appointments              ║");
                System.out.println("║  4. View Pending Appointments          ║");
                System.out.println("║  5. View Completed Appointments        ║");
                System.out.println("║  6. Conduct Consultation / Open Chat   ║");
                System.out.println("║  7. Issue Prescription                 ║");
                System.out.println("║  8. View My Issued Prescriptions       ║");
                System.out.println("║  9. Logout                             ║");
                System.out.println("╚════════════════════════════════════════╝\n");
                int choice = getMenuInput("Enter your choice: ");
                switch (choice) {
                    case 1: doctor.displayProfile(); pauseScreen(); break;
                    case 2: clearScreen(); doctor.promptSetAvailability(scanner); pauseScreen(); break;
                    case 3: doctor.viewAppointments("ALL"); pauseScreen(); break;
                    case 4: doctor.viewAppointments("PENDING"); pauseScreen(); break;
                    case 5: doctor.viewAppointments("COMPLETED"); pauseScreen(); break;
                    case 6: conductConsultation(doctor); break;
                    case 7:
                        clearScreen();
                        System.out.println("\n╔════════════════════════════════════════╗");
                        System.out.println("║      ISSUE PRESCRIPTION                ║");
                        System.out.println("╚════════════════════════════════════════╝\n");
                        doctor.promptIssuePrescription(patients, scanner);
                        pauseScreen(); break;
                    case 8: doctor.viewIssuedPrescriptions(); pauseScreen(); break;
                    case 9:
                        currentUser = null; currentUserType = null;
                        System.out.println("\n✓ Logged out successfully."); pauseScreen(); return;
                    default: System.out.println("✗ Invalid choice."); pauseScreen();
                }
            } catch (Exception e) { System.out.println("Error: " + e.getMessage()); scanner.nextLine(); pauseScreen(); }
        }
    }

    private void conductConsultation(Doctor doctor) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      CONDUCT CONSULTATION              ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        ArrayList<Appointment> pending = new ArrayList<>();
        for (Appointment a : doctor.getAppointments())
            if (a.getStatus().equals("PENDING") || a.getStatus().equals("CONFIRMED"))
                pending.add(a);
        if (pending.isEmpty()) { System.out.println("✗ No pending appointments."); pauseScreen(); return; }
        for (int i = 0; i < pending.size(); i++) {
            Appointment a = pending.get(i);
            System.out.println((i + 1) + ". " + a.getPatient().getName() + " | ID: " + a.getAppointmentId());
        }
        System.out.print("\nEnter Appointment ID: ");
        String id = scanner.nextLine().trim();
        for (Appointment a : doctor.getAppointments()) {
            if (a.getAppointmentId().equals(id)) {
                doctor.runConsultationSession(a, scanner);
                return;
            }
        }
        System.out.println("✗ Appointment not found."); pauseScreen();
    }

    // ADMIN SECTION
    private void adminLogin() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ADMIN LOGIN                    ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.print("Email: ");    String email    = scanner.nextLine().trim();
        System.out.print("Password: "); String password = scanner.nextLine().trim();
        for (Admin a : admins) {
            if (a.login(email, password)) {
                currentUser = a; currentUserType = "ADMIN";
                System.out.println("\n✓ Login successful! Welcome, Admin");
                pauseScreen(); adminDashboard(); return;
            }
        }
        System.out.println("\n✗ Invalid credentials."); pauseScreen();
    }

    private void adminDashboard() {
        Admin admin = (Admin) currentUser;
        while (currentUserType != null && currentUserType.equals("ADMIN")) {
            try {
                clearScreen();
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
                int choice = getMenuInput("Enter your choice: ");
                switch (choice) {
                    case 1: admin.displayProfile(); pauseScreen(); break;
                    case 2: admin.viewAllAppointments(appointments); pauseScreen(); break;
                    case 3: admin.generateReport(doctors, patients, appointments); pauseScreen(); break;
                    case 4: viewAllDoctors(); pauseScreen(); break;
                    case 5: viewAllPatients(); pauseScreen(); break;
                    case 6: addDoctor(admin); break;
                    case 7: removeDoctor(admin); break;
                    case 8: removePatient(admin); break;
                    case 9:
                        currentUser = null; currentUserType = null;
                        System.out.println("\n✓ Logged out successfully."); pauseScreen(); return;
                    default: System.out.println("✗ Invalid choice."); pauseScreen();
                }
            } catch (Exception e) { System.out.println("Error: " + e.getMessage()); scanner.nextLine(); pauseScreen(); }
        }
    }

    private void addDoctor(Admin admin) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ADD NEW DOCTOR                 ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        String userId = "D" + (doctors.size() + 1001);
        System.out.print("Name: ");            String name   = scanner.nextLine().trim();
        System.out.print("Email: ");           String email  = scanner.nextLine().trim();
        System.out.print("Phone: ");           String phone  = scanner.nextLine().trim();
        System.out.print("Password: ");        String pass   = scanner.nextLine().trim();
        System.out.print("Specialization: ");  String spec   = scanner.nextLine().trim();
        System.out.print("License Number: ");  String lic    = scanner.nextLine().trim();
        System.out.print("Years of Experience: ");
        Integer exp = parseIntInput();
        if (exp == null) { System.out.println("✗ Invalid experience — doctor not added."); pauseScreen(); return; }
        System.out.print("Consultation Fee (Rs.): ");
        double fee;
        try { fee = Double.parseDouble(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("✗ Invalid fee — doctor not added."); pauseScreen(); return; }
        try {
            Doctor d = new Doctor(userId, name, email, phone, pass, spec, lic, exp, fee);
            admin.addDoctor(doctors, d);
            System.out.println("  Doctor ID: " + userId);
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Failed to add doctor: " + e.getMessage());
        }
        pauseScreen();
    }

    private void viewAllDoctors() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ALL DOCTORS                    ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        if (doctors.isEmpty()) { System.out.println("✗ No doctors registered."); return; }
        for (int i = 0; i < doctors.size(); i++) {
            Doctor d = doctors.get(i);
            System.out.println((i + 1) + ". Dr. " + d.getName());
            System.out.println("   ID: " + d.getUserId());
            System.out.println("   Specialization: " + d.getSpecialization());
            System.out.println("   Experience: " + d.getExperienceYears() + " years\n");
        }
    }

    private void viewAllPatients() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ALL PATIENTS                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        if (patients.isEmpty()) { System.out.println("✗ No patients registered."); return; }
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            System.out.println((i + 1) + ". " + p.getName());
            System.out.println("   ID: " + p.getUserId());
            System.out.println("   Email: " + p.getEmail());
            System.out.println("   Appointments: " + p.getAppointments().size() + "\n");
        }
    }

    private void removeDoctor(Admin admin) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      REMOVE DOCTOR                     ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        if (doctors.isEmpty()) { System.out.println("✗ No doctors to remove."); pauseScreen(); return; }
        for (int i = 0; i < doctors.size(); i++)
            System.out.println((i + 1) + ". Dr. " + doctors.get(i).getName() + " (ID: " + doctors.get(i).getUserId() + ")");
        int choice = getMenuInput("\nSelect doctor (1-" + doctors.size() + ") or 0 to cancel: ");
        if (choice == 0) { System.out.println("Cancelled."); pauseScreen(); return; }
        if (choice < 1 || choice > doctors.size()) { System.out.println("✗ Invalid selection."); pauseScreen(); return; }
        Doctor d = doctors.get(choice - 1);
        System.out.print("\nRemove Dr. " + d.getName() + "? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            admin.removeDoctor(doctors, d.getUserId());
        } else { System.out.println("Cancelled."); }
        pauseScreen();
    }

    private void removePatient(Admin admin) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      REMOVE PATIENT                    ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        if (patients.isEmpty()) { System.out.println("✗ No patients to remove."); pauseScreen(); return; }
        for (int i = 0; i < patients.size(); i++)
            System.out.println((i + 1) + ". " + patients.get(i).getName() + " (ID: " + patients.get(i).getUserId() + ")");
        int choice = getMenuInput("\nSelect patient (1-" + patients.size() + ") or 0 to cancel: ");
        if (choice == 0) { System.out.println("Cancelled."); pauseScreen(); return; }
        if (choice < 1 || choice > patients.size()) { System.out.println("✗ Invalid selection."); pauseScreen(); return; }
        Patient p = patients.get(choice - 1);
        System.out.print("\nRemove " + p.getName() + "? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            admin.removePatient(patients, p.getUserId());
        } else { System.out.println("Cancelled."); }
        pauseScreen();
    }

    // Utility

    private int getMenuInput(String prompt) {
        while (true) {
            if (!prompt.isEmpty()) System.out.print(prompt);
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("✗ Please enter a valid number."); }
        }
    }

    private Integer parseIntInput() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else { System.out.print("\033[H\033[2J"); System.out.flush(); }
        } catch (Exception e) { for (int i = 0; i < 50; i++) System.out.println(); }
    }

    private void pauseScreen() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    //  Persistence
    private void saveAllData() {
        System.out.println("\nSaving data...");
        fileHandler.savePatients(patients);
        fileHandler.saveDoctors(doctors);
        fileHandler.saveAppointments(appointments);
        fileHandler.saveAdmins(admins);
        System.out.println("✓ Data saved successfully.");
    }

    private void loadAllData() {
        patients     = fileHandler.loadPatients();
        doctors      = fileHandler.loadDoctors();
        appointments = fileHandler.loadAppointments();
        admins       = fileHandler.loadAdmins();
    }

    private void resetSampleData() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      RESET SAMPLE DATA                 ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.println("This will clear all data and reload fresh sample data.\n");
        System.out.print("Are you sure? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            doctors.clear(); patients.clear(); appointments.clear(); admins.clear();
            addSampleData();
            saveAllData();
            System.out.println("\n✓ Sample data reloaded successfully!");
            System.out.println("  - 3 sample doctors  - 1 admin  - 1 patient");
        } else { System.out.println("\n✗ Cancelled."); }
        pauseScreen();
    }

    // Sample data
    private void addSampleData() {
        Doctor d1 = new Doctor("D001", "Fatima Khan", "fatima@hospital.com",
            "0301-1111111", "doc123", "Cardiologist", "PMC-12345", 10, 2000.0);
        Doctor d2 = new Doctor("D002", "Ali Raza", "ali@hospital.com",
            "0302-2222222", "doc123", "Dermatologist", "PMC-12346", 8, 1500.0);
        Doctor d3 = new Doctor("D003", "Sara Ahmed", "sara@hospital.com",
            "0303-3333333", "doc123", "Pediatrician", "PMC-12347", 12, 1800.0);
        for (int i = 1; i <= 7; i++) {
            d1.setAvailability(LocalDate.now().plusDays(i), LocalTime.of(9,  0), LocalTime.of(17, 0));
            d2.setAvailability(LocalDate.now().plusDays(i), LocalTime.of(10, 0), LocalTime.of(16, 0));
            d3.setAvailability(LocalDate.now().plusDays(i), LocalTime.of(8,  0), LocalTime.of(14, 0));
        }
        doctors.add(d1); doctors.add(d2); doctors.add(d3);
        admins.add(new Admin("A001", "Admin User", "admin@system.com",
            "0300-0000000", "admin123", "super_admin"));
        patients.add(new Patient("P001", "Ahmed Ali", "ahmed@email.com",
            "0300-1234567", "pass123", 28, "Male", "B+", "Rawalpindi"));
        System.out.println("✓ Sample data loaded.");
    }
}