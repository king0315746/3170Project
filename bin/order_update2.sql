UPDATE orders o
SET o.shipping_status = 'Y'
Where o.order_id = '$ORDER_ID';

