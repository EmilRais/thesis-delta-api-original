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

public class SubscriptionValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptSubscription() throws Exception {
        String subscription = converter.toJson(Factory.Subscriptions.matasSubscription());
        Result result = client.to(ValidationTestService.class).with(subscription).post("/validate/user/subscription");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectSubscription() throws Exception {
        String subscription = converter.toJson(new Subscription("", null, null, null, ""));
        Result result = client.to(ValidationTestService.class).with(subscription).post("/validate/user/subscription");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("User id was empty", "Benefit club id was empty", "Membership number was missing", "Membership level was empty", "Date was empty");
    }
}
