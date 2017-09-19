package dk.developer.delta.api.services;

import dk.developer.database.DatabaseFront;
import dk.developer.delta.api.Factory;
import dk.developer.delta.api.Application;
import dk.developer.delta.api.concepts.BenefitClub;
import dk.developer.delta.api.concepts.Configuration;
import dk.developer.delta.api.concepts.LogEvent;
import dk.developer.delta.api.concepts.User;
import dk.developer.delta.api.services.BenefitService.Container;
import dk.developer.delta.api.services.BenefitService.VisitPayload;
import dk.developer.testing.EmbeddedHibernateDatabaseTest;
import dk.developer.testing.MockClient;
import dk.developer.testing.Result;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static dk.developer.delta.api.Status.LOADED_CONFIGURATION;
import static dk.developer.delta.api.Status.LOADED_EVERYTHING;
import static dk.developer.delta.api.Status.LOGGED_BENEFIT_PAIR_VISIT;
import static dk.developer.delta.api.concepts.LogEventType.NAVIGATED_TO_BENEFIT_PAIR;
import static dk.developer.server.Server.Status.ERROR;
import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Convenience.list;

public class BenefitServiceAcceptanceTest extends EmbeddedHibernateDatabaseTest {
    private MockClient client;
    private DatabaseFront database;
    private Converter converter;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
        database = Application.database(embeddedDatabase("test.cfg.xml"));
    }

    @Test
    public void shouldLoadOneOfEverything() throws Exception {
        database.save(Factory.BenefitClubs.matas());
        database.save(Factory.BenefitClubs.adidas());
        database.save(Factory.MembershipLevels.matasPremium());
        database.save(Factory.Benefits.cheapPerfumes());
        database.save(Factory.Categories.industryShop());
        database.save(Factory.Categories.interestGolf());
        database.save(Factory.Categories.moodRomance());
        database.save(Factory.Categories.productToys());
        database.save(Factory.Categories.weatherRain());

        Result result = client.from(BenefitService.class).get("/benefit/all");
        ASSERT.that(result.status()).isEqualTo(LOADED_EVERYTHING);

        Container container = result.content(Container.class);
        ASSERT.that(container.getBenefitClubs()).containsExactly(Factory.BenefitClubs.matas(), Factory.BenefitClubs.adidas());
        ASSERT.that(container.getMembershipLevels()).containsExactly(Factory.MembershipLevels.matasPremium());
        ASSERT.that(container.getBenefits()).containsExactly(Factory.Benefits.cheapPerfumes());

        ASSERT.that(container.getCategories()).containsExactly(
                Factory.Categories.shop(), Factory.Categories.golf(), Factory.Categories.romance(), Factory.Categories.toys(), Factory.Categories.rain()
        );
    }

    @Test
    public void shouldLoadConfiguration() throws Exception {
        database.save(Factory.Configurations.someConfiguration());
        Result result = client.from(BenefitService.class).get("/benefit/configuration");
        ASSERT.that(result.status()).isEqualTo(LOADED_CONFIGURATION);
        ASSERT.that(result.content(Configuration.class)).isEqualTo(Factory.Configurations.someConfiguration());
    }

    @Test
    public void shouldFailLoggingBenefitPairVisitWithoutPayload() throws Exception {
        Result result = client.to(BenefitService.class).with("").post("/benefit/visit/benefit/pair");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content()).isEqualTo(list("Payload was missing"));
    }

    @Test
    public void shouldFailLoggingBenefitPairVisitIfBenefitClubDoesNotExist() throws Exception {
        String json = converter.toJson(new VisitPayload("some-id", "no-benefit-club-has-this-id"));
        Result result = client.to(BenefitService.class).with(json).post("/benefit/visit/benefit/pair");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content()).isEqualTo(list("Benefit club did not exist"));
    }

    @Test
    public void shouldLogBenefitPairVisit() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        BenefitClub benefitClub = Factory.BenefitClubs.matas();
        database.save(benefitClub);
        String json = converter.toJson(new VisitPayload("some-id", benefitClub.getId()));
        Result result = client.to(BenefitService.class).with(json).post("/benefit/visit/benefit/pair");
        ASSERT.that(result.status()).isEqualTo(LOGGED_BENEFIT_PAIR_VISIT);
        ASSERT.that(result.content()).isNull();

        LogEvent event = database.load(LogEvent.class).matching("userId").with(user.getId());
        String expectedMessage = "(BenefitClub: 1, BenefitLocation: some-id)";
        ASSERT.that(event).isEqualTo(new LogEvent(null, NAVIGATED_TO_BENEFIT_PAIR, expectedMessage, null));
    }
}
