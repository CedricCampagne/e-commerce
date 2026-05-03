-- ===========================
-- USERS
-- ===========================
INSERT INTO users (id, name, email, password) VALUES
(1, 'Alice Martin', 'alice@example.com', 'Azerty1234');


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
INSERT INTO products (id, name, description, price, category_id) VALUES
(1, 'Clavier mécanique RGB', 'Clavier gaming switch rouge', 89.99, 1),
(2, 'Souris sans fil Logitech', 'Souris ergonomique 16000 DPI', 49.99, 1),
(3, 'Aspirateur robot Xiaomi', 'Aspirateur connecté avec cartographie', 249.99, 2),
(4, 'Machine à café Philips', 'Machine expresso automatique', 399.99, 2),
(5, 'Chaussures running Nike', 'Chaussures légères pour course à pied', 119.99, 3),
(6, 'Tapis de yoga', 'Tapis antidérapant 6mm', 24.99, 3);


-- ===========================
-- CART ITEMS (panier)
-- ===========================
INSERT INTO cart_items (id, user_id, product_id, quantity) VALUES
(1, 1, 1, 1),  -- Alice a 1 clavier mécanique
(2, 1, 5, 2);  -- Alice a 2 paires de chaussures running


-- ===========================
-- ORDERS
-- ===========================
INSERT INTO orders (id, user_id, total_amount, status, created_at) VALUES
(1, 1, 329.97, 'PAID', NOW());


-- ===========================
-- ORDER ITEMS
-- ===========================
INSERT INTO order_items (id, order_id, product_id, quantity, price) VALUES
(1, 1, 1, 1, 89.99),  -- 1 clavier
(2, 1, 5, 2, 119.99); -- 2 chaussures running
