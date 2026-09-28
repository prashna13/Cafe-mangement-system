package com.example.managecafe.model;

/**
 * Represents a menu item in the cafe management system.
 * Each menu item has details such as name, description, category, price, and availability status.
 */
public class MenuItem {
    private int id;
    private String name;
    private String description;
    private String category;
    private double price;
    private boolean available;

    /**
     * Default constructor for creating an empty MenuItem.
     */
    public MenuItem() {
    }

    /**
     * Constructor for creating a MenuItem with specified values.
     *
     * @param id The unique identifier for the menu item
     * @param name The name of the menu item
     * @param description A description of the menu item
     * @param category The category to which the menu item belongs
     * @param price The price of the menu item
     * @param available Whether the menu item is currently available
     */
    public MenuItem(int id, String name, String description, String category, double price, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.available = available;
    }

    /**
     * Gets the menu item ID.
     *
     * @return The menu item ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the menu item name.
     *
     * @return The menu item name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the menu item description.
     *
     * @return The menu item description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the category to which the menu item belongs.
     *
     * @return The menu item category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the menu item price.
     *
     * @return The menu item price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Checks if the menu item is available.
     *
     * @return true if the menu item is available, false otherwise
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Sets the menu item ID.
     *
     * @param id The menu item ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the menu item name.
     *
     * @param name The menu item name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the menu item description.
     *
     * @param description The menu item description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the category to which the menu item belongs.
     *
     * @param category The menu item category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Sets the menu item price.
     *
     * @param price The menu item price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets whether the menu item is available.
     *
     * @param available The availability status to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
