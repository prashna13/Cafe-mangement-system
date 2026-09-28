package com.example.managecafe.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an order in the cafe management system.
 * An order contains information about the table, time, status, total amount, and items ordered.
 */
public class Order {
    private int orderId;
    private int tableNumber;
    private LocalDateTime orderTime;
    private double totalAmount;
    private String orderStatus; // e.g., "Pending", "Confirmed", "Served", "Paid"
    private List<OrderItem> orderItems;

    /**
     * Default constructor for creating an empty Order.
     */
    public Order() {
    }

    /**
     * Constructor for creating an Order with specified values.
     *
     * @param orderId The unique identifier for the order
     * @param tableNumber The table number associated with the order
     * @param orderTime The date and time when the order was placed
     * @param totalAmount The total amount for the order
     * @param orderStatus The current status of the order
     */
    public Order(int orderId, int tableNumber, LocalDateTime orderTime, double totalAmount, String orderStatus) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.orderTime = orderTime;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
    }

    /**
     * Gets the order ID.
     *
     * @return The order ID
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Sets the order ID.
     *
     * @param orderId The order ID to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets the table number associated with the order.
     *
     * @return The table number
     */
    public int getTableNumber() {
        return tableNumber;
    }

    /**
     * Sets the table number associated with the order.
     *
     * @param tableNumber The table number to set
     */
    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    /**
     * Gets the date and time when the order was placed.
     *
     * @return The order time
     */
    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    /**
     * Sets the date and time when the order was placed.
     *
     * @param orderTime The order time to set
     */
    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * Gets the total amount for the order.
     *
     * @return The total amount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the total amount for the order.
     *
     * @param totalAmount The total amount to set
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Gets the current status of the order.
     *
     * @return The order status
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * Sets the current status of the order.
     *
     * @param orderStatus The order status to set
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * Gets the list of items in the order.
     *
     * @return The list of order items
     */
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    /**
     * Sets the list of items in the order.
     *
     * @param orderItems The list of order items to set
     */
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    /**
     * Returns a string representation of the Order.
     *
     * @return A string containing the order details
     */
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", tableNumber=" + tableNumber +
                ", orderTime=" + orderTime +
                ", totalAmount=" + totalAmount +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }
}
