package com.telemedicine.models;

/**
 * Basic contract for classes that support save/load operations.
 * Demonstrates abstraction through a shared persistence interface.
 */
public interface PersistableInterface {

    // Save current state
    void save();

    // Load saved state
    void load();
}
