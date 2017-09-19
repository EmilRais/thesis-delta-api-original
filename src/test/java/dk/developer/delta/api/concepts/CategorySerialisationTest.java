package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class CategorySerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(Category.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("CategoryInput.json");
        Category benefit = converter.fromJson(json, Category.class);

        ASSERT.that(benefit).isEqualTo(Factory.Categories.shop());
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(Factory.Categories.shop());
        String expectedJson = tool.readFilteredJsonFile("CategoryOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
