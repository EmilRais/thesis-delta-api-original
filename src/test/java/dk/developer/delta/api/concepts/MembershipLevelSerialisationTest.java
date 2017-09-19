package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class MembershipLevelSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(MembershipLevel.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("MembershipLevelInput.json");
        MembershipLevel level = converter.fromJson(json, MembershipLevel.class);

        ASSERT.that(level).isEqualTo(Factory.MembershipLevels.matasPremium());
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(Factory.MembershipLevels.matasPremium());
        String expectedJson = tool.readFilteredJsonFile("MembershipLevelOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
