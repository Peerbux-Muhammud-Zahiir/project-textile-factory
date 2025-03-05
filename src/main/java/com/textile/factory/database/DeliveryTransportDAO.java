package com.textile.factory.database;

import com.textile.factory.model.DeliveryTransport;
import java.sql.*;

public class DeliveryTransportDAO {
    // Add a new delivery record
    public void addDelivery(DeliveryTransport delivery) throws SQLException {
        String query = "INSERT INTO delivery_transport (order_id, transport_method, delivery_address, estimated_cost, delivery_status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, delivery.getOrderId());
            stmt.setString(2, delivery.getTransportMethod());
            stmt.setString(3, delivery.getDeliveryAddress());
            stmt.setDouble(4, delivery.getEstimatedCost());
            stmt.setString(5, delivery.getDeliveryStatus());
            stmt.executeUpdate();
        }
    }

    // Update delivery status
    public void updateDeliveryStatus(int deliveryId, String status) throws SQLException {
        String query = "UPDATE delivery_transport SET delivery_status = ? WHERE delivery_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, deliveryId);
            stmt.executeUpdate();
        }
    }
}