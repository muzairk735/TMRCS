package com.telemedicine.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

public abstract class Person implements Serializable, UserInterface {
    private static final long serialVersionUID = 1L;

    private static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    private static final String PHONE_PATTERN = "^\\+?(?=(?:\\D*\\d){10,15}\\D*$)[\\d\\s\\-()]+$";

    protected String    userId;
    protected String    name;
    protected String    email;
    protected String    phoneNumber;
    protected String    password;
    protected LocalDate registrationDate;

    public Person(
            String userId,
            String name,
            String email,
            String phoneNumber,
            String password) {
        this.userId           = userId;
        this.name             = validateAndNormalizeName(name);
        this.email            = validateAndNormalizeEmail(email);
        this.phoneNumber      = validateAndNormalizePhone(phoneNumber);
        this.password         = validatePassword(password);
        this.registrationDate = LocalDate.now();
    }

    private static String validateAndNormalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (name.trim().length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters.");
        }
        return name.trim();
    }

    private static String validateAndNormalizeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        String normalized = email.trim().toLowerCase();
        if (!normalized.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        return normalized;
    }

    private static String validateAndNormalizePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty.");
        }
        String normalized = phone.trim();
        if (!normalized.matches(PHONE_PATTERN)) {
            throw new IllegalArgumentException(
                    "Invalid phone number. Must be 10-15 digits, optionally starting with '+'.");
        }
        return normalized;
    }

    private static String validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        return password;
    }

    public abstract void displayProfile();

    @Override
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    @Override
    public void updateProfile(String name) {
        setName(name);
    }

    @Override
    public void updateProfile(String name, String email) {
        setName(name);
        setEmail(email);
    }

    @Override
    public void updateProfile(String name, String email, String phone) {
        setName(name);
        setEmail(email);
        setPhoneNumber(phone);
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = validateAndNormalizeName(name);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = validateAndNormalizeEmail(email);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = validateAndNormalizePhone(phoneNumber);
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = validatePassword(password);
    }
}