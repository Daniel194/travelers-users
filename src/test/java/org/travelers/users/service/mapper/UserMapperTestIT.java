package org.travelers.users.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.travelers.users.UsersApp;
import org.travelers.users.service.dto.CountryDTO;
import org.travelers.users.service.dto.UserDTO;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.travelers.users.util.TestUtil.convertObjectToJson;
import static org.travelers.users.util.TestUtil.getUserDTO;

@SpringBootTest(classes = UsersApp.class)
public class UserMapperTestIT {

    @Autowired
    private UserMapper mapper;

    @Test
    void mapToUser() throws IOException {
        UserDTO userDTO = getUserDTO();
        String json = convertObjectToJson(userDTO);

        UserDTO userDTO2 = mapper.mapToUser(json);

        assertThat(userDTO2.getLogin()).isEqualTo(userDTO.getLogin());
    }

    @Test
    void mapToJson() throws IOException {
        UserDTO userDTO = getUserDTO();

        String json2 = mapper.mapToJson(userDTO);

        assertThat(json2).isNotNull();
        assertThat(json2).isNotEmpty();
        assertThat(json2).isNotBlank();
    }

    @Test
    void mapToCountry() throws IOException {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setCountry("TST");
        countryDTO.setLogin("test");

        String json = convertObjectToJson(countryDTO);

        CountryDTO countryDTO2 = mapper.mapToCountry(json);

        assertThat(countryDTO2.getLogin()).isEqualTo(countryDTO.getLogin());
    }

}
