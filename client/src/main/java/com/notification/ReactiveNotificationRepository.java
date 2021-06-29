package com.notification;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class ReactiveNotificationRepository implements NotificationRepository{

    private ReceiverOptions<GenericData.Record, GenericData.Record> receiverOptions = null;
    private static final String BOOTSTRAP_SERVERS = "localhost:19092";
    private static final String TOPIC = "asgard.customer.customer";
    private SimpleDateFormat dateFormat =null;
    private static final Logger log = LoggerFactory.getLogger(ReactiveNotificationRepository.class.getName());
    @Override
    public Flux<Notification> findAll() {
        //Flux<ReceiverRecord<String,String>> kafkaFlux = kafkaReceiver.receive();

        //simulate data streaming every 1 second.
        Map<String, Object> props = new HashMap<String,Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "sample-consumer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put("schema.registry.url", "http://localhost:8881");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        receiverOptions = ReceiverOptions.create(props);
        dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");
        ReceiverOptions<GenericData.Record, GenericData.Record> options =
                receiverOptions.subscription(Collections.singleton(TOPIC));

        /**  Flux<Comment> kafkaFlux = KafkaReceiver.create(options).receive().map(record->
         new Comment(record.key(),record.value(),dateFormat.format(new Date())));
         **/
        Flux<Notification> kafkaFlux = KafkaReceiver.create(options).receive().map(this::apply);
        return kafkaFlux;
    }

    private CustomerDetails getCustomerDetails(Integer id, GenericData.Record record){
        if(record == null){
            CustomerDetails details  = new CustomerDetails();
            details.setFirstname("");
            details.setLastname("");
            return details;
        }
        CustomerDetails details  = new CustomerDetails();
        details.setId(id);
        details.setFirstname(record.get("firstname").toString());
        details.setLastname(record.get("lastname").toString());
        return details;
    }
    private Notification apply(ReceiverRecord<GenericData.Record, GenericData.Record> record) {
        System.out.println("heer");
        System.out.println((Integer)record.key().get("id"));
        System.out.println(record.value().get("before"));
        System.out.println(record.value().get("after"));
        return new Notification((Integer)record.key().get("id"),
                getCustomerDetails((Integer)record.key().get("id"),(GenericData.Record)record.value().get("before")),
                getCustomerDetails((Integer)record.key().get("id"), (GenericData.Record)record.value().get("after")));

    }
}
