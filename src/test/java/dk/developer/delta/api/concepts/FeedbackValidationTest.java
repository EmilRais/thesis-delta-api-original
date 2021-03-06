package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.core.type.TypeReference;
import dk.developer.delta.api.Factory;
import dk.developer.testing.MockClient;
import dk.developer.testing.Result;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static dk.developer.server.Server.Status.ERROR;
import static dk.developer.testing.Truth.ASSERT;

public class FeedbackValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptFeedback() throws Exception {
        String feedback = converter.toJson(Factory.Feedbacks.positiveFeedback());
        Result result = client.to(ValidationTestService.class).with(feedback).post("/validate/feedback");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectFeedback() throws Exception {
        String feedback = converter.toJson(new Feedback("", null));
        Result result = client.to(ValidationTestService.class).with(feedback).post("/validate/feedback");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("User id was empty", "Message was empty");
    }
}
