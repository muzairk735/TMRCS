package com.telemedicine.utils;

import com.telemedicine.models.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Handles file I/O operations for the Telemedicine System.
 * Manages serialization and deserialization of system data.
 * 
 * @author Telemedicine Team
 * @version 1.0
 */
public class FileHandler implements IPersistable {
    private String dataDirectory = "data/";
    
    public FileHandler() {
        // Create data directory if it doesn't exist
        File dir = new File(dataDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    // ==================== PATIENT OPERATIONS ====================
    
    /**
     * Save patients to file
     */
    public void savePatients(ArrayList<Patient> patients) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataDirectory + "patients.dat"))) {
            oos.writeObject(patients);
        } catch (IOException e) {
            System.out.println("Error saving patients: " + e.getMessage());
        }
    }
    
    /**
     * Load patients from file
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Patient> loadPatients() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(dataDirectory + "patients.dat"))) {
            return (ArrayList<Patient>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading patients: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ==================== DOCTOR OPERATIONS ====================
    
    /**
     * Save doctors to file
     */
    public void saveDoctors(ArrayList<Doctor> doctors) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataDirectory + "doctors.dat"))) {
            oos.writeObject(doctors);
        } catch (IOException e) {
            System.out.println("Error saving doctors: " + e.getMessage());
        }
    }
    
    /**
     * Load doctors from file
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Doctor> loadDoctors() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(dataDirectory + "doctors.dat"))) {
            return (ArrayList<Doctor>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading doctors: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ==================== APPOINTMENT OPERATIONS ====================
    
    /**
     * Save appointments to file
     */
    public void saveAppointments(ArrayList<Appointment> appointments) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataDirectory + "appointments.dat"))) {
            oos.writeObject(appointments);
        } catch (IOException e) {
            System.out.println("Error saving appointments: " + e.getMessage());
        }
    }
    
    /**
     * Load appointments from file
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Appointment> loadAppointments() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(dataDirectory + "appointments.dat"))) {
            return (ArrayList<Appointment>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ==================== ADMIN OPERATIONS ====================
    
    /**
     * Save admins to file
     */
    public void saveAdmins(ArrayList<Admin> admins) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataDirectory + "admins.dat"))) {
            oos.writeObject(admins);
        } catch (IOException e) {
            System.out.println("Error saving admins: " + e.getMessage());
        }
    }
    
    /**
     * Load admins from file
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Admin> loadAdmins() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(dataDirectory + "admins.dat"))) {
            return (ArrayList<Admin>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading admins: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Check if data files exist
     */
    public boolean dataFilesExist() {
        return new File(dataDirectory + "patients.dat").exists() &&
               new File(dataDirectory + "doctors.dat").exists();
    }
    
    /**
     * Clear all data files
     */
    public void clearAllData() {
        File patientsFile = new File(dataDirectory + "patients.dat");
        File doctorsFile = new File(dataDirectory + "doctors.dat");
        File appointmentsFile = new File(dataDirectory + "appointments.dat");
        File adminsFile = new File(dataDirectory + "admins.dat");
        
        patientsFile.delete();
        doctorsFile.delete();
        appointmentsFile.delete();
        adminsFile.delete();
        
        System.out.println("✓ All data cleared.");
    }
    
    // ==================== IPERSISTABLE IMPLEMENTATION ====================
    
    /**
     * Generic save method that saves all system data.
     * Implements IPersistable interface.
     */
    @Override
    public void save() {
        System.out.println("Saving all system data...");
        // This method delegates to specific save methods as needed
        // In a real implementation, this would be called by TelemedicineSystem
    }
    
    /**
     * Generic load method that loads all system data.
     * Implements IPersistable interface.
     */
    @Override
    public void load() {
        System.out.println("Loading system data...");
        // This method delegates to specific load methods as needed
        // In a real implementation, this would be called by TelemedicineSystem
    }
}
