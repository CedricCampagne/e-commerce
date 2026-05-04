-- ===========================
-- USERS
-- ===========================
INSERT INTO users (id, username, email, password)
VALUES (1, 'Alice Martin', 'alice@example.com', 'Azerty1234');


-- ===========================
-- CATEGORIES
-- ===========================
INSERT INTO categories (id, name) VALUES
(1, 'Informatique'),
(2, 'Maison & Cuisine'),
(3, 'Sport & Loisirs');


-- ===========================
-- PRODUCTS
-- ===========================
INSERT INTO products (id, name, description, price, stock, image_url, category_id)
VALUES
(1, 'Clavier mécanique RGB', 'Clavier gaming switch rouge', 89.99, 10, NULL, 1),
(2, 'Souris sans fil Logitech', 'Souris ergonomique 16000 DPI', 49.99, 15, NULL, 1),
(3, 'Aspirateur robot Xiaomi', 'Aspirateur connecté avec cartographie', 249.99, 5, NULL, 2),
(4, 'Machine à café Philips', 'Machine expresso automatique', 399.99, 3, NULL, 2),
(5, 'Chaussures running Nike', 'Chaussures légères pour course à pied', 119.99, 20, NULL, 3),
(6, 'Tapis de yoga', 'Tapis antidérapant 6mm', 24.99, 30, NULL, 3);


-- ===========================
-- CART ITEMS
-- ===========================
INSERT INTO cart_items (id, user_id, product_id, quantity)
VALUES
(1, 1, 1, 1),
(2, 1, 5, 2);


-- ===========================
-- ORDERS
-- ===========================
INSERT INTO orders (id, user_id, total_price, created_at)
VALUES
(1, 1, 329.97, NOW());


-- ===========================
-- ORDER ITEMS
-- ===========================
INSERT INTO order_items (id, order_id, product_id, quantity, price_at_purchase)
VALUES
(1, 1, 1, 1, 89.99),
(2, 1, 5, 2, 119.99);
