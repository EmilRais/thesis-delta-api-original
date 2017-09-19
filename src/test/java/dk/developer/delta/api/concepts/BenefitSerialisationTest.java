package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory.Benefits;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class BenefitSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(Benefit.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("BenefitInput.json");
        Benefit benefit = converter.fromJson(json, Benefit.class);

        ASSERT.that(benefit).isEqualTo(Benefits.cheapPerfumes());
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(Benefits.cheapPerfumes());
        String expectedJson = tool.readFilteredJsonFile("BenefitOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
