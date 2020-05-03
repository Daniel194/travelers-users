package org.travelers.users.cucumber.stepdefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.travelers.users.service.dto.UserDTO;
import org.travelers.users.web.rest.UserResource;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class GetUserStepDefs extends StepDefs {

    private final UserResource userResource;

    private MockMvc restUserMockMvc;

    public GetUserStepDefs(UserResource userResource) {
        this.userResource = userResource;
    }

    @Before
    public void setup() {
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
    }

    @When("user wants to get account details of {string}")
    public void user_wants_account_details(String login) throws Exception {
        actions = restUserMockMvc.perform(get("/api/user/" + login)
            .accept(MediaType.APPLICATION_JSON));
    }

    @When("user {string} is login and wants to see its account details")
    public void user_login_wants_account_details(String login) throws Exception {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(login, login));
        SecurityContextHolder.setContext(securityContext);

        actions = restUserMockMvc.perform(get("/api/user")
            .accept(MediaType.APPLICATION_JSON));
    }

    @Then("the response is {string}")
    public void response_is(String expectedResult) {
        int statusCode = actions.andReturn().getResponse().getStatus();

        switch (expectedResult) {
            case "SUCCESSFUL":
                assertThat(statusCode).isIn(200, 201);
                break;
            case "FAIL":
                assertThat(statusCode).isBetween(400, 504);
                break;
            default:
                fail("Unexpected error");
        }
    }

    @And("following account details is returned")
    public void account_details_returned(DataTable dataTable) throws Exception {
        UserDTO user = (UserDTO) dataTable.asList(UserDTO.class).get(0);

        actions
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(user.getId()))
            .andExpect(jsonPath("$.login").value(user.getLogin()))
            .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
            .andExpect(jsonPath("$.lastName").value(user.getLastName()))
            .andExpect(jsonPath("$.email").value(user.getEmail()))
            .andExpect(jsonPath("$.imageUrl").value(user.getImageUrl()))
            .andExpect(jsonPath("$.description").value(user.getDescription()))
            .andExpect(jsonPath("$.placeOfBirth").value(user.getPlaceOfBirth()));
    }

    @And("with the following visited countries is returned")
    public void visited_countries_returned(DataTable dataTable) throws Exception {
        Map<String, String> visitedCountries = dataTable.asMap(String.class, String.class);

        for (Map.Entry<String, String> entry : visitedCountries.entrySet()) {
            actions.andExpect(jsonPath("$.visitedCountries." + entry.getKey()).value(entry.getValue()));
        }

    }

}
