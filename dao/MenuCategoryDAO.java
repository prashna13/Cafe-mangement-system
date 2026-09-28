package com.example.managecafe.dao;

import com.example.managecafe.model.MenuCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for menu categories.
 * Handles database operations related to menu categories.
 */
public class MenuCategoryDAO {
    private final Connection connection;

    /**
     * Constructs a MenuCategoryDAO with the specified database connection.
     *
     * @param connection The database connection to use
     */
    public MenuCategoryDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves all menu categories from the database.
     *
     * @return A list of all menu categories
     * @throws SQLException If a database access error occurs
     */
    public List<MenuCategory> getAllCategories() throws SQLException {
        List<MenuCategory> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM menu_categories";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                categories.add(createCategoryFromResultSet(resultSet));
            }
            return categories;
        }
    }

    /**
     * Creates a MenuCategory object from the current row in the ResultSet.
     *
     * @param resultSet The ResultSet containing category data
     * @return A populated MenuCategory object
     * @throws SQLException If a database access error occurs
     */
    private MenuCategory createCategoryFromResultSet(ResultSet resultSet) throws SQLException {
        MenuCategory category = new MenuCategory();
        category.setId(resultSet.getInt("id"));
        category.setCategoryName(resultSet.getString("name"));
        return category;
    }
}
