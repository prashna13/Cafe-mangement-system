package com.example.managecafe.controller;

import com.example.managecafe.model.User;
import com.example.managecafe.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for the settings screen that displays user information.
 */
public class SettingsController {
    // UI components
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Button backButton;

    // Current user data
    private User currentUser;

    /**
     * Initializes the controller and loads user information.
     */
    @FXML
    public void initialize() {
        // Get current user from session
        currentUser = SessionManager.getInstance().getLoggedInUser();
        displayUserInfo();
    }

    /**
     * Displays the current user's information in the UI.
     */
    private void displayUserInfo() {
        if (currentUser != null) {
            usernameLabel.setText("Username: " + currentUser.getUsername());
            emailLabel.setText("Email: " + currentUser.getEmail());
            roleLabel.setText("Role: " + currentUser.getRole());
        } else {
            usernameLabel.setText("Username: N/A");
            emailLabel.setText("Email: N/A");
            roleLabel.setText("Role: N/A");
        }
    }

    /**
     * Handles the back button action to return to dashboard.
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/managecafe/dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = ((Node) event.getSource()).getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Cafe Management Dashboard");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading dashboard.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
