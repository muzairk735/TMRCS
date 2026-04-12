# Telemedicine System - Issues Identified

**Date:** April 12, 2026  
**Status:** Needs Fixing  
**Priority Levels:** HIGH (must fix), MEDIUM (should fix), LOW (nice to fix)

---

## Issue #1: Circular References & Object Duplication

**Priority:** HIGH  
**Category:** Design/Serialization Risk  
**Location:** Appointment creation flow (Patient.java, Doctor.java, TelemedicineSystem.java)

**Description:**
When an Appointment is created:
1. Added to Patient.appointments
2. Added to Doctor.appointments (via doctor.addAppointment())
3. Added to TelemedicineSystem.appointments

The problem: Each reference holds the full Appointment object with embedded Patient and Doctor objects. When serialized/deserialized, this creates duplicate instances of Patient and Doctor.

**Example:**
```java
// In Patient.bookAppointment()
Appointment appointment = new Appointment(appointmentId, this, doctor, ...);
this.appointments.add(appointment);  // Full object stored
doctor.addAppointment(appointment);  // Same object stored
// In TelemedicineSystem
appointments.add(newAppointment);    // Same object stored again (but with duplicates!)
```

**Impact:**
- After load() from disk, object identity is lost
- Three separate copies of Appointment exist instead of one
- Updates to one copy don't reflect in others
- Inconsistent state during runtime

**Solution (for later):**
- Use IDs instead of object references for associations
- Implement a proper ORM or persistence layer
- Use weak references or lazy loading
- Consider UUID-based relationships

---

## Issue #2: No Direct Prescription Tracking

**Priority:** MEDIUM  
**Category:** Design/Query Limitation  
**Location:** Patient.java, Doctor.java

**Description:**
Current design:
- Prescription object references Patient and Doctor
- Patient has NO ArrayList<Prescription>
- Doctor has NO ArrayList<Prescription>

Consequence: Prescriptions are only accessible through Appointment.prescription

**Code Example:**
```java
// Patient can't directly query their prescriptions
// Must iterate through appointments to find prescriptions
for (Appointment apt : patient.appointments) {
    if (apt.prescription != null) { /* found it */ }
}
```

**Impact:**
- No direct way to list all prescriptions for a patient
- No direct way to list all prescriptions issued by a doctor
- Limited query capabilities
- Business logic is scattered

**Solution (for later):**
- Add `ArrayList<Prescription> prescriptions` to Patient
- Add `ArrayList<Prescription> issuedPrescriptions` to Doctor
- Create methods: `patient.viewPrescriptions()`, `doctor.viewIssuedPrescriptions()`
- Maintain bidirectional consistency

---

## Issue #3: Medical Record Composition Semantics Unclear

**Priority:** LOW  
**Category:** Documentation/Design Clarity  
**Location:** Patient.java, MedicalRecord.java

**Description:**
Current implementation:
- MedicalRecord has reference to Patient: `private Patient patient;`
- Patient has ArrayList<MedicalRecord>: `private ArrayList<MedicalRecord> medicalHistory;`

The relationship is correct (composition) but semantically unclear:
- Patient OWNS MedicalRecords (composition from Patient's perspective)
- MedicalRecord BELONGS TO Patient (back-reference only)

**Code Example:**
```java
// This is correct:
MedicalRecord record = new MedicalRecord(id, patient, ...);
patient.addMedicalRecord(record);

// But the Patient reference in MedicalRecord is just for access,
// not for ownership transfer
```

**Impact:**
- Low - functionality is correct
- Design intent is unclear from code alone
- Could confuse future developers

**Solution (for later):**
- Add detailed Javadoc explaining the relationship
- Consider renaming: `MedicalRecord(id, diagnosis, treatment, doctorName)` 
- Separate the "belongs to which patient" lookup into a different concern
- Document: "Patient owns MedicalRecords via composition; MedicalRecord has a reference back to Patient for convenience"

---

## Issue #4: Appointment Not Auto-Synced Across Collections

**Priority:** MEDIUM  
**Category:** Data Consistency Risk  
**Location:** Patient.java, Doctor.java, TelemedicineSystem.java

**Description:**
When an Appointment is booked:
1. local bookAppointment() creates Appointment and adds to Patient.appointments
2. Also calls doctor.addAppointment() to add to Doctor.appointments
3. TelemedicineSystem retrieves and adds to its own appointments list

If anyone modifies Appointment through one reference, others don't see the change.

**Code Example:**
```java
// In Patient.bookAppointment()
Appointment apt = new Appointment(...);
this.appointments.add(apt);           // Reference #1
doctor.addAppointment(apt);           // Reference #2
// Later in TelemedicineSystem
appointments.add(apt);                // Reference #3

// Problem: if you do
apt.completeAppointment();            // Updates apt
// References in other collections don't update because it's same object
// but if serialized/deserialized, they become different objects!
```

**Impact:**
- MEDIUM - Potential inconsistency bugs
- Hard to debug state issues
- Batch updates might miss some collections
- Serialization amplifies the problem

**Solution (for later):**
- Use an Observer pattern to sync updates
- Create an AppointmentManager service
- Use weak references with a central registry
- Implement AppointmentListener interface
- Store appointments only in TelemedicineSystem, reference by ID in Patient/Doctor

---

## Issue #5: IUser Interface Completeness

**Priority:** LOW  
**Category:** Interface Design  
**Location:** Person.java, IUser.java, Admin.java

**Description:**
Actually, this is implemented CORRECTLY. No issue here.

IUser defines:
- login() ✅
- displayProfile() ✅ (abstract in Person)
- updateProfile() ✅ (3 overloads)

Admin implements all of these via inheritance from Person.

**Resolution:** This is working as intended. No fix needed.

---

## Issue #6: TimeSlot-Doctor Relationship Clarity

**Priority:** LOW  
**Category:** Design Documentation  
**Location:** TimeSlot.java, Doctor.java

**Description:**
TimeSlot holds a reference to Doctor: `private Doctor doctor;`

But the relationship semantics could be clearer:
- Is this a strong association or weak reference?
- What happens when a Doctor is deleted? Should TimeSlots be orphaned?
- Can TimeSlots exist without a Doctor in the business logic?

**Code Example:**
```java
public TimeSlot(String slotId, LocalDate date, LocalTime startTime,
               LocalTime endTime, Doctor doctor) {
    this.doctor = doctor;  // What's the ownership model here?
}
```

**Impact:**
- LOW - Works correctly currently
- Cascading deletes aren't implemented
- Unclear if this needs cleanup logic

**Solution (for later):**
- Document: "TimeSlot is managed by Doctor; TimeSlots are cleaned up when Doctor is removed"
- Implement cleanup: `doctor.removeTimeSlot(slotId)` on time expiration
- Consider: Should TimeSlots be owned by Doctor or by TelemedicineSystem?

---

## Issue #7: No Cascade Delete/Update Logic

**Priority:** MEDIUM  
**Category:** Business Logic  
**Location:** People all model classes with relationships

**Description:**
No cascade behavior is implemented:
- If Patient is deleted, what happens to their Appointments?
- If Doctor is deleted, what happens to their TimeSlots and Appointments?
- If Appointment is deleted, Prescription becomes orphaned

**Scenarios Missing:**
```
DELETE Patient → Should cascade delete their Appointments
DELETE Doctor → Should cascade delete their TimeSlots
DELETE Appointment → Should cascade delete Prescription
DELETE Prescription → Should remove from related Appointment
```

**Impact:**
- MEDIUM - Could cause orphaned data
- No cleanup on user removal
- Data integrity issues possible

**Solution (for later):**
- Implement `onDelete()` cascade methods
- Create a DataConsistencyManager
- Add cleanup logic to admin operations
- Traverse relationships and clean orphaned data

---

## Issue #8: Serialization with Embedded Objects

**Priority:** HIGH  
**Category:** Serialization/Deserialization  
**Location:** All classes with Serializable

**Description:**
When serializing with ObjectOutputStream, embedded objects (Patient, Doctor in Appointment) are serialized inline.

**Problem Flow:**
```
Before serialize:
  Appointment apt {
    Patient p1
    Doctor d1
  }
  Both stored in multiple collections

After serialize → deserialize:
  Appointment apt {
    Patient p1_copy (NEW instance!)
    Doctor d1_copy (NEW instance!)
  }
  
Now p1 != p1_copy  (different memory addresses)
And d1 != d1_copy  (different memory addresses)
```

**Impact:**
- HIGH - Object identity breaks
- Equality checks fail after load
- Collections become inconsistent
- Hard to track down bugs

**Solution (for later):**
- Implement custom serialization (writeObject/readObject)
- Use ID-based references instead of object references
- Implement object graph support with a persistence manager
- Use transient fields for cross-object references

---

## Summary Table

| # | Issue | Priority | Severity | Fix Effort | Status |
|---|-------|----------|----------|-----------|--------|
| 1 | Circular References | HIGH | CRITICAL | Medium | ⏳ Pending |
| 2 | No Prescription Tracking | MEDIUM | MAJOR | Low | ⏳ Pending |
| 3 | Medical Record Semantics | LOW | MINOR | Low | ⏳ Pending |
| 4 | Appointment Sync | MEDIUM | MAJOR | Medium | ⏳ Pending |
| 5 | IUser Completeness | LOW | NONE | None | ✅ No Fix Needed |
| 6 | TimeSlot Clarity | LOW | MINOR | Low | ⏳ Pending |
| 7 | No Cascade Delete | MEDIUM | MAJOR | Medium | ⏳ Pending |
| 8 | Serialization Embedding | HIGH | CRITICAL | High | ⏳ Pending |

---

## Quick Fix Priority Order

**Phase 1 (Must Do):**
1. Issue #8 - Implement proper serialization
2. Issue #1 - Refactor to use IDs for associations

**Phase 2 (Should Do):**
3. Issue #4 - Add sync mechanism for appointments
4. Issue #7 - Implement cascade delete logic
5. Issue #2 - Add prescription tracking

**Phase 3 (Nice to Do):**
6. Issue #3 - Improve documentation
7. Issue #6 - Clarify TimeSlot ownership

---

**Last Updated:** April 12, 2026  
**Next Review:** After fixes are implemented
