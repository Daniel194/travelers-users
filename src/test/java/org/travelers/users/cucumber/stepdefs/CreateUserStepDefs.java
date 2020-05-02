package org.travelers.users.cucumber.stepdefs;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.assertj.core.api.AbstractBooleanAssert;
import org.travelers.users.config.KafkaProperties;
import org.travelers.users.domain.User;
import org.travelers.users.repository.UserRepository;
import org.travelers.users.service.dto.UserDTO;
import org.travelers.users.service.mapper.UserMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateUserStepDefs extends StepDefs {

    private final UserRepository userRepository;
    private final KafkaProperties kafkaProperties;
    private final UserMapper userMapper;

    private UserDTO user;

    public CreateUserStepDefs(UserRepository userRepository, KafkaProperties kafkaProperties, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.kafkaProperties = kafkaProperties;
        this.userMapper = userMapper;
    }

    @Given("user wants to create an account with the following attributes")
    public void user_create_account_with(DataTable dataTable) {
        user = (UserDTO) dataTable.asList(UserDTO.class).get(0);
    }

    @When("^user save the new account .*?")
    public void user_save_account() throws JsonProcessingException {
        String message = userMapper.mapToJson(user);

        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        producer.send(new ProducerRecord<>("create-new-user", message));
    }

    @Then("the save is {string}")
    public void save_is(String expectedResult) throws InterruptedException {
        Thread.sleep(5000);

        Optional<User> user = userRepository.findByLogin(this.user.getLogin());
        AbstractBooleanAssert<?> booleanAssert = assertThat(areEquals(user.orElse(new User()), this.user));

        if ("SUCCESSFUL".equals(expectedResult)) {
            booleanAssert.isTrue();
        } else if ("FAIL".equals(expectedResult)) {
            booleanAssert.isFalse();
        }
    }

    private boolean areEquals(User user, UserDTO userDTO) {
        return userDTO.getLogin().equals(user.getLogin()) &&
            userDTO.getFirstName().equals(user.getFirstName()) &&
            userDTO.getLastName().equals(user.getLastName()) &&
            userDTO.getEmail().equals(user.getEmail()) &&
            userDTO.getImageUrl().equals(user.getImageUrl());
    }
}
