package dk.developer.delta.api;

import dk.developer.delta.api.concepts.Gender;
import dk.developer.delta.api.concepts.Subscription;
import dk.developer.delta.api.concepts.User;
import dk.developer.facebook.Facebook;
import dk.developer.security.Credential;

import java.util.Map;
import java.util.Set;

import static dk.developer.facebook.FacebookUserField.*;
import static dk.developer.utility.Convenience.set;
import static dk.developer.utility.Converter.converter;

public class FacebookUserFactory implements UserFactory {
    private final Facebook facebook;

    public static UserFactory create(Facebook facebook) {
        return new FacebookUserFactory(facebook);
    }

    private FacebookUserFactory(Facebook facebook) {
        this.facebook = facebook;
    }

    @Override
    public User create(Credential credential) {
        Map<String, Object> personalInformation = facebook.extractPersonalInformation(credential.getToken(), NAME.value(), FIRST_NAME.value(), LAST_NAME.value(), EMAIL.value(),
                GENDER.value(), LOCATION.value(), LOCALE.value(), AGE_RANGE.value());

        String name = name(personalInformation);
        String firstName = firstName(personalInformation);
        String lastName = lastName(personalInformation);
        String email = email(personalInformation);
        Gender gender = gender(personalInformation);
        String location = location(personalInformation);
        String locale = locale(personalInformation);
        String ageRange = ageRange(personalInformation);
        Set<Subscription> subscriptions = set();

        return new User(credential.getUserId(), name, firstName, lastName, email, gender, location, locale, ageRange, null, null, subscriptions);
    }

    private String name(Map<String, Object> personalInformation) {
        return (String) personalInformation.get("name");
    }

    private String firstName(Map<String, Object> personalInformation) {
        return (String) personalInformation.get("first_name");
    }

    private String lastName(Map<String, Object> personalInformation) {
        return (String) personalInformation.get("last_name");
    }

    private String email(Map<String, Object> personalInformation) {
        return (String) personalInformation.get("email");
    }

    private Gender gender(Map<String, Object> personalInformation) {
        String gender = (String) personalInformation.get("gender");
        if ( gender.equals("male") )
            return Gender.MALE;

        if ( gender.equals("female") )
            return Gender.FEMALE;

        throw new RuntimeException("Did not recognise gender: " + gender);
    }

    @SuppressWarnings("unchecked")
    private String location(Map<String, Object> personalInformation) {
        Map<String, Object> location = (Map<String, Object>) personalInformation.get("location");
        if ( location == null )
            return "";

        return (String) location.get("name");
    }

    private String locale(Map<String, Object> personalInformation) {
        return (String) personalInformation.get("locale");
    }

    private String ageRange(Map<String, Object> personalInformation) {
        Object ageRange = personalInformation.get("age_range");
        return converter().toJson(ageRange);
    }
}
