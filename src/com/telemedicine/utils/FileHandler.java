package com.telemedicine.utils;

import com.telemedicine.models.*;
import java.io.*;
import java.util.ArrayList;

public class FileHandler implements PersistableInterface {
    private String dataDirectory = "data/";
    
    public FileHandler() {
        File dir = new File(dataDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    // PATIENT OPERATIONS
    
    public void savePatients(ArrayList<Patient> patients) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataDirectory + "patients.dat"))) {
            oos.writeObject(patients);
        } catch (IOException e) {
            System.out.println("Error saving patients: " + e.getMessage());
        }
    }
    
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
    
    // DOCTOR OPERATIONS
    public void saveDoctors(ArrayList<Doctor> doctors) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataDirectory + "doctors.dat"))) {
            oos.writeObject(doctors);
        } catch (IOException e) {
            System.out.println("Error saving doctors: " + e.getMessage());
        }
    }
    
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
    
    // APPOINTMENT OPERATIONS 
    
    public void saveAppointments(ArrayList<Appointment> appointments) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataDirectory + "appointments.dat"))) {
            oos.writeObject(appointments);
        } catch (IOException e) {
            System.out.println("Error saving appointments: " + e.getMessage());
        }
    }
    
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
    
    //  ADMIN OPERATIONS 
    
    public void saveAdmins(ArrayList<Admin> admins) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataDirectory + "admins.dat"))) {
            oos.writeObject(admins);
        } catch (IOException e) {
            System.out.println("Error saving admins: " + e.getMessage());
        }
    }
    
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
    
    //  UTILITY METHODS 
    public boolean dataFilesExist() {
        return new File(dataDirectory + "patients.dat").exists() &&
               new File(dataDirectory + "doctors.dat").exists();
    }
    
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
    
    // IPERSISTABLE IMPLEMENTATION 
    @Override
    public void save() {
        System.out.println("Saving all system data...");
        
    }
    
    @Override
    public void load() {
        System.out.println("Loading system data...");
        
    }
}
