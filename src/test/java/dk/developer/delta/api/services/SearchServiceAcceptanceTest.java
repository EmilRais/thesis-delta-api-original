package dk.developer.delta.api.services;

import dk.developer.database.DatabaseFront;
import dk.developer.delta.api.concepts.CategoryType;
import dk.developer.delta.api.Application;
import dk.developer.delta.api.Factory;
import dk.developer.delta.api.Factory.Configurations;
import dk.developer.delta.api.Factory.LogEvents;
import dk.developer.delta.api.concepts.LogEvent;
import dk.developer.delta.api.concepts.User;
import dk.developer.testing.EmbeddedHibernateDatabaseTest;
import dk.developer.testing.MockClient;
import dk.developer.testing.Result;
import dk.developer.utility.Converter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static dk.developer.delta.api.Status.*;
import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Convenience.set;

public class SearchServiceAcceptanceTest extends EmbeddedHibernateDatabaseTest {
    private MockClient client;
    private Converter converter;
    private DatabaseFront database;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
        database = Application.database(embeddedDatabase("test.cfg.xml"));
    }

    @Test(enabled = false)
    public void shouldPerformPlainSearch() throws Exception {
        database.save(Configurations.someConfiguration());

        SearchService.PlainSearch search = new SearchService.PlainSearch(5, 56.1234, 10.1234, set("2", "3"));
        String json = converter.toJson(search);

        Result result = client.to(SearchService.class).with(json).post("/search/plain");
        SearchService.SearchResults searchResults = result.content(SearchService.SearchResults.class);
        ASSERT.that(searchResults.getBenefitLocations()).hasSize(5);
        ASSERT.that(searchResults.getPrimaryBenefitTriples()).hasSize(5);
        ASSERT.that(searchResults.getSecondaryBenefitTriples()).hasSize(12);
        ASSERT.that(result.status()).isEqualTo(PLAIN_SEARCH);
    }

    @Test(enabled = false)
    public void shouldPerformTextSearch() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        database.save(Configurations.someConfiguration());

        SearchService.TextSearch search = new SearchService.TextSearch(10, "Klub", 56.1234, 10.1234, set("2", "3"));
        String json = converter.toJson(search);

        Result result = client.to(SearchService.class).with(json).post("/search/text");
        SearchService.SearchResults searchResults = result.content(SearchService.SearchResults.class);
        ASSERT.that(searchResults.getBenefitLocations()).hasSize(0);
        ASSERT.that(searchResults.getPrimaryBenefitTriples()).hasSize(0);
        ASSERT.that(searchResults.getSecondaryBenefitTriples()).hasSize(0);
        ASSERT.that(result.status()).isEqualTo(TEXT_SERACH);

        LogEvent logEvent = database.load(LogEvent.class).matching("userId").with(user.getId());
        ASSERT.that(logEvent).isEqualTo(LogEvents.textSearch());
    }

    @Test(enabled = false)
    public void shouldPerformCategorySearch() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        database.save(Configurations.someConfiguration());

        SearchService.CategorySearch search = new SearchService.CategorySearch(7, "5", CategoryType.MOOD, 56.1234, 10.1234, set("2", "3"));
        String json = converter.toJson(search);

        Result result = client.to(SearchService.class).with(json).post("/search/category");
        SearchService.SearchResults searchResults = result.content(SearchService.SearchResults.class);
        ASSERT.that(searchResults.getBenefitLocations()).hasSize(13);
        ASSERT.that(searchResults.getPrimaryBenefitTriples()).hasSize(13);
        ASSERT.that(searchResults.getSecondaryBenefitTriples()).hasSize(154);
        ASSERT.that(result.status()).isEqualTo(CATEGORY_SEARCH);

        LogEvent logEvent = database.load(LogEvent.class).matching("userId").with(user.getId());
        ASSERT.that(logEvent).isEqualTo(LogEvents.categorySearch());
    }

    @Test(enabled = false)
    public void shouldPerformBenefitClubSearch() throws Exception {
        database.save(Configurations.someConfiguration());

        String json = converter.toJson(new SearchService.BenefitClubSearch("2"));
        Result result = client.to(SearchService.class).with(json).post("/search/benefit/club");
        ASSERT.that(result.status()).isEqualTo(BENEFIT_CLUB_SEARCH);

        SearchService.SearchResults searchResults = result.content(SearchService.SearchResults.class);
        ASSERT.that(searchResults.getBenefitLocations()).hasSize(2103);
        ASSERT.that(searchResults.getPrimaryBenefitTriples()).hasSize(3523);
        ASSERT.that(searchResults.getSecondaryBenefitTriples()).isEmpty();
    }
}