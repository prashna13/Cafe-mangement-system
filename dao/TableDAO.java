package com.example.managecafe.dao;

import com.example.managecafe.model.Table;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {
    private Connection connection;

    public TableDAO(Connection connection) {
        this.connection = connection;
    }

    // Get all tables
    public List<Table> getAllTables() throws SQLException {
        List<Table> tables = new ArrayList<>();
        String sql = "SELECT id, status, capacity FROM tables";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Table table = new Table();
                table.setId(resultSet.getInt("id"));
                table.setStatus(resultSet.getString("status"));
                table.setCapacity(resultSet.getInt("capacity"));
                tables.add(table);
            }
        }
        return tables;
    }

    // Get table by ID
    public Table getTableById(int id) throws SQLException {
        Table table = null;
        String sql = "SELECT id, status, capacity FROM tables WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                table = new Table();
                table.setId(resultSet.getInt("id"));
                table.setStatus(resultSet.getString("status"));
                table.setCapacity(resultSet.getInt("capacity"));
            }
        }
        return table;
    }

    // Add a new table
    public void addTable(Table table) throws SQLException {
        String sql = "INSERT INTO tables (status, capacity) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, table.getStatus());
            preparedStatement.setInt(2, table.getCapacity());
            preparedStatement.executeUpdate();
        }
    }

    // Update an existing table
    public void updateTable(Table table) throws SQLException {
        String sql = "UPDATE tables SET status = ?, capacity = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, table.getStatus());
            preparedStatement.setInt(2, table.getCapacity());
            preparedStatement.setInt(3, table.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Delete a table by ID
    public void deleteTable(int id) throws SQLException {
        String sql = "DELETE FROM tables WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    //Get Active Table Count
    public int getActiveTables() throws SQLException {
        int activeTablesCount = 0;
        String sql = "SELECT COUNT(*) FROM tables WHERE status = 'Occupied'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                activeTablesCount = resultSet.getInt(1);
            }
        }
        return activeTablesCount;
    }
}
