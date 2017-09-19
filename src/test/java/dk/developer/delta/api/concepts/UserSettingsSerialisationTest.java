package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class UserSettingsSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(UserSettings.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("UserSettingsInput.json");
        UserSettings settings = converter.fromJson(json, UserSettings.class);

        ASSERT.that(settings).isEqualTo(Factory.Settings.someSettings());
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(Factory.Settings.someSettings());
        String expectedJson = tool.readFilteredJsonFile("UserSettingsOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
