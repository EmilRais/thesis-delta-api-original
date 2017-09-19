package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.core.type.TypeReference;
import dk.developer.delta.api.Factory.LogEvents;
import dk.developer.testing.MockClient;
import dk.developer.testing.Result;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static dk.developer.server.Server.Status.ERROR;
import static dk.developer.testing.Truth.ASSERT;

public class LogEventValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptLogEvent() throws Exception {
        String event = converter.toJson(LogEvents.textSearch());
        Result result = client.to(ValidationTestService.class).with(event).post("/validate/log/event");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectLogEvent() throws Exception {
        String event = converter.toJson(new LogEvent("", null, "", null));
        Result result = client.to(ValidationTestService.class).with(event).post("/validate/log/event");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("User id was empty", "Log event type was missing", "Value was empty");
    }
}
