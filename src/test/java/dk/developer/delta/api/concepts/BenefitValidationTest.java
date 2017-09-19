package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.core.type.TypeReference;
import dk.developer.delta.api.Factory.Benefits;
import dk.developer.testing.MockClient;
import dk.developer.testing.Result;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static dk.developer.server.Server.Status.ERROR;
import static dk.developer.testing.Truth.ASSERT;

public class BenefitValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptBenefit() throws Exception {
        String benefit = converter.toJson(Benefits.cheapPerfumes());
        Result result = client.to(ValidationTestService.class).with(benefit).post("/validate/benefit");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectBenefit() throws Exception {
        String benefit = converter.toJson(new Benefit(null, "", null, ""));
        Result result = client.to(ValidationTestService.class).with(benefit).post("/validate/benefit");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("Benefit id was empty", "Membership level id was empty", "Description was empty", "Condition was the empty string");
    }
}
