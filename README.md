# CDC with debezium

This project attempts to build a small notification service which pushes DB changes to kafka (Debezium) and display the updates all the way to your browser. 

**Getting started **

Pre requisite 

Install Docker in your local system. 
A suitable JAVA IDE and JDK installation is required if you want to run your client application 

Steps

 1. In your workspace  clone this repository git clone https://github.com/akrishnamoorthy/debezium.git
 2. Go to debezium and type docker-compose up -d. 
 3. Once the images are downloaded , verify by running docker ps and you should see following services up 
 4. 
    **docker ps --format "table {{.ID}}\t{{.Names}}"**
    
    <img width="563" alt="image" src="https://user-images.githubusercontent.com/1224501/123890667-3b0c6080-d975-11eb-9f29-91d153bf8161.png">
    
 4. Understanding the data model : Go to a terminal and type 
    **docker exec -it dz_myql mysql -udebeziumuser -pdebeziumpw **
    mysql > use customer;
    mysql > select * from customer; 
    mysql > select * from customer_orders;
    
    <img width="556" alt="image" src="https://user-images.githubusercontent.com/1224501/124215998-d1c25400-db12-11eb-9456-8f5ee32cfb05.png">
  
 6. Seeing kafka topics : Go to a new terminal and run 
    **docker-compose exec ksqldb-cli ksql http://dz_ksql_server:8088 **
    ksql > show topics;
    
7. Keep the above terminal open and run the below payload 
   URL : http://localhost:8083/connectors
   Http method : POST 
   Authorization: None
   Body :
   
   <img width="580" alt="image" src="https://user-images.githubusercontent.com/1224501/124219993-be1aeb80-db1a-11eb-94bd-73af531f4f56.png">

  
8. Go to your ksql command run show topics again

   <img width="491" alt="image" src="https://user-images.githubusercontent.com/1224501/124216064-f9192100-db12-11eb-8cf5-61de70bd65e4.png">
   
9 . Import the mvn project (Detailed instructions , please google!) and bring up your browser at http://localhost:8080
    You should see the existing data from your DB displayed in the page
    ![Uploading image.png…]()


10. Go to your mysql and insert a new data

    mysql > insert into customer_orders(customer_id, dealer_details, order_city, invoice_no)  values(1,'New dealer information-pakistan', 'Lahore','343XS');
    
    mysql > update customer_orders set dealer_details = 'Updated dealer information - Pakistan' where dealer_details = 'New dealer information-pakistan';
    
    <img width="1027" alt="image" src="https://user-images.githubusercontent.com/1224501/124217585-37fca600-db16-11eb-9889-1d773e714cd4.png">
    
11. Dealing with deletes :
    Go to mysql> delete from customer_orders where id = 5;
    
    <img width="1142" alt="image" src="https://user-images.githubusercontent.com/1224501/124218110-32ec2680-db17-11eb-8955-1c6d135f3466.png">
    
12. Aggregating data over multiple streams with ksql
    Run below ksql streams joins and modify the topic to listen to enriched customer order information 
    docker-compose exec ksqldb-cli ksql http://dz_ksql_server:8088
     ksql> SET 'auto.offset.reset' = 'earliest';
     ksql> CREATE TABLE customer WITH (KAFKA_TOPIC='asgard.customer.customer',VALUE_FORMAT='AVRO', KEY='id');
     ksql> CREATE STREAM customer_orders WITH (KAFKA_TOPIC='asgard.customer.customer_orders',VALUE_FORMAT='AVRO');
     ksql> CREATE STREAM customer_order_details_all  AS select CO.*, C.*  from customer_orders CO LEFT JOIN customer C on CO.customer_id = C.id;
     
 13. Changes in the db schema is managed automatically by AVRO as long as the changes are backward compatible. 
     Go to http://localhost:8881/subjects/asgard.customer.customer_orders-value/versions/latest . You will find that AVRO Schema is as below. 
     
     <img width="706" alt="image" src="https://user-images.githubusercontent.com/1224501/124220503-8a8c9100-db1b-11eb-8862-48f97e81415f.png">

  
     mysql>alter table customer_orders add order_country VARCHAR(40);
     mysql> update customer_orders set order_country = 'USA'; 
     You can see that the version of schema is automatically updated and the new column added.
     
     <img width="810" alt="image" src="https://user-images.githubusercontent.com/1224501/124220324-52854e00-db1b-11eb-830a-1a8fbbb52c22.png">
