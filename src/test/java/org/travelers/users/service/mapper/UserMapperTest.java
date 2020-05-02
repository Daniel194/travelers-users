package org.travelers.users.service.mapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.travelers.users.UsersApp;
import org.travelers.users.domain.User;
import org.travelers.users.service.dto.UserDTO;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.travelers.users.util.TestUtil.*;

@SpringBootTest(classes = UsersApp.class)
class UserMapperTest {

    @InjectMocks
    private UserMapper mapper;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void usersToUserDTOs() {
        List<UserDTO> userDTOs = mapper.usersToUserDTOs(Arrays.asList(getUser(), null));

        assertThat(userDTOs).isNotEmpty();
        assertThat(userDTOs).size().isEqualTo(1);
    }

    @Test
    void userToUserDTO() {
        User user = getUser();
        UserDTO userDTO = mapper.userToUserDTO(user);

        assertThat(areEqual(user, userDTO)).isTrue();
    }

    @Test
    void userDTOsToUsers() {
        List<User> users = mapper.userDTOsToUsers(Arrays.asList(getUserDTO(), null));

        assertThat(users).isNotEmpty();
        assertThat(users).size().isEqualTo(1);
    }

    @Test
    void userDTOToUser() {
        UserDTO userDTO = getUserDTO();
        User user = mapper.userDTOToUser(userDTO);

        areEqual(user, userDTO);

        assertThat(areEqual(user, userDTO)).isTrue();
    }

    @Test
    void mapToUser() throws IOException {
        UserDTO userDTO = getUserDTO();
        String json = convertObjectToJson(userDTO);

        doReturn(userDTO).when(objectMapper).readValue(json, UserDTO.class);

        mapper.mapToUser(json);

        verify(objectMapper).readValue(json, UserDTO.class);
        verifyNoMoreInteractions(objectMapper);
    }

    @Test
    void mapToJson() throws IOException {
        UserDTO userDTO = getUserDTO();
        String json = convertObjectToJson(userDTO);

        doReturn(json).when(objectMapper).writeValueAsString(userDTO);

        mapper.mapToJson(userDTO);

        verify(objectMapper).writeValueAsString(userDTO);
        verifyNoMoreInteractions(objectMapper);
    }

}
