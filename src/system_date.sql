SELECT 
    (SELECT MAX(order_date) FROM orders) AS max_order_date,
    s_date 
FROM 
    s_date;