package dk.developer.delta.api;

import com.fasterxml.jackson.core.type.TypeReference;
import dk.developer.facebook.Facebook;
import dk.developer.delta.api.concepts.User;
import dk.developer.security.Credential;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;
import static java.util.stream.Collectors.toMap;

public class FacebookUserFactoryTest {
    private static final String ALL_PRESENT_USER_ID = "341422523311112";
    private static final String SOME_MISSING_USER_ID = "73241212312355";
    private User allPresentUser;
    private User someMissingUser;

    @BeforeClass(enabled = false)
    public void setUp() throws Exception {
        Facebook facebook = Application.facebook();
        UserFactory factory = FacebookUserFactory.create(facebook);
        Map<String, Credential> testUsers = testUsers(facebook);

        Credential allPublicCredential = testUsers.get(ALL_PRESENT_USER_ID);
        allPresentUser = factory.create(allPublicCredential);

        Credential hiddenBirthyearCredential = testUsers.get(SOME_MISSING_USER_ID);
        someMissingUser = factory.create(hiddenBirthyearCredential);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Credential> testUsers(Facebook facebook) {
        String url = "https://graph.facebook.com/" + facebook.getAppId() + "/accounts/test-users";

        Response response = ClientBuilder.newClient().target(url)
                .queryParam("access_token", facebook.appToken())
                .request().get();

        String json = response.readEntity(String.class);
        Map<String, Object> objectMap = converter().fromJson(json, new TypeReference<Map<String, Object>>() {
        });
        List<Map<String, String>> testUsers = (List<Map<String, String>>) objectMap.get("data");
        return testUsers.stream().collect(toMap(
                object -> object.get("id"),
                object -> new Credential(object.get("id"), object.get("access_token"))));
    }

    @Test(enabled = false)
    public void shouldSetIdToSomething() throws Exception {
        ASSERT.that(allPresentUser.getId()).isNotEmpty();
    }

    @Test(enabled = false)
    public void shouldSetUserIdProperly() throws Exception {
        ASSERT.that(allPresentUser.getUserId()).isEqualTo("109363072773850");
    }

    @Test(enabled = false)
    public void shouldSetNameProperly() throws Exception {
        ASSERT.that(allPresentUser.getName()).isEqualTo("Bob Alaakdgbfjhc Zamoreman");
    }

    @Test(enabled = false)
    public void shouldSetFirstNameProperly() throws Exception {
        ASSERT.that(allPresentUser.getFirstName()).isEqualTo("Bob");
    }

    @Test(enabled = false)
    public void shouldSetLastNameProperly() throws Exception {
        ASSERT.that(allPresentUser.getLastName()).isEqualTo("Zamoreman");
    }

    @Test(enabled = false)
    public void shouldSetEmailProperly() throws Exception {
        ASSERT.that(allPresentUser.getEmail()).isEqualTo("iaskonz_zamoreman_1451390417@tfbnw.net");
    }

    @Test(enabled = false)
    public void shouldSetLocationProperlyWhenPresent() throws Exception {
        ASSERT.that(allPresentUser.getLocation()).isEqualTo("Aarhus, Denmark");
    }

    @Test(enabled = false)
    public void shouldNullLocationWhenMissing() throws Exception {
        ASSERT.that(someMissingUser.getLocation()).isEmpty();
    }

    @Test(enabled = false)
    public void shouldSetLocaleProperly() throws Exception {
        ASSERT.that(allPresentUser.getLocale()).isEqualTo("da_DK");
    }

    @Test(enabled = false)
    public void shouldSetAgeRangeProperly() throws Exception {
        ASSERT.that(allPresentUser.getAgeRange()).isEqualTo("{\"min\":21}");
    }
}