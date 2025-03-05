package org.springframework.samples.petclinic.pets.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.samples.petclinic.pets.domain.Pet;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class PetManagement {


    private final PetRepository petRepository;
    private final KafkaTemplate<String, Integer> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(PetController.class);

    public PetManagement(PetRepository petRepository, KafkaTemplate<String, Integer> kafkaTemplate) {
        this.petRepository = petRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
        topics = "ownerDeleted",
        groupId = "kafka-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenOwnerDeleted(Integer ownerId) {
        List<Integer> petsId = petRepository.findByOwnerId(ownerId).stream().map(Pet::getId).toList();
        petRepository.deleteByOwnerId(ownerId);
        for (Integer petId : petsId) {
            sendPetDeleted(petId);
        }
    }

    void sendPetDeleted(final Integer data) {
        CompletableFuture<SendResult<String, Integer>> future = kafkaTemplate.send("petDeleted",data);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sending message to Kafka Listener: {}", data);
            }
            else {
                log.error("Failed to send message to Kafka Listener: {}", data, ex);
            }
        });
    }


}
