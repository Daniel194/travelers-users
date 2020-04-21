package org.travelers.users.cucumber.stepdefs;

import gherkin.ast.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UpdateUserStepDefs extends StepDefs {

    @Given("user wants to update basic information with the following attributes")
    public void user_wants_update_basic_information_with_attributes(DataTable dataTable) {

    }

    @When("user update the account with the new basic information {string}")
    public void user_update_account_with_basic_information(String testContext) {

    }

    @Given("user wants to update detail information with the following attributes")
    public void user_wants_update_datail_information_with_attributes(DataTable dataTable) {

    }

    @When("user update the account with the new detail information")
    public void user_update_account_with_detail_information(String testContext) {

    }

    @Given("log in user wants to add/remove a visited country with the following attributes")
    public void user_wants_add_visited_country_with_attributes() {

    }

    @When("user remove a visited country")
    public void user_remove_visited_country() {

    }

    @When("user add a new visited country")
    public void user_add_visited_country() {

    }

    @Then("the update is {string}")
    public void update_is(String expectedResult) {

    }
}
