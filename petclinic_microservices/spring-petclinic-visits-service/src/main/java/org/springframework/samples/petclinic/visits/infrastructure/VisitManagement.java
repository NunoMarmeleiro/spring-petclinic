package org.springframework.samples.petclinic.visits.infrastructure;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class VisitManagement {
    private final VisitRepository visitRepository;
    private final KafkaTemplate<String, Integer> kafkaTemplate;

    public VisitManagement(VisitRepository visitRepository, KafkaTemplate<String, Integer> kafkaTemplate) {
        this.visitRepository = visitRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


    @KafkaListener(
        topics = "petDeleted",
        groupId = "kafka-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void listenPetDeleted(Integer petId) {
        visitRepository.deleteByPetId(petId);
    }


}
