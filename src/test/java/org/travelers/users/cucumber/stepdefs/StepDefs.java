package org.travelers.users.cucumber.stepdefs;

import gherkin.ast.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.test.web.servlet.ResultActions;

public abstract class StepDefs {

    protected ResultActions actions;

    @Given("user/users with the following attributes")
    public void user_with_attributes(DataTable dataTable) {

    }

    @And("with the following social platforms")
    public void user_with_social_platforms(DataTable dataTable) {

    }

    @And("with the following visited countries")
    public void user_with_visited_countries(DataTable dataTable) {

    }

    @When("user/users already exists")
    public void user_already_exist() {

    }

}
