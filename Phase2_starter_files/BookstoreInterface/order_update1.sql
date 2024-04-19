SET @v_order_id = '$ORDER_ID';
SELECT o.shipping_status,od.quantity
From orders o join ordering od on o.order_id = od.order_id
Where o.order_id = @v_order_id;

