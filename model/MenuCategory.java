package com.example.managecafe.model;

/**
 * Represents a category for menu items in the cafe management system.
 * Categories help organize menu items into logical groups like beverages, food, desserts, etc.
 */
public class MenuCategory {
    private int id;
    private String categoryName;

    /**
     * Default constructor for creating an empty MenuCategory.
     */
    public MenuCategory() {
    }

    /**
     * Constructor for creating a MenuCategory with specified values.
     *
     * @param id The unique identifier for the category
     * @param categoryName The name of the category
     */
    public MenuCategory(int id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    /**
     * Gets the category ID.
     *
     * @return The category ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the category ID.
     *
     * @param id The category ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the category name.
     *
     * @return The category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the category name.
     *
     * @param categoryName The category name to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Returns a string representation of the MenuCategory.
     * This is important for displaying the category in UI components like ComboBox.
     *
     * @return The category name
     */
    @Override
    public String toString() {
        return categoryName;
    }
}
