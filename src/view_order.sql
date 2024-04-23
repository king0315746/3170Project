SELECT o.order_id AS "order_id", o.shipping_status AS "Shipping Status", b.no_of_copies AS "No of Copies Available"
,b.ISBN AS "ISBN",od.quantity as "quantity",o.charge as "Charge"
FROM orders o
INNER JOIN ordering od ON o.order_id = od.order_id
INNER JOIN book b ON od.ISBN = b.ISBN
WHERE od.order_id like ?;