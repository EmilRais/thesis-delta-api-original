package dk.developer.delta.api;

import dk.developer.database.DatabaseFront;
import dk.developer.security.Credential;
import dk.developer.server.Application;
import dk.developer.delta.api.concepts.User;
import dk.developer.testing.MockClient;
import dk.developer.testing.Result;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.database.MemoryDatabase.memoryDatabase;
import static dk.developer.server.Server.Status.INVALID_CREDENTIAL;
import static dk.developer.utility.Converter.converter;

public class LoginFilterTest {
    private MockClient client;
    private DatabaseFront database;

    @BeforeMethod
    public void setUp() throws Exception {
        database = Application.database(memoryDatabase());
        client = MockClient.create(LoginFilter.class);

        String credential = converter().toJson(new Credential("thomas", "token"));
        client.setAuthorisationHeader(credential);
    }

    @Test
    public void shouldFailIfUserDoesNotExist() throws Exception {
        Result result = client.from(FilterTestService.class).get("/filter/login");
        ASSERT.that(result.content()).isEqualTo("Your credential is invalid");
        ASSERT.that(result.status()).isEqualTo(INVALID_CREDENTIAL);
    }

    @Test
    public void shouldFailIfUserExistsButCredentialIsInvalid() throws Exception {
        database.save(new User("thomas", null, null, null, null, null, null, null, null, null, null, null));
        Application.security(credential -> false);

        Result result = client.from(FilterTestService.class).get("/filter/login");
        ASSERT.that(result.content()).isEqualTo("Your credential is invalid");
        ASSERT.that(result.status()).isEqualTo(INVALID_CREDENTIAL);
    }

    @Test
    public void shouldSucceedIfExistsAndCredentialIsValid() throws Exception {
        database.save(new User("thomas", null, null, null, null, null, null, null, null, null, null, null));
        Application.security(credential -> true);

        Result result = client.from(FilterTestService.class).get("/filter/login");
        ASSERT.that(result.content()).isEqualTo("Was allowed to pass");
        ASSERT.that(result.status()).isEqualTo("Test");
    }
}