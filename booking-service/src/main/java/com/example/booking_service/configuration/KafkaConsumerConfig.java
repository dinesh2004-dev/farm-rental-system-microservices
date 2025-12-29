package com.example.booking_service.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.events.EquipmentReservationFailedEvent;
import org.example.events.EquipmentReservedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public Map<String,Object> consumerConfigs(){

        Map<String,Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"booking-service-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);

        return props;
    }

    @Bean
    public ConsumerFactory<String,EquipmentReservedEvent> equipmentReservedEventFactory(){

        JsonDeserializer<EquipmentReservedEvent> deserializer =
                new JsonDeserializer<>(EquipmentReservedEvent.class,false);

        deserializer.addTrustedPackages("org.example.events");

        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),
                new StringDeserializer(),
                deserializer);
    }

    @Bean
    public ConsumerFactory<String, EquipmentReservationFailedEvent> equipmentReservationFailedEventFactory(){

        JsonDeserializer<EquipmentReservationFailedEvent> deserializer =
                new JsonDeserializer<>(EquipmentReservationFailedEvent.class,false);

        deserializer.addTrustedPackages("org.example.events");

        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),
                new StringDeserializer(),
                deserializer);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,EquipmentReservedEvent>> equipmentReservedEventKafkaListenerContainerFactory(){

        ConcurrentKafkaListenerContainerFactory<String,EquipmentReservedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(equipmentReservedEventFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,EquipmentReservationFailedEvent>> equipmentReservationFailedEventKafkaListenerContainerFactory(){

        ConcurrentKafkaListenerContainerFactory<String,EquipmentReservationFailedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(equipmentReservationFailedEventFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }


}
