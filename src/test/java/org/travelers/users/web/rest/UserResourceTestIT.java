package org.travelers.users.web.rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.travelers.users.UsersApp;
import org.travelers.users.domain.User;
import org.travelers.users.repository.UserRepository;
import org.travelers.users.service.dto.UserDetailsDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.travelers.users.util.TestUtil.*;

@AutoConfigureMockMvc
@WithMockUser(value = UserResourceTestIT.TEST_USER_LOGIN)
@SpringBootTest(classes = UsersApp.class)
class UserResourceTestIT {
    static final String TEST_USER_LOGIN = "test";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc restUserMockMvc;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setup() {
        cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).clear();
    }

    @Test
    public void getExistingUser() throws Exception {
        User user = getUser();
        userRepository.save(user);

        assertThat(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).get(user.getLogin())).isNull();

        restUserMockMvc.perform(get("/api/user/" + user.getLogin())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.login").value(user.getLogin()))
            .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
            .andExpect(jsonPath("$.lastName").value(user.getLastName()))
            .andExpect(jsonPath("$.email").value(user.getEmail()))
            .andExpect(jsonPath("$.imageUrl").value(user.getImageUrl()))
            .andExpect(jsonPath("$.description").value(user.getDescription()))
            .andExpect(jsonPath("$.placeOfBirth").value(user.getPlaceOfBirth()));

        assertThat(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).get(user.getLogin())).isNotNull();
    }

    @Test
    public void getNotExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/user/nouser")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void getUserByLogin() throws Exception {
        User user = getUser();
        user.setLogin(TEST_USER_LOGIN);
        userRepository.save(user);

        assertThat(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).get(user.getLogin())).isNull();

        restUserMockMvc.perform(get("/api/user")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.login").value(user.getLogin()))
            .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
            .andExpect(jsonPath("$.lastName").value(user.getLastName()))
            .andExpect(jsonPath("$.email").value(user.getEmail()))
            .andExpect(jsonPath("$.imageUrl").value(user.getImageUrl()))
            .andExpect(jsonPath("$.description").value(user.getDescription()))
            .andExpect(jsonPath("$.placeOfBirth").value(user.getPlaceOfBirth()));

        assertThat(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).get(user.getLogin())).isNotNull();
    }

    @Test
    @WithUnauthenticatedMockUser
    public void getUserByNoLogin() throws Exception {
        restUserMockMvc.perform(get("/api/user")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateDetails() throws Exception {
        User user = getUser();
        user.setLogin(TEST_USER_LOGIN);
        userRepository.save(user);

        UserDetailsDTO userDetails = new UserDetailsDTO();
        userDetails.setDescription("Test12345");
        userDetails.setPlaceOfBirth("TST");

        restUserMockMvc.perform(put("/api/user/details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(userDetails)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.login").value(user.getLogin()))
            .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
            .andExpect(jsonPath("$.lastName").value(user.getLastName()))
            .andExpect(jsonPath("$.email").value(user.getEmail()))
            .andExpect(jsonPath("$.imageUrl").value(user.getImageUrl()))
            .andExpect(jsonPath("$.placeOfBirth").value(userDetails.getPlaceOfBirth()))
            .andExpect(jsonPath("$.description").value(userDetails.getDescription()));
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

}
