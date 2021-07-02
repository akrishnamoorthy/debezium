package com.notification;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
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
    private static final String TOPIC = "asgard.customer.customer_orders";
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
        Flux<Notification> kafkaFlux = KafkaReceiver.create(options).receive().filter(this::filter).map(this::apply);
        return kafkaFlux;
    }

    private CustomerOrder getCustomerOrder(GenericData.Record record){
        CustomerOrder details  = new CustomerOrder();
        details.setId((Integer)record.get("id"));
        details.setCustomerId(record.get("customer_id").toString());
        details.setOrderCity(record.get("order_city").toString());
        details.setInvoiceNo(record.get("invoice_no").toString());
        details.setDealerDetails(record.get("dealer_details").toString());
        return details;
    }
    private boolean filter(ReceiverRecord<GenericData.Record, GenericData.Record> record) {
       return record.value() !=null;
    }
    private Notification apply(ReceiverRecord<GenericData.Record, GenericData.Record> record) {
        System.out.println("Received message");
        if(record.value() != null){
           if(record.value().get("__deleted").toString().equals("true")){
            System.out.println("deleted");
            return new  Notification((Integer)record.key().get("id"),
                    new CustomerOrder((Integer)record.key().get("id"),
                            "DELETED"));
        }
        System.out.println("ID:" + (Integer)record.value().get("id"));
        System.out.println("AFTER:" + record.value());
        return new Notification((Integer)record.value().get("id"),
                getCustomerOrder((GenericData.Record)record.value()));
    }
        return null;
    }
}
