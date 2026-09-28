package com.example.managecafe.model;

/**
 * Represents an individual item within an order in the cafe management system.
 * Each order item contains details about the menu item ordered, quantity, price, and subtotal.
 */
public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int menuItemId;
    private int quantity;
    private double itemPrice; // Price of the menu item at the time of the order
    private double subtotal;  // quantity * itemPrice
    private MenuItem menuItem; // Reference to the associated menu item

    /**
     * Default constructor for creating an empty OrderItem.
     */
    public OrderItem() {
    }

    /**
     * Constructor for creating an OrderItem with specified values including orderItemId.
     *
     * @param orderItemId The unique identifier for the order item
     * @param orderId The ID of the order to which this item belongs
     * @param menuItemId The ID of the menu item ordered
     * @param quantity The quantity of the menu item ordered
     * @param itemPrice The price of the menu item at the time of ordering
     */
    public OrderItem(int orderItemId, int orderId, int menuItemId, int quantity, double itemPrice) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.subtotal = quantity * itemPrice;
    }

    /**
     * Constructor for creating an OrderItem without an orderItemId (for new items).
     *
     * @param orderId The ID of the order to which this item belongs
     * @param menuItemId The ID of the menu item ordered
     * @param quantity The quantity of the menu item ordered
     * @param itemPrice The price of the menu item at the time of ordering
     */
    public OrderItem(int orderId, int menuItemId, int quantity, double itemPrice) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.subtotal = quantity * itemPrice;
    }

    /**
     * Gets the order item ID.
     *
     * @return The order item ID
     */
    public int getOrderItemId() {
        return orderItemId;
    }

    /**
     * Sets the order item ID.
     *
     * @param orderItemId The order item ID to set
     */
    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    /**
     * Gets the order ID to which this item belongs.
     *
     * @return The order ID
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Sets the order ID to which this item belongs.
     *
     * @param orderId The order ID to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets the menu item ID.
     *
     * @return The menu item ID
     */
    public int getMenuItemId() {
        return menuItemId;
    }

    /**
     * Sets the menu item ID.
     *
     * @param menuItemId The menu item ID to set
     */
    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    /**
     * Gets the quantity of the menu item ordered.
     *
     * @return The quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the menu item ordered and updates the subtotal.
     *
     * @param quantity The quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = quantity * this.itemPrice; // Update subtotal
    }

    /**
     * Gets the price of the menu item at the time of ordering.
     *
     * @return The item price
     */
    public double getItemPrice() {
        return itemPrice;
    }

    /**
     * Sets the price of the menu item and updates the subtotal.
     *
     * @param itemPrice The item price to set
     */
    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
        this.subtotal = quantity * itemPrice; // Update subtotal
    }

    /**
     * Gets the subtotal for this order item (quantity * itemPrice).
     *
     * @return The subtotal
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Sets the subtotal for this order item.
     *
     * @param subtotal The subtotal to set
     */
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Gets the associated menu item.
     *
     * @return The menu item
     */
    public MenuItem getMenuItem() {
        return menuItem;
    }

    /**
     * Sets the associated menu item.
     *
     * @param menuItem The menu item to set
     */
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    /**
     * Returns a string representation of the OrderItem.
     *
     * @return A string containing the order item details
     */
    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", orderId=" + orderId +
                ", menuItemId=" + menuItemId +
                ", quantity=" + quantity +
                ", itemPrice=" + itemPrice +
                ", subtotal=" + subtotal +
                '}';
    }
}
