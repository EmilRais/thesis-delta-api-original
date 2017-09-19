package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class LogEventSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(LogEvent.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("LogEventInput.json");
        LogEvent configuration = converter.fromJson(json, LogEvent.class);

        ASSERT.that(configuration).isEqualTo(Factory.LogEvents.textSearch());
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(Factory.LogEvents.textSearch());
        String expectedJson = tool.readFilteredJsonFile("LogEventOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
