-- Create Database
CREATE DATABASE IF NOT EXISTS textile_factory;
USE textile_factory;

-- Table: users
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('sales_person', 'inventory_officer', 'is_manager') NOT NULL
);

-- Table: customers
CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200),
    contact_details VARCHAR(100)
);

-- Table: items
CREATE TABLE items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    item_number VARCHAR(50) NOT NULL,
    category VARCHAR(50),
    size VARCHAR(20),
    cost_price DECIMAL(10, 2),
    stock_level INT,
    out_of_stock_threshold INT
);

-- Table: orders
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    order_date DATE,
    transport_charges DECIMAL(10, 2),
    total_amount DECIMAL(10, 2),
    status ENUM('pending', 'shipped', 'backordered'),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- Table: order_items
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    item_id INT,
    quantity INT,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (item_id) REFERENCES items(item_id)
);

-- Table: payments
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    payment_method ENUM('cash', 'cheque', 'credit_card'),
    amount DECIMAL(10, 2),
    payment_date DATE,
    credit_card_number VARCHAR(20),
    expiration_date VARCHAR(10),
    bank_number VARCHAR(20),
    card_holder_name VARCHAR(100),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

-- Table: delivery_transport
CREATE TABLE delivery_transport (
    delivery_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    transport_method ENUM('truck', 'ship', 'air'),
    delivery_address VARCHAR(200),
    estimated_cost DECIMAL(10, 2),
    delivery_status ENUM('pending', 'in_transit', 'delivered'),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

-- Insert Sample Data into users
INSERT INTO users (username, password, role) VALUES
('john_doe', 'john123', 'sales_person'),
('jane_smith', 'jane123', 'inventory_officer'),
('alice_wong', 'alice123', 'is_manager');

-- Insert Sample Data into customers
INSERT INTO customers (name, address, contact_details) VALUES
('Textile World Inc.', '123 Fabric St, New York', 'contact@textileworld.com'),
('Fashion Trends Ltd.', '456 Style Ave, Los Angeles', 'info@fashiontrends.com'),
('Elegant Apparel Co.', '789 Design Blvd, Chicago', 'support@elegantapparel.com');

-- Insert Sample Data into items
INSERT INTO items (item_number, category, size, cost_price, stock_level, out_of_stock_threshold) VALUES
('FAB001', 'Fabric', 'Large', 25.00, 1000, 100),
('THR002', 'Thread', 'Small', 5.00, 5000, 500),
('BTN003', 'Button', 'Medium', 0.50, 10000, 1000);

-- Insert Sample Data into orders
INSERT INTO orders (customer_id, order_date, transport_charges, total_amount, status) VALUES
(1, '2023-10-01', 15.00, 500.00, 'shipped'),
(2, '2023-10-02', 20.00, 750.00, 'pending'),
(3, '2023-10-03', 10.00, 300.00, 'backordered');

-- Insert Sample Data into order_items
INSERT INTO order_items (order_id, item_id, quantity) VALUES
(1, 1, 20),  -- Order 1: 20 units of Fabric
(1, 2, 100), -- Order 1: 100 units of Thread
(2, 3, 500); -- Order 2: 500 units of Button

-- Insert Sample Data into payments
INSERT INTO payments (order_id, payment_method, amount, payment_date, credit_card_number, expiration_date, bank_number, card_holder_name) VALUES
(1, 'credit_card', 500.00, '2023-10-01', '1234-5678-9012-3456', '12/25', 'BANK001', 'John Doe'),
(2, 'cheque', 750.00, '2023-10-02', NULL, NULL, NULL, NULL),
(3, 'cash', 300.00, '2023-10-03', NULL, NULL, NULL, NULL);

-- Insert Sample Data into delivery_transport
INSERT INTO delivery_transport (order_id, transport_method, delivery_address, estimated_cost, delivery_status) VALUES
(1, 'truck', '123 Fabric St, New York', 15.00, 'delivered'),
(2, 'ship', '456 Style Ave, Los Angeles', 20.00, 'pending'),
(3, 'air', '789 Design Blvd, Chicago', 10.00, 'in_transit');