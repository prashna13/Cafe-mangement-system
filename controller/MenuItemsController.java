package com.example.managecafe.controller;

import com.example.managecafe.dao.MenuItemDAO;
import com.example.managecafe.dao.MenuCategoryDAO;
import com.example.managecafe.model.MenuItem;
import com.example.managecafe.model.MenuCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing menu items in the cafe system.
 */
public class MenuItemsController {
    // UI components for navigation
    @FXML
    private Button backButton;

    // Table components for displaying menu items
    @FXML
    private TableView<MenuItem> menuItemsTable;
    @FXML
    private TableColumn<MenuItem, Integer> idColumn;
    @FXML
    private TableColumn<MenuItem, String> nameColumn;
    @FXML
    private TableColumn<MenuItem, String> categoryColumn;
    @FXML
    private TableColumn<MenuItem, Double> priceColumn;
    @FXML
    private TableColumn<MenuItem, Boolean> availableColumn;
    @FXML
    private TableColumn<MenuItem, Void> actionsColumn;

    // Filter and search components
    @FXML
    private ComboBox<MenuCategory> categoryFilterComboBox;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Button clearButton;

    // Form components for adding/editing menu items
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ComboBox<MenuCategory> categoryComboBox;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<Boolean> availableComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button clearFormButton;

    // Data access and state variables
    private Connection connection;
    private MenuItemDAO menuItemDAO;
    private MenuCategoryDAO categoryDAO;
    private ObservableList<MenuItem> allMenuItems = FXCollections.observableArrayList();
    private MenuItem currentMenuItem; // Tracks item being edited

    /**
     * Sets database connection and initializes DAOs.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        this.menuItemDAO = new MenuItemDAO(this.connection);
        this.categoryDAO = new MenuCategoryDAO(this.connection);
        populateMenuItemsTable();
        loadCategories();
    }

    /**
     * Initializes UI components and sets up event handlers.
     */
    @FXML
    public void initialize() {
        // Set up button actions
        backButton.setOnAction(this::handleBackButtonAction);
        saveButton.setOnAction(event -> handleSaveButtonAction());
        clearFormButton.setOnAction(event -> handleClearFormButtonAction());
        searchButton.setOnAction(event -> handleSearchButtonAction());
        clearButton.setOnAction(event -> handleClearFilterSearchButtonAction());
        categoryFilterComboBox.setOnAction(event -> handleCategoryFilterAction());

        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        // Set up available options
        availableComboBox.getItems().addAll(true, false);
        availableComboBox.setValue(true);

        setupActionsColumn();
    }

    /**
     * Loads categories from database for dropdowns.
     */
    private void loadCategories() {
        System.out.println("loadCategories() method is being called");
        try {
            List<MenuCategory> categories = categoryDAO.getAllCategories();
            System.out.println("Loaded categories from database: " + categories);
            ObservableList<MenuCategory> categoryList = FXCollections.observableArrayList(categories);
            System.out.println("ObservableList of categories: " + categoryList);

            // Set up category selection dropdown
            categoryComboBox.setItems(categoryList);

            // Set up filter dropdown with "All" option
            categoryFilterComboBox.getItems().clear();
            categoryFilterComboBox.getItems().add(new MenuCategory(0, "All"));
            categoryFilterComboBox.getItems().addAll(categoryList);
            categoryFilterComboBox.setValue(new MenuCategory(0, "All"));
        } catch (SQLException e) {
            showAlert("Database Error", "Error loading categories: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles back button click to return to dashboard.
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
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
     * Handles save button click to add or update menu item.
     */
    @FXML
    private void handleSaveButtonAction() {
        // Get form values
        String name = nameField.getText();
        String description = descriptionField.getText();
        MenuCategory selectedCategory = categoryComboBox.getValue();
        String priceText = priceField.getText();
        Boolean available = availableComboBox.getValue();

        // Validate required fields
        if (name.isEmpty() || priceText.isEmpty() || selectedCategory == null) {
            showAlert("Error", "Please fill in all required fields (Name, Category, Price).");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);

            // Update existing or create new item
            if (currentMenuItem != null) {
                currentMenuItem.setName(name);
                currentMenuItem.setDescription(description);
                currentMenuItem.setCategory(selectedCategory.getCategoryName());
                currentMenuItem.setPrice(price);
                currentMenuItem.setAvailable(available);
                menuItemDAO.updateMenuItem(currentMenuItem);
            } else {
                MenuItem itemToSave = new MenuItem(0, name, description, selectedCategory.getCategoryName(), price, available);
                menuItemDAO.addMenuItem(itemToSave);
            }

            populateMenuItemsTable();
            handleClearFormButtonAction();
            currentMenuItem = null;
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid price format. Please enter a number.");
        } catch (SQLException e) {
            showAlert("Database Error", "Error saving menu item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Clears the form fields and resets editing state.
     */
    @FXML
    private void handleClearFormButtonAction() {
        nameField.clear();
        descriptionField.clear();
        categoryComboBox.setValue(null);
        priceField.clear();
        availableComboBox.setValue(true);
        currentMenuItem = null;
    }

    /**
     * Handles search button click to filter items.
     */
    @FXML
    private void handleSearchButtonAction() {
        String searchText = searchField.getText().toLowerCase();
        filterMenuItems(categoryFilterComboBox.getValue(), searchText);
    }

    /**
     * Clears all filters and shows all menu items.
     */
    @FXML
    private void handleClearFilterSearchButtonAction() {
        categoryFilterComboBox.setValue(new MenuCategory(0, "All"));
        searchField.clear();
        filterMenuItems(new MenuCategory(0, "All"), "");
    }

    /**
     * Handles category filter selection change.
     */
    @FXML
    private void handleCategoryFilterAction() {
        filterMenuItems(categoryFilterComboBox.getValue(), searchField.getText());
    }

    /**
     * Loads all menu items from database to table.
     */
    private void populateMenuItemsTable() {
        try {
            List<MenuItem> items = menuItemDAO.getAllMenuItems();
            allMenuItems.clear();
            allMenuItems.addAll(items);
            menuItemsTable.setItems(allMenuItems);
        } catch (SQLException e) {
            showAlert("Database Error", "Error fetching menu items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Filters menu items by category and search text.
     */
    private void filterMenuItems(MenuCategory category, String searchText) {
        ObservableList<MenuItem> filteredList = allMenuItems.stream()
                .filter(item -> {
                    boolean categoryMatch = category.getId() == 0 || item.getCategory().equals(category.getCategoryName());
                    boolean searchMatch = searchText.isEmpty() ||
                            item.getName().toLowerCase().contains(searchText) ||
                            (item.getDescription() != null && item.getDescription().toLowerCase().contains(searchText));
                    return categoryMatch && searchMatch;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        menuItemsTable.setItems(filteredList);
    }

    /**
     * Sets up the actions column with edit and delete buttons.
     */
    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<MenuItem, Void>() {
            private final HBox buttons = new HBox(10);
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            {
                editButton.setOnAction(event -> {
                    MenuItem item = getTableView().getItems().get(getIndex());
                    loadMenuItemForEdit(item);
                });
                deleteButton.setOnAction(event -> {
                    MenuItem item = getTableView().getItems().get(getIndex());
                    deleteMenuItem(item.getId());
                });
                buttons.getChildren().addAll(editButton, deleteButton);
                HBox.setMargin(editButton, new Insets(0, 5, 0, 0));
                HBox.setMargin(deleteButton, new Insets(0, 0, 0, 5));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });
    }

    /**
     * Loads a menu item into the form for editing.
     */
    private void loadMenuItemForEdit(MenuItem item) {
        currentMenuItem = item;
        nameField.setText(item.getName());
        descriptionField.setText(item.getDescription());
        try {
            List<MenuCategory> categories = categoryDAO.getAllCategories();
            for (MenuCategory cat : categories) {
                if (cat.getCategoryName().equals(item.getCategory())) {
                    categoryComboBox.setValue(cat);
                    break;
                }
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Error loading categories for edit: " + e.getMessage());
            e.printStackTrace();
        }
        priceField.setText(String.valueOf(item.getPrice()));
        availableComboBox.setValue(item.isAvailable());
    }

    /**
     * Deletes a menu item after confirmation.
     */
    private void deleteMenuItem(int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Item");
        alert.setContentText("Are you sure you want to delete this menu item?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    menuItemDAO.deleteMenuItem(id);
                    populateMenuItemsTable();
                } catch (SQLException e) {
                    showAlert("Database Error", "Error deleting menu item: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
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
