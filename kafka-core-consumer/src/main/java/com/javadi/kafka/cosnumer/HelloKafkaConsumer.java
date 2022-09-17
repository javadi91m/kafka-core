package com.javadi.kafka.cosnumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class HelloKafkaConsumer {

    @KafkaListener(topics = "t-hello", groupId = "specific-spring-consumer")
    public void consume(String message) {
        System.out.println("message received:> " + message);
    }

}
