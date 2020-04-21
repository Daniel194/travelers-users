package org.travelers.users.cucumber.stepdefs;

import gherkin.ast.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreateUserStepDefs extends StepDefs {


    @Given("user wants to create an account with the following attributes")
    public void user_create_account_with(DataTable dataTable) {

    }

    @When("user save the new account {string}")
    public void user_save_account(String testContext) {

    }

    @Then("the save is {string}")
    public void save_is(String expectedResult) {

    }

}
