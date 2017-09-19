package dk.developer.delta.api.concepts;

import dk.developer.delta.api.Factory;
import dk.developer.testing.JsonTool;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Converter.converter;

public class FeedbackSerialisationTest {
    private JsonTool tool;
    private Converter converter;

    @BeforeClass
    public void setUp() throws Exception {
        tool = new JsonTool(Feedback.class);
        converter = converter();
    }

    @Test
    public void fromJson() throws Exception {
        String json = tool.readFilteredJsonFile("FeedbackInput.json");
        Feedback feedback = converter.fromJson(json, Feedback.class);

        ASSERT.that(feedback).isEqualTo(Factory.Feedbacks.positiveFeedback());
    }

    @Test
    public void toJson() throws Exception {
        String json = converter.toJson(Factory.Feedbacks.positiveFeedback());
        String expectedJson = tool.readFilteredJsonFile("FeedbackOutput.json");

        ASSERT.that(json).isEqualTo(expectedJson);
    }
}
