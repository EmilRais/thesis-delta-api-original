package dk.developer.delta.api.concepts;

import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class UserSettingsConstructorSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(UserSettingsConstructor.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("UserSettingsConstructorInput.json");
        UserSettingsConstructor settings = converter.fromJson(json, UserSettingsConstructor.class);

        ASSERT.that(settings).isEqualTo(new UserSettingsConstructor(true, 20, false));
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(new UserSettingsConstructor(true, 20, false));
        String expectedJson = tool.readFilteredJsonFile("UserSettingsConstructorOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
