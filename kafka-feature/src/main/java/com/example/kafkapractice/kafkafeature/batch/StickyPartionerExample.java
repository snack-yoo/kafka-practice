package com.example.kafkapractice.kafkafeature.batch;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class StickyPartionerExample {

    static void batched(Properties props) {
        KafkaProducer producer = new KafkaProducer(props);

        List<Future> futures = IntStream.range(0, 10).boxed().map(idx -> {
            String message = "Batched Message of " + idx;
            ProducerRecord<String, String> record = new ProducerRecord("kafka-feature", message);
            return producer.send(record, (metadata, exception) -> {
                Optional.ofNullable(exception)
                        .ifPresentOrElse(
                                e -> {
                                    e.printStackTrace();
                                },
                                () -> {
                                    log.info("[BATCHED] SEND SUCCESS... \n message: {}\n partition: {}\n offset: {}", message, metadata.partition(), metadata.offset());
                                }
                        );
            });
        })
                        .collect(Collectors.toList());

        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        producer.flush();
        producer.close();
    }

    static void nonBatched(Properties props) {
        KafkaProducer producer = new KafkaProducer(props);

        IntStream.range(0, 10).forEach(idx -> {
            String message = "NON-Batched Message of " + idx;
            ProducerRecord<String, String> record = new ProducerRecord("kafka-feature", message);
            producer.send(record, (metadata, exception) -> {
                Optional.ofNullable(exception)
                        .ifPresentOrElse(
                                e -> {
                                    e.printStackTrace();
                                },
                                () -> {
                                    log.info("[NON-Batched] SEND SUCCESS... \n message: {}\n partition: {}\n offset: {}", message, metadata.partition(), metadata.offset());
                                }
                        );
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer.flush();
        producer.close();
    }

    public static void main(String[] args) {
        // props
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // topic
        /*
        Topic: kafka-feature	TopicId: 7v8I9nxDTFCLUqApE3o1iA	PartitionCount: 3	ReplicationFactor: 1	Configs: segment.bytes=1073741824
        Topic: kafka-feature	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
        Topic: kafka-feature	Partition: 1	Leader: 0	Replicas: 0	Isr: 0
        Topic: kafka-feature	Partition: 2	Leader: 0	Replicas: 0	Isr: 0
         */

        Thread batchedThread = new Thread(() -> batched(props));
        Thread nonBatchedThread = new Thread(() -> nonBatched(props));

        batchedThread.run();
        try {
            log.info("batchedThread test waiting....");
            batchedThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.info("batchedThread test done....");
        }
        nonBatchedThread.run();


        try {
            log.info("nonBatchedThread test waiting....");
            nonBatchedThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.info("nonBatchedThread test done....");
        }
    }
}
