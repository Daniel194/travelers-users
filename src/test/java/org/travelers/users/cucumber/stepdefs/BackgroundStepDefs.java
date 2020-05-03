package org.travelers.users.cucumber.stepdefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.cache.CacheManager;
import org.travelers.users.domain.User;
import org.travelers.users.repository.UserRepository;

import java.util.List;

public class BackgroundStepDefs {

    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    private List<User> users;

    public BackgroundStepDefs(UserRepository userRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
    }

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
        cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).clear();
    }

}
