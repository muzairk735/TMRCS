package com.telemedicine.models;

// Common contract for every user type (Patient, Doctor, Admin)
public interface UserInterface {

    boolean login(String email, String password);

    void displayProfile();

    // Overloads — update only what's passed in
    void updateProfile(String name);

    void updateProfile(String name, String email);

    void updateProfile(String name, String email, String phone);
}
