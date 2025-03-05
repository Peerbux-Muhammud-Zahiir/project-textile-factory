package com.textile.factory.service;

import java.sql.*;

import com.textile.factory.database.OrderDAO;
import com.textile.factory.model.Order;

public class OrderService {
    private OrderDAO orderDAO = new OrderDAO();

    public void placeOrder(Order order) {
        try {
            orderDAO.addOrder(order);
            System.out.println("Order placed successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to place order: " + e.getMessage());
        }
    }

    public void viewAllOrders() {
        try {
            orderDAO.getAllOrders().forEach(order -> System.out.println(order));
        } catch (SQLException e) {
            System.out.println("Failed to fetch orders: " + e.getMessage());
        }
    }
}