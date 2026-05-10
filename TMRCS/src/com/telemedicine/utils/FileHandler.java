package com.telemedicine.utils;

import com.telemedicine.models.*;
import java.io.*;
import java.util.ArrayList;

public class FileHandler implements PersistableInterface {
    private static final String DATA_DIR = "data/";

    private final ArrayList<String> dataFiles;

    public FileHandler() {
        dataFiles = new ArrayList<>();
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public <T extends Serializable> void save(String filename, ArrayList<T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_DIR + filename))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.out.println("Error saving " + filename + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> ArrayList<T> load(String filename, Class<T> clazz) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_DIR + filename))) {
            return (ArrayList<T>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading " + filename + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void savePatients(ArrayList<Patient> patients) {
        save("patients.dat", patients);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Patient> loadPatients() {
        return (ArrayList<Patient>) load("patients.dat", Patient.class);
    }

    public void saveDoctors(ArrayList<Doctor> doctors) {
        save("doctors.dat", doctors);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Doctor> loadDoctors() {
        return (ArrayList<Doctor>) load("doctors.dat", Doctor.class);
    }

    public void saveAppointments(ArrayList<Appointment> appointments) {
        save("appointments.dat", appointments);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Appointment> loadAppointments() {
        return (ArrayList<Appointment>) load("appointments.dat", Appointment.class);
    }

    public void saveAdmins(ArrayList<Admin> admins) {
        save("admins.dat", admins);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Admin> loadAdmins() {
        return (ArrayList<Admin>) load("admins.dat", Admin.class);
    }

    public boolean dataFilesExist() {
        return new File(DATA_DIR + "patients.dat").exists() &&
                new File(DATA_DIR + "doctors.dat").exists();
    }

    public void clearAllData() {
        String[] files = {"patients.dat", "doctors.dat", "appointments.dat", "admins.dat"};
        for (String file : files) {
            new File(DATA_DIR + file).delete();
        }
        System.out.println("✓ All data cleared.");
    }

    @Override
    public void saveAll() {
        System.out.println("Saving all system data...");
    }

    @Override
    public void loadAll() {
        System.out.println("Loading system data...");
    }
}