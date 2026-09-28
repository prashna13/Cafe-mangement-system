package com.example.managecafe.model;

import java.time.LocalDate;

/**
 * Represents an entry in a sales report for the cafe management system.
 * Each entry contains the date and the total sales amount for that date.
 */
public class SalesReportEntry {
    private LocalDate salesDate;
    private double totalSales;

    /**
     * Constructor for creating a SalesReportEntry with specified values.
     *
     * @param salesDate The date for which the sales are reported
     * @param totalSales The total sales amount for the date
     */
    public SalesReportEntry(LocalDate salesDate, double totalSales) {
        this.salesDate = salesDate;
        this.totalSales = totalSales;
    }

    /**
     * Gets the date for which the sales are reported.
     *
     * @return The sales date
     */
    public LocalDate getSalesDate() {
        return salesDate;
    }

    /**
     * Sets the date for which the sales are reported.
     *
     * @param salesDate The sales date to set
     */
    public void setSalesDate(LocalDate salesDate) {
        this.salesDate = salesDate;
    }

    /**
     * Gets the total sales amount for the date.
     *
     * @return The total sales amount
     */
    public double getTotalSales() {
        return totalSales;
    }

    /**
     * Sets the total sales amount for the date.
     *
     * @param totalSales The total sales amount to set
     */
    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }
}
