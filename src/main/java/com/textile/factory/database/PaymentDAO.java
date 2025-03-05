package com.textile.factory.database;

import com.textile.factory.model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    // Add a new payment
    public void addPayment(Payment payment) throws SQLException {
        String query = "INSERT INTO payments (order_id, payment_method, amount, payment_date, credit_card_number, expiration_date, bank_number, card_holder_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, payment.getOrderId());
            stmt.setString(2, payment.getPaymentMethod());
            stmt.setDouble(3, payment.getAmount());
            stmt.setDate(4, Date.valueOf(payment.getPaymentDate()));
            stmt.setString(5, payment.getCreditCardNumber());
            stmt.setString(6, payment.getExpirationDate());
            stmt.setString(7, payment.getBankNumber());
            stmt.setString(8, payment.getCardHolderName());
            stmt.executeUpdate();
        }
    }

    // Get all payments for an order
    public List<Payment> getPaymentsByOrderId(int orderId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("order_id"),
                        rs.getString("payment_method"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date").toLocalDate(),
                        rs.getString("credit_card_number"),
                        rs.getString("expiration_date"),
                        rs.getString("bank_number"),
                        rs.getString("card_holder_name")
                    );
                    payments.add(payment);
                }
            }
        }
        return payments;
    }
}