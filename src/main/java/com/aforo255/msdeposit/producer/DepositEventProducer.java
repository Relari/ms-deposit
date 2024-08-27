package com.aforo255.msdeposit.producer;

import com.aforo255.msdeposit.model.domain.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


@Slf4j
@Component
public class DepositEventProducer {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "transaction-events";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final KafkaSender<Integer, String> sender;
    private final SimpleDateFormat dateFormat;

    public DepositEventProducer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "sample-producer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        SenderOptions<Integer, String> senderOptions = SenderOptions.create(props);

        sender = KafkaSender.create(senderOptions);
        dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");
    }

    public Single<Transaction> sendDepositEvent(Transaction depositEvent) {
        return sender.send(
                Mono.fromCallable(() -> SenderRecord.create(
                        new ProducerRecord<>(
                                TOPIC, depositEvent.getId(), MAPPER.writeValueAsString(depositEvent)), depositEvent.getId()
                        )
                ))
                .doOnNext(r -> {
                    var metadata = r.recordMetadata();
                    log.debug("Message {} sent successfully, topic={} partition={} offset={} timestamp={}",
                            r.correlationMetadata(),
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset(),
                            dateFormat.format(new Date(metadata.timestamp())));
                    new CountDownLatch(depositEvent.getId()).countDown();
                })
                .doOnError(throwable -> log.error("Error enviando el mensage", throwable))
                .as(RxJava2Adapter::fluxToFlowable)
                .ignoreElements()
                .andThen(Single.fromCallable(() -> depositEvent));
    }

}
