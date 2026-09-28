package com.example.managecafe.controller;

import com.example.managecafe.dao.*;
import com.example.managecafe.model.MenuItem;
import com.example.managecafe.model.Order;
import com.example.managecafe.model.OrderItem;
import com.example.managecafe.model.Table;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for creating new orders in the cafe system.
 */
public class NewOrderController {
    // UI components for table selection and menu items
    @FXML
    private ComboBox<Table> tableComboBox;
    @FXML
    private ListView<MenuItem> menuItemListView;

    // Order items table components
    @FXML
    private TableView<OrderItem> currentOrderTableView;
    @FXML
    private TableColumn<OrderItem, String> orderItemNameColumn;
    @FXML
    private TableColumn<OrderItem, Integer> orderItemQuantityColumn;
    @FXML
    private TableColumn<OrderItem, Double> orderItemPriceColumn;
    @FXML
    private TableColumn<OrderItem, Double> orderItemSubtotalColumn;
    @FXML
    private TableColumn<OrderItem, Void> orderItemActionsColumn;

    // Order summary and action components
    @FXML
    private Label orderTotalLabel;
    @FXML
    private Button addItemButton;
    @FXML
    private Button clearOrderButton;
    @FXML
    private Button saveOrderButton;
    @FXML
    private Button backButton;

    // Data access objects and state variables
    private Connection connection;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private MenuItemDAO menuItemDAO;
    private TableDAO tableDAO;
    private ObservableList<MenuItem> availableMenuItems = FXCollections.observableArrayList();
    private ObservableList<OrderItem> currentOrderItems = FXCollections.observableArrayList();
    private Order currentOrder; // Current order being created

    /**
     * Sets database connection and initializes DAOs.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        this.orderDAO = new OrderDAO(this.connection);
        this.orderItemDAO = new OrderItemDAO(this.connection);
        this.menuItemDAO = new MenuItemDAO(this.connection);
        this.tableDAO = new TableDAO(this.connection);
        initializeData();
    }

    /**
     * Initializes UI components and sets up event handlers.
     */
    @FXML
    public void initialize() {
        // Configure table columns
        orderItemNameColumn.setCellValueFactory(new PropertyValueFactory<>("menuItem"));
        orderItemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderItemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        orderItemSubtotalColumn.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        orderItemActionsColumn.setCellFactory(param -> new OrderItemActionsCell());

        // Set table data source
        currentOrderTableView.setItems(currentOrderItems);

        // Disable save button when order is empty
        saveOrderButton.disableProperty().bind(Bindings.isEmpty(currentOrderItems));

        // Set up button actions
        addItemButton.setOnAction(this::handleAddItemAction);
        clearOrderButton.setOnAction(this::handleClearOrderAction);
        saveOrderButton.setOnAction(this::handleSaveOrderAction);
        backButton.setOnAction(this::handleBackButtonAction);
    }

    /**
     * Loads initial data for tables and menu items.
     */
    private void initializeData() {
        loadTables();
        loadMenuItems();
        currentOrder = new Order();
        currentOrder.setOrderItems(currentOrderItems);
    }

    /**
     * Loads available tables from database.
     */
    private void loadTables() {
        try {
            List<Table> tables = tableDAO.getAllTables();
            tableComboBox.getItems().addAll(tables);
            if (!tables.isEmpty()) {
                tableComboBox.setValue(tables.get(0));
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Error loading tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads menu items from database.
     */
    private void loadMenuItems() {
        try {
            List<MenuItem> menuItems = menuItemDAO.getAllMenuItems();
            availableMenuItems.addAll(menuItems);
            menuItemListView.setItems(availableMenuItems);

            // Custom cell factory to display only item names
            menuItemListView.setCellFactory(lv -> new ListCell<MenuItem>() {
                @Override
                protected void updateItem(MenuItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            });
        } catch (SQLException e) {
            showAlert("Database Error", "Error loading menu items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles adding an item to the current order.
     */
    @FXML
    private void handleAddItemAction(ActionEvent event) {
        MenuItem selectedMenuItem = menuItemListView.getSelectionModel().getSelectedItem();
        if (selectedMenuItem == null) {
            showAlert("Input Error", "Please select a menu item to add.");
            return;
        }

        // Prompt for quantity
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Quantity");
        dialog.setHeaderText("Enter quantity for " + selectedMenuItem.getName() + ":");
        dialog.setContentText("Quantity:");

        dialog.showAndWait().ifPresent(quantityStr -> {
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    showAlert("Input Error", "Quantity must be greater than zero.");
                    return;
                }

                // Check if item already exists in order
                for (OrderItem orderItem : currentOrderItems) {
                    if (orderItem.getMenuItemId() == selectedMenuItem.getId()) {
                        orderItem.setQuantity(orderItem.getQuantity() + quantity);
                        updateOrderTotal();
                        currentOrderTableView.refresh();
                        return;
                    }
                }

                // Add new item to order
                OrderItem orderItem = new OrderItem(currentOrder.getOrderId(), selectedMenuItem.getId(),
                        quantity, selectedMenuItem.getPrice());
                orderItem.setMenuItem(selectedMenuItem);
                currentOrderItems.add(orderItem);
                updateOrderTotal();
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Invalid quantity. Please enter a valid number.");
            }
        });
    }

    /**
     * Clears all items from the current order.
     */
    @FXML
    private void handleClearOrderAction(ActionEvent event) {
        currentOrderItems.clear();
        updateOrderTotal();
    }

    /**
     * Saves the current order to the database.
     */
    @FXML
    private void handleSaveOrderAction(ActionEvent event) {
        Table selectedTable = tableComboBox.getValue();
        if (selectedTable == null) {
            showAlert("Input Error", "Please select a table for the order.");
            return;
        }

        if (currentOrderItems.isEmpty()) {
            showAlert("Input Error", "Cannot save an empty order.");
            return;
        }

        try {
            // Set order properties
            currentOrder.setTableNumber(selectedTable.getId());
            currentOrder.setOrderTime(LocalDateTime.now());
            currentOrder.setOrderStatus("Pending");
            currentOrder.setTotalAmount(calculateOrderTotal());

            // Begin transaction
            connection.setAutoCommit(false);

            // Save order and get ID
            int orderId = orderDAO.addOrder(currentOrder);
            currentOrder.setOrderId(orderId);

            // Save order items
            for (OrderItem orderItem : currentOrderItems) {
                orderItem.setOrderId(orderId);
                orderItemDAO.addOrderItem(orderItem);
            }

            connection.commit();
            showAlert("Success", "Order saved successfully. Order ID: " + orderId);

            // Clear and close
            handleClearOrderAction(event);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                showAlert("Database Error", "Error rolling back transaction: " + ex.getMessage());
                ex.printStackTrace();
            }
            showAlert("Database Error", "Error saving order: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                showAlert("Database Error", "Error resetting auto commit: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Calculates the total amount for the current order.
     */
    private double calculateOrderTotal() {
        double total = 0;
        for (OrderItem item : currentOrderItems) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * Updates the order total display.
     */
    private void updateOrderTotal() {
        orderTotalLabel.setText(String.format("%.2f", calculateOrderTotal()));
    }

    /**
     * Shows an alert dialog with the given title and message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the back button action to close the window.
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    /**
     * Custom cell for the actions column with remove button.
     */
    private class OrderItemActionsCell extends TableCell<OrderItem, Void> {
        private final Button removeButton = new Button("Remove");

        public OrderItemActionsCell() {
            removeButton.setOnAction(event -> {
                OrderItem item = getTableView().getItems().get(getIndex());
                currentOrderItems.remove(item);
                updateOrderTotal();
                getTableView().refresh();
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(removeButton);
            }
        }
    }
}
