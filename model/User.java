package com.example.managecafe.model;

/**
 * Represents a user in the cafe management system.
 * Users can have different roles such as admin or staff with different access privileges.
 */
public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String role; // e.g., "admin", "staff", etc.

    /**
     * Default constructor for creating an empty User.
     */
    public User() {}

    /**
     * Constructor for creating a User with specified values.
     *
     * @param id The unique identifier for the user
     * @param username The username for login
     * @param email The email address of the user
     * @param password The password for authentication
     * @param role The role of the user in the system
     */
    public User(int id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Gets the user ID.
     *
     * @return The user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user ID.
     *
     * @param id The user ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the username.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email address.
     *
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password.
     *
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the role of the user.
     *
     * @return The user role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role The user role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns a string representation of the User.
     * Note that the password is not included for security reasons.
     *
     * @return A string containing the user details
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
