package com.telemedicine.app;

import com.telemedicine.models.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Shared runtime state container for the entire application.
 *
 * <p>Rather than each menu handler holding its own copy of the collections,
 * or every class being wired directly to every other class, a single
 * {@code AppContext} instance is created at startup and passed into every
 * handler. Because Java passes objects by reference, all handlers operate
 * on the exact same lists and session fields — mutations made in one handler
 * are immediately visible to the others.</p>
 *
 * <p>Fields are intentionally {@code public} so handler classes can read and
 * write them directly without boilerplate getters/setters. This is safe
 * because {@code AppContext} is an internal wiring object, not a public API.</p>
 *
 * <p>The session fields ({@link #currentUser} and {@link #currentUserType})
 * are set on login and cleared on logout via {@link #clearSession()}.</p>
 */
public class AppContext {

    // -------------------------------------------------------------------------
    // Persistent data collections (populated by DataManager on startup)
    // -------------------------------------------------------------------------

    /** All registered patients in the system. */
    public ArrayList<Patient>     patients;

    /** All registered doctors in the system. */
    public ArrayList<Doctor>      doctors;

    /** All admin accounts in the system. */
    public ArrayList<Admin>       admins;

    /** Master list of every appointment, kept in sync with patient/doctor lists. */
    public ArrayList<Appointment> appointments;

    // -------------------------------------------------------------------------
    // Shared I/O and session state
    // -------------------------------------------------------------------------

    /** Single Scanner instance shared across all handlers to avoid input conflicts. */
    public Scanner scanner;

    /**
     * The currently logged-in user, or {@code null} when no session is active.
     * Cast to {@link Patient}, {@link Doctor}, or {@link Admin} as needed after
     * checking {@link #currentUserType}.
     */
    public Person  currentUser;

    /**
     * String tag for the active session role: {@code "PATIENT"}, {@code "DOCTOR"},
     * {@code "ADMIN"}, or {@code null} when no one is logged in.
     */
    public String  currentUserType;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Initialises all collections to empty lists and opens a {@link Scanner}
     * on {@code System.in}. Data is loaded separately by {@link com.telemedicine.data.DataManager}.
     */
    public AppContext() {
        patients        = new ArrayList<>();
        doctors         = new ArrayList<>();
        admins          = new ArrayList<>();
        appointments    = new ArrayList<>();
        scanner         = new Scanner(System.in);
        currentUser     = null;
        currentUserType = null;
    }

    // -------------------------------------------------------------------------
    // Session management
    // -------------------------------------------------------------------------

    /**
     * Clears the active login session by nulling both session fields.
     * Called by each menu handler's logout option.
     */
    public void clearSession() {
        currentUser     = null;
        currentUserType = null;
    }
}
