package com.textile.factory.gui;

import com.textile.factory.database.OrderDAO;
import com.textile.factory.model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class OrderManagementFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JButton addButton, updateButton, deleteButton, refreshButton, generateReportButton, backButton;

    public OrderManagementFrame() {
        setTitle("Order Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Table to display orders
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Order ID");
        tableModel.addColumn("Customer ID");
        tableModel.addColumn("Order Date");
        tableModel.addColumn("Transport Charges");
        tableModel.addColumn("Total Amount");
        tableModel.addColumn("Status");

        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        panel.add(scrollPane);

        // Buttons for CRUD operations
        addButton = new JButton("Add Order");
        updateButton = new JButton("Update Order");
        deleteButton = new JButton("Delete Order");
        refreshButton = new JButton("Refresh");
        generateReportButton = new JButton("Generate Quotation");
        backButton = new JButton("Back to Dashboard");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(generateReportButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel);

        // Add action listeners
        addButton.addActionListener(e -> addOrder());
        updateButton.addActionListener(e -> updateOrder());
        deleteButton.addActionListener(e -> deleteOrder());
        refreshButton.addActionListener(e -> loadOrders());
        generateReportButton.addActionListener(e -> generateQuotation());
        backButton.addActionListener(e -> {
            new DashboardFrame("sales_person").setVisible(true);
            dispose(); // Close the current frame
        });

        add(panel);
        loadOrders(); // Load orders when the frame is opened
        setVisible(true);
    }

    // Method to load orders into the table
    private void loadOrders() {
        tableModel.setRowCount(0); // Clear the table
        try {
            OrderDAO orderDAO = new OrderDAO();
            List<Order> orders = orderDAO.getAllOrders();
            for (Order order : orders) {
                tableModel.addRow(new Object[]{
                    order.getOrderId(),
                    order.getCustomerId(),
                    order.getOrderDate(),
                    order.getTransportCharges(),
                    order.getTotalAmount(),
                    order.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to add a new order
    private void addOrder() {
        // Open a dialog to input order details
        String customerId = JOptionPane.showInputDialog(this, "Enter Customer ID:");
        String orderDate = JOptionPane.showInputDialog(this, "Enter Order Date (YYYY-MM-DD):");
        String transportCharges = JOptionPane.showInputDialog(this, "Enter Transport Charges:");
        String totalAmount = JOptionPane.showInputDialog(this, "Enter Total Amount:");
        String status = JOptionPane.showInputDialog(this, "Enter Status (pending/shipped/backordered):");

        try {
            Order order = new Order(
                0, // Order ID will be auto-generated
                Integer.parseInt(customerId),
                orderDate,
                Double.parseDouble(transportCharges),
                Double.parseDouble(totalAmount),
                status
            );

            OrderDAO orderDAO = new OrderDAO();
            orderDAO.addOrder(order);
            loadOrders(); // Refresh the table
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input or database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to update an existing order
    private void updateOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int orderId = (int) orderTable.getValueAt(selectedRow, 0);
        String customerId = JOptionPane.showInputDialog(this, "Enter Customer ID:", orderTable.getValueAt(selectedRow, 1));
        String orderDate = JOptionPane.showInputDialog(this, "Enter Order Date (YYYY-MM-DD):", orderTable.getValueAt(selectedRow, 2));
        String transportCharges = JOptionPane.showInputDialog(this, "Enter Transport Charges:", orderTable.getValueAt(selectedRow, 3));
        String totalAmount = JOptionPane.showInputDialog(this, "Enter Total Amount:", orderTable.getValueAt(selectedRow, 4));
        String status = JOptionPane.showInputDialog(this, "Enter Status (pending/shipped/backordered):", orderTable.getValueAt(selectedRow, 5));

        try {
            Order order = new Order(
                orderId,
                Integer.parseInt(customerId),
                orderDate,
                Double.parseDouble(transportCharges),
                Double.parseDouble(totalAmount),
                status
            );

            OrderDAO orderDAO = new OrderDAO();
            orderDAO.updateOrder(order);
            loadOrders(); // Refresh the table
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input or database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to delete an order
    private void deleteOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int orderId = (int) orderTable.getValueAt(selectedRow, 0);
        try {
            OrderDAO orderDAO = new OrderDAO();
            orderDAO.deleteOrder(orderId);
            loadOrders(); // Refresh the table
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to generate a quotation report
    private void generateQuotation() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to generate a quotation.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int orderId = (int) orderTable.getValueAt(selectedRow, 0);
        try {
            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.getOrderById(orderId);
            if (order != null) {
                String report = "Quotation for Order ID: " + order.getOrderId() + "\n" +
                               "Customer ID: " + order.getCustomerId() + "\n" +
                               "Order Date: " + order.getOrderDate() + "\n" +
                               "Transport Charges: $" + order.getTransportCharges() + "\n" +
                               "Total Amount: $" + order.getTotalAmount() + "\n" +
                               "Status: " + order.getStatus();
                JOptionPane.showMessageDialog(this, report, "Quotation", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to generate quotation: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}