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
        System.out.println("║  5. Exit                               ║");
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
                System.out.println("║  7. View Medical History               ║");
                System.out.println("║  8. Logout                             ║");
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
                        patient.viewMedicalHistory();
                        pauseScreen();
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
                System.out.println("║  8. Logout                             ║");
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
                doctor.conductConsultation(apt);
                pauseScreen();
                return;
            }
        }
        
        System.out.println("✗ Appointment not found.");
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
                System.out.println("║  1. View Profile                       ║");
                System.out.println("║  2. View All Appointments              ║");
                System.out.println("║  3. Generate Report                    ║");
                System.out.println("║  4. View All Doctors                   ║");
                System.out.println("║  5. View All Patients                  ║");
                System.out.println("║  6. Logout                             ║");
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
