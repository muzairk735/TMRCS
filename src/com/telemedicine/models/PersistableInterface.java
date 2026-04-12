package com.telemedicine.models;

/**
 * Interface for data persistence operations.
 * Defines contracts for saving and loading data to/from storage.
 */
public interface PersistableInterface {
    
    /**
     * Saves data to persistent storage.
     */
    void save();
    
    /**
     * Loads data from persistent storage.
     */
    void load();
}
