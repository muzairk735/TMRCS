package com.telemedicine.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Abstract base class representing a user in the system.
 * Serves as the parent for Patient, Doctor, and Admin — demonstrating inheritance.
 * Implements UserInterface to enforce a common contract (abstraction).
 * Fields are protected to allow subclass access while keeping them hidden from outside (encapsulation).
 */
public abstract class Person implements Serializable, UserInterface {
    private static final long serialVersionUID = 1L;

    // Regex patterns for input validation
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?(?=(?:\\D*\\d){10,15}\\D*$)[\\d\\s\\-()]+$");

    // Core identity fields — accessible by subclasses
    protected String userId;
    protected String name;
    protected String email;
    protected String phoneNumber;
    protected String password;
    protected LocalDate registrationDate;

    /**
     * Constructs a Person with validated credentials.
     * All input is validated before assignment — encapsulation in action.
     */
    public Person(
            String userId,
            String name,
            String email,
            String phoneNumber,
            String password) {
        setName(name);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setPassword(password);
        this.userId = userId;
        this.registrationDate = LocalDate.now();
    }

    /**
     * Abstract method — each subclass defines how its profile is displayed.
     * Demonstrates polymorphism: same method name, different behavior.
     */
    public abstract void displayProfile();

    /** Checks email and password match for login */
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    // Overloaded updateProfile methods — update name only, name+email, or all three
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

    // --- Getters and validated setters ---

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be empty.");
        if (name.trim().length() < 2)
            throw new IllegalArgumentException("Name must be at least 2 characters.");
        this.name = name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("Email cannot be empty.");
        if (!EMAIL_PATTERN.matcher(email.trim()).matches())
            throw new IllegalArgumentException("Invalid email format: " + email);
        this.email = email.trim().toLowerCase();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty())
            throw new IllegalArgumentException("Phone number cannot be empty.");
        if (!PHONE_PATTERN.matcher(phoneNumber.trim()).matches())
            throw new IllegalArgumentException(
                    "Invalid phone number. Must be 10-15 digits, optionally starting with '+'.");
        this.phoneNumber = phoneNumber.trim();
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        this.password = password;
    }
}