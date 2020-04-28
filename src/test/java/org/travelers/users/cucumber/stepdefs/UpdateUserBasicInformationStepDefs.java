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

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateUserBasicInformationStepDefs extends StepDefs {

    private UserDTO user;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Given("user wants to update basic information with the following attributes")
    public void user_wants_update_basic_information_with_attributes(DataTable dataTable) {
        user = (UserDTO) dataTable.asList(UserDTO.class).get(0);
    }

    @When("user update the account with the new basic information {string}")
    public void user_update_account_with_basic_information(String testContext) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.writeValueAsString(user);

        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        producer.send(new ProducerRecord<>("update-user", message));
    }

    @Then("the update is {string}")
    public void update_is(String expectedResult) {
        User user = userRepository.findByLogin(this.user.getLogin()).orElse(new User());

        if ("SUCCESSFUL".equals(expectedResult)) {
            assertThat(areEqual(user, this.user)).isTrue();
        } else if ("FAIL".equals(expectedResult)) {
            assertThat(areEqual(user, this.user)).isFalse();
        }

    }

    private boolean areEqual(User user1, UserDTO user2) {
        return user2.getLogin().equals(user1.getLogin()) &&
            user2.getFirstName().equals(user1.getFirstName()) &&
            user2.getLastName().equals(user1.getLastName()) &&
            user2.getEmail().equals(user1.getEmail()) &&
            user2.getImageUrl().equals(user1.getImageUrl()) &&
            user2.getDescription().equals(user1.getDescription()) &&
            user2.getDateOfBirth().equals(user1.getDateOfBirth()) &&
            user2.getPlaceOfBirth().equals(user1.getPlaceOfBirth());
    }

}
