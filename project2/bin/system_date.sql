DROP PROCEDURE IF EXISTS set_systemday;
-- 创建存储过程
DELIMITER //

CREATE PROCEDURE set_systemday()
BEGIN
    -- 创建日期变量并设置默认值为2021年1月1日
    SET @systemday := '$DATE';

    -- 获取orders表中的最大order_date
    SELECT MAX(order_date) COLLATE gbk_chinese_ci INTO @max_order_date FROM orders;

    -- 检查日期变量是否比最大的order_date还要大
    IF @systemday COLLATE gbk_chinese_ci > @max_order_date THEN
        -- 如果是，返回系统日期
        SELECT @systemday AS system_date;
    ELSE
        -- 如果不是，返回空值
        SELECT NULL AS system_date;
    END IF;
END //

DELIMITER ;

CALL set_systemday();