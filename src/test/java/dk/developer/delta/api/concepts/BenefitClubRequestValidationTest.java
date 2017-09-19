package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.core.type.TypeReference;
import dk.developer.delta.api.Factory.Requests;
import dk.developer.testing.MockClient;
import dk.developer.testing.Result;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static dk.developer.server.Server.Status.ERROR;
import static dk.developer.testing.Truth.ASSERT;

public class BenefitClubRequestValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptRequest() throws Exception {
        String request = converter.toJson(Requests.legoRequest());
        Result result = client.to(ValidationTestService.class).with(request).post("/validate/request");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldAcceptRequestWithNoOrganisationOrDescription() throws Exception {
        String request = converter.toJson(new BenefitClubRequest("NOT-GENERATED-YET", "Køleskabsklubben", "", ""));
        Result result = client.to(ValidationTestService.class).with(request).post("/validate/request");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectRequest() throws Exception {
        String feedback = converter.toJson(new BenefitClubRequest("", "", null, null));
        Result result = client.to(ValidationTestService.class).with(feedback).post("/validate/request");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("User id was empty", "Benefit club name was empty", "Benefit club organisation was missing", "Benefit club description was missing");
    }
}
