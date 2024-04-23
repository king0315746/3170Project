DROP PROCEDURE IF EXISTS set_systemDate;

-- 创建存储过程
DELIMITER //

CREATE PROCEDURE set_systemDate(IN date_param DATE)
BEGIN
    -- 创建日期变量并使用传入的参数值
    SET @systemday := date_param;

    -- 获取orders表中的最大order_date
    SELECT MAX(order_date) INTO @max_order_date FROM orders;

    -- 检查日期变量是否比最大的order_date还要大
    IF @systemday  > @max_order_date THEN
        -- 如果是，返回系统日期
        SELECT @systemday AS system_date;
    ELSE
        -- 如果不是，返回空值
        SELECT NULL AS system_date;
    END IF;
END //

DELIMITER ;