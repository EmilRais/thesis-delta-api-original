package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.Collection;
import dk.developer.database.DatabaseObject;
import dk.developer.validation.single.Email;
import dk.developer.validation.single.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Collection("")
public class User extends DatabaseObject {
    @NotEmpty(message = "Id was empty")
    private String id = "NOT-GENERATED-YET";

    @NotEmpty(message = "User id was empty")
    private String userId;

    @NotEmpty(message = "Name was empty")
    private String name;

    @NotEmpty(message = "First name was empty")
    private String firstName;

    @NotEmpty(message = "Last name was empty")
    private String lastName;

    @NotNull(message = "Email was missing")
    @Email(message = "Email was invalid")
    private String email;

    @NotNull(message = "Gender was missing")
    private Gender gender;

    @NotNull(message = "Location was missing")
    private String location;

    @NotEmpty(message = "Locale was empty")
    private String locale;

    @NotEmpty(message = "Age range was empty")
    private String ageRange;

    @NotEmpty(message = "Last login was missing")
    private String lastLogin;

    @NotNull(message = "Settings were missing")
    private UserSettings settings;

    @NotNull(message = "Subscriptions were missing")
    private Set<Subscription> subscriptions;

    @JsonCreator
    public User(@JsonProperty("userId") String userId,
                @JsonProperty("name") String name,
                @JsonProperty("firstName") String firstName,
                @JsonProperty("lastName") String lastName,
                @JsonProperty("email") String email,
                @JsonProperty("gender") Gender gender,
                @JsonProperty("location") String location,
                @JsonProperty("locale") String locale,
                @JsonProperty("ageRange") String ageRange,
                @JsonProperty("lastLogin") String lastLogin,
                @JsonProperty("settings") UserSettings settings,
                @JsonProperty("subscriptions") Set<Subscription> subscriptions) {
        this.userId = userId;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.location = location;
        this.locale = locale;
        this.ageRange = ageRange;
        this.lastLogin = lastLogin;
        this.settings = settings;
        this.subscriptions = subscriptions;
    }

    @Override
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Gender getGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }

    public String getLocale() {
        return locale;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        this.settings = settings;
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(name, user.name) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                gender == user.gender &&
                Objects.equals(location, user.location) &&
                Objects.equals(locale, user.locale) &&
                Objects.equals(ageRange, user.ageRange) &&
                Objects.equals(lastLogin, user.lastLogin) &&
                Objects.equals(settings, user.settings) &&
                Objects.equals(subscriptions, user.subscriptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, firstName, lastName, email, gender, location, locale, ageRange, lastLogin, settings, subscriptions);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", location='" + location + '\'' +
                ", locale='" + locale + '\'' +
                ", ageRange='" + ageRange + '\'' +
                ", lastLogin='" + lastLogin + '\'' +
                ", settings=" + settings +
                ", subscriptions=" + subscriptions +
                '}';
    }

    /* ### HIBERNATE ### */

    private User() {
    }

    private String get_id() {
        return id;
    }

    private void set_id(String id) {
        this.id = id;
    }

    private void setUserId(String userId) {
        this.userId = userId;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private int getGenderId() {
        return gender.ordinal() + 1;
    }

    private void setGenderId(int id) {
        gender = Gender.values()[id - 1];
    }

    private void setLocation(String location) {
        this.location = location;
    }

    private void setLocale(String locale) {
        this.locale = locale;
    }

    private void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    /* ### END OF HIBERNATE ### */
}
