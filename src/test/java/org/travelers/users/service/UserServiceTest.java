package org.travelers.users.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.travelers.users.UsersApp;
import org.travelers.users.domain.User;
import org.travelers.users.repository.UserRepository;
import org.travelers.users.repository.search.UserSearchRepository;
import org.travelers.users.service.dto.UserDTO;
import org.travelers.users.service.dto.UserDetailsDTO;
import org.travelers.users.service.mapper.UserMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.mockito.Mockito.*;
import static org.travelers.users.util.TestUtil.*;
import static org.travelers.users.util.TestUtil.getUser;

@SpringBootTest(classes = UsersApp.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSearchRepository userSearchRepository;

    @Mock
    private UserMapper userMapper;

    @Test
    public void getByLogin() {
        User user = getUser();

        doReturn(Optional.of(user)).when(userRepository).findByLogin(user.getLogin());

        UserDTO userDTO = userService.getByLogin(user.getLogin()).orElse(new UserDTO());

        assertThat(areEqual(user, userDTO)).isTrue();

        verify(userRepository).findByLogin(user.getLogin());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getCurrent() {
        User user = getUser();

        setCurrentUser(user.getLogin());

        doReturn(Optional.of(user)).when(userRepository).findByLogin(user.getLogin());

        UserDTO userDTO = userService.getCurrent().orElse(new UserDTO());

        assertThat(areEqual(user, userDTO)).isTrue();

        verify(userRepository).findByLogin(user.getLogin());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void update() {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setDescription("Test12345");

        UserDTO userDTO = getUserDTO();
        User user = getUser();

        setCurrentUser(user.getLogin());

        doReturn(Optional.of(user)).when(userRepository).findByLogin(user.getLogin());
        doReturn(userDTO).when(userMapper).userToUserDTO(user);

        userService.update(userDetailsDTO);

        verify(userRepository).findByLogin(user.getLogin());
        verify(userMapper).userToUserDTO(user);
        verify(userRepository).save(user);
        verify(userSearchRepository).save(user);
        verifyNoMoreInteractions(userRepository, userMapper, userSearchRepository);
    }

    private void setCurrentUser(String login) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(login, login));
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void search() {
        String query = "test=query";
        List<User> users = Arrays.asList(getUser(), getUser(), getUser());

        doReturn(users).when(userSearchRepository).search(queryStringQuery(query));

        assertThat(userService.search(query).size()).isEqualTo(users.size());
    }

}
