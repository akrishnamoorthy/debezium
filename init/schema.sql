CREATE DATABASE ustream;
GRANT ALL PRIVILEGES ON ustream.* TO `ustream`@`%`;
USE ustream;
CREATE TABLE `address` (
         `id` int(11) NOT NULL AUTO_INCREMENT,
         `address_line` varchar(255),
         `city` varchar(7),
         `state` varchar(7),
         `country` varchar(7),
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



