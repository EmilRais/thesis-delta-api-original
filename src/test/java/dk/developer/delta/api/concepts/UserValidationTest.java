package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.core.type.TypeReference;
import dk.developer.delta.api.Factory.Users;
import dk.developer.testing.MockClient;
import dk.developer.testing.Result;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static dk.developer.server.Server.Status.ERROR;
import static dk.developer.testing.Truth.ASSERT;

public class UserValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptUser() throws Exception {
        String user = converter.toJson(Users.thomas());
        Result result = client.to(ValidationTestService.class).with(user).post("/validate/user");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectUser() throws Exception {
        String user = converter.toJson(new User("", null, "", null, "not@email", null, null, "", null, "", null, null));
        Result result = client.to(ValidationTestService.class).with(user).post("/validate/user");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("User id was empty", "Name was empty", "First name was empty", "Last name was empty", "Email was invalid", "Gender was missing", "Location was missing", "Locale was empty", "Age range was empty", "Last login was missing", "Settings were missing", "Subscriptions were missing");
    }
}
