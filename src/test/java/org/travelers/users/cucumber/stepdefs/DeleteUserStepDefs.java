package org.travelers.users.cucumber.stepdefs;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.travelers.users.config.KafkaProperties;
import org.travelers.users.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteUserStepDefs extends StepDefs {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProperties kafkaProperties;

    private String login;

    @When("admin wants to delete account details of {string}")
    public void admin_delete_account_details(String login) {
        this.login = login;
        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        producer.send(new ProducerRecord<>("delete-user", login));
    }

    @Then("the delete is 'SUCCESSFUL'")
    public void delete_is() {
        assertThat(userRepository.findByLogin(login).isPresent()).isFalse();
    }

}
