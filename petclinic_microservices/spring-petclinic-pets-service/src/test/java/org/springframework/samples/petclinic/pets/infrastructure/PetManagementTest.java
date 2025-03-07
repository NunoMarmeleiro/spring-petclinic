package org.springframework.samples.petclinic.pets.infrastructure;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.samples.petclinic.pets.domain.Pet;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {"ownerDeleted", "petDeleted"})
public class PetManagementTest {
    /*
    TODO NOT WORKING
    @Autowired
    private KafkaTemplate<String, Integer> kafkaTemplate;

    @Autowired
    private PetRepository petRepository;

    private Pet pet1, pet2;
    private final int ownerId = 10;

    @BeforeEach
    void setup() {
        pet1 = new Pet();
        pet1.setName("Basil");
        pet1.setOwnerId(ownerId);
        pet1 = petRepository.save(pet1);

        pet2 = new Pet();
        pet2.setName("Max");
        pet2.setOwnerId(ownerId);
        pet2 = petRepository.save(pet2);
    }

    @Test
    void shouldDeletePetsWhenOwnerDeletedEventIsReceived() {
        kafkaTemplate.send("ownerDeleted", ownerId);

        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            List<Pet> pets = petRepository.findByOwnerId(ownerId);
            assertThat(pets).isEmpty();
        });

        List<Integer> deletedPetIds = consumeMessages("petDeleted");
        assertThat(deletedPetIds).containsExactlyInAnyOrder(pet1.getId(), pet2.getId());
    }

    private List<Integer> consumeMessages(String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put("auto.offset.reset", "earliest"); // Read from beginning

        try (KafkaConsumer<String, Integer> consumer = new KafkaConsumer<>(props, new StringDeserializer(), new IntegerDeserializer())) {
            consumer.subscribe(Collections.singleton(topic));

            Iterable<ConsumerRecord<String, Integer>> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5)).records(topic);

            return StreamSupport.stream(records.spliterator(), false)
                .map(ConsumerRecord::value)
                .toList();
        }
    }*/

}
