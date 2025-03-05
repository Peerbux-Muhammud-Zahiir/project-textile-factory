package com.textile.factory.service;

import com.textile.factory.database.OrderDAO;
import com.textile.factory.model.Order;
import java.sql.SQLException;
import java.util.List;

public class ReportService {
    private OrderDAO orderDAO = new OrderDAO();

    // Generate a report of all orders
    public void generateOrderReport() {
        try {
            List<Order> orders = orderDAO.getAllOrders();
            for (Order order : orders) {
                System.out.println(order);
            }
        } catch (SQLException e) {
            System.out.println("Failed to generate report: " + e.getMessage());
        }
    }
}