package dk.developer.delta.api.concepts;

import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class SubscriptionConstructorSerialisationTest {
    private JsonTool tool;
    private Converter converter;
    private SubscriptionConstructor constructor;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(SubscriptionConstructor.class);
        converter = converter();
        constructor = new SubscriptionConstructor("5", "1234 abc", "14");
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("SubscriptionConstructorInput.json");
        SubscriptionConstructor subscription = converter.fromJson(json, SubscriptionConstructor.class);

        ASSERT.that(subscription).isEqualTo(constructor);
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(constructor);
        String expectedJson = tool.readFilteredJsonFile("SubscriptionConstructorOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
