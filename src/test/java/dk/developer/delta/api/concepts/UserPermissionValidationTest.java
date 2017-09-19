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

public class UserPermissionValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptPermission() throws Exception {
        String permission = converter.toJson(Factory.Permissions.thomasPermission());
        Result result = client.to(ValidationTestService.class).with(permission).post("/validate/user/permission");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectPermission() throws Exception {
        String permission = converter.toJson(new UserPermission("", null, "", null));
        Result result = client.to(ValidationTestService.class).with(permission).post("/validate/user/permission");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("Facebook user id was empty", "Permission id was missing", "Date was empty", "Allowed was missing");
    }
}
