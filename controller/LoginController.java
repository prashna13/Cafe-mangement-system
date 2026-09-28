package com.example.managecafe.controller;

import com.example.managecafe.dao.UserDAO;
import com.example.managecafe.model.User;
import com.example.managecafe.util.SessionManager;
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
 * Controller class for the login screen of the cafe management system.
 * Handles user authentication and navigation to the dashboard or registration screen.
 */
public class LoginController {
    // UI components for user input and interaction
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button createAccountButton;

    // Data access object for user-related database operations
    private final UserDAO userDAO = new UserDAO();

    /**
     * Initializes the login controller.
     * This method is automatically called after the FXML is loaded.
     */
    @FXML
    public void initialize() {
        // Initialization logic if needed for the login screen
    }

    /**
     * Handles the login button click event.
     * Authenticates the user credentials against the database.
     *
     * @param event The action event triggered by clicking the login button
     */
    @FXML
    private void loginUser(ActionEvent event) {
        // Retrieve user input from form fields
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate that both fields have been filled
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter both username and password.");
            return;
        }

        try {
            // Attempt to authenticate the user with provided credentials
            User user = userDAO.authenticate(username, password);

            if (user != null) {
                // Authentication successful - store user in session and navigate to dashboard
                SessionManager.getInstance().setLoggedInUser(user);
                openDashboard(event);
            } else {
                // Authentication failed - show error message
                showAlert(Alert.AlertType.ERROR, "Invalid username or password.");
            }
        } catch (SQLException e) {
            // Database error occurred during authentication
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the create account button click event.
     * Opens the registration form for new users.
     *
     * @param event The action event triggered by clicking the create account button
     */
    @FXML
    public void handleCreateAccountButtonAction(ActionEvent event) {
        System.out.println("Create New Account button clicked!");
        try {
            // Load the registration form FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/managecafe/register.fxml"));
            Parent root = loader.load();

            // Create and configure a new stage for the registration form
            Stage stage = new Stage();
            stage.setTitle("Create New Account");
            stage.setScene(new Scene(root));
            stage.initOwner(((Node) event.getSource()).getScene().getWindow()); // Set the owner
            stage.show();
        } catch (IOException e) {
            // Handle errors loading the registration form
            showAlert(Alert.AlertType.ERROR, "Error loading create account view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Opens the dashboard view after successful authentication.
     *
     * @param event The action event from the login button
     */
    private void openDashboard(ActionEvent event) {
        try {
            // Load the dashboard FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/managecafe/dashboard.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the dashboard scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Cafe Management Dashboard");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Handle errors loading the dashboard
            showAlert(Alert.AlertType.ERROR, "Error loading dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Utility method to display alert dialogs to the user.
     *
     * @param type The type of alert (ERROR, WARNING, INFORMATION, etc.)
     * @param message The message to display in the alert dialog
     */
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
