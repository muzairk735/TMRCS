# Telemedicine & Remote Consultation System (TMRCS)

A comprehensive Java-based telemedicine platform that enables patients to schedule appointments with doctors, manage medical records, and conduct remote consultations through multiple channels.

## Table of Contents

- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [System Architecture](#system-architecture)
- [User Roles](#user-roles)
- [Technology Stack](#technology-stack)

## Features

### Core Functionality
- **Appointment Management** — Patients can book, view, and cancel appointments with doctors
- **Multiple Consultation Modes** — Support for VIDEO, PHONE, and CHAT consultations
- **Medical Records** — Track patient medical history, diagnoses, and prescriptions
- **Prescription Management** — Doctors can issue prescriptions during consultations
- **Messaging System** — In-app messaging for consultations and communication
- **User Management** — Role-based access control for Patients, Doctors, and Admins

### User Roles

#### Patient
- View available doctors and their schedules
- Book and manage appointments
- View medical history and prescriptions
- Participate in remote consultations
- Send and receive messages

#### Doctor
- Manage appointment schedule and availability
- View patient information and medical history
- Conduct remote consultations
- Issue prescriptions
- Communicate with patients

#### Admin
- Manage user accounts (create, modify, deactivate)
- Monitor system activity
- Reset system data
- Manage sample data for testing

## Project Structure

```
TMRCS/
├── src/
│   └── com/telemedicine/
│       ├── app/               # Application entry point and main controller
│       │   ├── Main.java      # JVM entry point
│       │   ├── TelemedicineSystem.java  # Top-level application controller
│       │   └── AppContext.java # Shared application state
│       ├── models/            # Core data models
│       │   ├── Person.java    # Base class for users
│       │   ├── Patient.java
│       │   ├── Doctor.java
│       │   ├── Admin.java
│       │   ├── Appointment.java
│       │   ├── MedicalRecord.java
│       │   ├── Prescription.java
│       │   ├── Medicine.java
│       │   ├── Message.java
│       │   ├── TimeSlot.java
│       │   └── UserInterface.java # Base interface for user types
│       ├── ui/                # Menu handlers for user interactions
│       │   ├── PatientMenuHandler.java
│       │   ├── DoctorMenuHandler.java
│       │   ├── AdminMenuHandler.java
│       │   └── UIHelper.java  # Utility methods for UI
│       └── utils/             # Utility classes
│           ├── DataManager.java    # Handles data persistence
│           └── FileHandler.java    # File I/O operations
├── data/                      # Data storage directory (serialized objects)
├── bin/                       # Compiled bytecode
└── README.md
```

## Getting Started

### Prerequisites
- Java 8 or higher
- JDK installed and configured

### Compilation

Navigate to the project root and compile:

```bash
javac -d bin src/com/telemedicine/app/*.java \
              src/com/telemedicine/models/*.java \
              src/com/telemedicine/ui/*.java \
              src/com/telemedicine/utils/*.java
```

Or use your IDE's build tools:
- **Eclipse**: Right-click project → Build Project
- **IntelliJ**: Build → Build Project
- **VS Code**: Use the provided build tasks

### Running the Application

From the project root:

```bash
java -cp bin com.telemedicine.app.Main
```

On first run, the system will initialize with sample data including doctors, patients, and administrators.

## Usage

### Main Menu

Upon startup, you'll see the main menu with options to:
1. **Login as Patient**
2. **Login as Doctor**
3. **Login as Admin**
4. **Exit**

### Patient Workflow
1. Login with patient credentials
2. Browse available doctors
3. View available time slots
4. Book an appointment
5. Wait for doctor confirmation
6. Participate in consultation
7. View prescriptions and medical records

### Doctor Workflow
1. Login with doctor credentials
2. View scheduled appointments
3. Review patient medical history
4. Conduct consultation with patient
5. Issue prescriptions if needed

### Admin Workflow
1. Login with admin credentials
2. Manage user accounts
3. Reset system data (if needed)
4. View system logs

## System Architecture

### Design Patterns

- **MVC Pattern** — Separation of models, UI handlers, and data management
- **Inheritance** — `Person` class serves as base for all user types
- **Interfaces** — `AppointmentViewerInterface`, `UserInterface`, `PersistableInterface`
- **Singleton Pattern** — `AppContext` for shared application state
- **Data Persistence** — Java serialization for saving/loading data

### Data Flow

```
Main.java
    ↓
TelemedicineSystem (wiring & bootstrap)
    ↓
AppContext (shared state)
    ↓
Menu Handlers (PatientMenuHandler, DoctorMenuHandler, AdminMenuHandler)
    ↓
Models (Patient, Doctor, Appointment, etc.)
    ↓
DataManager (persistence layer)
    ↓
FileHandler (disk I/O)
```

## Technology Stack

- **Language**: Java
- **Data Persistence**: Java Object Serialization
- **Architecture**: Command-line interface (menu-driven)
- **Paradigm**: Object-Oriented Programming (OOP)

## Key Classes

| Class | Purpose |
|-------|---------|
| `TelemedicineSystem` | Main application controller and orchestrator |
| `AppContext` | Centralized storage for all application data |
| `Person` | Base class for all users in the system |
| `Patient` / `Doctor` / `Admin` | Specialized user types with role-specific functionality |
| `Appointment` | Models a consultation booking |
| `DataManager` | Handles all persistence operations |
| `UIHelper` | Provides common UI utilities (input validation, formatting) |

## Sample Credentials

On first run, the system seeds sample users. Typical default credentials:
- **Admin**: Usually an admin user with elevated privileges
- **Doctor**: Dr. Jane Smith, Dr. Michael Johnson (examples)
- **Patient**: John Doe, Sarah Williams (examples)

Check the data files after first run or console output for exact credentials.

## Future Enhancements

- Video/audio call integration
- Email notifications
- Payment processing
- Advanced scheduling algorithms
- Mobile app companion
- Database integration (MySQL, PostgreSQL)
- REST API for external integration

## License

This is an educational project developed as part of an OOP semester course.

---

**For support or questions**, review the inline documentation in each source file. The codebase includes comprehensive JavaDoc comments throughout.
