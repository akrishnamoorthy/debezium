package com.notification;

import reactor.core.publisher.Flux;

public interface NotificationRepository {

    Flux<Notification> findAll();
}
