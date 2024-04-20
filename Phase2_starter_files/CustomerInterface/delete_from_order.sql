DROP PROCEDURE IF EXISTS remove_book;

DELIMITER //

CREATE PROCEDURE remove_book(IN delete_quantity INT, IN v_order_id VARCHAR(8), IN v_ISBN VARCHAR(13))
BEGIN
    -- 声明变量
    DECLARE v_shipping_status CHAR(1);
    DECLARE v_quantity INT;
    DECLARE v_order_date DATE;

    -- 获取当前订单的Shipping Status和Quantity
    SELECT orders.shipping_status, ordering.quantity, orders.order_date
    INTO v_shipping_status, v_quantity, v_order_date
    FROM orders
    Join ordering on orders.order_id = ordering.order_id
    WHERE orders.order_id = v_order_id;

    -- 检查Shipping Status是否为"N"，如果不是，则返回错误信息
    IF (v_shipping_status != 'N' OR v_quantity <= 0) THEN
        SELECT 'The books in the order are shipped' AS error_message;
        -- 返回错误消息
    ELSE
        -- 更新订单中书的Quantity和书的剩余数量
        UPDATE ordering od
        Join orders o on o.order_id = o.order_id
        SET od.quantity = od.quantity - delete_quantity, -- 替换为要删除的副本数量
            o.order_date = CURDATE() -- 更新为当前系统日期
        WHERE o.order_id = v_order_id; -- 替换为实际的订单 ID

        UPDATE book
        SET no_of_copies = no_of_copies + delete_quantity -- 替换为要删除的副本数量
        WHERE ISBN = v_ISBN; -- 替换为实际的书籍 ISBN

        -- 返回成功消息
        SELECT 'Copies removed successfully' AS success_message;
    END IF;

    SELECT od.order_id,od.quantity as 'quantity left', o.order_date, o.shipping_status 
    FROM ordering od
    JOIN orders o on od.order_id = o.order_id
    WHERE od.order_id = v_order_id;
END //

DELIMITER ;
call remove_book(1,'00000002','1-1234-1234-1')
