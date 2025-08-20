-- migrate:up
-- Seed products
INSERT INTO products (product_id, name, quantity, price)
VALUES
    ('prod1', 'Laptop', 10, 999.99),
    ('prod2', 'Mouse', 50, 29.99);

-- Seed orders
INSERT INTO orders (order_id, customer_id, total_amount, order_date, status)
VALUES
    ('order1', 'cust1', 1029.98, '2025-08-19 10:00:00', 'PENDING'),
    ('order2', 'cust2', 29.99, '2025-08-19 11:00:00', 'PENDING');

-- Seed order_items
INSERT INTO order_items (order_id, product_id, quantity, price)
VALUES
    ('order1', 'prod1', 1, 999.99),
    ('order1', 'prod2', 1, 29.99),
    ('order2', 'prod2', 1, 29.99);

-- Seed payments
INSERT INTO payments (payment_id, order_id, amount, status, payment_date)
VALUES
    ('pay1', 'order1', 1029.98, 'COMPLETED', '2025-08-19 10:05:00'),
    ('pay2', 'order2', 29.99, 'COMPLETED', '2025-08-19 11:05:00');

-- migrate:down
DELETE FROM payments;
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM products;