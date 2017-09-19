package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class ConfigurationSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(Configuration.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("ConfigurationInput.json");
        Configuration configuration = converter.fromJson(json, Configuration.class);

        ASSERT.that(configuration).isEqualTo(Factory.Configurations.someConfiguration());
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(Factory.Configurations.someConfiguration());
        String expectedJson = tool.readFilteredJsonFile("ConfigurationOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
