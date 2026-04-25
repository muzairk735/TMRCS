package com.telemedicine.models;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Person implements Serializable, UserInterface {
    private static final long serialVersionUID = 1L;
    
    // Attributes
    protected String userId;
    protected String name;
    protected String email;
    protected String phoneNumber;
    protected String password;
    protected LocalDate registrationDate;
    
    // Constructor
    public Person(String userId, String name, String email, 
                  String phoneNumber, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.registrationDate = LocalDate.now();
    }
    
    public abstract void displayProfile();
    
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }
    
    // Method Overloading examples
    public void updateProfile(String name) {
        this.name = name;
    }
    
    public void updateProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    public void updateProfile(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phone;
    }
    
    public String getUserId() { 
        return userId; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }
    
    public LocalDate getRegistrationDate() { 
        return registrationDate; 
    }
    
    public String getPassword() { 
        return password; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }
}
