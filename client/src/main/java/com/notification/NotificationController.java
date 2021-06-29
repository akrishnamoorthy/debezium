package com.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class NotificationController {

        @Autowired
        private NotificationRepository notificationRepository;

        @GetMapping(path = "/notifications/stream",
                produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public Flux<Notification> feed() {
            return this.notificationRepository.findAll();
        }

}

