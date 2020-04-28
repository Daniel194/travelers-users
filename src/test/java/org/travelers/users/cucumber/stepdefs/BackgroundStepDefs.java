package org.travelers.users.cucumber.stepdefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.travelers.users.domain.User;
import org.travelers.users.repository.UserRepository;

import java.util.List;

public class BackgroundStepDefs {

    private List<User> users;

    @Autowired
    private UserRepository userRepository;

    @Given("user/users with the following attributes")
    public void user_with_attributes(DataTable dataTable) {
        DataTableType.entry(User.class);
        this.users = dataTable.asList(User.class);
    }

    @And("with the following social platforms")
    public void user_with_social_platforms(DataTable dataTable) {
        users.forEach(user -> user.setSocialPlatforms(dataTable.asMap(String.class, String.class)));
    }

    @And("with the following visited countries")
    public void user_with_visited_countries(DataTable dataTable) {
        users.forEach(user -> user.setVisitedCountries(dataTable.asMap(String.class, String.class)));
    }

    @When("user/users already exists")
    public void user_already_exist() {
        userRepository.deleteAll();
        userRepository.saveAll(users);
    }

}
