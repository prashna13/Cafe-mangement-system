package com.example.managecafe.model;

/**
 * Represents a table in the cafe management system.
 * Each table has an ID, capacity, and status indicating its current availability.
 */
public class Table {
    private int id;
    private int capacity;
    private String status; // e.g., "Available", "Occupied", "Reserved"

    /**
     * Default constructor for creating an empty Table.
     */
    public Table() {
    }

    /**
     * Constructor for creating a Table with specified values.
     *
     * @param id The unique identifier for the table
     * @param capacity The seating capacity of the table
     * @param status The current status of the table
     */
    public Table(int id, int capacity, String status) {
        this.id = id;
        this.capacity = capacity;
        this.status = status;
    }

    /**
     * Gets the table ID.
     *
     * @return The table ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the table ID.
     *
     * @param id The table ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the seating capacity of the table.
     *
     * @return The table capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the seating capacity of the table.
     *
     * @param capacity The table capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets the current status of the table.
     *
     * @return The table status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the table.
     *
     * @param status The table status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns a string representation of the Table.
     *
     * @return A string containing the table details
     */
    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                ", capacity=" + capacity +
                ", status='" + status + '\'' +
                '}';
    }
}
