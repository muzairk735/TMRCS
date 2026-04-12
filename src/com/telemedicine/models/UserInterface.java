package com.telemedicine.models;

/**
 * Interface for all user types in the Telemedicine System.
 * Defines common operations that all users (Patient, Doctor, Admin) must support.
 */
public interface UserInterface {
    
    /**
     * Authenticates a user with email and password.
     * @param email User's email address
     * @param password User's password
     * @return true if authentication successful, false otherwise
     */
    boolean login(String email, String password);
    
    /**
     * Displays the user's profile information.
     */
    void displayProfile();
    
    /**
     * Updates user profile with new name.
     * @param name The new name
     */
    void updateProfile(String name);
    
    /**
     * Updates user profile with new name and email.
     * @param name The new name
     * @param email The new email
     */
    void updateProfile(String name, String email);
    
    /**
     * Updates user profile with new name, email, and phone.
     * @param name The new name
     * @param email The new email
     * @param phone The new phone number
     */
    void updateProfile(String name, String email, String phone);
}
