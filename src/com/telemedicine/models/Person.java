package com.telemedicine.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.regex.Pattern;

// Base class for all users — holds common fields and validation logic
public abstract class Person implements Serializable, UserInterface {
    private static final long serialVersionUID = 1L;

    // Basic format checks — not RFC-perfect but good enough for this domain
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?(?=(?:\\D*\\d){10,15}\\D*$)[\\d\\s\\-()]+$");

    protected String userId;
    protected String name;
    protected String email;
    protected String phoneNumber;
    protected String password;
    protected LocalDate registrationDate;

    // --- Validators --- //

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be empty.");
        if (name.trim().length() < 2)
            throw new IllegalArgumentException("Name must be at least 2 characters.");
    }

    private static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("Email cannot be empty.");
        if (!EMAIL_PATTERN.matcher(email.trim()).matches())
            throw new IllegalArgumentException("Invalid email format: " + email);
    }

    private static void validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty())
            throw new IllegalArgumentException("Phone number cannot be empty.");
        if (!PHONE_PATTERN.matcher(phone.trim()).matches())
            throw new IllegalArgumentException(
                    "Invalid phone number. Must be 10-15 digits, optionally starting with '+'.");
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }

    public Person(
            String userId,
            String name,
            String email,
            String phoneNumber,
            String password) {
        validateName(name);
        validateEmail(email);
        validatePhone(phoneNumber);
        validatePassword(password);
        this.userId = userId;
        this.name = name.trim();
        this.email = email.trim().toLowerCase(); // always lowercase for consistent matching
        this.phoneNumber = phoneNumber.trim();
        this.password = password;
        this.registrationDate = LocalDate.now();
    }

    // Each subclass decides how it displays itself
    public abstract void displayProfile();

    // Simple email + password check — no hashing (plain-text passwords for now)
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    // Overloaded profile updaters — only touch the fields you pass
    public void updateProfile(String name) {
        setName(name);
    }

    public void updateProfile(String name, String email) {
        setName(name);
        setEmail(email);
    }

    public void updateProfile(String name, String email, String phone) {
        setName(name);
        setEmail(email);
        setPhoneNumber(phone);
    }

    // --- Getters & Setters --- //

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email.trim().toLowerCase();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        validatePhone(phoneNumber);
        this.phoneNumber = phoneNumber.trim();
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        validatePassword(password);
        this.password = password;
    }
}
