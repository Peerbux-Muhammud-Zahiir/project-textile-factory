package com.textile.factory.database;

import com.textile.factory.model.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    // Add a new item
    public void addItem(Item item) throws SQLException {
        String query = "INSERT INTO items (item_number, category, size, cost_price, stock_level, out_of_stock_threshold) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, item.getItemNumber());
            stmt.setString(2, item.getCategory());
            stmt.setString(3, item.getSize());
            stmt.setDouble(4, item.getCostPrice());
            stmt.setInt(5, item.getStockLevel());
            stmt.setInt(6, item.getOutOfStockThreshold());
            stmt.executeUpdate();
        }
    }

    // Update an existing item
    public void updateItem(Item item) throws SQLException {
        String query = "UPDATE items SET item_number = ?, category = ?, size = ?, cost_price = ?, stock_level = ?, out_of_stock_threshold = ? WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, item.getItemNumber());
            stmt.setString(2, item.getCategory());
            stmt.setString(3, item.getSize());
            stmt.setDouble(4, item.getCostPrice());
            stmt.setInt(5, item.getStockLevel());
            stmt.setInt(6, item.getOutOfStockThreshold());
            stmt.setInt(7, item.getItemId());
            stmt.executeUpdate();
        }
    }

    // Delete an item by ID
    public void deleteItem(int itemId) throws SQLException {
        String query = "DELETE FROM items WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
        }
    }

    // Get all items
    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String query = "SELECT * FROM items";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Item item = new Item(
                    rs.getInt("item_id"),
                    rs.getString("item_number"),
                    rs.getString("category"),
                    rs.getString("size"),
                    rs.getDouble("cost_price"),
                    rs.getInt("stock_level"),
                    rs.getInt("out_of_stock_threshold")
                );
                items.add(item);
            }
        }
        return items;
    }

    // Get items with low stock
    public List<Item> getLowStockItems() throws SQLException {
        List<Item> lowStockItems = new ArrayList<>();
        String query = "SELECT * FROM items WHERE stock_level < out_of_stock_threshold";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Item item = new Item(
                    rs.getInt("item_id"),
                    rs.getString("item_number"),
                    rs.getString("category"),
                    rs.getString("size"),
                    rs.getDouble("cost_price"),
                    rs.getInt("stock_level"),
                    rs.getInt("out_of_stock_threshold")
                );
                lowStockItems.add(item);
            }
        }
        return lowStockItems;
    }
}