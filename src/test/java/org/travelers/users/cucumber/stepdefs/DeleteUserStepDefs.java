package org.travelers.users.cucumber.stepdefs;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DeleteUserStepDefs extends StepDefs {

    @When("admin wants to delete account details of {string}")
    public void admin_delete_account_details(String login) {

    }

    @Then("the delete is {string}")
    public void delete_is(String expectedResult) {

    }

}
