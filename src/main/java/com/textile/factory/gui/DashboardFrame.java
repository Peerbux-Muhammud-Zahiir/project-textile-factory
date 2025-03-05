package com.textile.factory.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton orderManagementButton;
    private JButton stockControlButton;
    private JButton logoutButton;

    public DashboardFrame(String role) {
        setTitle("Dashboard - " + role);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        orderManagementButton = new JButton("Order Management");
        stockControlButton = new JButton("Stock Control");
        logoutButton = new JButton("Logout");

        // Add buttons based on the user's role
        panel.add(orderManagementButton);
        if (role.equals("inventory_officer") || role.equals("is_manager")) {
            panel.add(stockControlButton);
        }

        // Add action listeners
        orderManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OrderManagementFrame().setVisible(true);
                dispose(); // Close the current frame
            }
        });

        stockControlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StockControlFrame().setVisible(true);
                dispose(); // Close the current frame
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame().setVisible(true);
                dispose(); // Close the current frame
            }
        });

        add(panel);
        setVisible(true);
    }
}