GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO `debeziumuser`@`%`;
FLUSH PRIVILEGES;
ALTER USER debeziumuser IDENTIFIED WITH mysql_native_password BY 'debeziumpw';
USE customer;
CREATE TABLE `customer` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `customer_type` int(11) DEFAULT NULL,
        `firstname` varchar(255) NOT NULL,
        `lastname` varchar(255) NOT NULL,
        `address` varchar(255) NOT NULL,
        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1  DEFAULT CHARSET=utf8;
CREATE TABLE `customer_orders` (
         `id` int(11) NOT NULL AUTO_INCREMENT,
         `customer_id` int(11) NOT NULL,
         `dealer_details` varchar(255),
         `order_city` varchar(100),
         `invoice_no` varchar(100),
        PRIMARY KEY (`id`),
        CONSTRAINT `customer_orders_FK_1` FOREIGN KEY(`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
INSERT INTO CUSTOMER(customer_type, firstname, lastname, address) VALUES(1, 'Soma', 'Prasad', 'No 23 ,Street 1 ,Calcutta WB');
INSERT INTO CUSTOMER_ORDERS(customer_id, dealer_details, order_city, invoice_no) VALUES (1, 'John and Abraham Pvl Ltd', 'Calcutta', '12ADSS1232');
INSERT INTO CUSTOMER_ORDERS(customer_id, dealer_details, order_city, invoice_no) VALUES (1, 'Colgatos Ltd', 'Calcutta', 'FSER2ADSS1232');
INSERT INTO CUSTOMER(customer_type, firstname, lastname, address) VALUES(1, 'Abhishek', 'Mishra', '253 Marinvilla, 5th Cross');
INSERT INTO CUSTOMER(customer_type, firstname, lastname, address) VALUES(1, 'Mona', 'Prasad', 'Cochin, 353 Nagara indira');
INSERT INTO CUSTOMER_ORDERS(customer_id, dealer_details, order_city, invoice_no) VALUES (2, 'John and Abraham Pvl Ltd', 'Calcutta', '12ADSS1232');
INSERT INTO CUSTOMER_ORDERS(customer_id, dealer_details, order_city, invoice_no) VALUES (3, 'Colgatos Ltd', 'Calcutta', 'FSER2ADSS1232');
INSERT INTO CUSTOMER(customer_type, firstname, lastname, address) VALUES(2, 'Akshita', 'Reube', '1 No-345 , ABA Layout');
INSERT INTO CUSTOMER_ORDERS(customer_id, dealer_details, order_city, invoice_no) VALUES (4, 'Colgatos Ltd', 'Calcutta', 'FSER2ADSS1232');
INSERT INTO CUSTOMER(customer_type, firstname, lastname, address) VALUES(2, 'William', 'Prasanna', '45 3 Rd , Water tank');
commit;
