package org.springframework.samples.petclinic.owners.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class OwnerManagement {

    KafkaTemplate<String, Integer> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(OwnerManagement.class);

    public OwnerManagement(KafkaTemplate<String, Integer> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    void sendOwnerDeleted(final Integer ownerId) {
        CompletableFuture<SendResult<String, Integer>> future = kafkaTemplate.send("ownerDeleted",ownerId);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sending message to Kafka Listener: {}", ownerId);
            }
            else {
                log.error("Failed to send message to Kafka Listener: {}", ownerId, ex);
            }
        });
    }

}
