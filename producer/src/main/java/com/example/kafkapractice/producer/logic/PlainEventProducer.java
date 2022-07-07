package com.example.kafkapractice.producer.logic;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@RequiredArgsConstructor
@Profile("plain-event")
@Component
public class PlainEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ListenableFuture<SendResult<String, String>> publish(String topic, String plainEvent) {
        log.debug("publish plain event : {}", plainEvent);
        return kafkaTemplate.send(topic, plainEvent);
    }
}
