package org.travelers.users.service;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.travelers.users.config.KafkaProperties;
import org.travelers.users.repository.UserRepository;
import org.travelers.users.service.dto.UserDTO;
import org.travelers.users.service.mapper.UserMapper;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;

@Service
public class UserConsumerService {

    private final Logger log = LoggerFactory.getLogger(UserConsumerService.class);

    private final UserRepository userRepository;
    private final KafkaProperties kafkaProperties;
    private final UserMapper userMapper;

    private KafkaConsumer<String, String> createNewUser;

    @Autowired
    public UserConsumerService(UserRepository userRepository, KafkaProperties kafkaProperties, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.kafkaProperties = kafkaProperties;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void setUp() {
        createNewUser = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        createNewUser.subscribe(Collections.singleton("create-new-user"));
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
}
