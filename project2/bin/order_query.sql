SET @customer_id = 'adafu';
SET @year = 2024;

SELECT order_id AS "Order ID", order_date AS "Order Date", shipping_status AS "Shipping Status", charge AS "Charge"
FROM orders
WHERE customer_id = @customer_id AND YEAR(order_date) = CAST(@year AS UNSIGNED)
ORDER BY order_id ASC;