SELECT order_id AS "Order ID", order_date AS "Order Date", shipping_status AS "Shipping Status", charge AS "Charge"
FROM orders
WHERE customer_id = 'cwwong' AND YEAR(order_date) = CAST('2005' AS UNSIGNED)
ORDER BY order_id ASC;