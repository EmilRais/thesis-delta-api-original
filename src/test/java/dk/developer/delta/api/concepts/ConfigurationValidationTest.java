package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.core.type.TypeReference;
import dk.developer.delta.api.Factory.Configurations;
import dk.developer.testing.MockClient;
import dk.developer.testing.Result;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static dk.developer.server.Server.Status.ERROR;
import static dk.developer.testing.Truth.ASSERT;

public class ConfigurationValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptConfiguration() throws Exception {
        String configuration = converter.toJson(Configurations.someConfiguration());
        Result result = client.to(ValidationTestService.class).with(configuration).post("/validate/configuration");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectConfiguration() throws Exception {
        String configuration = converter.toJson(new Configuration("", null, null, "", ""));
        Result result = client.to(ValidationTestService.class).with(configuration).post("/validate/configuration");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("App version was empty", "Default only own benefits setting was missing", "Default number of locations setting was missing", "Elastic search url was empty", "Terms and conditions were empty");
    }
}
