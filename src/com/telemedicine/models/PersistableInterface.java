package com.telemedicine.models;

// Anything that needs to read/write its own state to disk
public interface PersistableInterface {

    void save();

    void load();
}
