package com.example.rstream;

import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverRecord;

public interface CommentRepository {

    Flux<Comment> findAll();

}
