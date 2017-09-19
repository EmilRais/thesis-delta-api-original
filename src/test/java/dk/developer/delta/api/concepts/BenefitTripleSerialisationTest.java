package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class BenefitTripleSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(BenefitTripleSerialisationTest.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("BenefitTripleInput.json");
        BenefitTriple benefitTriple = converter.fromJson(json, BenefitTriple.class);

        ASSERT.that(benefitTriple).isEqualTo(Factory.BenefitPairs.matasPerfumePair());
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(Factory.BenefitPairs.matasPerfumePair());
        String expectedJson = tool.readFilteredJsonFile("BenefitTripleOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}