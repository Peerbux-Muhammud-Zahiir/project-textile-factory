package com.textile.factory.gui;

import com.textile.factory.database.ItemDAO;
import com.textile.factory.model.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class StockControlFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable stockTable;
    private DefaultTableModel tableModel;
    private JButton addButton, updateButton, deleteButton, refreshButton, lowStockReportButton, backButton;

    public StockControlFrame() {
        setTitle("Stock Control");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Table to display stock
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Item ID");
        tableModel.addColumn("Item Number");
        tableModel.addColumn("Category");
        tableModel.addColumn("Size");
        tableModel.addColumn("Cost Price");
        tableModel.addColumn("Stock Level");
        tableModel.addColumn("Out of Stock Threshold");

        stockTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(stockTable);
        panel.add(scrollPane);

        // Buttons for CRUD operations
        addButton = new JButton("Add Item");
        updateButton = new JButton("Update Item");
        deleteButton = new JButton("Delete Item");
        refreshButton = new JButton("Refresh");
        lowStockReportButton = new JButton("Generate Low Stock Report");
        backButton = new JButton("Back to Dashboard");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(lowStockReportButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel);

        // Add action listeners
        addButton.addActionListener(e -> addItem());
        updateButton.addActionListener(e -> updateItem());
        deleteButton.addActionListener(e -> deleteItem());
        refreshButton.addActionListener(e -> loadStock());
        lowStockReportButton.addActionListener(e -> generateLowStockReport());
        backButton.addActionListener(e -> {
            new DashboardFrame("inventory_officer").setVisible(true);
            dispose(); // Close the current frame
        });

        add(panel);
        loadStock(); // Load stock when the frame is opened
        setVisible(true);
    }

    // Method to load stock into the table
    private void loadStock() {
        tableModel.setRowCount(0); // Clear the table
        try {
            ItemDAO itemDAO = new ItemDAO();
            List<Item> items = itemDAO.getAllItems();
            for (Item item : items) {
                tableModel.addRow(new Object[]{
                    item.getItemId(),
                    item.getItemNumber(),
                    item.getCategory(),
                    item.getSize(),
                    item.getCostPrice(),
                    item.getStockLevel(),
                    item.getOutOfStockThreshold()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load stock: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to add a new item
    private void addItem() {
        // Open a dialog to input item details
        String itemNumber = JOptionPane.showInputDialog(this, "Enter Item Number:");
        String category = JOptionPane.showInputDialog(this, "Enter Category:");
        String size = JOptionPane.showInputDialog(this, "Enter Size:");
        String costPrice = JOptionPane.showInputDialog(this, "Enter Cost Price:");
        String stockLevel = JOptionPane.showInputDialog(this, "Enter Stock Level:");
        String outOfStockThreshold = JOptionPane.showInputDialog(this, "Enter Out of Stock Threshold:");

        try {
            Item item = new Item(
                0, // Item ID will be auto-generated
                itemNumber,
                category,
                size,
                Double.parseDouble(costPrice),
                Integer.parseInt(stockLevel),
                Integer.parseInt(outOfStockThreshold)
            );

            ItemDAO itemDAO = new ItemDAO();
            itemDAO.addItem(item);
            loadStock(); // Refresh the table
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input or database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to update an existing item
    private void updateItem() {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int itemId = (int) stockTable.getValueAt(selectedRow, 0);
        String itemNumber = JOptionPane.showInputDialog(this, "Enter Item Number:", stockTable.getValueAt(selectedRow, 1));
        String category = JOptionPane.showInputDialog(this, "Enter Category:", stockTable.getValueAt(selectedRow, 2));
        String size = JOptionPane.showInputDialog(this, "Enter Size:", stockTable.getValueAt(selectedRow, 3));
        String costPrice = JOptionPane.showInputDialog(this, "Enter Cost Price:", stockTable.getValueAt(selectedRow, 4));
        String stockLevel = JOptionPane.showInputDialog(this, "Enter Stock Level:", stockTable.getValueAt(selectedRow, 5));
        String outOfStockThreshold = JOptionPane.showInputDialog(this, "Enter Out of Stock Threshold:", stockTable.getValueAt(selectedRow, 6));

        try {
            Item item = new Item(
                itemId,
                itemNumber,
                category,
                size,
                Double.parseDouble(costPrice),
                Integer.parseInt(stockLevel),
                Integer.parseInt(outOfStockThreshold)
            );

            ItemDAO itemDAO = new ItemDAO();
            itemDAO.updateItem(item);
            loadStock(); // Refresh the table
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input or database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to delete an item
    private void deleteItem() {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int itemId = (int) stockTable.getValueAt(selectedRow, 0);
        try {
            ItemDAO itemDAO = new ItemDAO();
            itemDAO.deleteItem(itemId);
            loadStock(); // Refresh the table
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete item: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to generate a low stock report
    private void generateLowStockReport() {
        try {
            ItemDAO itemDAO = new ItemDAO();
            List<Item> lowStockItems = itemDAO.getLowStockItems();
            StringBuilder report = new StringBuilder("Low Stock Report:\n");
            for (Item item : lowStockItems) {
                report.append("Item ID: ").append(item.getItemId())
                      .append(", Item Number: ").append(item.getItemNumber())
                      .append(", Stock Level: ").append(item.getStockLevel())
                      .append(", Threshold: ").append(item.getOutOfStockThreshold())
                      .append("\n");
            }
            JOptionPane.showMessageDialog(this, report.toString(), "Low Stock Report", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to generate report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}