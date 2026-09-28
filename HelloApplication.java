package com.example.managecafe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Main application class for the Cafe Management System.
 * Handles application startup, database connection, and shutdown.
 */
public class HelloApplication extends Application {
    private static Connection connection;

    /**
     * Starts the JavaFX application by loading the login screen.
     *
     * @param stage The primary stage for this application
     * @throws IOException If the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cafe Management System - Login");
        stage.setScene(scene);
        stage.setResizable(false); // Prevent window resizing by the user
        stage.setWidth(1000);      // Set the width of the window
        stage.setHeight(700);      // Set the height of the window
        stage.show();
    }

    /**
     * Gets a connection to the database.
     * Creates a new connection if one doesn't exist, otherwise returns the existing connection.
     *
     * @return A connection to the database, or null if connection fails
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // *******************************************************************
                // ********** REPLACE THESE WITH YOUR ACTUAL DATABASE DETAILS **********
                // *******************************************************************
                String url = "jdbc:mysql://localhost:3306/managecafe_db"; // e.g., "jdbc:mysql://localhost:3306/managecafe_db"
                String user = "root";                                    // e.g., "root"
                String password = "7099";                                // e.g., "your_secret_password"
                // *******************************************************************
                // *******************************************************************

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Database connection established.");
            } catch (SQLException e) {
                System.err.println("Error establishing database connection: " + e.getMessage());
                // Consider showing an alert to the user using JavaFX Alert.  This is important for user feedback.
                //  However, since this is a static method, we can't directly use Platform.runLater.
                //  A better approach might be to log the error and handle it in the calling code
                //  (e.g., in the login controller) where you *do* have a JavaFX context.
                return null; //  Important: Return null to indicate failure.
            }
        }
        return connection;
    }

    /**
     * Closes the database connection when the application stops.
     *
     * @throws Exception If an error occurs during shutdown
     */
    @Override
    public void stop() throws Exception {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                //  Consider logging this error as well.  It might be useful for debugging.
            }
        }
        super.stop();
    }

    /**
     * The main entry point for the application.
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
