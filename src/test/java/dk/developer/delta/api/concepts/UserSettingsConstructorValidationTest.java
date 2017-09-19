package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.core.type.TypeReference;
import dk.developer.testing.MockClient;
import dk.developer.testing.Result;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static dk.developer.server.Server.Status.ERROR;
import static dk.developer.testing.Truth.ASSERT;

public class UserSettingsConstructorValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptUserSettingsConstructor() throws Exception {
        String settings = converter.toJson(new UserSettingsConstructor(true, 1, true));
        Result result = client.to(ValidationTestService.class).with(settings).post("/validate/user/settings/constructor");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectUserSettingsConstructor() throws Exception {
        String settings = converter.toJson(new UserSettingsConstructor(null, null, null));
        Result result = client.to(ValidationTestService.class).with(settings).post("/validate/user/settings/constructor");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("Only own benefits was missing", "Number of locations was missing");
    }
}
