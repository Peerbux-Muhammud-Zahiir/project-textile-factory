package com.textile.factory.gui;

import com.textile.factory.database.OrderDAO;
import com.textile.factory.model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        addButton.addActionListener(e -> showOrderForm(null));
        updateButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Order selectedOrder = getOrderFromTable(selectedRow);
            showOrderForm(selectedOrder);
        });
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

    private Order getOrderFromTable(int row) {
        return new Order(
            (int) orderTable.getValueAt(row, 0),
            (int) orderTable.getValueAt(row, 1),
            (String) orderTable.getValueAt(row, 2),
            (double) orderTable.getValueAt(row, 3),
            (double) orderTable.getValueAt(row, 4),
            (String) orderTable.getValueAt(row, 5)
        );
    }

    private void showOrderForm(Order order) {
        JDialog dialog = new JDialog(this, order == null ? "Add New Order" : "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(0, 2, 5, 5));

        // Form fields
        JLabel customerIdLabel = new JLabel("Customer ID:");
        JTextField customerIdField = new JTextField();
        if (order != null) customerIdField.setText(String.valueOf(order.getCustomerId()));

        JLabel orderDateLabel = new JLabel("Order Date (YYYY-MM-DD):");
        JTextField orderDateField = new JTextField();
        if (order != null) orderDateField.setText(order.getOrderDate());

        JLabel transportChargesLabel = new JLabel("Transport Charges:");
        JTextField transportChargesField = new JTextField();
        if (order != null) transportChargesField.setText(String.valueOf(order.getTransportCharges()));

        JLabel totalAmountLabel = new JLabel("Total Amount:");
        JTextField totalAmountField = new JTextField();
        if (order != null) totalAmountField.setText(String.valueOf(order.getTotalAmount()));

        JLabel statusLabel = new JLabel("Status:");
        JPanel statusPanel = new JPanel();
        ButtonGroup statusGroup = new ButtonGroup();
        JRadioButton pendingRadio = new JRadioButton("pending");
        JRadioButton shippedRadio = new JRadioButton("shipped");
        JRadioButton backorderedRadio = new JRadioButton("backordered");
        statusGroup.add(pendingRadio);
        statusGroup.add(shippedRadio);
        statusGroup.add(backorderedRadio);
        statusPanel.add(pendingRadio);
        statusPanel.add(shippedRadio);
        statusPanel.add(backorderedRadio);

        if (order != null) {
            switch (order.getStatus()) {
                case "pending":
                    pendingRadio.setSelected(true);
                    break;
                case "shipped":
                    shippedRadio.setSelected(true);
                    break;
                case "backordered":
                    backorderedRadio.setSelected(true);
                    break;
            }
        } else {
            pendingRadio.setSelected(true); // Default selection
        }

        // Add components to dialog
        dialog.add(customerIdLabel);
        dialog.add(customerIdField);
        dialog.add(orderDateLabel);
        dialog.add(orderDateField);
        dialog.add(transportChargesLabel);
        dialog.add(transportChargesField);
        dialog.add(totalAmountLabel);
        dialog.add(totalAmountField);
        dialog.add(statusLabel);
        dialog.add(statusPanel);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String status = "";
                    if (pendingRadio.isSelected()) status = "pending";
                    else if (shippedRadio.isSelected()) status = "shipped";
                    else if (backorderedRadio.isSelected()) status = "backordered";

                    Order newOrder = new Order(
                        order != null ? order.getOrderId() : 0,
                        Integer.parseInt(customerIdField.getText()),
                        orderDateField.getText(),
                        Double.parseDouble(transportChargesField.getText()),
                        Double.parseDouble(totalAmountField.getText()),
                        status
                    );

                    OrderDAO orderDAO = new OrderDAO();
                    if (order == null) {
                        orderDAO.addOrder(newOrder);
                    } else {
                        orderDAO.updateOrder(newOrder);
                    }
                    loadOrders();
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid input or database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.add(new JLabel()); // Empty cell for layout
        dialog.add(submitButton);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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
                               "Transport Charges: Rs" + order.getTransportCharges() + "\n" +
                               "Total Amount: Rs" + order.getTotalAmount() + "\n" +
                               "Status: " + order.getStatus();
                JOptionPane.showMessageDialog(this, report, "Quotation", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to generate quotation: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}