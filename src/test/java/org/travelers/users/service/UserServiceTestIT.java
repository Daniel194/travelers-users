package org.travelers.users.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.travelers.users.UsersApp;
import org.travelers.users.domain.User;
import org.travelers.users.repository.UserRepository;
import org.travelers.users.service.dto.UserDTO;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.travelers.users.util.TestUtil.areEqual;
import static org.travelers.users.util.TestUtil.getUser;

@SpringBootTest(classes = UsersApp.class)
public class UserServiceTestIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository repository;

    @Test
    public void getByLogin() {
        User user = getUser();

        repository.save(user);

        UserDTO userDTO = userService.getByLogin(user.getLogin()).orElse(new UserDTO());

        assertThat(areEqual(user, userDTO)).isTrue();
    }

    @Test
    public void getCurrent() {
        User user = getUser();

        repository.save(user);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getLogin()));
        SecurityContextHolder.setContext(securityContext);

        UserDTO userDTO = userService.getCurrent().orElse(new UserDTO());

        assertThat(areEqual(user, userDTO)).isTrue();
    }
}
