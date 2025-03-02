package org.springframework.samples.petclinic.pets.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PetManagement {


    private final PetRepository petRepository;

    public PetManagement(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @KafkaListener(
        topics = "ownerDeleted",
        groupId = "kafka-group"
    )
    public void listenOwnerDeleted(Integer ownerId) {
        petRepository.deleteByOwnerId(ownerId);
    }
}
