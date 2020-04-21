package org.travelers.users.cucumber.stepdefs;

import gherkin.ast.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GetUserStepDefs extends StepDefs {

    @When("user wants to get account details of {string}")
    public void user_wants_account_details(String login) {

    }

    @When("user {string} is login and wants to see its account details")
    public void user_login_wants_account_details(String login) {

    }

    @Then("the response is {string}")
    public void response_is(String expectedResult) {

    }

    @And("following account details is returned")
    public void account_details_returned(DataTable dataTable) {

    }

}
