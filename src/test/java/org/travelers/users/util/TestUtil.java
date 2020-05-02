package org.travelers.users.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.travelers.users.domain.User;
import org.travelers.users.service.dto.UserDTO;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

public final class TestUtil {

    private static final ObjectMapper mapper = createObjectMapper();
    public static final MediaType APPLICATION_JSON = MediaType.APPLICATION_JSON;

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static byte[] createByteArray(int size, String data) {
        byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++) {
            byteArray[i] = Byte.parseByte(data, 2);
        }
        return byteArray;
    }

    public static class ZonedDateTimeMatcher extends TypeSafeDiagnosingMatcher<String> {

        private final ZonedDateTime date;

        public ZonedDateTimeMatcher(ZonedDateTime date) {
            this.date = date;
        }

        @Override
        protected boolean matchesSafely(String item, Description mismatchDescription) {
            try {
                if (!date.isEqual(ZonedDateTime.parse(item))) {
                    mismatchDescription.appendText("was ").appendValue(item);
                    return false;
                }
                return true;
            } catch (DateTimeParseException e) {
                mismatchDescription.appendText("was ").appendValue(item)
                    .appendText(", which could not be parsed as a ZonedDateTime");
                return false;
            }

        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a String representing the same Instant as ").appendValue(date);
        }
    }

    public static ZonedDateTimeMatcher sameInstant(ZonedDateTime date) {
        return new ZonedDateTimeMatcher(date);
    }

    public static <T> void equalsVerifier(Class<T> clazz) throws Exception {
        T domainObject1 = clazz.getConstructor().newInstance();
        assertThat(domainObject1.toString()).isNotNull();
        assertThat(domainObject1).isEqualTo(domainObject1);
        assertThat(domainObject1.hashCode()).isEqualTo(domainObject1.hashCode());

        Object testOtherObject = new Object();
        assertThat(domainObject1).isNotEqualTo(testOtherObject);
        assertThat(domainObject1).isNotEqualTo(null);

        T domainObject2 = clazz.getConstructor().newInstance();
        assertThat(domainObject1).isNotEqualTo(domainObject2);

        assertThat(domainObject1.hashCode()).isEqualTo(domainObject2.hashCode());
    }

    public static FormattingConversionService createFormattingConversionService() {
        DefaultFormattingConversionService dfcs = new DefaultFormattingConversionService();
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(dfcs);
        return dfcs;
    }

    public static User getUser() {
        User user = new User();

        user.setLogin(RandomStringUtils.randomAlphanumeric(5));
        user.setFirstName(RandomStringUtils.randomAlphanumeric(5));
        user.setLastName(RandomStringUtils.randomAlphanumeric(5));
        user.setEmail(RandomStringUtils.randomAlphanumeric(5) + "@test.com");
        user.setImageUrl(RandomStringUtils.randomAlphanumeric(5));
        user.setDescription(RandomStringUtils.randomAlphanumeric(20));
        user.setDateOfBirth(LocalDate.now());
        user.setPlaceOfBirth(RandomStringUtils.randomAlphanumeric(3));
        user.setSocialPlatforms(randomMap(3));
        user.setVisitedCountries(randomIntegerMap(3));

        return user;
    }

    public static UserDTO getUserDTO() {
        UserDTO user = new UserDTO();

        user.setLogin(StringUtils.lowerCase(RandomStringUtils.randomAlphanumeric(5)));
        user.setFirstName(RandomStringUtils.randomAlphanumeric(5));
        user.setLastName(RandomStringUtils.randomAlphanumeric(5));
        user.setEmail(RandomStringUtils.randomAlphanumeric(5) + "@test.com");
        user.setImageUrl(RandomStringUtils.randomAlphanumeric(5));
        user.setDescription(RandomStringUtils.randomAlphanumeric(20));
        user.setDateOfBirth(LocalDate.now());
        user.setPlaceOfBirth(RandomStringUtils.randomAlphanumeric(3));
        user.setSocialPlatforms(randomMap(3));
        user.setVisitedCountries(randomIntegerMap(3));

        return user;
    }

    public static Map<String, String> randomMap(int size) {
        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < size; i++) {
            map.put(RandomStringUtils.randomAlphanumeric(3), RandomStringUtils.randomAlphanumeric(3));
        }

        return map;
    }

    public static Map<String, Integer> randomIntegerMap(int size) {
        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < size; i++) {
            map.put(RandomStringUtils.randomAlphanumeric(3), ThreadLocalRandom.current().nextInt(1, 10));
        }

        return map;
    }

    public static boolean areEqual(User user, UserDTO userDTO) {
        return userDTO.getLogin().equals(user.getLogin()) &&
            userDTO.getFirstName().equals(user.getFirstName()) &&
            userDTO.getLastName().equals(user.getLastName()) &&
            userDTO.getEmail().equals(user.getEmail()) &&
            userDTO.getImageUrl().equals(user.getImageUrl()) &&
            userDTO.getDescription().equals(user.getDescription()) &&
            userDTO.getDateOfBirth().equals(user.getDateOfBirth()) &&
            userDTO.getPlaceOfBirth().equals(user.getPlaceOfBirth()) &&
            userDTO.getSocialPlatforms().equals(user.getSocialPlatforms()) &&
            userDTO.getVisitedCountries().equals(user.getVisitedCountries());
    }

    private TestUtil() {
    }
}
