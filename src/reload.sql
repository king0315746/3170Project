Use  My_database; -- local database,need to change to host database when implement

DROP TABLE IF EXISTS ordering;
DROP TABLE IF EXISTS book_author;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS customer;

CREATE TABLE book (
    ISBN CHAR(13) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    unit_price INT NOT NULL,
    no_of_copies INT NOT NULL
);

-- Customer table
CREATE TABLE customer (
    customer_id CHAR(10) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    shipping_address VARCHAR(200) NOT NULL,
    credit_card_no CHAR(19) NOT NULL
);

-- Order table
CREATE TABLE orders (
    order_id CHAR(8) PRIMARY KEY,
    order_date DATE,
    shipping_status CHAR(1) NOT NULL,
    charge INT NOT NULL,
    Customer_ID CHAR(10),
    FOREIGN KEY (Customer_ID) REFERENCES Customer(Customer_ID)
);

-- Ordering table (to handle the many-to-many relationship between Book and Order)
CREATE TABLE ordering (
    Order_ID CHAR(8),
    ISBN CHAR(13),
    Quantity INT NOT NULL,
    PRIMARY KEY (Order_ID, ISBN),
    FOREIGN KEY (Order_ID) REFERENCES Orders(Order_ID),
    FOREIGN KEY (ISBN) REFERENCES Book(ISBN)
);


/*BookAuthor table (to handle the many-to-many relationship between Book and Author)*/
CREATE TABLE book_author (
    ISBN CHAR(13),
    author_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (ISBN, Author_name),
    FOREIGN KEY (ISBN) REFERENCES book(ISBN)
    
);

  LOAD DATA INFILE 'book.txt' INTO TABLE book
  FIELDS TERMINATED BY '|' 
  LINES TERMINATED BY '\n'
  (ISBN, title, unit_price, no_of_copies);

  LOAD DATA INFILE 'customer.txt' INTO TABLE customer
  FIELDS TERMINATED BY '|' 
  LINES TERMINATED BY '\n'
  (customer_id,name,shipping_address,credit_card_no);

  LOAD DATA INFILE 'orders.txt' INTO TABLE Orders
  FIELDS TERMINATED BY '|' 
  LINES TERMINATED BY '\r\n'
  (order_id,order_date,shipping_status,charge,customer_id);

  LOAD DATA INFILE 'book_author.txt' INTO TABLE book_author
  FIELDS TERMINATED BY '|' 
  LINES TERMINATED BY '\r\n'
  (ISBN, author_name);



  LOAD DATA INFILE 'ordering.txt' INTO TABLE Ordering
  FIELDS TERMINATED BY '|' 
  LINES TERMINATED BY '\r\n'
  (order_id,ISBN,quantity);