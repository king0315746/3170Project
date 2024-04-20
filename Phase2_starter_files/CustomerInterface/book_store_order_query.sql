-- 声明变量并设置值为 yyyy-mm
DECLARE @month VARCHAR(7);
SET @month = '2005-09'; /*java input*/

-- 任务 1：返回指定月份内的所有 order_id、customer_id、order_date 和 charge
SELECT order_id, customer_id, order_date, charge
FROM orders
WHERE order_date >= DATE_FORMAT(CONCAT(@month, '-01'), '%Y-%m-%d')
    AND order_date < DATE_ADD(DATE_FORMAT(CONCAT(@month, '-01'), '%Y-%m-%d'), INTERVAL 1 MONTH);

-- 任务 2：返回指定月份内的总 charge
SELECT SUM(charge) AS total_charge
FROM orders
WHERE order_date >= DATE_FORMAT(CONCAT(@month, '-01'), '%Y-%m-%d')
    AND order_date < DATE_ADD(DATE_FORMAT(CONCAT(@month, '-01'), '%Y-%m-%d'), INTERVAL 1 MONTH);