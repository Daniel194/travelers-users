package org.travelers.users.service.dto;

import org.travelers.users.config.Constants;
import org.travelers.users.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Map;

public class UserDTO {

    private String id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private String description;

    private LocalDate dateOfBirth;

    @Size(min = 3, max = 3)
    private String placeOfBirth;

    private Map<@Size(min = 3, max = 3) String, String> visitedCountries;

    private Map<String, String> socialPlatforms;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.imageUrl = user.getImageUrl();
        this.description = user.getDescription();
        this.dateOfBirth = user.getDateOfBirth();
        this.placeOfBirth = user.getPlaceOfBirth();
        this.visitedCountries = user.getVisitedCountries();
        this.socialPlatforms = user.getSocialPlatforms();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public Map<String, String> getVisitedCountries() {
        return visitedCountries;
    }

    public void setVisitedCountries(Map<String, String> visitedCountries) {
        this.visitedCountries = visitedCountries;
    }

    public Map<String, String> getSocialPlatforms() {
        return socialPlatforms;
    }

    public void setSocialPlatforms(Map<String, String> socialPlatforms) {
        this.socialPlatforms = socialPlatforms;
    }
}
