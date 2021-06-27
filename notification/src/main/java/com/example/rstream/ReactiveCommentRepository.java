package com.example.rstream;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Repository
public class ReactiveCommentRepository implements CommentRepository {
	private ReceiverOptions<String, String> receiverOptions = null;
	  private static final String BOOTSTRAP_SERVERS = "localhost:19092";
	    private static final String TOPIC = "asgard.customer.customer";
	    private  SimpleDateFormat dateFormat =null;
	    private static final Logger log = LoggerFactory.getLogger(ReactiveCommentRepository.class.getName());
    @Override
    public Flux<Comment> findAll() {
    	//Flux<ReceiverRecord<String,String>> kafkaFlux = kafkaReceiver.receive();
	
        //simulate data streaming every 1 second.
    	 Map<String, Object> props = new HashMap<String,Object>();
         props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
         props.put(ConsumerConfig.CLIENT_ID_CONFIG, "sample-consumer");
         props.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group");
         props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
         props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
         props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
         receiverOptions = ReceiverOptions.create(props);
         dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");
    	 ReceiverOptions<String, String> options = receiverOptions.subscription(Collections.singleton(TOPIC));

       /**  Flux<Comment> kafkaFlux = KafkaReceiver.create(options).receive().map(record->
                 new Comment(record.key(),record.value(),dateFormat.format(new Date())));
        **/
       Flux<Comment> kafkaFlux = KafkaReceiver.create(options).receive().map(record-> {
           System.out.println("heer");
           System.out.println(record);
           return new Comment("c","c",dateFormat.format(new Date()));
       });


        return kafkaFlux;
    }

    private List<Comment> generateComment(long interval) {
        Comment obj = new Comment(
			"some", 
			"some", 
			null);
        return Arrays.asList(obj);

    }

}

