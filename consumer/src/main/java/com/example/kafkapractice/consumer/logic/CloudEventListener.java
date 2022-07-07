package com.example.kafkapractice.consumer.logic;

import io.cloudevents.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Profile("cloud-event")
@Component
@KafkaListener(topics = {"foo", "bar"})
public class CloudEventListener {
    @KafkaHandler
    public void onNotNull(@Payload CloudEvent cloudEvent, @Headers Map<String, Object> headers) {
        log.debug("consume cloudevent header : {}", headers);
        log.debug("consume cloudevent payload : {}", cloudEvent);
    }

    @KafkaHandler
    public void onNull(@Payload(required = false) KafkaNull kafkaNull, @Headers Map<String, Object> headers) {
        log.debug("consume null header : {}", headers);
        log.debug("consume null payload : {}", kafkaNull);
    }
}
