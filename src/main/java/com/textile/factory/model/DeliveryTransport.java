package com.textile.factory.model;

public class DeliveryTransport {
    private int deliveryId;
    private int orderId;
    private String transportMethod;
    private String deliveryAddress;
    private double estimatedCost;
    private String deliveryStatus;

    // Constructor
    public DeliveryTransport(int deliveryId, int orderId, String transportMethod, String deliveryAddress, double estimatedCost, String deliveryStatus) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.transportMethod = transportMethod;
        this.deliveryAddress = deliveryAddress;
        this.estimatedCost = estimatedCost;
        this.deliveryStatus = deliveryStatus;
    }

    // Getters and Setters
    public int getDeliveryId() { return deliveryId; }
    public int getOrderId() { return orderId; }
    public String getTransportMethod() { return transportMethod; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public double getEstimatedCost() { return estimatedCost; }
    public String getDeliveryStatus() { return deliveryStatus; }
}