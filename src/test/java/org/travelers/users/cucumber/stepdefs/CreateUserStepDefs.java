package org.travelers.users.cucumber.stepdefs;


import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.travelers.users.config.KafkaProperties;
import org.travelers.users.domain.User;
import org.travelers.users.repository.UserRepository;
import org.travelers.users.service.dto.UserDTO;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateUserStepDefs extends StepDefs {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProperties kafkaProperties;

    private UserDTO user;

    @Given("user wants to create an account with the following attributes")
    public void user_create_account_with(DataTable dataTable) {
        userRepository.deleteAll();
        user = (UserDTO) dataTable.asList(UserDTO.class).get(0);
    }

    @When("user save the new account {string}")
    public void user_save_account(String testContext) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.writeValueAsString(user);

        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        producer.send(new ProducerRecord<>("create-new-user", message));
    }

    @Then("the save is {string}")
    public void save_is(String expectedResult) {
        Optional<User> user = userRepository.findByLogin(this.user.getLogin());

        if ("SUCCESSFUL".equals(expectedResult)) {
            assertThat(user.isPresent()).isTrue();
        } else if ("FAIL".equals(expectedResult)) {
            assertThat(user.isPresent()).isFalse();
        }
    }
}
