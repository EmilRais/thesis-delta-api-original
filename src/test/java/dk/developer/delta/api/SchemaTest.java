package dk.developer.delta.api;

import dk.developer.database.DatabaseFront;
import dk.developer.database.HibernateDatabase;
import dk.developer.delta.api.concepts.*;
import org.hibernate.SessionFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static dk.developer.testing.Truth.ASSERT;

public class SchemaTest {
    private static SessionFactory factory;
    private static DatabaseFront database;

    @BeforeClass(enabled = false)
    public void setUp() throws Exception {
        factory = new HibernateDatabase.SessionFactoryBuilder("production.cfg.xml").create();
        database = DatabaseFront.create(new HibernateDatabase(factory));
    }

    @AfterClass(enabled = false)
    public void tearDown() throws Exception {
        factory.close();
    }

    @Test(enabled = false)
    public void shouldMatchUserSchema() throws Exception {
        User user = database.load(User.class).matching("_id").with("54");
        ASSERT.that(user).isNotNull();
        System.out.println(user);
    }

    @Test(enabled = false)
    public void shouldMatchUserSettingsSchema() throws Exception {
        List<UserSettings> settings = database.loadAll(UserSettings.class).everything();
        ASSERT.that(settings).isNotEmpty();

        settings.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchSubscriptionSchema() throws Exception {
        List<Subscription> subscriptions = database.loadAll(Subscription.class).everything();
        ASSERT.that(subscriptions).isNotEmpty();

        subscriptions.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchUserPermissionSchema() throws Exception {
        List<UserPermission> permissions = database.loadAll(UserPermission.class).everything();
        ASSERT.that(permissions).isNotEmpty();

        permissions.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchBenefitClubSchema() throws Exception {
        List<BenefitClub> benefitClubs = database.loadAll(BenefitClub.class).everything();
        ASSERT.that(benefitClubs).isNotEmpty();

        benefitClubs.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchMembershipLevelSchema() throws Exception {
        List<MembershipLevel> membershipLevels = database.loadAll(MembershipLevel.class).everything();
        ASSERT.that(membershipLevels).isNotEmpty();

        membershipLevels.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchBenefitSchema() throws Exception {
        List<Benefit> benefits = database.loadAll(Benefit.class).everything();
        ASSERT.that(benefits).isNotEmpty();

        benefits.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchConfigurationSchema() throws Exception {
        List<Configuration> configuration = database.loadAll(Configuration.class).everything();
        ASSERT.that(configuration).hasSize(1);

        configuration.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchLogEventSchema() throws Exception {
        List<LogEvent> events = database.loadAll(LogEvent.class).everything();
        ASSERT.that(events).isNotEmpty();

        events.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchFeedbackSchema() throws Exception {
        List<Feedback> feedbacks = database.loadAll(Feedback.class).everything();
        ASSERT.that(feedbacks).isNotEmpty();

        feedbacks.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchBenefitClubRequestSchema() throws Exception {
        List<BenefitClubRequest> requests = database.loadAll(BenefitClubRequest.class).everything();
        ASSERT.that(requests).isNotEmpty();

        requests.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchIndustryCategorySchema() throws Exception {
        List<IndustryCategory> categories = database.loadAll(IndustryCategory.class).everything();
        ASSERT.that(categories).isNotEmpty();

        categories.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchInterestCategorySchema() throws Exception {
        List<InterestCategory> categories = database.loadAll(InterestCategory.class).everything();
        ASSERT.that(categories).isNotEmpty();

        categories.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchMoodCategorySchema() throws Exception {
        List<MoodCategory> categories = database.loadAll(MoodCategory.class).everything();
        ASSERT.that(categories).isNotEmpty();

        categories.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchProductCategorySchema() throws Exception {
        List<ProductCategory> categories = database.loadAll(ProductCategory.class).everything();
        ASSERT.that(categories).isNotEmpty();

        categories.forEach(System.out::println);
    }

    @Test(enabled = false)
    public void shouldMatchWeatherCategorySchema() throws Exception {
        List<WeatherCategory> categories = database.loadAll(WeatherCategory.class).everything();
        ASSERT.that(categories).isNotEmpty();

        categories.forEach(System.out::println);
    }
}
