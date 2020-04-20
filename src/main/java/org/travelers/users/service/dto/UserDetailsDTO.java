package org.travelers.users.service.dto;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Map;

public class UserDetailsDTO {

    private String description;

    private LocalDate dateOfBirth;

    @Size(min = 3, max = 3)
    private String placeOfBirth;

    private Map<String, String> socialPlatforms;

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

    public Map<String, String> getSocialPlatforms() {
        return socialPlatforms;
    }

    public void setSocialPlatforms(Map<String, String> socialPlatforms) {
        this.socialPlatforms = socialPlatforms;
    }
}
