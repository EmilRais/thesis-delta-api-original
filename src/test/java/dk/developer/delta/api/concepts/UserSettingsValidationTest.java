package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.core.type.TypeReference;
import dk.developer.delta.api.Factory;
import dk.developer.testing.MockClient;
import dk.developer.testing.Result;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static dk.developer.server.Server.Status.ERROR;
import static dk.developer.testing.Truth.ASSERT;

public class UserSettingsValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptUserSettings() throws Exception {
        String settings = converter.toJson(Factory.Settings.someSettings());
        Result result = client.to(ValidationTestService.class).with(settings).post("/validate/user/settings");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectUserSettings() throws Exception {
        String settings = converter.toJson(new UserSettings(null, null));
        Result result = client.to(ValidationTestService.class).with(settings).post("/validate/user/settings");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("Only own benefits was missing", "Number of locations was missing");
    }
}
