package com.textile.factory.model;

public class Order {
    private int orderId;
    private int customerId;
    private String orderDate;
    private double transportCharges;
    private double totalAmount;
    private String status;

    // Constructor
    public Order(int orderId, int customerId, String orderDate, double transportCharges, double totalAmount, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.transportCharges = transportCharges;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; } // Add this method
    public int getCustomerId() { return customerId; }
    public String getOrderDate() { return orderDate; }
    public double getTransportCharges() { return transportCharges; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
}