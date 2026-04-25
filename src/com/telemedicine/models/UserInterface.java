package com.telemedicine.models;

public interface UserInterface {

    boolean login(String email, String password);

    void displayProfile();

    void updateProfile(String name);

    void updateProfile(String name, String email);

    void updateProfile(String name, String email, String phone);
}
