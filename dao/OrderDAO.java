package com.example.managecafe.dao;

import com.example.managecafe.model.Order;
import com.example.managecafe.model.SalesReportEntry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for orders.
 * Handles database operations related to orders and sales reporting.
 */
public class OrderDAO {
    private final Connection connection;

    /**
     * Constructs an OrderDAO with the specified database connection.
     *
     * @param connection The database connection to use
     */
    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adds a new order to the database.
     *
     * @param order The order to add
     * @return The generated order ID, or -1 if an error occurs
     * @throws SQLException If a database access error occurs (handled internally)
     */
    public int addOrder(Order order) {
        String sql = "INSERT INTO orders (table_id, order_date, order_status, total_amount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, order.getTableNumber());
            statement.setObject(2, order.getOrderTime());
            statement.setString(3, order.getOrderStatus());
            statement.setDouble(4, order.getTotalAmount());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Creating order failed, no rows affected.");
                return -1;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    System.err.println("Creating order failed, no ID obtained.");
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding order: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId The ID of the order to retrieve
     * @return The order with the specified ID, or null if not found or an error occurs
     */
    public Order getOrderById(int orderId) {
        String sql = "SELECT order_id, table_id, order_date, order_status, total_amount FROM orders WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getInt("order_id"));
                order.setTableNumber(resultSet.getInt("table_id"));
                order.setOrderTime(resultSet.getObject("order_date", LocalDateTime.class));
                order.setOrderStatus(resultSet.getString("order_status"));
                order.setTotalAmount(resultSet.getDouble("total_amount"));
                return order;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error getting order by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return A list of all orders, or an empty list if an error occurs
     */
    public List<Order> getAllOrders() {
        String sql = "SELECT order_id, table_id, order_date, order_status, total_amount FROM orders";
        List<Order> orderList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getInt("order_id"));
                order.setTableNumber(resultSet.getInt("table_id"));
                order.setOrderTime(resultSet.getObject("order_date", LocalDateTime.class));
                order.setOrderStatus(resultSet.getString("order_status"));
                order.setTotalAmount(resultSet.getDouble("total_amount"));
                orderList.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all orders: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list on error
        }
        return orderList;
    }

    /**
     * Gets the total number of orders for the current day.
     *
     * @return The count of orders for today, or 0 if an error occurs
     */
    public int getTotalOrdersToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        String sql = "SELECT COUNT(*) FROM orders WHERE order_date >= ? AND order_date <= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, startOfDay);
            statement.setObject(2, endOfDay);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            System.err.println("Error getting total orders today: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Gets the total revenue for the current day.
     *
     * @return The total revenue for today, or 0.0 if an error occurs
     */
    public double getTotalRevenueToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        String sql = "SELECT SUM(total_amount) FROM orders WHERE order_date >= ? AND order_date <= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, startOfDay);
            statement.setObject(2, endOfDay);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double revenue = resultSet.getDouble(1);
                return resultSet.wasNull() ? 0.0 : revenue;
            }
            return 0.0;
        } catch (SQLException e) {
            System.err.println("Error getting total revenue today: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * Gets the count of currently occupied tables.
     *
     * @return The number of occupied tables, or 0 if an error occurs
     */
    public int getActiveTableCount() {
        String sql = "SELECT COUNT(*) FROM tables WHERE status = 'Occupied'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            System.err.println("Error getting active table count: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Gets the most recent orders, limited to the specified count.
     *
     * @param limit The maximum number of orders to retrieve
     * @return A list of the most recent orders, or an empty list if an error occurs
     */
    public List<Order> getRecentOrders(int limit) {
        String sql = "SELECT order_id, table_id, order_date, order_status, total_amount FROM orders ORDER BY order_date DESC LIMIT ?";
        List<Order> recentOrders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getInt("order_id"));
                order.setTableNumber(resultSet.getInt("table_id"));
                order.setOrderTime(resultSet.getObject("order_date", LocalDateTime.class));
                order.setOrderStatus(resultSet.getString("order_status"));
                order.setTotalAmount(resultSet.getDouble("total_amount"));
                recentOrders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent orders: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list on error
        }
        return recentOrders;
    }

    /**
     * Gets daily sales data for the specified date range.
     *
     * @param startDate The start date for the report
     * @param endDate The end date for the report
     * @return A list of daily sales entries, or an empty list if an error occurs
     */
    public List<SalesReportEntry> getDailySales(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT DATE(order_date) AS sale_date, SUM(total_amount) AS daily_total " +
                "FROM orders " +
                "WHERE DATE(order_date) >= ? AND DATE(order_date) <= ? " +
                "GROUP BY DATE(order_date) " +
                "ORDER BY sale_date";
        List<SalesReportEntry> salesReport = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, java.sql.Date.valueOf(startDate));
            statement.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                LocalDate saleDate = resultSet.getDate("sale_date").toLocalDate();
                double dailyTotal = resultSet.getDouble("daily_total");
                salesReport.add(new SalesReportEntry(saleDate, dailyTotal));
            }
        } catch (SQLException e) {
            System.err.println("Error getting daily sales: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list on error
        }
        return salesReport;
    }

    /**
     * Retrieves orders within a specified date range.
     *
     * @param startDate The start date for filtering orders.
     * @param endDate The end date for filtering orders.
     * @return A list of orders within the specified date range, or an empty list if an error occurs.
     */
    public List<Order> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT order_id, table_id, order_date, order_status, total_amount " +
                "FROM orders " +
                "WHERE DATE(order_date) >= ? AND DATE(order_date) <= ?";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, java.sql.Date.valueOf(startDate));
            statement.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getInt("order_id"));
                order.setTableNumber(resultSet.getInt("table_id"));
                order.setOrderTime(resultSet.getObject("order_date", LocalDateTime.class));
                order.setOrderStatus(resultSet.getString("order_status"));
                order.setTotalAmount(resultSet.getDouble("total_amount"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error getting orders by date range: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list on error
        }
        return orders;
    }
}
