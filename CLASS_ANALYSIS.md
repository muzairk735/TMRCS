# Telemedicine System - Complete Class & Relationship Analysis

## Architecture Overview

- **Total Classes:** 12 (1 Abstract, 11 Concrete)
- **Total Interfaces:** 3 (IUser, IAppointmentViewer, IPersistable)
- **Total Relationships:** 30+ including interface implementations

---

## 1. CLASS & INTERFACE INVENTORY

### All Interfaces in the System

| # | Interface Name | Package | Purpose |
|---|---|---|---|
| 1 | `IUser` | models | Common user operations (login, profile management) |
| 2 | `IAppointmentViewer` | models | Appointment viewing and cancellation operations |
| 3 | `IPersistable` | models | Data persistence contract (save/load) |

### All Classes in the System

| # | Class Name | Package | Type | Purpose |
|---|------------|---------|------|---------|
| 1 | `Person` | models | Abstract Class | Base class for all users |
| 2 | `Patient` | models | Concrete Class | Patient user type |
| 3 | `Doctor` | models | Concrete Class | Doctor user type |
| 4 | `Admin` | models | Concrete Class | Administrator user type |
| 5 | `Appointment` | models | Concrete Class | Appointment records |
| 6 | `TimeSlot` | models | Concrete Class | Doctor availability slots |
| 7 | `Prescription` | models | Concrete Class | Medical prescriptions |
| 8 | `Medicine` | models | Concrete Class | Medicine details |
| 9 | `MedicalRecord` | models | Concrete Class | Patient medical history |
| 10 | `FileHandler` | utils | Concrete Class | Data persistence |
| 11 | `TelemedicineSystem` | com.telemedicine | Concrete Class | Main controller |
| 12 | `Main` | com.telemedicine | Concrete Class | Entry point |

**Total Classes: 12** (1 Abstract, 11 Concrete)  
**Total Interfaces: 3**

---

## 2. INTERFACE DEFINITIONS

### **IUser Interface**
```
Package: com.telemedicine.models
Purpose: Defines common user authentication and profile management operations

Methods:
  + login(email: String, password: String): boolean
  + displayProfile(): void
  + updateProfile(name: String): void
  + updateProfile(name: String, email: String): void
  + updateProfile(name: String, email: String, phone: String): void

Implemented By:
  ├── Person (abstract)
  │   ├── Patient
  │   ├── Doctor
  │   └── Admin

Design Rationale:
  - Guarantees all user types support login functionality
  - Allows polymorphic code to work with IUser references
  - Enforces consistent authentication and profile management across all user types
```

### **IAppointmentViewer Interface**
```
Package: com.telemedicine.models
Purpose: Defines appointment viewing and management operations

Methods:
  + viewAppointments(): void
  + viewAppointments(status: String): void
  + cancelAppointment(appointmentId: String): void

Implemented By:
  ├── Patient (books, views, and cancels own appointments)
  └── Doctor (views and cancels appointments)

Does NOT implement:
  └── Admin (does not directly manage appointments)

Design Rationale:
  - Follows Interface Segregation Principle (ISP)
  - Only includes methods that ALL implementing classes actually use
  - Supports polymorphic handling of appointment-aware user types
```

### **IPersistable Interface**
```
Package: com.telemedicine.models
Purpose: Defines data persistence operations

Methods:
  + save(): void
  + load(): void

Implemented By:
  └── FileHandler (handles all system data persistence)

Design Rationale:
  - Decouples persistence logic from business logic
  - Makes persistence operations explicit in the design
  - Allows for easy switching between persistence strategies (JSON, database, etc.)
```

---

## 3. DETAILED CLASS DEFINITIONS

### ABSTRACT CLASS

#### **Person** (Abstract Base Class)
```
Package: com.telemedicine.models
Implements: Serializable, IUser

Attributes:
  - userId: String (protected)
  - name: String (protected)
  - email: String (protected)
  - phoneNumber: String (protected)
  - password: String (protected)
  - registrationDate: LocalDate (protected)

Methods (IUser Implementation):
  + login(email: String, password: String): boolean
  + displayProfile(): void (abstract - implemented by subclasses)
  + updateProfile(name: String): void
  + updateProfile(name: String, email: String): void
  + updateProfile(name: String, email: String, phone: String): void

Constructors:
  + Person(userId, name, email, phoneNumber, password)

Purpose: 
  - Base class for all user types (Patient, Doctor, Admin)
  - Provides common authentication and profile management
  - Enforces displayProfile() implementation in subclasses
  - Implements IUser interface for polymorphic user handling
```

---

### CONCRETE CLASSES - USER TYPES

#### **Patient** (Extends Person)
```
Package: com.telemedicine.models
Implements: Serializable, IAppointmentViewer

Attributes:
  - age: int
  - gender: String
  - bloodGroup: String
  - address: String
  - medicalHistory: ArrayList<MedicalRecord>
  - appointments: ArrayList<Appointment>

Methods (IAppointmentViewer Implementation):
  + viewAppointments(): void
  + viewAppointments(status: String): void
  + cancelAppointment(appointmentId: String): void

Methods (Patient-specific):
  + bookAppointment(doctor, dateTime, symptoms, mode): void
  + viewMedicalHistory(): void
  + addMedicalRecord(record): void
  + getters/setters

Purpose:
  - Represents a patient in the system
  - Manages personal health data and medical history
  - Implements IAppointmentViewer for appointment management
  - Books appointments and views medical records
```

#### **Doctor** (Extends Person)
```
Package: com.telemedicine.models
Implements: Serializable, IAppointmentViewer

Attributes:
  - specialization: String
  - licenseNumber: String
  - experienceYears: int
  - consultationFee: double
  - availability: ArrayList<TimeSlot>
  - appointments: ArrayList<Appointment>
  - rating: double
  - totalRatings: int

Methods (IAppointmentViewer Implementation):
  + viewAppointments(): void
  + viewAppointments(status: String): void
  + cancelAppointment(appointmentId: String): void

Methods (Doctor-specific):
  + setAvailability(date, startTime, endTime): void
  + getAvailableSlots(date): ArrayList<TimeSlot>
  + conductConsultation(appointment): void
  + issuePrescription(patient, diagnosis, medicines, notes): void
  + addRating(rating): void
  + getters/setters

Purpose:
  - Represents a doctor in the system
  - Manages availability and consultation appointments
  - Implements IAppointmentViewer for appointment operations
  - Issues prescriptions and conducts consultations
```
  + viewAppointments(status): void
  + viewAppointments(): void (Overloads)
  + conductConsultation(appointment): void
  + issuePrescription(patient, diagnosis, medicines, notes): void
  + addAppointment(appointment): void
  + addRating(rating): void
  + getters/setters

Purpose:
  - Represents a doctor in the system
  - Manages availability, appointments, and prescriptions
```

#### **Admin** (Extends Person)
```
Package: com.telemedicine.models
Implements: Serializable

Attributes:
  - adminLevel: String (super_admin, admin)

Methods:
  + Admin(userId, name, email, phone, password, adminLevel)
  + displayProfile(): void (Override)
  + addDoctor(doctorList, doctor): void
  + removeDoctor(doctorList, doctorId): void
  + viewAllAppointments(appointments): void
  + generateReport(doctors, patients, appointments): void
  + getters/setters

Purpose:
  - System administrator
  - Manages doctors and generates reports
```

---

### CONCRETE CLASSES - DOMAIN MODELS

#### **Appointment**
```
Package: com.telemedicine.models
Implements: Serializable

Attributes:
  - appointmentId: String
  - patient: Patient (association - references independent patient)
  - doctor: Doctor (association - references independent doctor)
  - appointmentDateTime: LocalDateTime
  - symptoms: String
  - status: String (PENDING, CONFIRMED, COMPLETED, CANCELLED)
  - consultationMode: String (VIDEO, PHONE, CHAT)
  - createdAt: LocalDateTime
  - prescription: Prescription (0..1 optional)

Methods:
  + Appointment(id, patient, doctor, dateTime, symptoms, mode)
  + confirmAppointment(): void
  + cancelAppointment(reason): void
  + completeAppointment(): void
  + displayAppointmentDetails(): void
  + isWithin24Hours(): boolean
  + getters/setters

Purpose:
  - Represents a scheduled consultation
  - Tracks appointment status and details
```

#### **TimeSlot**
```
Package: com.telemedicine.models
Implements: Serializable

Attributes:
  - slotId: String
  - date: LocalDate
  - startTime: LocalTime
  - endTime: LocalTime
  - isAvailable: boolean
  - doctor: Doctor (reference)

Methods:
  + TimeSlot(slotId, date, startTime, endTime, doctor)
  + markAsBooked(): void
  + markAsAvailable(): void
  + isSlotAvailable(): boolean
  + isConflict(otherSlot): boolean
  + getDurationInMinutes(): int
  + displaySlotInfo(): void
  + getters

Purpose:
  - Manages doctor availability
  - Prevents double-booking and time conflicts
```

#### **Prescription**
```
Package: com.telemedicine.models
Implements: Serializable

Attributes:
  - prescriptionId: String
  - patient: Patient (association - references independent patient)
  - doctor: Doctor (association - references independent doctor)
  - issuedDate: LocalDate
  - diagnosis: String
  - medicines: ArrayList<Medicine> (composition - owns medicine list)
  - additionalNotes: String

Methods:
  + Prescription(id, patient, doctor, diagnosis, medicines, notes)
  + addMedicine(medicine): void
  + displayPrescription(): void
  + generatePrescriptionReport(): void
  + getters/setters

Purpose:
  - Represents a medical prescription
  - Contains list of medicines and diagnosis
```

#### **Medicine**
```
Package: com.telemedicine.models
Implements: Serializable

Attributes:
  - medicineName: String
  - dosage: String
  - frequency: String
  - durationDays: int
  - instructions: String

Methods:
  + Medicine(name, dosage, frequency, duration, instructions)
  + displayMedicineInfo(): void
  + getMedicineDetails(): String
  + getters

Purpose:
  - Represents a medicine in a prescription
  - Contains dosage and usage instructions
```

#### **MedicalRecord**
```
Package: com.telemedicine.models
Implements: Serializable

Attributes:
  - recordId: String
  - patient: Patient (composition)
  - recordDate: LocalDate
  - diagnosis: String
  - treatment: String
  - doctorName: String
  - testResults: ArrayList<String>
  - notes: String

Methods:
  + MedicalRecord(id, patient, diagnosis, treatment, doctorName)
  + displayRecord(): void
  + displayFullRecord(): void
  + addTestResult(result): void
  + updateNotes(notes): void
  + getters

Purpose:
  - Stores patient medical history
  - Contains diagnosis, treatment, and test results
```

---

### UTILITY CLASSES

#### **FileHandler**
```
Package: com.telemedicine.utils
Implements: IPersistable

Attributes:
  - dataDirectory: String = "data/"

Methods (IPersistable Implementation):
  + save(): void (generic save for all data)
  + load(): void (generic load for all data)

Methods (Specific Persistence):
  + savePatients(patients): void
  + loadPatients(): ArrayList<Patient>
  + saveDoctors(doctors): void
  + loadDoctors(): ArrayList<Doctor>
  + saveAppointments(appointments): void
  + loadAppointments(): ArrayList<Appointment>
  + saveAdmins(admins): void
  + loadAdmins(): ArrayList<Admin>
  + dataFilesExist(): boolean
  + clearAllData(): void

Purpose:
  - Handles data persistence (serialization)
  - Saves and loads all system data
```

---

### SYSTEM CLASSES

#### **TelemedicineSystem**
```
Package: com.telemedicine
Implements: (No interface)

Attributes:
  - patients: ArrayList<Patient>
  - doctors: ArrayList<Doctor>
  - admins: ArrayList<Admin>
  - appointments: ArrayList<Appointment>
  - fileHandler: FileHandler
  - scanner: Scanner
  - currentUser: Person
  - currentUserType: String

Methods:
  + TelemedicineSystem()
  + start(): void
  + patientLogin(): void
  + patientDashboard(): void
  + doctorLogin(): void
  + doctorDashboard(): void
  + adminLogin(): void
  + adminDashboard(): void
  + registerPatient(): void
  + bookAppointment(patient): void
  + [30+ other methods for menus and operations]
  - private helper methods for UI and logic

Purpose:
  - Main controller of the system
  - Handles all user interactions and business logic
```

#### **Main**
```
Package: com.telemedicine
Implements: (No interface)

Attributes: (None)

Methods:
  + main(args[]): void

Purpose:
  - Application entry point
  - Creates TelemedicineSystem and starts it
```

---

## 4. RELATIONSHIP MATRIX

### Interface Implementations

```
IUser (Interface)
  └── Implemented by:
      ├── Person (abstract)
      │   ├── Patient
      │   ├── Doctor
      │   └── Admin

IAppointmentViewer (Interface)
  └── Implemented by:
      ├── Patient (manages own appointments)
      └── Doctor (manages patient appointments)

IPersistable (Interface)
  └── Implemented by:
      └── FileHandler (system data persistence)

Serializable (Java Built-in)
  └── Implemented by:
      ├── Person
      ├── Patient
      ├── Doctor
      ├── Admin
      ├── Appointment
      ├── TimeSlot
      ├── Prescription
      ├── Medicine
      └── MedicalRecord
```

### Inheritance Relationships (IS-A)

```
Person (Abstract, implements IUser)
    ├── Patient (implements IAppointmentViewer)
    ├── Doctor (implements IAppointmentViewer)
    └── Admin (core user type)
```

---

### Composition Relationships (HAS-A Strong)

```
Appointment:
  - Contains Prescription (optional, 0..1)

Prescription:
  - Contains ArrayList<Medicine> (required)

MedicalRecord:
  - Contains Patient (required)
```

---

### Aggregation Relationships (HAS-A Weak)

```
Patient:
  ◇ Has many Appointments (can exist independently)
  ◇ Has many MedicalRecords (can exist independently)

Doctor:
  ◇ Has many Appointments (can exist independently)
  ◇ Has many TimeSlots (can exist independently)
```

---

### Association Relationships (USES)

```
Appointment ─────────> Patient (1-to-1)
Appointment ─────────> Doctor (1-to-1)
Prescription ────────> Patient (1-to-1)
Prescription ────────> Doctor (1-to-1)
TimeSlot ────────────> Doctor (Many-to-1)
Prescription ────────> Appointment (1-to-1 optional)
TelemedicineSystem ──> All model classes (Uses)
FileHandler ──────────> All model classes (Uses)
```

---

## 5. COMPLETE RELATIONSHIP TABLE

| From | To | Type | Cardinality | Strength | Description |
|------|-----|------|-------------|----------|-------------|
| **Interface Implementations** |
| Person | IUser | Interface | - | Strong | Person class implements IUser interface |
| Patient | IAppointmentViewer | Interface | - | Strong | Patient implements appointment viewing operations |
| Doctor | IAppointmentViewer | Interface | - | Strong | Doctor implements appointment viewing operations |
| FileHandler | IPersistable | Interface | - | Strong | FileHandler implements persistence operations |
| **Inheritance** |
| Patient | Person | Inheritance | 1:1 | Strong | Patient extends Person |
| Doctor | Person | Inheritance | 1:1 | Strong | Doctor extends Person |
| Admin | Person | Inheritance | 1:1 | Strong | Admin extends Person |
| **Aggregation** |
| Patient | Appointment | Aggregation | 1:* | Weak | Patient has many appointments |
| Patient | MedicalRecord | Aggregation | 1:* | Weak | Patient has medical history |
| Doctor | Appointment | Aggregation | 1:* | Weak | Doctor has many appointments |
| Doctor | TimeSlot | Aggregation | 1:* | Weak | Doctor has availability slots |
| **Association** |
| Appointment | Patient | Association | 1:1 | Weak | Appointment references independent patient |
| Appointment | Doctor | Association | 1:1 | Weak | Appointment references independent doctor |
| Prescription | Patient | Association | 1:1 | Weak | Prescription references independent patient |
| Prescription | Doctor | Association | 1:1 | Weak | Prescription references independent doctor |
| TimeSlot | Doctor | Association | *:1 | Medium | Slot belongs to doctor |
| **Composition** |
| Appointment | Prescription | Composition | 1:0..1 | Strong | Appointment may have prescription |
| Prescription | Medicine | Composition | 1:* | Strong | Prescription contains medicines |
| MedicalRecord | Patient | Composition | 1:1 | Strong | Record belongs to patient |

---

## 6. UML CLASS DIAGRAM (Text Format)

```
                    ┌─────────────────────────────────────┐
                    │      <<interface>>                  │
                    │     Serializable                    │
                    └─────────────────────────────────────┘
                                   ▲
                                   │ implements
                    ┌──────────────┴──────────────┐
                    │                             │
        ┌───────────┴────────────┐      ┌────────┴─────────┐
        │                        │      │                  │
    ┌───────────────────────┐  ...  ┌───────────────────┐
    │   <<abstract>>        │       │  Appointment      │
    │      Person           │       │  implements Ser.  │
    │  implements Ser.      │       ├───────────────────┤
    ├───────────────────────┤       │ - appointmentId   │
    │ # userId: String      │       │ - patient*        │────┐
    │ # name: String        │       │ - doctor*         │    │
    │ # email: String       │       │ - dateTime        │    │
    │ # phoneNumber: String │       │ - symptoms        │    │
    │ # password: String    │       │ - status          │    │
    │ # registrationDate    │       │ - mode            │    │
    ├───────────────────────┤       │ - prescription     │    │
    │ + login(): boolean    │       ├───────────────────┤    │
    │ + {abstract}          │       │ + confirm()       │    │
    │   displayProfile()    │       │ + cancel()        │    │
    │ + updateProfile()     │       │ + complete()      │    │
    └─────────┬─────────────┘       │ + display()       │    │
              │                      │ + isWithin24h()   │    │
         ┌────┼────┬────┐           └───────────────────┘    │
         │    │    │    │                                    │
      ┌──┴─┐ │   │   │                                    │
      │    │ │   │   │                                    │
   ┌─────┐ ┌─────┐ ┌─────┐                                 │
   │     │ │     │ │     │◇────────────────┐               │
   │Pati-│ │ Doctor
   │ent  │ │     │ │Admin │  (aggregation) │               │
   │     │ │     │ │     │                │               │
   └─────┘ └─────┘ └─────┘                │               │
   - age│  - spec │ - level    ┌──────────────────────┐    │
   - gender - expYears           │    TimeSlot        │    │
   - blood │ - fee      │        │ implements Ser.    │    │
   - addr  │ - rating   │        ├──────────────────┐ │    │
   - medH◇─┤           │        │ - slotId       │ │    │
   - appt◇─┤           │        │ - date         │ │    │
         │ - appt◇────┤        │ - startTime    │ │    │
         │ - avail◇──┘        │ - endTime      │ │    │
         │                    │ - isAvailable  │ │    │
         │                    │ - doctor*      │ │    │
         │                    └──────────────┐ │    │
         │                                   │ │    │
    ┌────┴──────────┐                       │ │    │
    │                │                      │ │    │
┌──────────────┐  ┌──────────────┐         │ │    │
│MedicalRecord │  │Prescription  │◄────────┘ │    │
│implements Ser│  │implements Ser│           │    │
├──────────────┤  ├──────────────┤           │    │
│ - recordId   │  │ - prescId    │           │    │
│ - patient*   │◄─┤ - patient*   │◄──────────┘
│ - recordDate │  │ - doctor*    │
│ - diagnosis  │  │ - issueDate  │
│ - treatment  │  │ - diagnosis  │
│ - doctorName │  │ - medicines◇─┼──┐
│ - testResult │  │ - notes      │  │
│ - notes      │  └──────────────┘  │
└──────────────┘                     │
                                     ▼
                            ┌──────────────────┐
                            │    Medicine      │
                            │ implements Ser.  │
                            ├──────────────────┤
                            │ - medicineName   │
                            │ - dosage         │
                            │ - frequency      │
                            │ - durationDays   │
                            │ - instructions   │
                            └──────────────────┘

┌──────────────────────────────────────────────────────┐
│         TelemedicineSystem (Main Controller)         │
├──────────────────────────────────────────────────────┤
│ - patients: ArrayList<Patient>                       │
│ - doctors: ArrayList<Doctor>                         │
│ - admins: ArrayList<Admin>                           │
│ - appointments: ArrayList<Appointment>               │
│ - fileHandler: FileHandler                           │
│ - currentUser: Person                                │
├──────────────────────────────────────────────────────┤
│ + start(): void                                      │
│ + patientLogin(): void                               │
│ + patientDashboard(): void                           │
│ + doctorLogin(): void                                │
│ + doctorDashboard(): void                            │
│ + adminLogin(): void                                 │
│ + adminDashboard(): void                             │
│ + [30+ other operations]                             │
└──────────────────────────────────────────────────────┘
        △
        │ creates & manages
        │
    ┌───────────┐
    │   Main    │
    ├───────────┤
    │ - none    │
    ├───────────┤
    │ + main()  │
    └───────────┘

┌──────────────────────────────────────────┐
│       FileHandler (Utility Class)        │
├──────────────────────────────────────────┤
│ - dataDirectory: String                  │
├──────────────────────────────────────────┤
│ + savePatients()                         │
│ + loadPatients()                         │
│ + saveDoctors()                          │
│ + loadDoctors()                          │
│ + saveAppointments()                     │
│ + loadAppointments()                     │
│ + saveAdmins()                           │
│ + loadAdmins()                           │
│ + dataFilesExist()                       │
│ + clearAllData()                         │
└──────────────────────────────────────────┘

Legend:
  ─────▶  Inheritance (IS-A)
  ◇─────  Aggregation (weak HAS-A)
  *─────  Composition (strong HAS-A)
  ─────▶  Association (USES)
  *      Required reference
  0..1   Optional reference
```

---

## 6. SUMMARY STATISTICS

### Class Distribution
- **Abstract Classes**: 1 (Person)
- **Concrete Classes**: 11
  - User Types: 3 (Patient, Doctor, Admin)
  - Domain Models: 6 (Appointment, TimeSlot, Prescription, Medicine, MedicalRecord)
  - Utility: 1 (FileHandler)
  - System: 2 (TelemedicineSystem, Main)

### Relationships Count
- **Inheritance**: 3 (Patient, Doctor, Admin extend Person)
- **Interface Implementation**: 9 classes implement Serializable
- **Composition**: 7 relationships (strong ownership)
- **Aggregation**: 6 relationships (weak ownership)
- **Association**: 2 relationships (usage)

### Total Relationships: 27

### Code Metrics
- **Total Classes**: 12
- **Total Interfaces**: 0 (but uses Serializable)
- **Total Methods**: 50+
- **Lines of Code**: 2000+
- **Packages**: 3

---

## 7. SERIALIZATION HIERARCHY

```
Serializable (Java Interface)
    ├── Person*
    │   ├── Patient
    │   ├── Doctor
    │   └── Admin
    ├── Appointment
    ├── TimeSlot
    ├── Prescription
    ├── Medicine
    └── MedicalRecord

(*) = Abstract class
Each has: private static final long serialVersionUID = 1L
```

---

## 8. PACKAGE STRUCTURE

```
com.telemedicine
├── Main.java
├── TelemedicineSystem.java
│
├── models/
│   ├── Person.java (abstract)
│   ├── Patient.java
│   ├── Doctor.java
│   ├── Admin.java
│   ├── Appointment.java
│   ├── TimeSlot.java
│   ├── Prescription.java
│   ├── Medicine.java
│   └── MedicalRecord.java
│
└── utils/
    └── FileHandler.java
```

---

## 9. CLASS DEPENDENCY GRAPH

```
Main
  └── TelemedicineSystem
      ├── FileHandler
      │   ├── Patient
      │   ├── Doctor
      │   ├── Admin
      │   └── Appointment
      │
      ├── Person (abstract)
      │   ├── Patient
      │   ├── Doctor
      │   └── Admin
      │
      ├── Appointment
      │   ├── Patient
      │   ├── Doctor
      │   └── Prescription
      │
      ├── TimeSlot
      │   └── Doctor
      │
      ├── Prescription
      │   ├── Patient
      │   ├── Doctor
      │   └── ArrayList<Medicine>
      │
      └── MedicalRecord
          └── Patient
```

---

This comprehensive analysis covers all classes, interfaces, and relationships in the Telemedicine System. The system demonstrates strong OOP principles with clear inheritance hierarchies and well-defined composition relationships.
