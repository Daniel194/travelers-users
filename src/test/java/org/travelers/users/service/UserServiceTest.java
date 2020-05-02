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
import org.travelers.users.service.dto.UserDTO;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.travelers.users.util.TestUtil.*;

@SpringBootTest(classes = UsersApp.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;


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

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getLogin()));
        SecurityContextHolder.setContext(securityContext);

        doReturn(Optional.of(user)).when(userRepository).findByLogin(user.getLogin());

        UserDTO userDTO = userService.getCurrent().orElse(new UserDTO());

        assertThat(areEqual(user, userDTO)).isTrue();

        verify(userRepository).findByLogin(user.getLogin());
        verifyNoMoreInteractions(userRepository);
    }

}
