package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class BenefitLocationSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(BenefitLocationSerialisationTest.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("BenefitLocationInput.json");
        BenefitLocation location = converter.fromJson(json, BenefitLocation.class);

        ASSERT.that(location).isEqualTo(Factory.BenefitLocations.matasLocation());
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(Factory.BenefitLocations.matasLocation());
        String expectedJson = tool.readFilteredJsonFile("BenefitLocationOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}