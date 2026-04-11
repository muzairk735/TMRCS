# Telemedicine System - Architecture & Class Relationships

## Class Hierarchy (Inheritance)

```
                    ┌─────────────────────┐
                    │  <<abstract>>       │
                    │      Person         │
                    │  implements         │
                    │  Serializable       │
                    ├─────────────────────┤
                    │ - userId: String    │
                    │ - name: String      │
                    │ - email: String     │
                    │ - phoneNumber: Str  │
                    │ - password: String  │
                    │ - regDate: LocalDate│
                    ├─────────────────────┤
                    │ + login(): boolean  │
                    │ + displayProfile()* │
                    │ + updateProfile()   │
                    └─────────────────────┘
                           △
                    ┌──────┼──────┐
                    │      │      │
            ┌───────┴──┐   │   ┌──┴────────┐
            │          │   │   │           │
        ┌────────┐ ┌────────┐ ┌───────┐
        │Patient │ │ Doctor │ │ Admin │
        └────────┘ └────────┘ └───────┘

* = abstract method (must override in subclass)
```

## Class Composition & Relationships

### Appointment Class
```
┌──────────────────────────────────────┐
│          Appointment                 │
│     implements Serializable          │
├──────────────────────────────────────┤
│ - appointmentId: String              │
│ - patient: Patient ────┐             │  (Composition)
│ - doctor: Doctor   ────┼──┐          │  (Composition)
│ - dateTime: LocalDateTime│           │
│ - symptoms: String      ││           │
│ - status: String        ││           │
│ - mode: String          ││           │
│ - prescription: Presc ──┴┼──┐        │
│                         ││  │
├──────────────────────────────────────┤
│ + confirmAppointment()                │
│ + cancelAppointment()                 │
│ + completeAppointment()               │
│ + displayDetails()                    │
│ + isWithin24Hours(): boolean          │
└──────────────────────────────────────┘
```

### Patient Class
```
┌──────────────────────────────────┐
│         Patient                  │
│ extends Person                   │
│ implements Serializable          │
├──────────────────────────────────┤
│ - age: int                       │
│ - gender: String                 │
│ - bloodGroup: String             │
│ - address: String                │
│ - appointments: ArrayList<Appt>  │◇─────┐
│ - medHistory: ArrayList<Record>  │◇─────┤ (Aggregation)
├──────────────────────────────────┤     │
│ + bookAppointment()              │     │
│ + viewAppointments()             │     │
│ + viewMedicalHistory()           │     │
│ + cancelAppointment()            │     │
└──────────────────────────────────┘     │
                                         │
        ┌────────────────────────────────┘
        │
    ┌───┴────────────┐
    │                │
┌──────────┐   ┌──────────┐
│Appointment│   │Medical   │
│           │   │Record    │
└──────────┘   └──────────┘
```

### Doctor Class
```
┌──────────────────────────────────┐
│         Doctor                   │
│ extends Person                   │
│ implements Serializable          │
├──────────────────────────────────┤
│ - specialization: String         │
│ - licenseNumber: String          │
│ - experienceYears: int           │
│ - consultationFee: double        │
│ - availability: ArrayList<Slot>  │◇────┐
│ - appointments: ArrayList<Appt>  │◇────┤ (Aggregation)
│ - rating: double                 │     │
│ - totalRatings: int              │     │
├──────────────────────────────────┤     │
│ + setAvailability()              │     │
│ + getAvailableSlots()            │     │
│ + viewAppointments(status)       │     │
│ + viewAppointments()¹            │     │
│ + conductConsultation()          │     │
│ + issuePrescription()            │     │
│ + addRating()                    │     │
└──────────────────────────────────┘     │
                                         │
        ┌────────────────────────────────┘
        │
    ┌───┴────────────┐
    │                │
┌──────────┐   ┌──────────┐
│TimeSlot  │   │Appointment
│          │   │
└──────────┘   └──────────┘

¹ = Method overloading
```

### TimeSlot Class
```
┌──────────────────────────────────┐
│         TimeSlot                 │
│     implements Serializable      │
├──────────────────────────────────┤
│ - slotId: String                 │
│ - date: LocalDate                │
│ - startTime: LocalTime           │
│ - endTime: LocalTime             │
│ - isAvailable: boolean           │
│ - doctor: Doctor                 │─────► (Reference)
├──────────────────────────────────┤
│ + markAsBooked()                 │
│ + markAsAvailable()              │
│ + isSlotAvailable(): boolean     │
│ + isConflict(): boolean          │
│ + getDurationInMinutes(): int    │
└──────────────────────────────────┘
```

### Prescription Class
```
┌──────────────────────────────────┐
│      Prescription                │
│   implements Serializable        │
├──────────────────────────────────┤
│ - prescriptionId: String         │
│ - patient: Patient          ────┐│ (Composition)
│ - doctor: Doctor            ────┼┼ (Composition)
│ - issuedDate: LocalDate         │
│ - diagnosis: String             │
│ - medicines: ArrayList<Med> ─┐  │
│ - additionalNotes: String       │
├──────────────────────────────────┤
│ + addMedicine()                  │
│ + displayPrescription()          │
│ + generateReport()               │
└──────────────────────────────────┘
        ▲
        │ (Composition)
        │
    ┌───┴─────────────┐
    │                 │
┌────────┐      ┌──────────┐
│Medicine│      │Appointment
└────────┘      └──────────┘
```

### Medicine Class
```
┌──────────────────────────────────┐
│         Medicine                 │
│    implements Serializable       │
├──────────────────────────────────┤
│ - medicineName: String           │
│ - dosage: String                 │
│ - frequency: String              │
│ - durationDays: int              │
│ - instructions: String           │
├──────────────────────────────────┤
│ + displayMedicineInfo()          │
│ + getMedicineDetails(): String   │
└──────────────────────────────────┘
```

### MedicalRecord Class
```
┌──────────────────────────────────┐
│      MedicalRecord               │
│   implements Serializable        │
├──────────────────────────────────┤
│ - recordId: String               │
│ - patient: Patient          ────┐│ (Composition)
│ - recordDate: LocalDate         │
│ - diagnosis: String             │
│ - treatment: String             │
│ - doctorName: String            │
│ - testResults: ArrayList<String>│
│ - notes: String                 │
├──────────────────────────────────┤
│ + displayRecord()                │
│ + displayFullRecord()            │
│ + addTestResult()                │
│ + updateNotes()                  │
└──────────────────────────────────┘
```

### Admin Class
```
┌──────────────────────────────────┐
│         Admin                    │
│ extends Person                   │
│ implements Serializable          │
├──────────────────────────────────┤
│ - adminLevel: String             │
├──────────────────────────────────┤
│ + addDoctor()                    │
│ + removeDoctor()                 │
│ + viewAllAppointments()          │
│ + generateReport()               │
└──────────────────────────────────┘
```

## Relationship Types

### 1. **Inheritance (IS-A)**
```
Patient    IS-A    Person
Doctor     IS-A    Person
Admin      IS-A    Person
```

### 2. **Composition (HAS-A) - Strong**
```
Appointment    HAS-A    Patient        (Can't exist without)
Appointment    HAS-A    Doctor         (Can't exist without)
Prescription   HAS-A    Patient        (Can't exist without)
Prescription   HAS-A    Doctor         (Required)
Prescription   HAS-A    ArrayList<Medicine>
MedicalRecord  HAS-A    Patient        (Required)
```

### 3. **Aggregation (HAS-A) - Weak**
```
Patient        HAS-MANY    Appointment        (Can exist independently)
Patient        HAS-MANY    MedicalRecord      (Can exist independently)
Doctor         HAS-MANY    Appointment        (Can exist independently)
Doctor         HAS-MANY    TimeSlot           (Can exist independently)
Prescription   HAS-MANY    Medicine           (Can exist independently)
```

### 4. **Association (Uses)**
```
Appointment  ──────────────────>  Prescription    (1-to-1 Optional)
TimeSlot     ──────────────────>  Doctor          (Many-to-1)
```

## Multiplicity Summary

| From | To | Relationship | Type |
|------|-----|--------------|------|
| Patient | Appointment | 1 → * | Aggregation |
| Patient | MedicalRecord | 1 → * | Aggregation |
| Doctor | Appointment | 1 → * | Aggregation |
| Doctor | TimeSlot | 1 → * | Aggregation |
| Appointment | Patient | * → 1 | Composition |
| Appointment | Doctor | * → 1 | Composition |
| Appointment | Prescription | 1 → 0..1 | Composition |
| Prescription | Medicine | 1 → * | Composition |
| Prescription | Patient | * → 1 | Composition |
| Prescription | Doctor | * → 1 | Composition |

## Data Flow

### Patient Registration Flow
```
User Input
    ↓
Patient.constructor()
    ↓
Add to ArrayList<Patient>
    ↓
FileHandler.savePatients()
    ↓
patients.dat (File)
```

### Appointment Booking Flow
```
User selects Doctor
    ↓
User selects TimeSlot
    ↓
Patient.bookAppointment()
    ↓
Create Appointment object
    ↓
Add to Patient.appointments
    ↓
Add to Doctor.appointments
    ↓
Mark TimeSlot as booked
    ↓
Add to system appointments list
    ↓
FileHandler.saveAppointments()
```

### Prescription Issuance Flow
```
Doctor selects Patient
    ↓
Doctor enters Diagnosis
    ↓
Doctor adds Medicines
    ↓
Doctor.issuePrescription()
    ↓
Create Prescription object
    ↓
Prescription contains:
  → Patient reference
  → Doctor reference
  → ArrayList<Medicine>
    ↓
FileHandler.savePrescriptions()
```

## Serialization

All model classes implement `Serializable` with `serialVersionUID`:
```
Person.serialVersionUID = 1L
Patient.serialVersionUID = 1L
Doctor.serialVersionUID = 1L
Admin.serialVersionUID = 1L
Appointment.serialVersionUID = 1L
TimeSlot.serialVersionUID = 1L
Prescription.serialVersionUID = 1L
Medicine.serialVersionUID = 1L
MedicalRecord.serialVersionUID = 1L
```

This allows objects to be:
- Saved to files
- Loaded from files
- Transmitted over networks (if needed in future)

---

**Total Classes**: 10  
**Total Relationships**: 15+  
**Total Methods**: 50+  
**Lines of Code**: 2000+
