DROP DATABASE IF EXISTS textile_factory ;
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
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- Table: order_items
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    item_id INT,
    quantity INT,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE
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
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- Table: delivery_transport
CREATE TABLE delivery_transport (
    delivery_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    transport_method ENUM('truck', 'ship', 'air'),
    delivery_address VARCHAR(200),
    estimated_cost DECIMAL(10, 2),
    delivery_status ENUM('pending', 'in_transit', 'delivered'),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- Insert Sample Data into users
INSERT INTO users (username, password, role) VALUES
('sales_person', '1234', 'sales_person'),
('inventory_officer', '1234', 'inventory_officer'),
('is_manager', '1234', 'is_manager');

-- Insert Sample Data into customers
INSERT INTO customers (name, address, contact_details) VALUES
('Tropical Textiles Ltd.', '12 Sir Seewoosagur Ramgoolam St, Port Louis', 'sales@tropicaltextiles.mu'),
('Paradise Garments Co.', '45 Coastal Road, Tamarin', 'info@paradisegarments.mu'),
('Island Stitch Creations', 'Floreal Shopping Plaza, Curepipe', 'orders@islandstitch.mu'),
('Mauritius Fashion Hub', 'Rue du Commerce, Quatre Bornes', 'contact@mauritiusfashion.mu'),
('Dodo Clothing Manufacturers', 'La Croisette Mall, Grand Baie', 'support@dodoclothing.mu'),
('Sega Apparel Ltd.', '23 Royal Road, Beau Bassin', 'customercare@segaapparel.mu'),
('Vanilla Textile Solutions', 'Industrial Zone, Phoenix', 'sales@vanillatextile.mu'),
('Lagoon Wear Enterprises', 'Marina Boulevard, Flic en Flac', 'info@lagoonwear.mu'),
('Creole Designs Ltd.', '18 Labourdonnais St, Mapou', 'designs@creole.mu'),
('Sugar Cane Fabrics Co.', 'Rivière du Rempart Industrial Park', 'fabric@sugarcane.mu');

-- Insert Sample Data into items
INSERT INTO items (item_number, category, size, cost_price, stock_level, out_of_stock_threshold) VALUES
('FAB001', 'Cotton Fabric', 'Large', 25.00, 1000, 100),
('FAB002', 'Rayon Fabric', 'Medium', 32.50, 800, 80),
('FAB003', 'Batik Print Fabric', 'Large', 45.00, 600, 60),
('THR001', 'Polyester Thread', 'Small', 5.00, 5000, 500),
('THR002', 'Cotton Thread', 'Medium', 6.50, 4000, 400),
('BTN001', 'Plastic Buttons', 'Medium', 0.50, 10000, 1000),
('BTN002', 'Mother-of-Pearl Buttons', 'Small', 2.50, 3000, 300),
('ZIP001', 'Nylon Zippers', 'Medium', 3.75, 2000, 200),
('ELC001', 'Elastic Bands', 'Large', 1.20, 5000, 500),
('LAC001', 'Shoe Laces', 'Medium', 2.80, 3000, 300),
('EMB001', 'Embroidery Thread', 'Small', 8.00, 1500, 150),
('PAT001', 'Traditional Sega Print', 'Large', 55.00, 400, 40),
('LIN001', 'Linen Fabric', 'Medium', 38.00, 700, 70),
('RIB001', 'Cotton Ribbon', 'Small', 1.50, 6000, 600),
('PAD001', 'Shoulder Pads', 'Medium', 4.25, 2500, 250);

-- Insert Sample Data into orders
INSERT INTO orders (customer_id, order_date, transport_charges, total_amount, status) VALUES
(1, '2025-01-15', 250.00, 12500.00, 'shipped'),
(2, '2025-02-03', 180.00, 8750.00, 'pending'),
(3, '2025-02-18', 300.00, 14200.00, 'shipped'),
(4, '2025-03-05', 150.00, 6800.00, 'pending'),
(5, '2025-03-22', 220.00, 11000.00, 'shipped'),
(6, '2025-04-10', 190.00, 9500.00, 'pending'),
(7, '2025-05-27', 275.00, 13250.00, 'shipped'),
(8, '2025-06-14', 160.00, 7200.00, 'pending'),
(9, '2025-07-08', 230.00, 11800.00, 'shipped'),
(10, '2025-08-30', 200.00, 10500.00, 'pending');

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
-- Insert Sample Data into delivery_transport (corrected)
INSERT INTO delivery_transport (order_id, transport_method, delivery_address, estimated_cost, delivery_status) VALUES
(1, 'truck', '12 Sir Seewoosagur Ramgoolam St, Port Louis', 250.00, 'delivered'),
(2, 'truck', '45 Coastal Road, Tamarin', 180.00, 'pending'),
(3, 'truck', 'Floreal Shopping Plaza, Curepipe', 300.00, 'in_transit'),
(4, 'ship', 'Rue du Commerce, Quatre Bornes', 1200.00, 'pending'),
(5, 'air', 'La Croisette Mall, Grand Baie', 3500.00, 'in_transit'),
(6, 'truck', '23 Royal Road, Beau Bassin', 190.00, 'delivered'),
(7, 'truck', 'Industrial Zone, Phoenix', 275.00, 'pending'),
(8, 'truck', 'Marina Boulevard, Flic en Flac', 160.00, 'in_transit'),
(9, 'ship', '18 Labourdonnais St, Mapou', 950.00, 'delivered'),
(10, 'truck', 'Rivière du Rempart Industrial Park', 200.00, 'pending');