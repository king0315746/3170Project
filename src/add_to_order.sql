-- 设置输入的 ISBN 和副本数量的用户变量
/*SET @v_order_id   = '00000002';
SET @input_ISBN = '1-1234-1234-1';
SET @input_copies = 1;java input here*/

UPDATE orders o
INNER JOIN ordering od ON o.order_id = od.order_id
INNER JOIN book b ON od.ISBN = b.ISBN
SET od.Quantity = od.Quantity + @input_copies,
    b.no_of_copies = b.no_of_copies - @input_copies,
    o.order_date = CURDATE() 
WHERE od.ISBN = @input_ISBN AND o.shipping_status = 'N' AND @input_copies <= b.no_of_copies;

/*SELECT * FROM ordering od
WHERE od.order_id = @v_order_id;*/