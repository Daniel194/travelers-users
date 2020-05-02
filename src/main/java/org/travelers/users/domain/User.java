package org.travelers.users.domain;


import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.travelers.users.config.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

@org.springframework.data.mongodb.core.mapping.Document(collection = "user")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "user_details")
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Indexed(unique = true)
    private String login;

    @Size(max = 50)
    @Field("first_name")
    private String firstName;

    @Size(max = 50)
    @Field("last_name")
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Indexed(unique = true)
    private String email;

    @Size(max = 256)
    @Field("image_url")
    private String imageUrl;

    private String description;

    @Field("date_of_birth")
    private LocalDate dateOfBirth;

    @Size(min = 3, max = 3)
    @Field("place_of_birth")
    private String placeOfBirth;

    @Field("visited_countries")
    private Map<@Size(min = 3, max = 3) String, String> visitedCountries;

    @Field("social_platforms")
    private Map<String, String> socialPlatforms;

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
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "User{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", description='" + description + '\'' +
            ", dateOfBirth=" + dateOfBirth +
            ", placeOfBirth='" + placeOfBirth + '\'' +
            ", visitedCountries=" + visitedCountries +
            ", socialPlatforms=" + socialPlatforms +
            '}';
    }
}
