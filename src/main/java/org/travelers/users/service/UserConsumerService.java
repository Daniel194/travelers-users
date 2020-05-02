package org.travelers.users.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.travelers.users.config.KafkaProperties;
import org.travelers.users.domain.User;
import org.travelers.users.repository.UserRepository;
import org.travelers.users.service.dto.CountryDTO;
import org.travelers.users.service.dto.UserDTO;
import org.travelers.users.service.mapper.UserMapper;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@Service
public class UserConsumerService {

    private final Logger log = LoggerFactory.getLogger(UserConsumerService.class);

    private final UserRepository userRepository;
    private final KafkaProperties kafkaProperties;
    private final UserMapper userMapper;

    private KafkaConsumer<String, String> createNewUser;
    private KafkaConsumer<String, String> updateUser;
    private KafkaConsumer<String, String> addCountry;
    private KafkaConsumer<String, String> removeCountry;
    private KafkaConsumer<String, String> deleteUser;

    @Autowired
    public UserConsumerService(UserRepository userRepository, KafkaProperties kafkaProperties, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.kafkaProperties = kafkaProperties;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void setUp() {
        Map<String, Object> createNewUserProps = kafkaProperties.getConsumerProps();
        createNewUserProps.put(ConsumerConfig.GROUP_ID_CONFIG, "create-new-user");

        createNewUser = new KafkaConsumer<>(createNewUserProps);
        createNewUser.subscribe(Collections.singleton("create-new-user"));

        Map<String, Object> updateUserProps = kafkaProperties.getConsumerProps();
        updateUserProps.put(ConsumerConfig.GROUP_ID_CONFIG, "update-user");

        updateUser = new KafkaConsumer<>(updateUserProps);
        updateUser.subscribe(Collections.singleton("update-user"));

        Map<String, Object> addCountryProps = kafkaProperties.getConsumerProps();
        addCountryProps.put(ConsumerConfig.GROUP_ID_CONFIG, "add-country");

        addCountry = new KafkaConsumer<>(addCountryProps);
        addCountry.subscribe(Collections.singleton("add-country"));

        Map<String, Object> removeCountryProps = kafkaProperties.getConsumerProps();
        removeCountryProps.put(ConsumerConfig.GROUP_ID_CONFIG, "remove-country");

        removeCountry = new KafkaConsumer<>(removeCountryProps);
        removeCountry.subscribe(Collections.singleton("remove-country"));

        Map<String, Object> deleteUserProps = kafkaProperties.getConsumerProps();
        deleteUserProps.put(ConsumerConfig.GROUP_ID_CONFIG, "delete-user");

        deleteUser = new KafkaConsumer<>(deleteUserProps);
        deleteUser.subscribe(Collections.singleton("delete-user"));
    }

    public void consumeCreateNewUser() {
        createNewUser.poll(Duration.ofSeconds(1)).forEach(record -> createNewUser(record.value()));
    }

    private void createNewUser(String user) {
        try {
            UserDTO userDTO = userMapper.mapToUser(user);

            userRepository.save(userMapper.userDTOToUser(userDTO));

        } catch (Exception ex) {
            log.trace("ERROR create-new-user: {}", ex.getMessage(), ex);
        }
    }

    public void consumeUpdateUser() {
        updateUser.poll(Duration.ofSeconds(1)).forEach(record -> updateUser(record.value()));
    }

    private void updateUser(String userJson) {
        try {
            UserDTO userDTO = userMapper.mapToUser(userJson);

            User user = userRepository.findByLogin(userDTO.getLogin())
                .orElseThrow(() -> new Exception("User " + userDTO.getLogin() + " not found"));

            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setImageUrl(userDTO.getImageUrl());

            userRepository.save(user);

        } catch (Exception ex) {
            log.trace("ERROR update-user: {}", ex.getMessage(), ex);
        }
    }

    public void consumeAddCountry() {
        addCountry.poll(Duration.ofSeconds(1)).forEach(record -> addCountry(record.value()));
    }

    private void addCountry(String countryJson) {
        try {
            CountryDTO countryDTO = userMapper.mapToCountry(countryJson);

            User user = userRepository.findByLogin(countryDTO.getLogin())
                .orElseThrow(() -> new Exception("User " + countryDTO.getLogin() + " not found"));

            Map<String, Integer> visitedCountries = user.getVisitedCountries();
            Integer count = visitedCountries.get(countryDTO.getCountry());

            count = count == null ? 1 : ++count;

            visitedCountries.put(countryDTO.getCountry(), count);

            user.setVisitedCountries(visitedCountries);

            userRepository.save(user);

        } catch (Exception ex) {
            log.trace("ERROR add-country: {}", ex.getMessage(), ex);
        }
    }

    public void consumeRemoveCountry() {
        removeCountry.poll(Duration.ofSeconds(1)).forEach(record -> removeCountry(record.value()));
    }

    private void removeCountry(String countryJson) {
        try {
            CountryDTO countryDTO = userMapper.mapToCountry(countryJson);

            User user = userRepository.findByLogin(countryDTO.getLogin())
                .orElseThrow(() -> new Exception("User " + countryDTO.getLogin() + " not found"));

            Map<String, Integer> visitedCountries = user.getVisitedCountries();

            Integer count = visitedCountries.get(countryDTO.getCountry());

            if (--count <= 0) {
                visitedCountries.remove(countryDTO.getCountry());
            } else {
                visitedCountries.put(countryDTO.getCountry(), count);
            }

            userRepository.save(user);

        } catch (Exception ex) {
            log.trace("ERROR remove-country: {}", ex.getMessage(), ex);
        }
    }

    public void consumeDeleteUser() {
        deleteUser.poll(Duration.ofSeconds(1)).forEach(record -> deleteUSer(record.value()));
    }

    private void deleteUSer(String login) {
        try {
            userRepository.deleteByLogin(login);

        } catch (Exception ex) {
            log.trace("ERROR update-user: {}", ex.getMessage(), ex);
        }
    }

}
