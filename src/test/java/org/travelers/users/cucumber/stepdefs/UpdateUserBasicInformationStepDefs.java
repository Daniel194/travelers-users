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

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateUserBasicInformationStepDefs extends StepDefs {

    private UserDTO user;

    private final UserRepository userRepository;
    private final KafkaProperties kafkaProperties;
    private final UserMapper userMapper;

    public UpdateUserBasicInformationStepDefs(UserRepository userRepository, KafkaProperties kafkaProperties, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.kafkaProperties = kafkaProperties;
        this.userMapper = userMapper;
    }

    @Given("user wants to update basic information with the following attributes")
    public void user_wants_update_basic_information_with_attributes(DataTable dataTable) {
        user = (UserDTO) dataTable.asList(UserDTO.class).get(0);
    }

    @When("^user update the account with the new basic information .*?")
    public void user_update_account_with_basic_information() throws JsonProcessingException {
        String message = userMapper.mapToJson(user);

        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        producer.send(new ProducerRecord<>("update-user", message));
    }

    @Then("the update is {string}")
    public void update_is(String expectedResult) throws InterruptedException {
        Thread.sleep(5000);

        User user = userRepository.findByLogin(this.user.getLogin()).orElse(new User());
        AbstractBooleanAssert<?> booleanAssert = assertThat(areEquals(user, this.user));

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
