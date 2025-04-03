package com.textile.factory.gui;

import com.textile.factory.database.ItemDAO;
import com.textile.factory.model.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class StockControlFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable stockTable;
    private DefaultTableModel tableModel;
    private JButton addButton, updateButton, deleteButton, refreshButton, lowStockReportButton, backButton;
    private String[] categories = {"T-Shirt", "Shirt", "Pants", "Dress", "Jacket", "Skirt", "Other"};

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
        addButton.addActionListener(e -> showItemForm(null));
        updateButton.addActionListener(e -> {
            int selectedRow = stockTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an item to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Item item = new Item(
                (int) stockTable.getValueAt(selectedRow, 0),
                (String) stockTable.getValueAt(selectedRow, 1),
                (String) stockTable.getValueAt(selectedRow, 2),
                (String) stockTable.getValueAt(selectedRow, 3),
                (double) stockTable.getValueAt(selectedRow, 4),
                (int) stockTable.getValueAt(selectedRow, 5),
                (int) stockTable.getValueAt(selectedRow, 6)
            );
            showItemForm(item);
        });
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

    // Method to show the item form (used for both add and update)
    private void showItemForm(Item existingItem) {
        JDialog dialog = new JDialog(this, existingItem == null ? "Add New Item" : "Update Item", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        // Item Number
        JTextField itemNumberField = new JTextField();
        formPanel.add(new JLabel("Item Number:"));
        formPanel.add(itemNumberField);

        // Category (Dropdown)
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryCombo);

        // Size (Radio Buttons)
        JPanel sizePanel = new JPanel();
        ButtonGroup sizeGroup = new ButtonGroup();
        JRadioButton smallRadio = new JRadioButton("Small");
        JRadioButton mediumRadio = new JRadioButton("Medium");
        JRadioButton largeRadio = new JRadioButton("Large");
        sizeGroup.add(smallRadio);
        sizeGroup.add(mediumRadio);
        sizeGroup.add(largeRadio);
        sizePanel.add(smallRadio);
        sizePanel.add(mediumRadio);
        sizePanel.add(largeRadio);
        formPanel.add(new JLabel("Size:"));
        formPanel.add(sizePanel);

        // Cost Price
        JTextField costPriceField = new JTextField();
        formPanel.add(new JLabel("Cost Price:"));
        formPanel.add(costPriceField);

        // Stock Level
        JTextField stockLevelField = new JTextField();
        formPanel.add(new JLabel("Stock Level:"));
        formPanel.add(stockLevelField);

        // Out of Stock Threshold
        JTextField thresholdField = new JTextField();
        formPanel.add(new JLabel("Out of Stock Threshold:"));
        formPanel.add(thresholdField);

        // If updating, populate fields with existing values
        if (existingItem != null) {
            itemNumberField.setText(existingItem.getItemNumber());
            categoryCombo.setSelectedItem(existingItem.getCategory());
            switch (existingItem.getSize()) {
                case "Small": smallRadio.setSelected(true); break;
                case "Medium": mediumRadio.setSelected(true); break;
                case "Large": largeRadio.setSelected(true); break;
            }
            costPriceField.setText(String.valueOf(existingItem.getCostPrice()));
            stockLevelField.setText(String.valueOf(existingItem.getStockLevel()));
            thresholdField.setText(String.valueOf(existingItem.getOutOfStockThreshold()));
        }

        // Submit button
        JButton submitButton = new JButton(existingItem == null ? "Add Item" : "Update Item");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get selected size
                    String size = "";
                    if (smallRadio.isSelected()) size = "Small";
                    else if (mediumRadio.isSelected()) size = "Medium";
                    else if (largeRadio.isSelected()) size = "Large";

                    Item item = new Item(
                        existingItem != null ? existingItem.getItemId() : 0,
                        itemNumberField.getText(),
                        (String) categoryCombo.getSelectedItem(),
                        size,
                        Double.parseDouble(costPriceField.getText()),
                        Integer.parseInt(stockLevelField.getText()),
                        Integer.parseInt(thresholdField.getText())
                    );

                    ItemDAO itemDAO = new ItemDAO();
                    if (existingItem == null) {
                        itemDAO.addItem(item);
                    } else {
                        itemDAO.updateItem(item);
                    }
                    loadStock();
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid input or database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(submitButton, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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