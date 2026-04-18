package com.telemedicine;

import com.telemedicine.models.*;
import com.telemedicine.utils.FileHandler;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Main controller for the Telemedicine & Remote Consultation System.
 * Manages menu-driven user interface and system operations.
 * 
 * @author Telemedicine Team
 * @version 1.0
 */
public class TelemedicineSystem {
    // Data storage
    private ArrayList<Patient> patients;
    private ArrayList<Doctor> doctors;
    private ArrayList<Admin> admins;
    private ArrayList<Appointment> appointments;
    private FileHandler fileHandler;
    private Scanner scanner;
    
    // Current logged-in user
    private Person currentUser;
    private String currentUserType;
    
    // Constructor
    public TelemedicineSystem() {
        this.patients = new ArrayList<>();
        this.doctors = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.fileHandler = new FileHandler();
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
        this.currentUserType = null;
        
        // Load existing data
        loadAllData();
        
        // Add sample data if empty
        if (doctors.isEmpty()) {
            addSampleData();
        }
    }
    
    // ==================== MAIN MENU ====================
    
    public void start() {
        while (true) {
            try {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");
                
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
        System.out.println("║              SYSTEM v1.0               ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Patient Login                      ║");
        System.out.println("║  2. Doctor Login                       ║");
        System.out.println("║  3. Admin Login                        ║");
        System.out.println("║  4. New Patient Registration           ║");
        System.out.println("║  5. Reset Sample Data                  ║");
        System.out.println("║  6. Exit                               ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    // ==================== PATIENT OPERATIONS ====================
    
    private void patientLogin() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         PATIENT LOGIN                  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        for (Patient patient : patients) {
            if (patient.login(email, password)) {
                currentUser = patient;
                currentUserType = "PATIENT";
                System.out.println("\n✓ Login successful! Welcome, " + patient.getName());
                pauseScreen();
                patientDashboard();
                return;
            }
        }
        
        System.out.println("\n✗ Invalid credentials. Please try again.");
        pauseScreen();
    }
    
    private void patientDashboard() {
        Patient patient = (Patient) currentUser;
        
        while (currentUserType != null && currentUserType.equals("PATIENT")) {
            try {
                clearScreen();
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║       PATIENT DASHBOARD                ║");
                System.out.println("║  Welcome, " + 
                                 String.format("%-29s", patient.getName()) + "║");
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
                
                int choice = getIntInput("Enter your choice: ");
                
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
                        currentUser = null;
                        currentUserType = null;
                        System.out.println("\n✓ Logged out successfully.");
                        pauseScreen();
                        return;
                    default:
                        System.out.println("✗ Invalid choice.");
                        pauseScreen();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
                pauseScreen();
            }
        }
    }
    
    private void registerPatient() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      PATIENT REGISTRATION              ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        String userId = "P" + (patients.size() + 1001);
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Age: ");
        int age = getIntInput("");
        System.out.print("Gender (Male/Female/Other): ");
        String gender = scanner.nextLine().trim();
        System.out.print("Blood Group: ");
        String bloodGroup = scanner.nextLine().trim();
        System.out.print("Address: ");
        String address = scanner.nextLine().trim();
        
        Patient newPatient = new Patient(userId, name, email, phone, 
                                        password, age, gender, bloodGroup, address);
        patients.add(newPatient);
        
        System.out.println("\n✓ Patient registered successfully!");
        System.out.println("  Your Patient ID: " + userId);
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
        
        int choice = getIntInput("Select option: ");
        
        switch (choice) {
            case 1:
                System.out.print("Enter new name: ");
                patient.setName(scanner.nextLine().trim());
                System.out.println("✓ Name updated.");
                break;
            case 2:
                System.out.print("Enter new email: ");
                patient.setEmail(scanner.nextLine().trim());
                System.out.println("✓ Email updated.");
                break;
            case 3:
                System.out.print("Enter new phone: ");
                patient.setPhoneNumber(scanner.nextLine().trim());
                System.out.println("✓ Phone updated.");
                break;
            case 4:
                System.out.print("Enter new address: ");
                patient.setAddress(scanner.nextLine().trim());
                System.out.println("✓ Address updated.");
                break;
            case 5:
                System.out.println("Update cancelled.");
                break;
            default:
                System.out.println("✗ Invalid choice.");
        }
        pauseScreen();
    }
    
    private void searchDoctors() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         SEARCH DOCTORS                 ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.print("Search by specialization (or press Enter for all): ");
        String searchTerm = scanner.nextLine().trim().toLowerCase();
        
        ArrayList<Doctor> searchResults = new ArrayList<>();
        
        if (searchTerm.isEmpty()) {
            searchResults = doctors;
        } else {
            for (Doctor doctor : doctors) {
                if (doctor.getSpecialization().toLowerCase().contains(searchTerm)) {
                    searchResults.add(doctor);
                }
            }
        }
        
        if (searchResults.isEmpty()) {
            System.out.println("✗ No doctors found matching your search.");
            return;
        }
        
        System.out.println("\nFound " + searchResults.size() + " doctor(s):\n");
        for (int i = 0; i < searchResults.size(); i++) {
            Doctor doc = searchResults.get(i);
            System.out.println((i + 1) + ". Dr. " + doc.getName());
            System.out.println("   Specialization: " + doc.getSpecialization());
            System.out.println("   Experience: " + doc.getExperienceYears() + " years");
            System.out.println("   Fee: Rs. " + doc.getConsultationFee());
            if (doc.getTotalRatings() > 0) {
                System.out.printf("   Rating: %.1f/5.0\n", doc.getRating());
            }
            System.out.println();
        }
    }
    
    private void bookAppointment(Patient patient) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        BOOK APPOINTMENT                ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        if (doctors.isEmpty()) {
            System.out.println("✗ No doctors available.");
            pauseScreen();
            return;
        }
        
        System.out.println("Available Doctors:\n");
        for (int i = 0; i < doctors.size(); i++) {
            Doctor doc = doctors.get(i);
            System.out.println((i + 1) + ". Dr. " + doc.getName() + 
                             " - " + doc.getSpecialization() +
                             " (Rs. " + doc.getConsultationFee() + ")");
        }
        
        int doctorChoice = getIntInput("\nSelect doctor (1-" + doctors.size() + "): ");
        
        if (doctorChoice < 1 || doctorChoice > doctors.size()) {
            System.out.println("✗ Invalid selection.");
            pauseScreen();
            return;
        }
        
        Doctor selectedDoctor = doctors.get(doctorChoice - 1);
        
        // Get appointment date
        System.out.print("\nEnter date (DD-MM-YYYY): ");
        String dateStr = scanner.nextLine();
        LocalDate appointmentDate;
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            appointmentDate = LocalDate.parse(dateStr, formatter);
            
            if (appointmentDate.isBefore(LocalDate.now())) {
                System.out.println("✗ Cannot book appointment in the past.");
                pauseScreen();
                return;
            }
        } catch (Exception e) {
            System.out.println("✗ Invalid date format.");
            pauseScreen();
            return;
        }
        
        // Show available time slots
        ArrayList<TimeSlot> availableSlots = 
            selectedDoctor.getAvailableSlots(appointmentDate);
        
        if (availableSlots.isEmpty()) {
            System.out.println("\n✗ No available slots for this date.");
            pauseScreen();
            return;
        }
        
        System.out.println("\nAvailable Time Slots:");
        for (int i = 0; i < availableSlots.size(); i++) {
            TimeSlot slot = availableSlots.get(i);
            System.out.println((i + 1) + ". " + slot.getStartTime() + 
                             " - " + slot.getEndTime());
        }
        
        int slotChoice = getIntInput("\nSelect time slot: ");
        
        if (slotChoice < 1 || slotChoice > availableSlots.size()) {
            System.out.println("✗ Invalid selection.");
            pauseScreen();
            return;
        }
        
        TimeSlot selectedSlot = availableSlots.get(slotChoice - 1);
        
        // Get symptoms
        System.out.print("Describe your symptoms: ");
        String symptoms = scanner.nextLine().trim();
        
        // Select consultation mode
        System.out.println("\nConsultation Mode:");
        System.out.println("1. Video Call");
        System.out.println("2. Phone Call");
        System.out.println("3. Chat");
        
        int modeChoice = getIntInput("Select mode: ");
        String mode;
        
        switch (modeChoice) {
            case 1: mode = "VIDEO"; break;
            case 2: mode = "PHONE"; break;
            case 3: mode = "CHAT"; break;
            default:
                System.out.println("Defaulting to VIDEO.");
                mode = "VIDEO";
        }
        
        // Create appointment
        LocalDateTime appointmentDateTime = LocalDateTime.of(
            appointmentDate,
            selectedSlot.getStartTime()
        );
        
        patient.bookAppointment(selectedDoctor, appointmentDateTime, 
                              symptoms, mode);
        selectedSlot.markAsBooked();
        
        // Add to system's appointment list
        Appointment newAppointment = patient.getAppointments()
                                           .get(patient.getAppointments().size() - 1);
        appointments.add(newAppointment);
        
        pauseScreen();
    }
    
    private void cancelAppointment(Patient patient) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      CANCEL APPOINTMENT                ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        if (patient.getAppointments().isEmpty()) {
            System.out.println("✗ No appointments to cancel.");
            pauseScreen();
            return;
        }
        
        System.out.println("Your Appointments:\n");
        for (int i = 0; i < patient.getAppointments().size(); i++) {
            Appointment apt = patient.getAppointments().get(i);
            System.out.println((i + 1) + ". ID: " + apt.getAppointmentId() +
                             " | Doctor: Dr. " + apt.getDoctor().getName() +
                             " | Status: " + apt.getStatus());
        }
        
        System.out.print("\nEnter Appointment ID to cancel: ");
        String appointmentId = scanner.nextLine().trim();
        
        patient.cancelAppointment(appointmentId);
        pauseScreen();
    }
    
    // ==================== DOCTOR OPERATIONS ====================
    
    private void doctorLogin() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         DOCTOR LOGIN                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        for (Doctor doctor : doctors) {
            if (doctor.login(email, password)) {
                currentUser = doctor;
                currentUserType = "DOCTOR";
                System.out.println("\n✓ Login successful! Welcome, Dr. " + doctor.getName());
                pauseScreen();
                doctorDashboard();
                return;
            }
        }
        
        System.out.println("\n✗ Invalid credentials. Please try again.");
        pauseScreen();
    }
    
    private void doctorDashboard() {
        Doctor doctor = (Doctor) currentUser;
        
        while (currentUserType != null && currentUserType.equals("DOCTOR")) {
            try {
                clearScreen();
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║       DOCTOR DASHBOARD                 ║");
                System.out.println("║  Welcome, Dr. " + 
                                 String.format("%-26s", doctor.getName()) + "║");
                System.out.println("╠════════════════════════════════════════╣");
                System.out.println("║  1. View Profile                       ║");
                System.out.println("║  2. Set Availability                   ║");
                System.out.println("║  3. View All Appointments              ║");
                System.out.println("║  4. View Pending Appointments          ║");
                System.out.println("║  5. View Completed Appointments        ║");
                System.out.println("║  6. Conduct Consultation               ║");
                System.out.println("║  7. Issue Prescription                 ║");
                System.out.println("║  8. View My Issued Prescriptions       ║");
                System.out.println("║  9. View Patient Responses             ║");
                System.out.println("║  10. Logout                            ║");
                System.out.println("╚════════════════════════════════════════╝\n");
                
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        doctor.displayProfile();
                        pauseScreen();
                        break;
                    case 2:
                        setDoctorAvailability(doctor);
                        break;
                    case 3:
                        doctor.viewAppointments("ALL");
                        pauseScreen();
                        break;
                    case 4:
                        doctor.viewAppointments("PENDING");
                        pauseScreen();
                        break;
                    case 5:
                        doctor.viewAppointments("COMPLETED");
                        pauseScreen();
                        break;
                    case 6:
                        conductConsultation(doctor);
                        break;
                    case 7:
                        issuePrescription(doctor);
                        break;
                    case 8:
                        doctor.viewIssuedPrescriptions();
                        pauseScreen();
                        break;
                    case 9:
                        viewPatientResponses(doctor);
                        break;
                    case 10:
                        currentUser = null;
                        currentUserType = null;
                        System.out.println("\n✓ Logged out successfully.");
                        pauseScreen();
                        return;
                    default:
                        System.out.println("✗ Invalid choice.");
                        pauseScreen();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
                pauseScreen();
            }
        }
    }
    
    private void setDoctorAvailability(Doctor doctor) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      SET AVAILABILITY                  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.print("Enter date (DD-MM-YYYY): ");
        String dateStr = scanner.nextLine();
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date = LocalDate.parse(dateStr, formatter);
            
            System.out.print("Enter start time (HH:MM): ");
            String startTimeStr = scanner.nextLine();
            LocalTime startTime = LocalTime.parse(startTimeStr);
            
            System.out.print("Enter end time (HH:MM): ");
            String endTimeStr = scanner.nextLine();
            LocalTime endTime = LocalTime.parse(endTimeStr);
            
            doctor.setAvailability(date, startTime, endTime);
        } catch (Exception e) {
            System.out.println("✗ Invalid input format.");
        }
        
        pauseScreen();
    }
    
    private void conductConsultation(Doctor doctor) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      CONDUCT CONSULTATION              ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        ArrayList<Appointment> pendingAppointments = new ArrayList<>();
        for (Appointment apt : doctor.getAppointments()) {
            if (apt.getStatus().equals("PENDING") || 
                apt.getStatus().equals("CONFIRMED")) {
                pendingAppointments.add(apt);
            }
        }
        
        if (pendingAppointments.isEmpty()) {
            System.out.println("✗ No pending appointments.");
            pauseScreen();
            return;
        }
        
        System.out.println("Pending/Confirmed Appointments:\n");
        for (int i = 0; i < pendingAppointments.size(); i++) {
            Appointment apt = pendingAppointments.get(i);
            System.out.println((i + 1) + ". " + apt.getPatient().getName() +
                             " | ID: " + apt.getAppointmentId());
        }
        
        System.out.print("\nEnter Appointment ID: ");
        String appointmentId = scanner.nextLine().trim();
        
        for (Appointment apt : doctor.getAppointments()) {
            if (apt.getAppointmentId().equals(appointmentId)) {
                // Start interactive consultation session
                runConsultationSession(apt, doctor);
                return;
            }
        }
        
        System.out.println("✗ Appointment not found.");
        pauseScreen();
    }
    
    /**
     * View consultation chat for a patient and allow them to reply to doctor's messages.
     */
    private void viewConsultationChat(Patient patient) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   VIEW CONSULTATION CHAT               ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Find appointments with messages
        ArrayList<Appointment> chatAppointments = new ArrayList<>();
        for (Appointment apt : patient.getAppointments()) {
            if (!apt.getConsultationMessages().isEmpty()) {
                chatAppointments.add(apt);
            }
        }
        
        if (chatAppointments.isEmpty()) {
            System.out.println("✗ You have no consultation chats yet.");
            pauseScreen();
            return;
        }
        
        System.out.println("Your Consultation Chats:\n");
        for (int i = 0; i < chatAppointments.size(); i++) {
            Appointment apt = chatAppointments.get(i);
            System.out.println((i + 1) + ". Dr. " + apt.getDoctor().getName() +
                             " | " + apt.getConsultationMode() + 
                             " | " + apt.getSymptoms().substring(0, Math.min(25, apt.getSymptoms().length())) + "...");
        }
        
        int choice = getIntInput("\nEnter chat number (or 0 to go back): ");
        if (choice < 1 || choice > chatAppointments.size()) {
            return;
        }
        
        Appointment selectedAppointment = chatAppointments.get(choice - 1);
        respondToChat(patient, selectedAppointment);
    }
    
    /**
     * Allow patient to view and respond to doctor's messages in a chat.
     */
    private void respondToChat(Patient patient, Appointment appointment) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      CONSULTATION CHAT                 ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ With: Dr. " + String.format("%-27s", appointment.getDoctor().getName()) + "║");
        System.out.println("║ Mode: " + String.format("%-32s", appointment.getConsultationMode()) + "║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Display all messages in the conversation
        System.out.println("--- Conversation History ---\n");
        for (Message msg : appointment.getConsultationMessages()) {
            msg.displayMessage();
        }
        
        System.out.println("\n--- Your Reply ---");
        System.out.println("Type your messages to respond (type 'done' when finished)\n");
        
        boolean respondingToChat = true;
        
        while (respondingToChat) {
            System.out.print(patient.getName() + ": ");
            String message = scanner.nextLine().trim();
            
            if (message.equalsIgnoreCase("done")) {
                respondingToChat = false;
                break;
            }
            
            if (message.isEmpty()) {
                continue;
            }
            
            // Save patient's message to the chat
            Message patientMsg = new Message(patient.getName(), "PATIENT", message, appointment.getConsultationMode());
            appointment.addConsultationMessage(patientMsg);
            System.out.println("✓ Message sent\n");
        }
        
        System.out.println("\n✓ Your replies have been saved. Doctor will see them when they log in.");
        pauseScreen();
    }
    
    /**
     * Run an interactive consultation session based on the consultation mode.
     * Supports VIDEO, PHONE, and CHAT with message exchange.
     */
    private void runConsultationSession(Appointment appointment, Doctor doctor) {
        clearScreen();
        Patient patient = appointment.getPatient();
        String mode = appointment.getConsultationMode();
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   CONSULTATION SESSION - " + 
                         String.format("%-18s", mode) + "║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Patient: " + String.format("%-29s", patient.getName()) + "║");
        System.out.println("║ Doctor: Dr. " + String.format("%-27s", doctor.getName()) + "║");
        System.out.println("║ Symptoms: " + String.format("%-29s", 
                         appointment.getSymptoms().substring(0, Math.min(28, appointment.getSymptoms().length()))) + "║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Mark appointment as confirmed
        appointment.confirmAppointment();
        
        // Run consultation based on mode
        switch (mode) {
            case "CHAT":
                runChatSession(appointment, doctor, patient);
                break;
            case "VIDEO":
                runVideoConsultation(appointment, doctor, patient);
                break;
            case "PHONE":
                runPhoneConsultation(appointment, doctor, patient);
                break;
            default:
                runChatSession(appointment, doctor, patient);
        }
        
        System.out.println("\n✓ Messages sent. Patient will see them when they log in.");
        pauseScreen();
    }
    
    /**
     * Run an asynchronous CHAT mode consultation.
     * Doctor sends one or more messages that are saved.
     * Patient can reply when they log in.
     */
    private void runChatSession(Appointment appointment, Doctor doctor, Patient patient) {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("        APPOINTMENT MESSAGING (CHAT)");
        System.out.println("═══════════════════════════════════════════\n");
        System.out.println("Send messages to " + patient.getName() + " (type 'done' when finished)\n");
        
        // Display previous messages if any
        if (!appointment.getConsultationMessages().isEmpty()) {
            System.out.println("--- Previous Messages in Conversation ---");
            for (Message msg : appointment.getConsultationMessages()) {
                msg.displayMessage();
            }
            System.out.println("--- New Messages ---\n");
        }
        
        boolean sendingMessages = true;
        
        while (sendingMessages) {
            System.out.print("Dr. " + doctor.getName() + ": ");
            String message = scanner.nextLine().trim();
            
            if (message.equalsIgnoreCase("done")) {
                sendingMessages = false;
                break;
            }
            
            if (message.isEmpty()) {
                continue;
            }
            
            // Save message to appointment chat
            Message doctorMsg = new Message(doctor.getName(), "DOCTOR", message, "CHAT");
            appointment.addConsultationMessage(doctorMsg);
            System.out.println("✓ Message saved\n");
        }
    }
    
    /**
     * Run an asynchronous VIDEO mode consultation.
     * Doctor sends one or more messages that are saved.
     * Patient can reply when they log in.
     */
    private void runVideoConsultation(Appointment appointment, Doctor doctor, Patient patient) {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("        APPOINTMENT MESSAGING (VIDEO)");
        System.out.println("═══════════════════════════════════════════\n");
        System.out.println("Send messages to " + patient.getName() + " (type 'done' when finished)\n");
        
        // Display previous messages if any
        if (!appointment.getConsultationMessages().isEmpty()) {
            System.out.println("--- Previous Messages in Conversation ---");
            for (Message msg : appointment.getConsultationMessages()) {
                msg.displayMessage();
            }
            System.out.println("--- New Messages ---\n");
        }
        
        // Log initial video session marker
        Message startMsg = new Message(doctor.getName(), "DOCTOR", 
            "[Doctor initiated video consultation session]", "VIDEO");
        appointment.addConsultationMessage(startMsg);
        System.out.println("✓ Video consultation session started\n");
        
        boolean sendingMessages = true;
        
        while (sendingMessages) {
            System.out.print("Dr. " + doctor.getName() + ": ");
            String message = scanner.nextLine().trim();
            
            if (message.equalsIgnoreCase("done")) {
                Message endMsg = new Message(doctor.getName(), "DOCTOR", 
                    "[Doctor ended video consultation session]", "VIDEO");
                appointment.addConsultationMessage(endMsg);
                sendingMessages = false;
                break;
            }
            
            if (message.isEmpty()) {
                continue;
            }
            
            // Save message to appointment chat
            Message doctorMsg = new Message(doctor.getName(), "DOCTOR", message, "VIDEO");
            appointment.addConsultationMessage(doctorMsg);
            System.out.println("✓ Message saved\n");
        }
    }
    
    /**
     * Run an asynchronous PHONE mode consultation.
     * Doctor sends one or more messages that are saved.
     * Patient can reply when they log in.
     */
    private void runPhoneConsultation(Appointment appointment, Doctor doctor, Patient patient) {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("        APPOINTMENT MESSAGING (PHONE)");
        System.out.println("═══════════════════════════════════════════\n");
        System.out.println("Send messages to " + patient.getName() + " (type 'done' when finished)\n");
        
        // Display previous messages if any
        if (!appointment.getConsultationMessages().isEmpty()) {
            System.out.println("--- Previous Messages in Conversation ---");
            for (Message msg : appointment.getConsultationMessages()) {
                msg.displayMessage();
            }
            System.out.println("--- New Messages ---\n");
        }
        
        // Log initial phone session marker
        Message startMsg = new Message(doctor.getName(), "DOCTOR", 
            "[Doctor initiated phone consultation]", "PHONE");
        appointment.addConsultationMessage(startMsg);
        System.out.println("✓ Phone consultation session started\n");
        
        boolean sendingMessages = true;
        
        while (sendingMessages) {
            System.out.print("Dr. " + doctor.getName() + ": ");
            String message = scanner.nextLine().trim();
            
            if (message.equalsIgnoreCase("done")) {
                Message endMsg = new Message(doctor.getName(), "DOCTOR", 
                    "[Doctor ended phone consultation]", "PHONE");
                appointment.addConsultationMessage(endMsg);
                sendingMessages = false;
                break;
            }
            
            if (message.isEmpty()) {
                continue;
            }
            
            // Save message to appointment chat
            Message doctorMsg = new Message(doctor.getName(), "DOCTOR", message, "PHONE");
            appointment.addConsultationMessage(doctorMsg);
            System.out.println("✓ Message saved\n");
        }
    }
    
    /**
     * Allow doctor to view patient responses to sent messages.
     */
    private void viewPatientResponses(Doctor doctor) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   VIEW PATIENT RESPONSES               ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Find appointments with messages where patient has responded
        ArrayList<Appointment> respondedAppointments = new ArrayList<>();
        for (Appointment apt : doctor.getAppointments()) {
            if (!apt.getConsultationMessages().isEmpty()) {
                // Check if there are any patient messages
                boolean hasPatientMessage = false;
                for (Message msg : apt.getConsultationMessages()) {
                    if (msg.getSenderType().equals("PATIENT")) {
                        hasPatientMessage = true;
                        break;
                    }
                }
                if (hasPatientMessage) {
                    respondedAppointments.add(apt);
                }
            }
        }
        
        if (respondedAppointments.isEmpty()) {
            System.out.println("✗ No patients have responded to your messages yet.");
            pauseScreen();
            return;
        }
        
        System.out.println("Patient Responses:\n");
        for (int i = 0; i < respondedAppointments.size(); i++) {
            Appointment apt = respondedAppointments.get(i);
            int patientMessageCount = 0;
            for (Message msg : apt.getConsultationMessages()) {
                if (msg.getSenderType().equals("PATIENT")) {
                    patientMessageCount++;
                }
            }
            System.out.println((i + 1) + ". " + apt.getPatient().getName() +
                             " | " + apt.getConsultationMode() + 
                             " | " + patientMessageCount + " response(s)");
        }
        
        int choice = getIntInput("\nEnter appointment number (or 0 to go back): ");
        if (choice < 1 || choice > respondedAppointments.size()) {
            return;
        }
        
        Appointment selectedAppointment = respondedAppointments.get(choice - 1);
        displayConsultationChat(doctor, selectedAppointment);
    }
    
    /**
     * Display the full consultation chat for a doctor to review.
     */
    private void displayConsultationChat(Doctor doctor, Appointment appointment) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      CONSULTATION CHAT HISTORY         ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Patient: " + String.format("%-29s", appointment.getPatient().getName()) + "║");
        System.out.println("║ Mode: " + String.format("%-32s", appointment.getConsultationMode()) + "║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Display all messages in the conversation
        for (Message msg : appointment.getConsultationMessages()) {
            msg.displayMessage();
        }
        
        pauseScreen();
    }
    
    private void issuePrescription(Doctor doctor) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      ISSUE PRESCRIPTION                ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine().trim();
        
        Patient selectedPatient = null;
        for (Patient patient : patients) {
            if (patient.getUserId().equals(patientId)) {
                selectedPatient = patient;
                break;
            }
        }
        
        if (selectedPatient == null) {
            System.out.println("✗ Patient not found.");
            pauseScreen();
            return;
        }
        
        System.out.print("Diagnosis: ");
        String diagnosis = scanner.nextLine().trim();
        
        ArrayList<Medicine> medicines = new ArrayList<>();
        boolean addMore = true;
        
        while (addMore) {
            System.out.print("\nMedicine name (or 'done' to finish): ");
            String medicineName = scanner.nextLine().trim();
            
            if (medicineName.equalsIgnoreCase("done")) {
                addMore = false;
            } else {
                System.out.print("Dosage: ");
                String dosage = scanner.nextLine().trim();
                System.out.print("Frequency: ");
                String frequency = scanner.nextLine().trim();
                System.out.print("Duration (days): ");
                int duration = getIntInput("");
                System.out.print("Instructions: ");
                String instructions = scanner.nextLine().trim();
                
                medicines.add(new Medicine(medicineName, dosage, frequency,
                                          duration, instructions));
                System.out.println("✓ Medicine added.");
            }
        }
        
        System.out.print("\nAdditional notes: ");
        String notes = scanner.nextLine().trim();
        
        doctor.issuePrescription(selectedPatient, diagnosis, medicines, notes);
        pauseScreen();
    }
    
    // ==================== ADMIN OPERATIONS ====================
    
    private void adminLogin() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ADMIN LOGIN                    ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        for (Admin admin : admins) {
            if (admin.login(email, password)) {
                currentUser = admin;
                currentUserType = "ADMIN";
                System.out.println("\n✓ Login successful! Welcome, Admin");
                pauseScreen();
                adminDashboard();
                return;
            }
        }
        
        System.out.println("\n✗ Invalid credentials. Please try again.");
        pauseScreen();
    }
    
    private void adminDashboard() {
        Admin admin = (Admin) currentUser;
        
        while (currentUserType != null && currentUserType.equals("ADMIN")) {
            try {
                clearScreen();
                System.out.println("\n╔════════════════════════════════════════╗");
                System.out.println("║       ADMIN DASHBOARD                  ║");
                System.out.println("╠════════════════════════════════════════╣");
                System.out.println("║  1. Remove Doctor                      ║");
                System.out.println("║  7. Remove Patient                     ║");
                System.out.println("║  8. Logout                             ║");
                System.out.println("╚════════════════════════════════════════╝\n");
                
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        admin.displayProfile();
                        pauseScreen();
                        break;
                    case 2:
                        admin.viewAllAppointments(appointments);
                        pauseScreen();
                        break;
                    case 3:
                        admin.generateReport(doctors, patients, appointments);
                        pauseScreen();
                        break;
                    case 4:
                        viewAllDoctors();
                        pauseScreen();
                        break;
                    case 5:
                        viewAllPatients();
                        pauseScreen();
                        break;
                    case 6:
                        removeDoctor(admin);
                        break;
                    case 7:
                        removePatient(admin);
                        break;
                    case 8:
                        currentUser = null;
                        currentUserType = null;
                        System.out.println("\n✓ Logged out successfully.");
                        pauseScreen();
                        return;
                    default:
                        System.out.println("✗ Invalid choice.");
                        pauseScreen();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
                pauseScreen();
            }
        }
    }
    
    private void viewAllDoctors() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ALL DOCTORS                    ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        if (doctors.isEmpty()) {
            System.out.println("✗ No doctors registered.");
            return;
        }
        
        for (int i = 0; i < doctors.size(); i++) {
            Doctor doc = doctors.get(i);
            System.out.println((i + 1) + ". Dr. " + doc.getName());
            System.out.println("   ID: " + doc.getUserId());
            System.out.println("   Specialization: " + doc.getSpecialization());
            System.out.println("   Experience: " + doc.getExperienceYears() + " years");
            System.out.println();
        }
    }
    
    private void viewAllPatients() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         ALL PATIENTS                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        if (patients.isEmpty()) {
            System.out.println("✗ No patients registered.");
            return;
        }
        
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            System.out.println((i + 1) + ". " + patient.getName());
            System.out.println("   ID: " + patient.getUserId());
            System.out.println("   Email: " + patient.getEmail());
            System.out.println("   Appointments: " + patient.getAppointments().size());
            System.out.println();
        }
    }
    
    /**
     * Remove a doctor from the system with cascade delete for prescriptions and appointments.
     * Issue #2 & #7: Prescription tracking and cascade delete
     */
    private void removeDoctor(Admin admin) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      REMOVE DOCTOR                     ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        if (doctors.isEmpty()) {
            System.out.println("✗ No doctors to remove.");
            pauseScreen();
            return;
        }
        
        System.out.println("Select a doctor to remove:\n");
        for (int i = 0; i < doctors.size(); i++) {
            Doctor doc = doctors.get(i);
            System.out.println((i + 1) + ". Dr. " + doc.getName() + " (ID: " + doc.getUserId() + ")");
        }
        
        int choice = getIntInput("\nSelect doctor (1-" + doctors.size() + ") or 0 to cancel: ");
        
        if (choice == 0) {
            System.out.println("Operation cancelled.");
            pauseScreen();
            return;
        }
        
        if (choice < 1 || choice > doctors.size()) {
            System.out.println("✗ Invalid selection.");
            pauseScreen();
            return;
        }
        
        Doctor doctorToRemove = doctors.get(choice - 1);
        String doctorId = doctorToRemove.getUserId();
        
        System.out.print("\nAre you sure you want to remove Dr. " + doctorToRemove.getName() + 
                         "? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("yes")) {
            admin.removeDoctor(doctors, doctorId);
            System.out.println("✓ Doctor and all associated data removed successfully.");
        } else {
            System.out.println("Operation cancelled.");
        }
        
        pauseScreen();
    }
    
    /**
     * Remove a patient from the system with cascade delete for prescriptions and appointments.
     * Issue #2 & #7: Prescription tracking and cascade delete
     */
    private void removePatient(Admin admin) {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      REMOVE PATIENT                    ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        if (patients.isEmpty()) {
            System.out.println("✗ No patients to remove.");
            pauseScreen();
            return;
        }
        
        System.out.println("Select a patient to remove:\n");
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            System.out.println((i + 1) + ". " + patient.getName() + " (ID: " + patient.getUserId() + ")");
        }
        
        int choice = getIntInput("\nSelect patient (1-" + patients.size() + ") or 0 to cancel: ");
        
        if (choice == 0) {
            System.out.println("Operation cancelled.");
            pauseScreen();
            return;
        }
        
        if (choice < 1 || choice > patients.size()) {
            System.out.println("✗ Invalid selection.");
            pauseScreen();
            return;
        }
        
        Patient patientToRemove = patients.get(choice - 1);
        String patientId = patientToRemove.getUserId();
        
        System.out.print("\nAre you sure you want to remove " + patientToRemove.getName() + 
                         "? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("yes")) {
            admin.removePatient(patients, patientId);
            System.out.println("✓ Patient and all associated data removed successfully.");
        } else {
            System.out.println("Operation cancelled.");
        }
        
        pauseScreen();
    }
    
    // ==================== UTILITY METHODS ====================
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                if (!prompt.isEmpty()) {
                    System.out.print(prompt);
                }
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("✗ Please enter a valid number.");
                if (prompt.isEmpty()) {
                    System.out.print("Try again: ");
                }
            }
        }
    }
    
    private void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    private void pauseScreen() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    // ==================== FILE OPERATIONS ====================
    
    private void saveAllData() {
        System.out.println("\nSaving data...");
        fileHandler.savePatients(patients);
        fileHandler.saveDoctors(doctors);
        fileHandler.saveAppointments(appointments);
        fileHandler.saveAdmins(admins);
        System.out.println("✓ Data saved successfully.");
    }
    
    private void loadAllData() {
        patients = fileHandler.loadPatients();
        doctors = fileHandler.loadDoctors();
        appointments = fileHandler.loadAppointments();
        admins = fileHandler.loadAdmins();
    }
    
    private void resetSampleData() {
        clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      RESET SAMPLE DATA                 ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.println("This will clear all data and load fresh sample doctors with availability.\n");
        System.out.print("Are you sure? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("yes")) {
            doctors.clear();
            patients.clear();
            appointments.clear();
            admins.clear();
            
            addSampleData();
            saveAllData();
            
            System.out.println("\n✓ Sample data reloaded successfully!");
            System.out.println("  - 3 sample doctors (with 7-day availability each)");
            System.out.println("  - 1 sample admin");
            System.out.println("  - 1 sample patient");
            pauseScreen();
        } else {
            System.out.println("\n✗ Cancelled.");
            pauseScreen();
        }
    }
    
    // ==================== SAMPLE DATA ====================
    
    private void addSampleData() {
        // Sample Doctors
        Doctor doc1 = new Doctor("D001", "Fatima Khan", 
            "fatima@hospital.com", "0301-1111111", "doc123",
            "Cardiologist", "PMC-12345", 10, 2000.0);
        
        Doctor doc2 = new Doctor("D002", "Ali Raza",
            "ali@hospital.com", "0302-2222222", "doc123",
            "Dermatologist", "PMC-12346", 8, 1500.0);
        
        Doctor doc3 = new Doctor("D003", "Sara Ahmed",
            "sara@hospital.com", "0303-3333333", "doc123",
            "Pediatrician", "PMC-12347", 12, 1800.0);
        
        // Set availability for next 7 days
        for (int i = 1; i <= 7; i++) {
            doc1.setAvailability(LocalDate.now().plusDays(i),
                               LocalTime.of(9, 0), LocalTime.of(17, 0));
            doc2.setAvailability(LocalDate.now().plusDays(i),
                               LocalTime.of(10, 0), LocalTime.of(16, 0));
            doc3.setAvailability(LocalDate.now().plusDays(i),
                               LocalTime.of(8, 0), LocalTime.of(14, 0));
        }
        
        doctors.add(doc1);
        doctors.add(doc2);
        doctors.add(doc3);
        
        // Sample Admin
        Admin admin = new Admin("A001", "Admin User", "admin@system.com",
                              "0300-0000000", "admin123", "super_admin");
        admins.add(admin);
        
        // Sample Patient
        Patient testPatient = new Patient("P001", "Ahmed Ali",
            "ahmed@email.com", "0300-1234567", "pass123",
            28, "Male", "B+", "Rawalpindi");
        patients.add(testPatient);
        
        System.out.println("✓ Sample data loaded.");
    }
}
