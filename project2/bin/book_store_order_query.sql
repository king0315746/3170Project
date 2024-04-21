SELECT order_id, customer_id, order_date, charge
FROM orders
WHERE order_date >= DATE_FORMAT('$MONTH-01', '%Y-%m-%d')
    AND order_date < DATE_ADD(DATE_FORMAT('$MONTH-01', '%Y-%m-%d'), INTERVAL 1 MONTH);