GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO `debeziumuser`@`%`;
FLUSH PRIVILEGES;
ALTER USER debeziumuser IDENTIFIED WITH mysql_native_password BY 'debeziumpw';
USE customer;
CREATE TABLE `address` (
         `id` int(11) NOT NULL AUTO_INCREMENT,
         `address_line` varchar(255),
         `city` varchar(100),
         `state` varchar(100),
         `country` varchar(2),
        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `customer` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `customer_type` int(11) DEFAULT NULL,
        `firstname` varchar(255) NOT NULL,
        `lastname` varchar(255) NOT NULL,
        `address` int(11) NOT NULL,
        PRIMARY KEY (`id`),
        CONSTRAINT `customer_address_FK_1` FOREIGN KEY(`address`) REFERENCES `address` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1  DEFAULT CHARSET=utf8;

INSERT INTO ADDRESS(address_line, city, state, country) VALUES ('Address line 1', 'Calcutta', 'WestBengal', 'IN');
INSERT INTO CUSTOMER(customer_type, firstname, lastname, address) VALUES(1, 'Soma', 'Prasad', 1);
INSERT INTO ADDRESS(address_line, city, state, country) VALUES ('Address line 2', 'Calcutta', 'WestBengal', 'IN');
INSERT INTO CUSTOMER(customer_type, firstname, lastname, address) VALUES(1, '', 'Prasad', 2);
INSERT INTO ADDRESS(address_line, city, state, country) VALUES ('Address line 3', 'Calcutta', 'WestBengal', 'IN');
INSERT INTO CUSTOMER(customer_type, firstname, lastname, address) VALUES(1, 'Soma', 'Prasad', 3);
INSERT INTO ADDRESS(address_line, city, state, country) VALUES ('Address line 4', 'Calcutta', 'WestBengal', 'IN');
INSERT INTO CUSTOMER(customer_type, firstname, lastname, address) VALUES(2, 'Soma', 'Prasad', 4);
INSERT INTO ADDRESS(address_line, city, state, country) VALUES ('Address line 5', 'Calcutta', 'WestBengal', 'IN');
INSERT INTO CUSTOMER(customer_type, firstname, lastname, address) VALUES(2, 'Soma', 'Prasad', 5);
commit;
