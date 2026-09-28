package com.example.managecafe.controller;

import com.example.managecafe.HelloApplication;
import com.example.managecafe.dao.OrderDAO;
import com.example.managecafe.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {

    @FXML
    private TableView<Order> ordersTableView;
    @FXML
    private TableColumn<Order, Integer> orderIdColumn;
    @FXML
    private TableColumn<Order, Integer> tableNumberColumn;
    @FXML
    private TableColumn<Order, LocalDateTime> orderDateColumn;
    @FXML
    private TableColumn<Order, Double> totalAmountColumn;
    @FXML
    private Button backToDashboardButton;
    @FXML
    private Button createNewOrderButton;

    private OrderDAO orderDAO;
    private ObservableList<Order> orderList = FXCollections.observableArrayList();
    private Window owner;  // Added for showing dialogs
    private Connection connection; // To be used if needed in this controller

    public void setOwner(Window owner) { //setter method for the owner.
        this.owner = owner;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        // You might perform actions that require the connection here if needed
        // For example, if you wanted to filter orders based on some criteria
        // when this view is loaded.
        // loadFilteredOrders();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the table columns
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tableNumberColumn.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        // Set the table's data source
        ordersTableView.setItems(orderList);
        // Initialize the orderDAO and load data
        try {
            Connection conn = HelloApplication.getConnection(); // Get the connection
            if (conn != null) {
                this.orderDAO = new OrderDAO(conn);
                loadOrders();
            } else {
                // Handle the case where the connection is null (important!)
                System.err.println("Failed to establish database connection in OrdersController.");
                showErrorAlert("Database Connection Error", "Failed to connect to the database. Please check your connection and try again.");
            }
        } catch (Exception e) {
            System.err.println("Exception in OrdersController: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Initialization Error", "An error occurred during initialization: " + e.getMessage());
        }

    }

    private void loadOrders() {
        if (orderDAO != null) {
            try {
                List<Order> orders = orderDAO.getAllOrders(); // Fetch orders from the database
                orderList.clear();             // Clear the current list
                orderList.addAll(orders);      // Add the fetched orders to the list
            } catch (Exception e) {
                System.err.println("Error loading orders: " + e.getMessage());
                e.printStackTrace();
                showErrorAlert("Error Loading Orders", "Failed to load orders from the database: " + e.getMessage());
            }
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }

    @FXML
    private void goToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/managecafe/dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(dashboardRoot);
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error Loading Dashboard", "Failed to load the dashboard view.");
        }
    }

    @FXML
    private void createNewOrder(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/managecafe/create_order.fxml"));
            Parent newOrderRoot = loader.load();
            NewOrderController newOrderController = loader.getController();
            newOrderController.setConnection(HelloApplication.getConnection()); // Pass the connection
            Stage stage = new Stage();
            stage.setTitle("Create New Order"); // Updated title to reflect actual FXML
            stage.setScene(new Scene(newOrderRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error Creating New Order", "Failed to load the create order view."); // Updated message
        }
    }
}