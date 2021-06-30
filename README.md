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
 4. Go to a new terminal and run 
    **docker-compose exec ksqldb-cli ksql http://dz_ksql_server:8088 **
    
 


