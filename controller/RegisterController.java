package com.example.managecafe.controller;

import com.example.managecafe.dao.UserDAO;
import com.example.managecafe.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for user registration functionality.
 */
public class RegisterController {
    // UI form components
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerButton;
    @FXML
    private Button backToLoginButton;

    // Data access object
    private final UserDAO userDAO = new UserDAO();

    /**
     * Initializes the controller and sets up event handlers.
     */
    @FXML
    private void initialize() {
        registerButton.setOnAction(this::registerUser);
        if (backToLoginButton != null) {
            backToLoginButton.setOnAction(this::backToLogin);
        }
    }

    /**
     * Handles user registration process.
     */
    private void registerUser(ActionEvent event) {
        // Get form values
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Validate input fields
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return;
        }

        // Validate email format
        if (!email.contains("@") || !email.contains(".")) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid email address.");
            return;
        }

        // Validate password length
        if (password.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Password must be at least 6 characters long.");
            return;
        }

        // Create user object
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("staff"); // Default role

        try {
            // Attempt to create user in database
            boolean success = userDAO.createUser(user);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Registration successful!");
                loadLoginScreen(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to register user.");
            }
        } catch (SQLException e) {
            // Handle specific database errors
            if (e.getMessage().contains("Duplicate entry")) {
                if (e.getMessage().contains("username")) {
                    showAlert(Alert.AlertType.ERROR, "Username already exists. Please choose another.");
                } else if (e.getMessage().contains("email")) {
                    showAlert(Alert.AlertType.ERROR, "Email already registered. Please use another email.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
            }
        }
    }

    /**
     * Navigates back to login screen.
     */
    @FXML
    private void backToLogin(ActionEvent event) {
        loadLoginScreen(event);
    }

    /**
     * Loads the login screen.
     */
    private void loadLoginScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/managecafe/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Get current stage and its owner
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Stage primaryStage = (Stage) currentStage.getOwner();

            if (primaryStage != null) {
                // If this is a child window, update the parent window
                primaryStage.setTitle("Login");
                primaryStage.setScene(scene);
                primaryStage.show();
            } else {
                // If no parent, create a new stage
                Stage newLoginStage = new Stage();
                newLoginStage.setTitle("Login");
                newLoginStage.setScene(scene);
                newLoginStage.show();
            }

            // Close the registration window
            currentStage.close();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading login screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Shows an alert dialog with the given type and message.
     */
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }
}
