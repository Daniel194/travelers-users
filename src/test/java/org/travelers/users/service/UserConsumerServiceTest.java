package org.travelers.users.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.travelers.users.util.TestUtil.convertObjectToJson;
import static org.travelers.users.util.TestUtil.getUser;

@SpringBootTest(classes = UsersApp.class)
class UserConsumerServiceTest {
    private static KafkaContainer kafkaContainer;

    @InjectMocks
    private UserConsumerService userConsumerService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private KafkaProperties kafkaProperties;

    @BeforeAll
    static void startServer() {
        kafkaContainer = new KafkaContainer("5.4.0");
        kafkaContainer.start();
    }

    @Test
    public void consumeCreateNewUser() throws JsonProcessingException {
        User user = getUser();
        UserDTO userDTO = new UserDTO(user);
        String userJson = convertObjectToJson(userDTO);

        doReturn(getConsumerProps()).when(kafkaProperties).getConsumerProps();
        doReturn(userDTO).when(userMapper).mapToUser(any(String.class));
        doReturn(user).when(userMapper).userDTOToUser(userDTO);

        KafkaProducer<String, String> producer = new KafkaProducer<>(getProducerProps());
        producer.send(new ProducerRecord<>("create-new-user", userJson));

        userConsumerService.setUp();
        userConsumerService.consumeCreateNewUser();

        verify(kafkaProperties).getConsumerProps();
        verify(userMapper).mapToUser(any(String.class));
        verify(userMapper).userDTOToUser(userDTO);
        verify(userRepository).save(user);
        verifyNoMoreInteractions(kafkaProperties, userMapper, userRepository);
    }

    private Map<String, Object> getProducerProps() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        return producerProps;
    }

    private Map<String, Object> getConsumerProps() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        consumerProps.put("auto.offset.reset", "earliest");
        consumerProps.put("group.id", "default-group");
        consumerProps.put("client.id", "default-client");
        return consumerProps;
    }

}