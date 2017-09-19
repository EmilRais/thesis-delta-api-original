package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class SubscriptionSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(Subscription.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("SubscriptionInput.json");
        Subscription subscription = converter.fromJson(json, Subscription.class);

        ASSERT.that(subscription).isEqualTo(Factory.Subscriptions.matasSubscription());
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(Factory.Subscriptions.matasSubscription());
        String expectedJson = tool.readFilteredJsonFile("SubscriptionOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
