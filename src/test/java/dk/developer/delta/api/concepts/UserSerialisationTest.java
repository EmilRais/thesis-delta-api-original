package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory;
import dk.developer.utility.Convenience;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class UserSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(User.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("UserInput.json");
        User user = converter.fromJson(json, User.class);

        User expectedUser = Factory.Users.thomas();
        expectedUser.setSubscriptions(Convenience.set(Factory.Subscriptions.matasSubscription()));
        ASSERT.that(user).isEqualTo(expectedUser);
    }

    @Test
    public void toJson() throws Exception {
        User user = Factory.Users.thomas();
        user.setSubscriptions(Convenience.set(Factory.Subscriptions.matasSubscription()));

        String json = converter.toJson(user);
        String expectedJson = tool.readFilteredJsonFile("UserOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
