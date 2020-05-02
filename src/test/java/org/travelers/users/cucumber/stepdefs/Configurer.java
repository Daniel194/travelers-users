package org.travelers.users.cucumber.stepdefs;


import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;
import org.travelers.users.domain.User;
import org.travelers.users.service.dto.CountryDTO;
import org.travelers.users.service.dto.UserDTO;
import org.travelers.users.service.dto.UserDetailsDTO;

import java.time.LocalDate;
import java.util.Locale;

public class Configurer implements TypeRegistryConfigurer {

    @Override
    public void configureTypeRegistry(TypeRegistry registry) {

        registry.defineDataTableType(new DataTableType(User.class, (TableEntryTransformer<User>) entry -> {
            User user = new User();
            user.setId(entry.get("id"));
            user.setLogin(entry.get("login"));
            user.setFirstName(entry.get("firstName"));
            user.setLastName(entry.get("lastName"));
            user.setEmail(entry.get("email"));
            user.setImageUrl(entry.get("imageUrl"));
            user.setDescription(entry.get("description"));
            user.setDateOfBirth(LocalDate.parse(entry.get("dateOfBirth")));
            user.setPlaceOfBirth(entry.get("placeOfBirth"));

            return user;
        }));

        registry.defineDataTableType(new DataTableType(UserDTO.class, (TableEntryTransformer<UserDTO>) entry -> {
            UserDTO user = new UserDTO();
            user.setId(entry.get("id"));
            user.setLogin(entry.get("login"));
            user.setFirstName(entry.get("firstName"));
            user.setLastName(entry.get("lastName"));
            user.setEmail(entry.get("email"));
            user.setImageUrl(entry.get("imageUrl"));
            user.setDescription(entry.get("description"));
            user.setPlaceOfBirth(entry.get("placeOfBirth"));

            String dateOfBirth = entry.get("dateOfBirth");

            if (dateOfBirth != null) {
                user.setDateOfBirth(LocalDate.parse(dateOfBirth));
            }

            return user;
        }));

        registry.defineDataTableType(new DataTableType(UserDetailsDTO.class, (TableEntryTransformer<UserDetailsDTO>) entry -> {
            UserDetailsDTO userDetails = new UserDetailsDTO();

            userDetails.setDateOfBirth(LocalDate.parse(entry.get("dateOfBirth")));
            userDetails.setDescription(entry.get("description"));
            userDetails.setPlaceOfBirth(entry.get("placeOfBirth"));

            return userDetails;
        }));

        registry.defineDataTableType(new DataTableType(CountryDTO.class, (TableEntryTransformer<CountryDTO>) entry -> {
            CountryDTO country = new CountryDTO();

            country.setLogin(entry.get("login"));
            country.setCountry(entry.get("country"));

            return country;
        }));
    }

    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

}
