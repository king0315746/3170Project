SET FOREIGN_KEY_CHECKS = 0;
  LOAD DATA INFILE '$PATH/book.txt' INTO TABLE book
  FIELDS TERMINATED BY '|' 
  LINES TERMINATED BY '\n'
  (ISBN, title, unit_price, no_of_copies);

  LOAD DATA INFILE '$PATH/orders.txt' INTO TABLE Orders
  FIELDS TERMINATED BY '|' 
  LINES TERMINATED BY '\n'
  (order_id, order_date, shipping_status, charge,customer_id);

  LOAD DATA INFILE '$PATH/book_author.txt' INTO TABLE book_author
  FIELDS TERMINATED BY '|' 
  LINES TERMINATED BY '\n'
  (ISBN, author_name);

  LOAD DATA INFILE '$PATH/customer.txt' INTO TABLE customer
  FIELDS TERMINATED BY '|' 
  LINES TERMINATED BY '\n'
  (customer_id, name, shipping_address, credit_card_no);

  LOAD DATA INFILE '$PATH/ordering.txt' INTO TABLE Ordering
  FIELDS TERMINATED BY '|' 
  LINES TERMINATED BY '\n'
  (order_id, ISBN,quantity);
SET FOREIGN_KEY_CHECKS = 1;