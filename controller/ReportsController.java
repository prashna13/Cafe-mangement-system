package com.example.managecafe.controller;

import com.example.managecafe.dao.OrderDAO;
import com.example.managecafe.model.SalesReportEntry;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for generating and displaying sales reports.
 */
public class ReportsController {
    // UI navigation components
    @FXML
    private Button backToDashboardButton;

    // Date selection components
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    // Report display components
    @FXML
    private TableView<SalesReportEntry> salesReportTableView;
    @FXML
    private TableColumn<SalesReportEntry, LocalDate> salesDateColumn;
    @FXML
    private TableColumn<SalesReportEntry, Double> dailySalesColumn;
    @FXML
    private Label totalSalesLabel;

    // Data access and state variables
    private Connection connection;
    private OrderDAO orderDAO;
    private ObservableList<SalesReportEntry> salesReportList = FXCollections.observableArrayList();
    private DashboardController dashboardController;

    /**
     * Sets the database connection and initializes the DAO.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        this.orderDAO = new OrderDAO(this.connection);
    }

    /**
     * Sets reference to dashboard controller if needed.
     */
    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    /**
     * Initializes UI components and sets up event handlers.
     */
    @FXML
    public void initialize() {
        // Set up navigation
        backToDashboardButton.setOnAction(this::goToDashboard);

        // Configure table columns
        salesDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getSalesDate()));

        // Format date display in table
        salesDateColumn.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<SalesReportEntry, LocalDate>() {
                private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(formatter.format(item));
                    }
                }
            };
        });

        // Configure sales amount column
        dailySalesColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getTotalSales()).asObject());

        // Set table data source
        salesReportTableView.setItems(salesReportList);
    }

    /**
     * Generates sales report based on selected date range.
     */
    @FXML
    private void generateSalesReport(ActionEvent event) {
        // Get selected dates
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        // Validate date selection
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            showAlert("Input Error", "Please select a valid start and end date.");
            return;
        }

        // Fetch sales data from database
        List<SalesReportEntry> salesData = orderDAO.getDailySales(startDate, endDate);

        // Update table with results
        salesReportList.clear();
        salesReportList.addAll(salesData);

        // Calculate and display total sales
        double totalSales = 0;
        for (SalesReportEntry entry : salesData) {
            totalSales += entry.getTotalSales();
        }
        totalSalesLabel.setText(String.format("₹%.2f", totalSales));
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
