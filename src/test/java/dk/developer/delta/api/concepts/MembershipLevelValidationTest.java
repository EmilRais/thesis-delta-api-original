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

public class MembershipLevelValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptMembershipLevel() throws Exception {
        String membershipLevel = converter.toJson(Factory.MembershipLevels.matasPremium());
        Result result = client.to(ValidationTestService.class).with(membershipLevel).post("/validate/membership/level");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectMembershipLevel() throws Exception {
        String membershipLevel = converter.toJson(new MembershipLevel("", null, "", null, ""));
        Result result = client.to(ValidationTestService.class).with(membershipLevel).post("/validate/membership/level");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("Benefit club id was empty", "Name was empty", "Condition was empty", "Sort key was missing", "Image was empty");
    }
}
