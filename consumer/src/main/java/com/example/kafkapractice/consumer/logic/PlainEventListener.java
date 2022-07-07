package com.example.kafkapractice.consumer.logic;

import com.example.kafkapractice.event.BarPayload;
import com.example.kafkapractice.event.FooPayload;
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
@Profile("plain-event")
@Component
@KafkaListener(topics = {"foo", "bar"})
public class PlainEventListener {
    @KafkaHandler
    public void onFoo(@Payload FooPayload fooPayload, @Headers Map<String, Object> headers) {
        log.debug("consume foo header : {}", headers);
        log.debug("consume foo payload : {}", fooPayload);
    }

    @KafkaHandler
    public void onBar(@Payload BarPayload barPayload, @Headers Map<String, Object> headers) {
        log.debug("consume bar header : {}", headers);
        log.debug("consume bar payload : {}", barPayload);
    }

    @KafkaHandler
    public void onNull(@Payload(required = false) KafkaNull kafkaNull, @Headers Map<String, Object> headers) {
        log.debug("consume null header : {}", headers);
        log.debug("consume null payload : {}", kafkaNull);
    }
}
