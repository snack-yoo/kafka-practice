package com.example.kafkapractice.producer.logic;


import io.cloudevents.CloudEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@RequiredArgsConstructor
@Profile("cloud-event")
@Component
public class CloudEventProducer {
    private final KafkaTemplate<String, CloudEvent> kafkaTemplate;

    public ListenableFuture<SendResult<String, CloudEvent>> publish(String topic, CloudEvent cloudEvent) {
        log.debug("publish cloud event : {}", cloudEvent);
        return kafkaTemplate.send(topic, cloudEvent);
    }
}
