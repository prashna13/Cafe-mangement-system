package com.example.managecafe.dao;

import com.example.managecafe.model.User;
import com.example.managecafe.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for users.
 * Handles database operations related to users including authentication and CRUD operations.
 */
public class UserDAO {

    /**
     * Authenticates a user with the provided username and password.
     *
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return The authenticated User object, or null if authentication fails
     * @throws SQLException If a database access error occurs
     */
    public User authenticate(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                return user;
            }
        }
        return null;
    }

    /**
     * Creates a new user in the database.
     *
     * @param user The user to create
     * @return true if the user was created successfully, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public boolean createUser(User user) throws SQLException {
        String query = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole() != null ? user.getRole() : "staff");

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users
     * @throws SQLException If a database access error occurs
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        }
        return users;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve
     * @return The user with the specified ID, or null if not found
     * @throws SQLException If a database access error occurs
     */
    public User getUserById(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                return user;
            }
        }
        return null;
    }

    /**
     * Updates an existing user in the database.
     *
     * @param user The user to update
     * @return true if the user was updated successfully, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public boolean updateUser(User user) throws SQLException {
        String query = "UPDATE users SET username = ?, email = ?, password = ?, role = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setInt(5, user.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Deletes a user from the database.
     *
     * @param id The ID of the user to delete
     * @return true if the user was deleted successfully, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public boolean deleteUser(int id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
