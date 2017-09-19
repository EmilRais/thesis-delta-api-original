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

public class BenefitClubValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptBenefitClub() throws Exception {
        String benefitClub = converter.toJson(Factory.BenefitClubs.matas());
        Result result = client.to(ValidationTestService.class).with(benefitClub).post("/validate/benefit/club");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectBenefitClub() throws Exception {
        String benefitClub = converter.toJson(new BenefitClub("", null, "", "", null, null));
        Result result = client.to(ValidationTestService.class).with(benefitClub).post("/validate/benefit/club");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("Name was empty", "Description was empty", "Identification was the empty string", "Image was empty", "Organisation was empty", "Location count was missing");
    }
}
