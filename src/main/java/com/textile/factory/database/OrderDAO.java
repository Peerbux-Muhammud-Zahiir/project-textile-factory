package com.textile.factory.database;

import com.textile.factory.model.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    // Add a new order
    public void addOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (customer_id, order_date, transport_charges, total_amount, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getCustomerId());
            stmt.setString(2, order.getOrderDate());
            stmt.setDouble(3, order.getTransportCharges());
            stmt.setDouble(4, order.getTotalAmount());
            stmt.setString(5, order.getStatus());
            stmt.executeUpdate();

            // Retrieve the generated order ID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setOrderId(rs.getInt(1)); // This line requires the setOrderId method
                }
            }
        }
    }

    // Delete an order by ID
    public void deleteOrder(int orderId) throws SQLException {
        String query = "DELETE FROM orders WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }

    // Modify an existing order
    public void updateOrder(Order order) throws SQLException {
        String query = "UPDATE orders SET customer_id = ?, order_date = ?, transport_charges = ?, total_amount = ?, status = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, order.getCustomerId());
            stmt.setString(2, order.getOrderDate());
            stmt.setDouble(3, order.getTransportCharges());
            stmt.setDouble(4, order.getTotalAmount());
            stmt.setString(5, order.getStatus());
            stmt.setInt(6, order.getOrderId());
            stmt.executeUpdate();
        }
    }

    // Search for an order by ID
    public Order getOrderById(int orderId) throws SQLException {
        String query = "SELECT * FROM orders WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getString("order_date"),
                        rs.getDouble("transport_charges"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    // Get all orders
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("order_id"),
                    rs.getInt("customer_id"),
                    rs.getString("order_date"),
                    rs.getDouble("transport_charges"),
                    rs.getDouble("total_amount"),
                    rs.getString("status")
                );
                orders.add(order);
            }
        }
        return orders;
    }
}