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
import org.travelers.users.service.dto.CountryDTO;
import org.travelers.users.service.dto.UserDTO;
import org.travelers.users.service.mapper.UserMapper;

import java.util.ArrayList;
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
    public void consumeCreateNewUser() throws JsonProcessingException {
        UserDTO userDTO = getUserDTO();

        KafkaProducer<String, String> producer = new KafkaProducer<>(new HashMap<>(getProducerProps()));
        producer.send(new ProducerRecord<>("create-new-user", convertObjectToJson(userDTO)));

        userConsumerService.consumeCreateNewUser();

        User user = userRepository.findByLogin(userDTO.getLogin()).orElse(new User());

        assertThat(areEqual(user, userDTO)).isTrue();
    }

    @Test
    public void consumeUpdateUser() throws JsonProcessingException {
        User user = getUser();
        userRepository.save(user);

        UserDTO userDTO = getUserDTO();
        userDTO.setLogin(user.getLogin());

        KafkaProducer<String, String> producer = new KafkaProducer<>(new HashMap<>(getProducerProps()));
        producer.send(new ProducerRecord<>("update-user", convertObjectToJson(userDTO)));

        userConsumerService.consumeUpdateUser();

        User newUser = userRepository.findByLogin(user.getLogin()).orElse(new User());

        assertThat(user.getEmail()).isNotEqualTo(newUser.getEmail());
        assertThat(userDTO.getEmail()).isEqualTo(newUser.getEmail());
    }

    @Test
    public void consumeAddCountry() throws JsonProcessingException {
        User user = getUser();
        userRepository.save(user);

        CountryDTO country = new CountryDTO();
        country.setCountry("TST");
        country.setLogin(user.getLogin());

        KafkaProducer<String, String> producer = new KafkaProducer<>(new HashMap<>(getProducerProps()));
        producer.send(new ProducerRecord<>("add-country", convertObjectToJson(country)));

        userConsumerService.consumeAddCountry();

        User newUser = userRepository.findByLogin(user.getLogin()).orElse(new User());

        assertThat(newUser.getLogin()).isEqualTo(country.getLogin());
        assertThat(newUser.getVisitedCountries().get(country.getCountry())).isNotNull();
        assertThat(newUser.getVisitedCountries().get(country.getCountry())).isEqualTo(1);
    }

    @Test
    public void consumeRemoveCountry() throws JsonProcessingException {
        User user = getUser();
        userRepository.save(user);

        CountryDTO country = new CountryDTO();
        country.setLogin(user.getLogin());
        country.setCountry(new ArrayList<>(user.getVisitedCountries().keySet()).get(0));

        KafkaProducer<String, String> producer = new KafkaProducer<>(new HashMap<>(getProducerProps()));
        producer.send(new ProducerRecord<>("remove-country", convertObjectToJson(country)));

        userConsumerService.consumeRemoveCountry();

        User newUser = userRepository.findByLogin(user.getLogin()).orElse(new User());
        Integer newCount = user.getVisitedCountries().get(country.getCountry()) - 1;

        assertThat(newUser.getLogin()).isEqualTo(country.getLogin());
        assertThat(newUser.getVisitedCountries().get(country.getCountry())).isNotNull();
        assertThat(newUser.getVisitedCountries().get(country.getCountry())).isEqualTo(newCount);
    }

    @Test
    public void consumeDeleteUser() {
        User user = getUser();
        userRepository.save(user);

        KafkaProducer<String, String> producer = new KafkaProducer<>(new HashMap<>(getProducerProps()));
        producer.send(new ProducerRecord<>("delete-user", user.getLogin()));

        userConsumerService.consumeDeleteUser();

        assertThat(userRepository.findByLogin(user.getLogin())).isEmpty();
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

        return consumerProps;
    }

}
