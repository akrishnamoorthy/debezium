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
   
   { "name": "debezium-mysql-connector-1", 
   "config": 
       {
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "tasks.max": "1",
    "database.hostname": "dz_mysql",
    "database.port": "3306",
    "database.user": "debeziumuser",
    "database.password": "debeziumpw",
    "database.server.name": "asgard",
    "database.allowPublicKeyRetrieval": "true",
    "database.server.id": "42",
    "database.whitelist": "customer",
    "database.history.kafka.bootstrap.servers": "kafka:9092",
    "database.history.kafka.topic": "dbhistory.customer",
    "value.converter":"io.confluent.connect.avro.AvroConverter",
    "key.converter":"io.confluent.connect.avro.AvroConverter", 
    "value.converter.schema.registry.url":"http://avro-schema-registry:8881",
    "key.converter.schema.registry.url":"http://avro-schema-registry:8881",
    "transforms":"unwrap",
    "transforms.unwrap.type":"io.debezium.transforms.ExtractNewRecordState",
    "key.converter.schemas.enabled": "false",
    "value.converter.schemas.enabled" : "false",
    "transforms.unwrap.drop.tombstones": "false",
    "transforms.unwrap.delete.handling.mode": "rewrite"
   }
}


9. Go to your ksql command run show topics again
   <img width="491" alt="image" src="https://user-images.githubusercontent.com/1224501/124216064-f9192100-db12-11eb-8cf5-61de70bd65e4.png">
   
10. Import the mvn project (Detailed instructions , please google!) and bring up your browser at http://localhost:8080
    You should see the existing data from your DB displayed in the page
    <img width="955" alt="image" src="https://user-images.githubusercontent.com/1224501/124217337-ccb2d400-db15-11eb-9313-899874532ef5.png">

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
     {"subject":"asgard.customer.customer_orders-value","version":1,"id":6,
     "schema":"
      {
        \"type\":\"record\",\"name\":\"Value\",
               \"namespace\":\"asgard.customer.customer_orders\",
               \"fields\":[
        {\"name\":\"id\",\"type\":\"int\"},
        {\"name\":\"customer_id\",\"type\":\"int\"},
        {\"name\":\"dealer_details\",\"type\":[\"null\",\"string\"],\"default\":null},
        {\"name\":\"order_city\",\"type\":[\"null\",\"string\"],\"default\":null},						
        {\"name\":\"invoice_no\",\"type\":[\"null\",\"string\"],\"default\":null},
        {\"name\":\"__deleted\",\"type\":[\"null\",\"string\"],\"default\":null}],
       \"connect.name\":\"asgard.customer.customer_orders.Value\"}"}

  
     mysql>alter table customer_orders add order_country VARCHAR(40);
     mysql> update customer_orders set order_country = 'USA'; 
     You can see that the version of schema is automatically updated and the new column added.
     
     {"subject":"asgard.customer.customer_orders-value",**"version":2,**"id":11,
     "schema":
      "{\"type\":\"record\",\"name\":\"Value\",
      \"namespace\":\"asgard.customer.customer_orders\",
      \"fields\":[
        {\"name\":\"id\",\"type\":\"int\"},
        {\"name\":\"customer_id\",\"type\":\"int\"},
        {\"name\":\"dealer_details\",\"type\":[\"null\",\"string\"],\"default\":null},
        {\"name\":\"order_city\",\"type\":[\"null\",\"string\"],\"default\":null},
        {\"name\":\"invoice_no\",\"type\":[\"null\",\"string\"],\"default\":null},
       ** {\"name\":\"order_country\",\"type\":[\"null\",\"string\"],\"default\":null},**
        {\"name\":\"__deleted\",\"type\":[\"null\",\"string\"],\"default\":null}],
      \"connect.name\":\"asgard.customer.customer_orders.Value\"}"}
    
