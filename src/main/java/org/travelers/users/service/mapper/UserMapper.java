package org.travelers.users.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.travelers.users.domain.User;
import org.travelers.users.service.dto.CountryDTO;
import org.travelers.users.service.dto.UserDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    private final ObjectMapper objectMapper;

    @Autowired
    public UserMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<UserDTO> usersToUserDTOs(List<User> users) {
        return users.stream()
            .filter(Objects::nonNull)
            .map(this::userToUserDTO)
            .collect(Collectors.toList());
    }

    public UserDTO userToUserDTO(User user) {
        return new UserDTO(user);
    }

    public List<User> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream()
            .filter(Objects::nonNull)
            .map(this::userDTOToUser)
            .collect(Collectors.toList());
    }

    public User userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            User user = new User();
            user.setId(userDTO.getId());
            user.setLogin(userDTO.getLogin());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setImageUrl(userDTO.getImageUrl());
            user.setDescription(userDTO.getDescription());
            user.setDateOfBirth(userDTO.getDateOfBirth());
            user.setPlaceOfBirth(userDTO.getPlaceOfBirth());
            user.setVisitedCountries(userDTO.getVisitedCountries());
            user.setSocialPlatforms(userDTO.getSocialPlatforms());

            return user;
        }
    }

    public UserDTO mapToUser(String user) throws JsonProcessingException {
        return objectMapper.readValue(user, UserDTO.class);
    }

    public String mapToJson(UserDTO user) throws JsonProcessingException {
        return objectMapper.writeValueAsString(user);
    }

    public CountryDTO mapToCountry(String countryJson) throws JsonProcessingException {
        return objectMapper.readValue(countryJson, CountryDTO.class);
    }
}
