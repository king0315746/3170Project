-- 设置输入的 ISBN 和副本数量的用户变量
SET @v_order_id   = '00000002';
SET @input_ISBN = '1-1234-1234-1';
SET @input_copies = 1;/*java input here*/

-- 获取当前订单中指定书籍的信息
SELECT o.order_id, o.shipping_status, b.no_of_copies AS "No of Copies Available"
FROM orders o
INNER JOIN ordering od ON o.order_id = od.order_id
INNER JOIN book b ON od.ISBN = b.ISBN
WHERE od.ISBN = @input_ISBN AND o.shipping_status = 'N'AND o.order_id = @v_order_id;

-- 更新订单中指定书籍的副本数量和库存数量
UPDATE orders o
INNER JOIN ordering od ON o.order_id = od.order_id
INNER JOIN book b ON od.ISBN = b.ISBN
SET od.Quantity = od.Quantity + @input_copies,
    b.no_of_copies = b.no_of_copies - @input_copies
    o.order_date = CURDATE() -- 更新为当前系统日期
WHERE od.ISBN = @input_ISBN AND o.shipping_status = 'N' AND @input_copies <= b.no_of_copies;

SELECT * FROM ordering od
WHERE od.order_id = @v_order_id;