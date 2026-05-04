package com.telemedicine.models;

/**
 * Common user behavior shared by system users.
 * Implemented by Person, then inherited by its subclasses.
 */
public interface UserInterface {

    // Login contract
    boolean login(String email, String password);

    // Profile display contract
    void displayProfile();

    // Overloaded profile updates
    void updateProfile(String name);

    void updateProfile(String name, String email);

    void updateProfile(String name, String email, String phone);
}
