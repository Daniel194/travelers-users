package org.travelers.users.cucumber.stepdefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.travelers.users.config.KafkaProperties;
import org.travelers.users.service.dto.CountryDTO;

public class UpdateUserVisitedCountriesInformationStepDefs extends StepDefs {

    private CountryDTO country;

    private final KafkaProperties kafkaProperties;

    public UpdateUserVisitedCountriesInformationStepDefs(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Given("log in user wants to add/remove a visited country with the following attributes")
    public void user_wants_add_visited_country_with_attributes(DataTable dataTable) {
        country = (CountryDTO) dataTable.asList(CountryDTO.class).get(0);
    }

    @When("user remove a visited country")
    public void user_remove_visited_country() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.writeValueAsString(country);

        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        producer.send(new ProducerRecord<>("remove-country", message));
    }

    @When("user add a new visited country")
    public void user_add_visited_country() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.writeValueAsString(country);

        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        producer.send(new ProducerRecord<>("add-country", message));
    }

    @Then("the update countries is 'SUCCESSFUL'")
    public void updated_countries_is_successful() throws InterruptedException {
        Thread.sleep(5000);
    }

}
