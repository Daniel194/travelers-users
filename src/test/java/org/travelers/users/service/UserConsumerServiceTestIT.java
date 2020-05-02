package org.travelers.users.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.KafkaContainer;
import org.travelers.users.UsersApp;
import org.travelers.users.config.KafkaProperties;
import org.travelers.users.domain.User;
import org.travelers.users.repository.UserRepository;
import org.travelers.users.service.dto.UserDTO;
import org.travelers.users.service.mapper.UserMapper;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.travelers.users.util.TestUtil.*;

@SpringBootTest(classes = UsersApp.class)
public class UserConsumerServiceTestIT {

    private static KafkaContainer kafkaContainer;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private UserConsumerService userConsumerService;

    @BeforeAll
    static void startServer() {
        kafkaContainer = new KafkaContainer("5.4.0");
        kafkaContainer.start();
    }

    @BeforeEach
    void setup() {
        KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setProducer(getProducerProps());
        kafkaProperties.setConsumer(getConsumerProps());

        userConsumerService = new UserConsumerService(userRepository, kafkaProperties, userMapper);
        userConsumerService.setUp();
    }

    @Test
    public void consumeCreateNewUser() throws JsonProcessingException, InterruptedException {
        UserDTO userDTO = getUserDTO();

        KafkaProducer<String, String> producer = new KafkaProducer<>(new HashMap<>(getProducerProps()));
        producer.send(new ProducerRecord<>("create-new-user", convertObjectToJson(userDTO)));

        userConsumerService.consumeCreateNewUser();

        User user = userRepository.findByLogin(userDTO.getLogin()).orElse(new User());

        assertThat(areEqual(user, userDTO)).isTrue();
    }

    private Map<String, String> getProducerProps() {
        Map<String, String> producerProps = new HashMap<>();
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        return producerProps;
    }

    private Map<String, String> getConsumerProps() {
        Map<String, String> consumerProps = new HashMap<>();
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        consumerProps.put("auto.offset.reset", "earliest");
        consumerProps.put("group.id", "default-group");
        consumerProps.put("client.id", "default-client");
        return consumerProps;
    }

}
