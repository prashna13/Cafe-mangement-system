package com.example.managecafe.dao;

import com.example.managecafe.model.MenuItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for menu items.
 * Handles database operations related to menu items including CRUD operations.
 */
public class MenuItemDAO {
    private final Connection connection;

    /**
     * Constructs a MenuItemDAO with the specified database connection.
     *
     * @param connection The database connection to use
     */
    public MenuItemDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves a menu item by its ID.
     *
     * @param id The ID of the menu item to retrieve
     * @return The menu item with the specified ID, or null if not found
     * @throws SQLException If a database access error occurs
     */
    public MenuItem getMenuItemById(int id) throws SQLException {
        String sql = "SELECT id, name, description, category, price, available FROM menu_items WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createMenuItemFromResultSet(resultSet);
            }
            return null;
        }
    }

    /**
     * Retrieves all menu items from the database.
     *
     * @return A list of all menu items
     * @throws SQLException If a database access error occurs
     */
    public List<MenuItem> getAllMenuItems() throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT id, name, description, category, price, available FROM menu_items";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                menuItems.add(createMenuItemFromResultSet(resultSet));
            }
            return menuItems;
        }
    }

    /**
     * Adds a new menu item to the database.
     *
     * @param menuItem The menu item to add
     * @throws SQLException If a database access error occurs
     */
    public void addMenuItem(MenuItem menuItem) throws SQLException {
        String sql = "INSERT INTO menu_items (name, description, category, price, available) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, menuItem.getName());
            statement.setString(2, menuItem.getDescription());
            statement.setString(3, menuItem.getCategory());
            statement.setDouble(4, menuItem.getPrice());
            statement.setBoolean(5, menuItem.isAvailable());
            statement.executeUpdate();

            // Get the auto-generated ID and set it on the menu item
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                menuItem.setId(generatedKeys.getInt(1));
            }
        }
    }

    /**
     * Updates an existing menu item in the database.
     *
     * @param menuItem The menu item to update
     * @throws SQLException If a database access error occurs
     */
    public void updateMenuItem(MenuItem menuItem) throws SQLException {
        String sql = "UPDATE menu_items SET name = ?, description = ?, category = ?, price = ?, available = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, menuItem.getName());
            statement.setString(2, menuItem.getDescription());
            statement.setString(3, menuItem.getCategory());
            statement.setDouble(4, menuItem.getPrice());
            statement.setBoolean(5, menuItem.isAvailable());
            statement.setInt(6, menuItem.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Deletes a menu item from the database.
     *
     * @param id The ID of the menu item to delete
     * @throws SQLException If a database access error occurs
     */
    public void deleteMenuItem(int id) throws SQLException {
        String sql = "DELETE FROM menu_items WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    /**
     * Creates a MenuItem object from the current row in the ResultSet.
     *
     * @param resultSet The ResultSet containing menu item data
     * @return A populated MenuItem object
     * @throws SQLException If a database access error occurs
     */
    private MenuItem createMenuItemFromResultSet(ResultSet resultSet) throws SQLException {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(resultSet.getInt("id"));
        menuItem.setName(resultSet.getString("name"));
        menuItem.setDescription(resultSet.getString("description"));
        menuItem.setCategory(resultSet.getString("category"));
        menuItem.setPrice(resultSet.getDouble("price"));
        menuItem.setAvailable(resultSet.getBoolean("available"));
        return menuItem;
    }
}
