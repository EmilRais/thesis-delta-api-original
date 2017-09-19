package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory.Requests;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class BenefitClubRequestSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(BenefitClubRequest.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("BenefitClubRequestInput.json");
        BenefitClubRequest benefitClubRequest = converter.fromJson(json, BenefitClubRequest.class);

        ASSERT.that(benefitClubRequest).isEqualTo(Requests.legoRequest());
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(Requests.legoRequest());
        String expectedJson = tool.readFilteredJsonFile("BenefitClubRequestOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
