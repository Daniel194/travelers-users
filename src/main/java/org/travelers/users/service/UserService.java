package org.travelers.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.travelers.users.domain.User;
import org.travelers.users.repository.UserRepository;
import org.travelers.users.security.SecurityUtils;
import org.travelers.users.service.dto.UserDTO;
import org.travelers.users.service.dto.UserDetailsDTO;
import org.travelers.users.service.mapper.UserMapper;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Optional<UserDTO> getByLogin(String login) {
        return userRepository.findByLogin(login).map(UserDTO::new);
    }

    public Optional<UserDTO> getCurrent() {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findByLogin)
            .map(UserDTO::new);
    }

    public Optional<UserDTO> update(UserDetailsDTO userDetailsDTO) {
        User user = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findByLogin).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setDescription(userDetailsDTO.getDescription());
        user.setDateOfBirth(userDetailsDTO.getDateOfBirth());
        user.setPlaceOfBirth(userDetailsDTO.getPlaceOfBirth());
        user.setSocialPlatforms(userDetailsDTO.getSocialPlatforms());

        userRepository.save(user);

        return Optional.of(userMapper.userToUserDTO(user));
    }

}
