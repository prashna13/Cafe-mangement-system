package com.example.managecafe.controller;

import com.example.managecafe.dao.TableDAO;
import com.example.managecafe.model.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for managing cafe tables.
 */
public class TablesController {
    // UI components
    @FXML
    private Button backToDashboardButton;
    @FXML
    private TableView<Table> tablesTableView;
    @FXML
    private TableColumn<Table, Integer> tableNumberColumn;
    @FXML
    private TableColumn<Table, Integer> capacityColumn;
    @FXML
    private TableColumn<Table, String> statusColumn;

    // Data access and state variables
    private Connection connection;
    private TableDAO tableDAO;
    private ObservableList<Table> tableList = FXCollections.observableArrayList();

    /**
     * Sets the database connection and initializes the DAO.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        this.tableDAO = new TableDAO(this.connection);
        loadTableData();
    }

    /**
     * Initializes UI components and sets up event handlers.
     */
    @FXML
    public void initialize() {
        // Set up navigation
        backToDashboardButton.setOnAction(this::goToDashboard);

        // Configure table columns
        tableNumberColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set table data source
        tablesTableView.setItems(tableList);
    }

    /**
     * Loads table data from the database.
     */
    private void loadTableData() {
        try {
            List<Table> tables = tableDAO.getAllTables();
            tableList.clear();
            tableList.addAll(tables);
        } catch (SQLException e) {
            showAlert("Database Error", "Error loading tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navigates back to the dashboard screen.
     */
    @FXML
    private void goToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/managecafe/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Cafe Management Dashboard");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error loading Dashboard", "Error loading the dashboard view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Shows an error alert with the given title and message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
