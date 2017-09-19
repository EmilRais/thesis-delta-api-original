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

public class SubscriptionConstructorValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptSubscriptionConstructor() throws Exception {
        String subscription = converter.toJson(new SubscriptionConstructor("15", "1234", "1"));
        Result result = client.to(ValidationTestService.class).with(subscription).post("/validate/user/subscription/constructor");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectSubscriptionConstructor() throws Exception {
        String subscription = converter.toJson(new SubscriptionConstructor("", null, ""));
        Result result = client.to(ValidationTestService.class).with(subscription).post("/validate/user/subscription/constructor");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("Benefit club id was empty", "Membership number was missing", "Membership level was empty");
    }
}
