package org.springframework.samples.petclinic.owners.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import java.util.Map;
import java.util.HashMap;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${kafka.config.bootstrap}")
    public String bootstrapServers;

    @Value("${kafka.config.consumer-group}")
    public String consumerGroup;

    @Bean
    public ConsumerFactory<Object, Object> kafkaReceiver() {
        final Map<String, Object> consumerProperties = new HashMap<>();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            org.apache.kafka.common.serialization.StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.IntegerDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(consumerProperties);
    }


    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        final Map<String, Object> producerProperties = new HashMap<>();
        producerProperties.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            bootstrapServers);
        producerProperties.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            org.apache.kafka.common.serialization.StringSerializer.class);
        producerProperties.put(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            org.apache.kafka.common.serialization.IntegerDeserializer.class);
        return new DefaultKafkaProducerFactory<>(producerProperties);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
