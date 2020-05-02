package org.travelers.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.travelers.users.repository.UserRepository;
import org.travelers.users.security.SecurityUtils;
import org.travelers.users.service.dto.UserDTO;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserDTO> getByLogin(String login) {
        return userRepository.findByLogin(login).map(UserDTO::new);
    }

    public Optional<UserDTO> getCurrent() {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findByLogin)
            .map(UserDTO::new);
    }

}
