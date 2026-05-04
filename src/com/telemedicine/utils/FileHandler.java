package com.telemedicine.utils;

import com.telemedicine.models.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Handles file-based data storage for the system.
 * Keeps persistence logic separate from the main system class.
 */
public class FileHandler implements PersistableInterface {
    // Folder used for serialized data files
    private String dataDirectory = "data/";

    public FileHandler() {
        File dir = new File(dataDirectory);
        if (!dir.exists()) {
            // Create data folder on first run
            dir.mkdirs();
        }
    }

    // Saves all patients to file
    public void savePatients(ArrayList<Patient> patients) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataDirectory + "patients.dat"))) {
            oos.writeObject(patients);
        } catch (IOException e) {
            System.out.println("Error saving patients: " + e.getMessage());
        }
    }

    // Loads patients, or returns an empty list if no file exists yet
    @SuppressWarnings("unchecked")
    public ArrayList<Patient> loadPatients() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(dataDirectory + "patients.dat"))) {
            return (ArrayList<Patient>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>(); // first run — no file yet
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading patients: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Saves all doctors to file
    public void saveDoctors(ArrayList<Doctor> doctors) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataDirectory + "doctors.dat"))) {
            oos.writeObject(doctors);
        } catch (IOException e) {
            System.out.println("Error saving doctors: " + e.getMessage());
        }
    }

    // Loads doctors from storage
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

    // Saves all appointments to file
    public void saveAppointments(ArrayList<Appointment> appointments) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataDirectory + "appointments.dat"))) {
            oos.writeObject(appointments);
        } catch (IOException e) {
            System.out.println("Error saving appointments: " + e.getMessage());
        }
    }

    // Loads appointments from storage
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

    // Saves all admin accounts
    public void saveAdmins(ArrayList<Admin> admins) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataDirectory + "admins.dat"))) {
            oos.writeObject(admins);
        } catch (IOException e) {
            System.out.println("Error saving admins: " + e.getMessage());
        }
    }

    // Loads admin accounts
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

    // Quick check for initial data files
    public boolean dataFilesExist() {
        return new File(dataDirectory + "patients.dat").exists() &&
                new File(dataDirectory + "doctors.dat").exists();
    }

    // Deletes all saved data files
    public void clearAllData() {
        new File(dataDirectory + "patients.dat").delete();
        new File(dataDirectory + "doctors.dat").delete();
        new File(dataDirectory + "appointments.dat").delete();
        new File(dataDirectory + "admins.dat").delete();
        System.out.println("✓ All data cleared.");
    }

    @Override
    // Placeholder for interface contract
    public void save() {
        System.out.println("Saving all system data...");
    }

    @Override
    // Placeholder for interface contract
    public void load() {
        System.out.println("Loading system data...");
    }
}
