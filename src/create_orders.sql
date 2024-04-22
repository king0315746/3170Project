DROP PROCEDURE IF EXISTS create_order;

DELIMITER //

CREATE PROCEDURE create_order(IN customer_id CHAR(10), IN book_isbn CHAR(13), IN quantity INT)
My_label:BEGIN
    DECLARE v_order_id CHAR(8);
    DECLARE order_date DATE;
    DECLARE total_charge INT;
    DECLARE available_copies INT;

    -- 获取当前最大的Order ID
    SELECT MAX(CAST(order_id AS UNSIGNED)) INTO @max_order_id FROM orders;

    -- 分配新的Order ID
    SET v_order_id = LPAD((@max_order_id+1), 8, '0');
    
    
    -- 分配当前系统日期作为Order Date
    SET order_date = CURDATE();

    -- 检查书的剩余数量是否足够 
    SELECT no_of_copies INTO available_copies
    FROM book
    WHERE ISBN = book_isbn;
    -- 如果书的剩余数量不足，则返回NULL
    IF available_copies < quantity THEN
        SELECT NULL;
        LEAVE My_label ; -- 使用RETURN语句立即返回
    END IF;

    -- 计算该书的总费用
    SET total_charge = (SELECT unit_price * quantity
                        FROM book
                        WHERE ISBN = book_isbn);

    -- 插入订购记录
    INSERT INTO orders (Order_ID, order_date, shipping_status, charge, Customer_ID)
    VALUES (v_order_id, order_date, 'N', total_charge, customer_id);

    INSERT INTO ordering (Order_ID, ISBN, Quantity)
    VALUES (v_order_id, book_isbn, quantity);

    -- 插入订单记录


    -- 返回True表示订单创建成功
    SELECT TRUE;
END 
//

DELIMITER ;
