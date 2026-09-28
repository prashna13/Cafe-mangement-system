package com.example.managecafe.dao;

import com.example.managecafe.model.OrderItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for order items.
 * Handles database operations related to individual items within orders.
 */
public class OrderItemDAO {
    private final Connection connection;
    private final MenuItemDAO menuItemDAO;

    /**
     * Constructs an OrderItemDAO with the specified database connection.
     *
     * @param connection The database connection to use
     */
    public OrderItemDAO(Connection connection) {
        this.connection = connection;
        this.menuItemDAO = new MenuItemDAO(connection);
    }

    /**
     * Retrieves all order items for a specific order.
     *
     * @param orderId The ID of the order
     * @return A list of order items for the specified order
     * @throws SQLException If a database access error occurs
     */
    public List<OrderItem> getOrderItemsByOrderId(int orderId) throws SQLException {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT order_item_id, order_id, menu_item_id, quantity, unit_price, subtotal FROM order_items WHERE order_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                OrderItem orderItem = createOrderItemFromResultSet(resultSet);
                orderItems.add(orderItem);
            }
            return orderItems;
        }
    }

    /**
     * Retrieves an order item by its ID.
     *
     * @param orderItemId The ID of the order item to retrieve
     * @return The order item with the specified ID, or null if not found
     * @throws SQLException If a database access error occurs
     */
    public OrderItem getOrderItemById(int orderItemId) throws SQLException {
        String sql = "SELECT order_item_id, order_id, menu_item_id, quantity, unit_price, subtotal FROM order_items WHERE order_item_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderItemId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return createOrderItemFromResultSet(resultSet);
            }
            return null;
        }
    }

    /**
     * Adds a new order item to the database.
     *
     * @param orderItem The order item to add
     * @throws SQLException If a database access error occurs
     */
    public void addOrderItem(OrderItem orderItem) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getMenuItemId());
            statement.setInt(3, orderItem.getQuantity());
            statement.setDouble(4, orderItem.getItemPrice());
            statement.setDouble(5, orderItem.getSubtotal());

            statement.executeUpdate();

            // Get the auto-generated ID and set it on the order item
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderItem.setOrderItemId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Failed to insert order item, no ID obtained.");
            }
        }
    }

    /**
     * Updates an existing order item in the database.
     *
     * @param orderItem The order item to update
     * @throws SQLException If a database access error occurs
     */
    public void updateOrderItem(OrderItem orderItem) throws SQLException {
        String sql = "UPDATE order_items SET order_id = ?, menu_item_id = ?, quantity = ?, unit_price = ?, subtotal = ? WHERE order_item_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getMenuItemId());
            statement.setInt(3, orderItem.getQuantity());
            statement.setDouble(4, orderItem.getItemPrice());
            statement.setDouble(5, orderItem.getSubtotal());
            statement.setInt(6, orderItem.getOrderItemId());

            statement.executeUpdate();
        }
    }

    /**
     * Deletes an order item from the database.
     *
     * @param orderItemId The ID of the order item to delete
     * @throws SQLException If a database access error occurs
     */
    public void deleteOrderItem(int orderItemId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_item_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderItemId);
            statement.executeUpdate();
        }
    }

    /**
     * Deletes all order items for a specific order.
     *
     * @param orderId The ID of the order whose items should be deleted
     * @throws SQLException If a database access error occurs
     */
    public void deleteOrderItemsByOrderId(int orderId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            statement.executeUpdate();
        }
    }

    /**
     * Creates an OrderItem object from the current row in the ResultSet.
     *
     * @param resultSet The ResultSet containing order item data
     * @return A populated OrderItem object
     * @throws SQLException If a database access error occurs
     */
    private OrderItem createOrderItemFromResultSet(ResultSet resultSet) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(resultSet.getInt("order_item_id"));
        orderItem.setOrderId(resultSet.getInt("order_id"));
        orderItem.setMenuItemId(resultSet.getInt("menu_item_id"));
        orderItem.setQuantity(resultSet.getInt("quantity"));
        orderItem.setItemPrice(resultSet.getDouble("unit_price"));
        orderItem.setSubtotal(resultSet.getDouble("subtotal"));
        return orderItem;
    }
}
