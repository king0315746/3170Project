SET @v_order_id = '00000002';-- java input

UPDATE orders o
SET o.shipping_status = 'Y'
Where o.order_id = @v_order_id;

