package com.example.managecafe.util;

import com.example.managecafe.model.User;

/**
 * Manages user sessions in the cafe management system.
 * Implemented as a singleton to ensure only one session manager exists throughout the application.
 */
public class SessionManager {
    private static SessionManager instance;
    private User loggedInUser;

    /**
     * Private constructor to prevent external instantiation.
     * This enforces the singleton pattern.
     */
    private SessionManager() {
        // Private constructor to prevent external instantiation
    }

    /**
     * Gets the singleton instance of the SessionManager.
     * If the instance doesn't exist, it creates one.
     *
     * @return The singleton SessionManager instance
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Gets the currently logged-in user.
     *
     * @return The logged-in user, or null if no user is logged in
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Sets the currently logged-in user.
     *
     * @param user The user to set as logged in
     */
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    /**
     * Clears the current session by setting the logged-in user to null.
     * This is typically called during logout.
     */
    public void clearSession() {
        this.loggedInUser = null;
    }
}
