package dk.developer.delta.api.concepts;

import com.fasterxml.jackson.core.type.TypeReference;
import dk.developer.delta.api.Factory.Categories;
import dk.developer.testing.MockClient;
import dk.developer.testing.Result;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static dk.developer.server.Server.Status.ERROR;
import static dk.developer.testing.Truth.ASSERT;

public class CategoryValidationTest {
    private MockClient client;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
    }

    @Test
    public void shouldAcceptCategory() throws Exception {
        String category = converter.toJson(Categories.shop());
        Result result = client.to(ValidationTestService.class).with(category).post("/validate/category");
        ASSERT.that(result.status()).isEqualTo("Valid");
    }

    @Test
    public void shouldRejectCategory() throws Exception {
        String category = converter.toJson(new Category(null, "", null));
        Result result = client.to(ValidationTestService.class).with(category).post("/validate/category");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("Id was empty", "Name was empty", "Type was missing");
    }
}
