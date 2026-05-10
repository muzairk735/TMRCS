# Telemedicine & Remote Consultation System

A CLI-based Java application for managing telemedicine appointments, doctor schedules, and patient medical records.

## Project Structure

```
TMRCS/
├── src/
│   └── com/telemedicine/
│       ├── Main.java
│       ├── TelemedicineSystem.java
│       ├── models/
│       │   ├── Person.java (Abstract)
│       │   ├── Patient.java
│       │   ├── Doctor.java
│       │   ├── Admin.java
│       │   ├── Appointment.java
│       │   ├── TimeSlot.java
│       │   ├── Prescription.java
│       │   ├── Medicine.java
│       │   └── MedicalRecord.java
│       └── utils/
│           └── FileHandler.java
├── bin/
├── data/
└── README.md
```

## Requirements

- **Java JDK**: 11 or higher
- **Operating System**: Windows, Linux, or macOS

## Compilation Instructions

### Option 1: Command Line (Windows)

1. Open Command Prompt and navigate to the project directory:
   ```bash
   cd "C:\Users\muzai\OneDrive\Desktop\OOP Semester Project\TMRCS"
   ```

2. Compile all Java files:
   ```bash
   javac -d bin src\com\telemedicine\*.java src\com\telemedicine\models\*.java src\com\telemedicine\utils\*.java
   ```

### Option 2: Command Line (Linux/macOS)

1. Open Terminal and navigate to the project directory:
   ```bash
   cd ~/path/to/TMRCS
   ```

2. Compile all Java files:
   ```bash
   javac -d bin src/com/telemedicine/*.java src/com/telemedicine/models/*.java src/com/telemedicine/utils/*.java
   ```

### Option 3: VS Code (Recommended)

1. Install the "Extension Pack for Java" by Microsoft
2. Open the project folder in VS Code
3. Right-click on `Main.java` and select "Run"
4. Or press `Ctrl+F5` to run directly

## Running the Application

### From Command Line (Windows)

```bash
java -cp bin com.telemedicine.Main
```

### From Command Line (Linux/macOS)

```bash
java -cp bin com.telemedicine.Main
```

## Default Login Credentials

### Patient
- **Email**: ahmed@email.com
- **Password**: pass123

### Doctor
- **Email**: fatima@hospital.com
- **Password**: doc123

### Admin
- **Email**: admin@system.com
- **Password**: admin123

## Features

### Patient Module
✓ User registration and login  
✓ View personal profile and update information  
✓ Search doctors by specialization  
✓ Book appointments with available time slots  
✓ View all appointments (pending, confirmed, completed)  
✓ Cancel appointments  
✓ View medical history and prescriptions  

### Doctor Module
✓ Doctor login and profile management  
✓ Set availability (date/time slots)  
✓ View appointments by status  
✓ Conduct consultations and mark as complete  
✓ Issue prescriptions with medicines  
✓ Manage patient information  

### Admin Module
✓ Admin login  
✓ View all system appointments  
✓ Generate system reports  
✓ View all registered doctors  
✓ View all registered patients  
✓ Monitor system statistics  

## Data Persistence

The system automatically saves data to `.dat` files in the `data/` directory:
- `data/patients.dat` - All patient data
- `data/doctors.dat` - All doctor data
- `data/appointments.dat` - All appointment records
- `data/admins.dat` - Admin accounts

Data is automatically loaded on startup and saved on exit.

## Menu Navigation

### Main Menu
```
1. Patient Login
2. Doctor Login
3. Admin Login
4. New Patient Registration
5. Exit
```

### Patient Dashboard
```
1. View Profile
2. Update Profile
3. Search Doctors
4. Book Appointment
5. View My Appointments
6. Cancel Appointment
7. View Medical History
8. Logout
```

### Doctor Dashboard
```
1. View Profile
2. Set Availability
3. View All Appointments
4. View Pending Appointments
5. View Completed Appointments
6. Conduct Consultation
7. Issue Prescription
8. Logout
```

### Admin Dashboard
```
1. View Profile
2. View All Appointments
3. Generate Report
4. View All Doctors
5. View All Patients
6. Logout
```

## Key OOP Concepts Implemented

### Abstraction
- `Person` is an abstract class serving as the base for Patient, Doctor, and Admin
- Abstract method `displayProfile()` implemented differently by each subclass

### Inheritance
- **Patient** extends Person
- **Doctor** extends Person  
- **Admin** extends Person
- Three-tier hierarchy demonstrating inheritance

### Encapsulation
- Private attributes with public getters and setters
- Data hiding and controlled access to object properties
- All model classes implement `Serializable` for file persistence

### Polymorphism
- **Method Overriding**: Each subclass overrides `displayProfile()` with its own implementation
- **Method Overloading**: `updateProfile()` has multiple versions with different parameters
- **viewAppointments()** in Doctor class has overloaded versions

### Composition
- `Appointment` contains references to `Patient` and `Doctor` objects
- `Patient` has a collection of `Appointment` objects
- `Prescription` contains a collection of `Medicine` objects
- `Doctor` has collections of `TimeSlot` and `Appointment` objects

## Time Slot Booking Logic

- Doctors can set availability with start and end times
- Time slots are automatically validated:
  - Cannot book in the past
  - Cannot book already booked slots
  - Prevents time conflicts
  - Checks availability with proper status tracking

## File Handling

All data is persisted using Java serialization:
```java
// Save data
fileHandler.savePatients(patients);
fileHandler.saveDoctors(doctors);
fileHandler.saveAppointments(appointments);

// Load data
patients = fileHandler.loadPatients();
doctors = fileHandler.loadDoctors();
appointments = fileHandler.loadAppointments();
```

## Exception Handling

The system handles:
- **InvalidInputException**: Invalid date format, out-of-range selections
- **FileNotFoundException**: Missing data files (creates new ones)
- **NumberFormatException**: Non-integer input for numeric fields
- **DateTimeParseException**: Incorrect date/time format

## Testing Scenarios

### Test Case 1: Patient Registration & Login
1. Select "New Patient Registration"
2. Fill in patient details
3. Use new credentials to login
4. View profile to verify registration

### Test Case 2: Doctor Availability & Appointment
1. Login as doctor with email: `fatima@hospital.com`
2. Set availability for upcoming dates
3. Logout and login as patient
4. Search for doctor and book appointment
5. Confirm appointment created

### Test Case 3: Issue Prescription
1. Login as doctor
2. View pending appointments
3. Conduct consultation
4. Issue prescription with medicines
5. Save system (prescription saved to file)

### Test Case 4: Data Persistence
1. Book an appointment as patient
2. Exit the system (data saves automatically)
3. Restart the application
4. Login as same patient
5. Verify appointment is still there

## Troubleshooting

### Issue: "Main class not found"
**Solution**: Ensure all files are compiled correctly to the `bin/` directory

### Issue: "No doctors available"
**Solution**: This is normal on first run - sample data is loaded from saved files. If no saved data exists, sample doctors are created automatically on first startup.

### Issue: "Cannot book appointment in the past"
**Solution**: Ensure you're booking appointments for future dates in DD-MM-YYYY format

### Issue: "Time slot conflicts"
**Solution**: The system prevents overlapping time slots automatically. Set availability with non-overlapping times.

## Code Statistics

- **Total Classes**: 10
- **Total Methods**: 50+
- **Lines of Code**: 2000+
- **Packages**: 3 (models, utils, main)

## Future Enhancements

- GUI using JavaFX or Swing
- Database integration (MySQL, PostgreSQL)
- Email/SMS notifications
- Payment gateway integration
- Video consultation feature
- Appointment reminders
- AI-based symptom checker
- Mobile application

## Notes

- The system uses 24-hour time format (HH:MM)
- Dates follow DD-MM-YYYY format
- Consultation fees are in Pakistani Rupees (Rs.)
- All data is stored locally in serialized format
- The application creates a `data/` directory automatically on first run

---

**Version**: 1.0 - Complete  
**Last Updated**: April 2026  
**Status**: Ready for Testing
