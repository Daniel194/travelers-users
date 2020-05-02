package org.travelers.users.cucumber.stepdefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.travelers.users.service.dto.UserDTO;
import org.travelers.users.service.dto.UserDetailsDTO;
import org.travelers.users.web.rest.UserResource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.travelers.users.util.TestUtil.convertObjectToJson;
import static org.travelers.users.util.TestUtil.convertObjectToJsonBytes;

public class UpdateUserDetailsInformationStepDefs extends StepDefs {

    private final UserResource userResource;

    private UserDetailsDTO userDetails;

    public UpdateUserDetailsInformationStepDefs(UserResource userResource) {
        this.userResource = userResource;
    }

    @Given("user {string} wants to update detail information with the following attributes")
    public void user_wants_update_detail_information_with_attributes(String login, DataTable dataTable) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(login, login));
        SecurityContextHolder.setContext(securityContext);

        userDetails = (UserDetailsDTO) dataTable.asList(UserDetailsDTO.class).get(0);
    }

    @And("with the following update for social platforms")
    public void user_update_social_platforms(DataTable dataTable) {
        userDetails.setSocialPlatforms(dataTable.asMap(String.class, String.class));
    }

    @When("user update the account with the new detail information")
    public void user_update_account_with_detail_information() throws Exception {
        MockMvc restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();

        actions = restUserMockMvc.perform(put("/api/user/details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(userDetails)));
    }

    @Then("the update details is 'SUCCESSFUL'")
    public void update_details_is() {
        assertThat(actions.andReturn().getResponse().getStatus()).isIn(200, 201);
    }

    @And("following updated account details is returned")
    public void updated_account_details_returned(DataTable dataTable) throws Exception {
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

    @And("with the following updated social platforms")
    public void updated_social_platforms(DataTable dataTable) throws Exception {
        Map<String, String> socialPlatforms = dataTable.asMap(String.class, String.class);

        for (Map.Entry<String, String> entry : socialPlatforms.entrySet()) {
            actions.andExpect(jsonPath("$.socialPlatforms." + entry.getKey()).value(entry.getValue()));
        }
    }

}
