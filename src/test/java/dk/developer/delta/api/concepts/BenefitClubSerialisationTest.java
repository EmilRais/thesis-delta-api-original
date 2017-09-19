package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class BenefitClubSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(BenefitClub.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("BenefitClubInput.json");
        BenefitClub club = converter.fromJson(json, BenefitClub.class);

        ASSERT.that(club).isEqualTo(Factory.BenefitClubs.matas());
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(Factory.BenefitClubs.matas());
        String expectedJson = tool.readFilteredJsonFile("BenefitClubOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
