package com.example.managecafe.controller;

import com.example.managecafe.HelloApplication;
import com.example.managecafe.dao.OrderDAO;
import com.example.managecafe.dao.TableDAO;
import com.example.managecafe.model.Order;
import com.example.managecafe.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DashboardController {

    @FXML
    protected Label userLabel;

    @FXML
    private Button logoutButton;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button menuItemsButton;
    @FXML
    private Button ordersButton;
    @FXML
    private Button tablesButton;
    @FXML
    private Button reportsButton;
    @FXML
    private Button settingsButton;

    @FXML
    protected Text ordersCountText;
    @FXML
    protected Text revenueText;
    @FXML
    protected Text activeTablesText;

    @FXML
    private TableView<Order> recentOrdersTable;
    @FXML
    private TableColumn<Order, Integer> orderIdColumn;
    @FXML
    private TableColumn<Order, Integer> tableNumberColumn;
    @FXML
    private TableColumn<Order, String> orderTimeColumn;
    @FXML
    private TableColumn<Order, String> statusColumn;
    @FXML
    private TableColumn<Order, Double> totalColumn;

    private OrderDAO orderDAO;
    private TableDAO tableDAO;
    private ObservableList<Order> recentOrdersList = FXCollections.observableArrayList();
    private User loggedInUser;
    private Connection connection;
    private Window owner;

    public void setOwner(Window owner) {
        this.owner = owner;
    }

    @FXML
    public void initialize() {
        logoutButton.setOnAction(this::logout);
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tableNumberColumn.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        orderTimeColumn.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        recentOrdersTable.setItems(recentOrdersList);

        connection = HelloApplication.getConnection();
        if (connection == null) {
            showAlert("Database Error", "Failed to connect to the database.  Application cannot function.");
            return;
        }
        orderDAO = new OrderDAO(connection);
        tableDAO = new TableDAO(connection); // Initialize TableDAO
        // Call a method that actually uses the connection and might throw SQLException.
        loadDashboardData(); // loadDashboardData() uses orderDAO and tableDAO which can throw SQLException
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        if (user != null) {
            userLabel.setText("Welcome, " + user.getUsername() + "!");
        } else {
            userLabel.setText("Welcome!");
        }
    }

    private void loadDashboardData() {
        if (orderDAO != null && tableDAO != null) {
            try {
                int totalOrders = orderDAO.getTotalOrdersToday();
                double totalRevenue = orderDAO.getTotalRevenueToday();
                int activeTables = tableDAO.getActiveTables();
                List<Order> recentOrders = orderDAO.getRecentOrders(5);

                ordersCountText.setText(String.valueOf(totalOrders));
                revenueText.setText(String.format("₹%.2f", totalRevenue));
                activeTablesText.setText(String.valueOf(activeTables));

                recentOrdersList.clear();
                recentOrdersList.addAll(recentOrders);
                recentOrdersTable.setItems(recentOrdersList);
            } catch (SQLException e) {
                showAlert("Database Error", "Error loading dashboard data: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Initialization Error", "OrderDAO or TableDAO is not initialized.");
        }
    }

    @FXML
    private void goToMenuItems(ActionEvent event) {
        loadView("/com/example/managecafe/menu_items.fxml", "Menu Items", event);
    }

    @FXML
    private void goToOrders(ActionEvent event) {
        loadView("/com/example/managecafe/orders.fxml", "Orders", event);
    }

    @FXML
    private void goToTables(ActionEvent event) {
        loadView("/com/example/managecafe/tables.fxml", "Tables", event);
    }

    @FXML
    private void goToReports(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/managecafe/reports.fxml"));
            Parent root = loader.load();
            ReportsController reportsController = loader.getController();
            reportsController.setConnection(connection);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Reports");
            stage.show();
        } catch (IOException e) {
            showAlert("Error Loading View", "Error loading the view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goToSettings(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/managecafe/settings.fxml"));
            Parent root = loader.load();
            SettingsController settingsController = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Settings");
            stage.show();
        } catch (IOException e) {
            showAlert("Error Loading View", "Error loading the view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        loadView("/com/example/managecafe/login.fxml", "Cafe Management System - Login", event);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadView(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

            Object controller = loader.getController();
            if (controller instanceof MenuItemsController) {
                ((MenuItemsController) controller).setConnection(connection);
            } else if (controller instanceof OrdersController) {
                ((OrdersController) controller).setConnection(connection);
            } else if (controller instanceof TablesController) {
                ((TablesController) controller).setConnection(connection);
            }
        } catch (IOException e) {
            showAlert("Error Loading View", "Error loading the view: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
