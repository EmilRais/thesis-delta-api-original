package dk.developer.delta.api.services;

import com.fasterxml.jackson.core.type.TypeReference;
import dk.developer.database.DatabaseFront;
import dk.developer.delta.api.Application;
import dk.developer.delta.api.Factory;
import dk.developer.delta.api.Mailer;
import dk.developer.delta.api.Status;
import dk.developer.delta.api.concepts.*;
import dk.developer.testing.EmbeddedHibernateDatabaseTest;
import dk.developer.testing.MockClient;
import dk.developer.testing.Result;
import dk.developer.utility.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static dk.developer.server.Server.Status.ERROR;
import static dk.developer.server.Server.Status.INVALID_CREDENTIAL;
import static dk.developer.testing.Truth.ASSERT;
import static dk.developer.utility.Convenience.list;

public class UserServiceAcceptanceTest extends EmbeddedHibernateDatabaseTest {
    private MockClient client;
    private Converter converter;
    private DatabaseFront database;

    @BeforeMethod
    public void setUp() throws Exception {
        client = MockClient.create();
        converter = Converter.converter();
        database = Application.database(embeddedDatabase("test.cfg.xml"));
        Application.mailer(new Mailer() {
            @Override
            public MailStatus feedbackMail(String message, String userId, String name) {
                return MailStatus.SENT;
            }

            @Override
            public MailStatus proposalMail(String message, String userId, String userEmail, String userName, String name) {
                return MailStatus.SENT;
            }
        });
        Application.security(credential -> true);
    }

    @Test
    public void shouldReportInvalidCredentialLoggingIn() throws Exception {
        Application.security(credential -> false);
        Result result = client.to(UserService.class).with("").post("/user/login");

        ASSERT.that(result.status()).isEqualTo(INVALID_CREDENTIAL);
        ASSERT.that(result.content()).isEqualTo("Not a valid Facebook user");
    }

    @Test
    public void shouldCreateNewUser() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        Application.userFactory(someCredential -> Factory.Users.thomas());
        Application.timestamper("2016-01-12 20:00:00.000"::toString);

        UserSettings settings = Factory.Users.thomas().getSettings();
        database.save(new Configuration("1.0", settings.getOnlyOwnBenefits(), settings.getNumberOfLocations(), "some-search-url", "some-terms-and-conditions"));

        Result result = client.to(UserService.class).with("").post("/user/login");
        ASSERT.that(result.status()).isEqualTo(Status.NEW_USER_LOGGED_IN);

        User expectedUser = Factory.Users.thomas();
        expectedUser.setLastLogin("2016-01-12 20:00:00.000");

        User storedUser = database.load(User.class).matching("userId").with(Factory.Credentials.thomas().getUserId());
        ASSERT.that(storedUser).isEqualTo(expectedUser);
    }

    @Test
    public void shouldLoginExistingUser() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        Application.timestamper("2016-01-12 20:00:00.000"::toString);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        Result result = client.to(UserService.class).with("").post("/user/login");
        ASSERT.that(result.status()).isEqualTo(Status.EXISTING_USER_LOGGED_IN);

        User expectedUser = Factory.Users.thomas();
        expectedUser.setLastLogin("2016-01-12 20:00:00.000");

        User storedUser = database.load(User.class).matching("userId").with(Factory.Credentials.thomas().getUserId());
        ASSERT.that(storedUser).isEqualTo(expectedUser);
    }

    @Test
    public void shouldLoadUser() throws Exception {
        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        Result result = client.from(UserService.class).get("/user/");
        ASSERT.that(result.status()).isEqualTo(Status.LOADED_USER);
        ASSERT.that(result.content(User.class)).isEqualTo(user);
    }

    @Test
    public void shouldRefreshLogin() throws Exception {
        Application.timestamper("2016-01-12 20:00:00.000"::toString);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        Result result = client.to(UserService.class).with("").post("/user/login/refresh");
        ASSERT.that(result.status()).isEqualTo(Status.EXISTING_USER_LOGGED_IN);

        User storedUser = database.load(User.class).matching("_id").with(user.getId());
        ASSERT.that(storedUser.getLastLogin()).isEqualTo("2016-01-12 20:00:00.000");
    }

    @Test
    public void shouldReportInvalidMessageSubmittingFeedback() throws Exception {
        Result result = client.to(UserService.class).with("").post("/user/feedback");

        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content()).isEqualTo(list("Message was empty"));
    }

    @Test
    public void shouldSubmitFeedback() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        String message = Factory.Feedbacks.positiveFeedback().getMessage();
        Result result = client.to(UserService.class).with(message).post("/user/feedback");
        ASSERT.that(result.status()).isEqualTo(Status.FEEDBACK_SUBMITTED);

        Feedback feedback = database.load(Feedback.class).matching("userId").with(user.getId());
        ASSERT.that(feedback).isNotNull();
        ASSERT.that(feedback.getId()).isEqualTo("1");
        ASSERT.that(feedback.getUserId()).isEqualTo(user.getId());
        ASSERT.that(feedback.getMessage()).isEqualTo(message);
    }

    @Test
    public void shouldReportInvalidSubscriptionConstructorSubmittingSubscription() throws Exception {
        Result result = client.to(UserService.class).with("").post("/user/subscribe");

        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content()).isEqualTo(list("Subscription was missing"));
    }

    @Test
    public void shouldFailSubscribingToNonExistingBenefitClub() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        String constructor = converter.toJson(new SubscriptionConstructor("1", "1234", "premium"));
        Result result = client.to(UserService.class).with(constructor).post("/user/subscribe");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content()).isEqualTo(list("Benefit club does not exist"));
    }

    @Test
    public void shouldFailSubmittingExistingSubscription() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        database.save(Factory.BenefitClubs.adidas());
        database.save(Factory.BenefitClubs.matas());

        database.save(new Subscription("1", "1", "1234", "premium", "2016-01-12 18:00:00.000"));
        database.save(new Subscription("1", "2", "1234", "premium", "2016-01-12 18:00:00.000"));

        String constructor = converter.toJson(new SubscriptionConstructor("1", "1234", "premium"));
        Result result = client.to(UserService.class).with(constructor).post("/user/subscribe");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content()).isEqualTo(list("User has already subscribed to that benefit club"));
    }

    @Test
    public void shouldSubmitSubscription() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        database.save(Factory.BenefitClubs.adidas());
        database.save(Factory.BenefitClubs.matas());

        database.save(new Subscription("1", "1", "1234", "premium", "2016-01-12 18:00:00.000"));

        Application.timestamper("2016-01-12 20:00:00.000"::toString);

        String constructor = converter.toJson(new SubscriptionConstructor("2", "1234", "premium"));
        Result result = client.to(UserService.class).with(constructor).post("/user/subscribe");
        ASSERT.that(result.status()).isEqualTo(Status.SUBSCRIPTION_SUBMITTED);

        Subscription loadedSubscription = database.load(Subscription.class).matching("benefitClubId").with("2");
        assertSubscriptionContains(loadedSubscription, "2", user.getId(), "2", "1234", "premium", "2016-01-12 20:00:00.000");

        User returnedUser = result.content(User.class);
        ASSERT.that(returnedUser.getSubscriptions()).hasSize(2);
        ASSERT.that(returnedUser.getSubscriptions()).contains(loadedSubscription);
    }

    @Test
    public void shouldUpdateUserSettings() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        String constructor = converter.toJson(new UserSettingsConstructor(false, 30, false));
        Result result = client.to(UserService.class).with(constructor).post("/user/settings");

        ASSERT.that(result.status()).isEqualTo(Status.SETTINGS_UPDATED);

        User expectedUser = Factory.Users.thomas();
        expectedUser.setSettings(new UserSettings(false, 30));
        ASSERT.that(result.content(User.class)).isEqualTo(expectedUser);

        User storedUser = database.load(User.class).matching("userId").with(Factory.Credentials.thomas().getUserId());
        ASSERT.that(storedUser).isEqualTo(expectedUser);
    }

    private void assertSubscriptionContains(Subscription subscription, String id, String userId, String benefitClubId, String
            membershipNumber, String membershipLevel, String date) {
        ASSERT.that(subscription).isNotNull();
        ASSERT.that(subscription.getId()).isEqualTo(id);
        ASSERT.that(subscription.getUserId()).isEqualTo(userId);
        ASSERT.that(subscription.getBenefitClubId()).isEqualTo(benefitClubId);
        ASSERT.that(subscription.getMembershipNumber()).isEqualTo(membershipNumber);
        ASSERT.that(subscription.getMembershipLevel()).isEqualTo(membershipLevel);
        ASSERT.that(subscription.getDate()).isEqualTo(date);
    }

    @Test
    public void shouldReportInvalidCredentialCheckingPermissionStatus() throws Exception {
        Application.security(credential -> false);
        Result result = client.from(UserService.class).get("/user/permission/status");

        ASSERT.that(result.status()).isEqualTo(INVALID_CREDENTIAL);
        ASSERT.that(result.content()).isEqualTo("Not a valid Facebook user");
    }

    @Test
    public void shouldReportUserHasNotGivenPermissionIfNoPermission() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        Result result = client.from(UserService.class).get("/user/permission/status");
        ASSERT.that(result.status()).isEqualTo(Status.USER_HAS_NOT_GIVEN_PERMISSION);
    }

    @Test
    public void shouldReportUserHasNotGivenPermissionIfDeclinedPermission() throws Exception {
        String credential = converter.toJson(Factory.Credentials.joseph());
        client.setAuthorisationHeader(credential);

        database.save(Factory.Permissions.josephPermission());

        Result result = client.from(UserService.class).get("/user/permission/status");
        ASSERT.that(result.status()).isEqualTo(Status.USER_HAS_NOT_GIVEN_PERMISSION);
    }

    @Test
    public void shouldReportUserHasGivenPermission() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        database.save(Factory.Permissions.thomasPermission());
        database.save(Factory.Permissions.josephPermission());

        Result result = client.from(UserService.class).get("/user/permission/status");
        ASSERT.that(result.status()).isEqualTo(Status.USER_HAS_GIVEN_PERMISSION);
    }

    @Test
    public void shouldFailUpdatingPermissionIfInvalidCredential() throws Exception {
        Application.security(credential -> false);
        String payload = converter.toJson(new UserService.PermissionPayload(1, true));
        Result result = client.to(UserService.class).with(payload).post("/user/permission");

        ASSERT.that(result.status()).isEqualTo(INVALID_CREDENTIAL);
        ASSERT.that(result.content()).isEqualTo("Not a valid Facebook user");
    }

    @Test
    public void shouldFailUpdatingPermissionIfNullPayload() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        Result result = client.to(UserService.class).with("").post("/user/permission");
        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content()).isEqualTo(list("Payload was missing"));
    }

    @Test
    public void shouldFailUpdatingPermissionIfInvalidPayload() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        String payload = converter.toJson(new UserService.PermissionPayload(null, null));
        Result result = client.to(UserService.class).with(payload).post("/user/permission");
        ASSERT.that(result.status()).isEqualTo(ERROR);

        ASSERT.that(result.content(new TypeReference<List<String>>() {}))
                .containsExactly("Allowed was missing", "Permission id was missing");
    }

    @Test
    public void shouldCreateNewPermissionIfPermissionHasNotBeenDecidedBefore() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        Application.timestamper(() -> "2016-01-12 16:00:00.000");

        String payload = converter.toJson(new UserService.PermissionPayload(1, true));
        Result result = client.to(UserService.class).with(payload).post("/user/permission");
        ASSERT.that(result.status()).isEqualTo(Status.USER_HAS_UPDATED_PERMISSION);

        UserPermission permission = database.load(UserPermission.class).matching("facebookUserId").with(Factory.Credentials.thomas().getUserId());
        ASSERT.that(permission).isEqualTo(Factory.Permissions.thomasPermission());
    }

    @Test
    public void shouldUpdatePermissionIfPermissionHasAlreadyBeenDecided() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        database.save(Factory.Permissions.thomasPermission());

        Application.timestamper(() -> "2016-01-12 17:00:00.000");

        String payload = converter.toJson(new UserService.PermissionPayload(1, false));
        Result result = client.to(UserService.class).with(payload).post("/user/permission");
        ASSERT.that(result.status()).isEqualTo(Status.USER_HAS_UPDATED_PERMISSION);

        UserPermission permission = database.load(UserPermission.class).matching("facebookUserId").with(Factory.Credentials.thomas().getUserId());
        ASSERT.that(permission.getDate()).isEqualTo("2016-01-12 17:00:00.000");
        ASSERT.that(permission.getAllowed()).isFalse();
    }

    @Test
    public void shouldFailSubmittingNullBenefitClubRequest() throws Exception {
        String request = "";
        Result result = client.to(UserService.class).with(request).post("/user/request");

        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content()).isEqualTo(list("Benefit club request was missing"));
    }

    @Test
    public void shouldFailSubmittingInvalidBenefitClubRequest() throws Exception {
        String request = converter.toJson(new BenefitClubRequest("NOT-GENERATED-YET", "", "", ""));
        Result result = client.to(UserService.class).with(request).post("/user/request");

        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content()).isEqualTo(list("Benefit club name was empty"));
    }

    @Test
    public void shouldFailSubmittingBenefitClubRequestFromOtherUser() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        String request = converter.toJson(Factory.Requests.legoRequest());
        Result result = client.to(UserService.class).with(request).post("/user/request");

        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content()).isEqualTo(list("The logged in user did not issue the request"));
    }

    @Test
    public void shouldSubmitValidBenefitClubRequest() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        BenefitClubRequest benefitClubRequest = Factory.Requests.legoRequest();
        benefitClubRequest.setUserId(user.getId());
        String request = converter.toJson(benefitClubRequest);
        Result result = client.to(UserService.class).with(request).post("/user/request");

        ASSERT.that(result.status()).isEqualTo(Status.REQUEST_SUBMITTED);

        BenefitClubRequest storedRequest = database.load(BenefitClubRequest.class).matching("userId").with(user.getId());
        ASSERT.that(storedRequest).isEqualTo(benefitClubRequest);
    }

    @Test
    public void shouldFailUpdatingSubscriptionToNonExistingBenefitClub() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        BenefitClub matas = Factory.BenefitClubs.matas();
        database.save(matas);
        Subscription subscription = new Subscription(user.getId(), matas.getId(), "1234", "premium", "2016-01-12 18:00:00.000");
        database.save(subscription);

        subscription.setBenefitClubId("9");
        String updatedSubscription = converter.toJson(subscription);
        Result result = client.to(UserService.class).with(updatedSubscription).post("/user/subscribe/update");

        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content()).isEqualTo(list("Benefit club does not exist"));
    }

    @Test
    public void shouldFailUpdatingAnotherUsersSubscription() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User thomas = Factory.Users.thomas();
        database.save(thomas.getSettings());
        database.save(thomas);

        User peter = Factory.Users.peter();
        database.save(peter.getSettings());
        database.save(peter);

        BenefitClub matas = Factory.BenefitClubs.matas();
        database.save(matas);
        Subscription subscription = new Subscription(peter.getId(), matas.getId(), "1234", "premium", "2016-01-12 18:00:00.000");
        database.save(subscription);

        String updatedSubscription = converter.toJson(subscription);
        Result result = client.to(UserService.class).with(updatedSubscription).post("/user/subscribe/update");

        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content()).isEqualTo(list("A user can only update own subscriptions"));
    }

    @Test
    public void shouldUpdateSubscription() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        BenefitClub matas = Factory.BenefitClubs.matas();
        database.save(matas);
        Subscription subscription = new Subscription(user.getId(), matas.getId(), "1234", "premium", "2016-01-12 18:00:00.000");
        database.save(subscription);

        subscription.setMembershipNumber("4321");
        subscription.setMembershipLevel("silver");

        String updatedSubscription = converter.toJson(subscription);
        Result result = client.to(UserService.class).with(updatedSubscription).post("/user/subscribe/update");

        ASSERT.that(result.status()).isEqualTo(Status.SUBSCRIPTION_UPDATED);

        Subscription loadedSubscription = database.load(Subscription.class).matching("benefitClubId").with(subscription.getBenefitClubId());
        ASSERT.that(loadedSubscription).isEqualTo(subscription);

        User returnedUser = result.content(User.class);
        ASSERT.that(returnedUser.getSubscriptions()).hasSize(1);
        ASSERT.that(returnedUser.getSubscriptions()).contains(loadedSubscription);

        User storedUser = database.load(User.class).matching("_id").with(returnedUser.getId());
        ASSERT.that(storedUser).isEqualTo(returnedUser);
    }

    @Test
    public void shouldFailDeletingAnotherUsersSubscription() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User thomas = Factory.Users.thomas();
        database.save(thomas.getSettings());
        database.save(thomas);

        User peter = Factory.Users.peter();
        database.save(peter.getSettings());
        database.save(peter);

        BenefitClub matas = Factory.BenefitClubs.matas();
        database.save(matas);
        Subscription subscription = new Subscription(peter.getId(), matas.getId(), "1234", "premium", "2016-01-12 18:00:00.000");
        database.save(subscription);

        Result result = client.to(UserService.class).with(subscription.getId()).post("/user/subscribe/delete");

        ASSERT.that(result.status()).isEqualTo(ERROR);
        ASSERT.that(result.content()).isEqualTo(list("A user can only delete own subscriptions"));
    }

    @Test
    public void shouldDeleteSubscription() throws Exception {
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        User user = Factory.Users.thomas();
        database.save(user.getSettings());
        database.save(user);

        BenefitClub matas = Factory.BenefitClubs.matas();
        database.save(matas);
        Subscription subscription = new Subscription(user.getId(), matas.getId(), "1234", "premium", "2016-01-12 18:00:00.000");
        database.save(subscription);

        Result result = client.to(UserService.class).with(subscription.getId()).post("/user/subscribe/delete");

        ASSERT.that(result.status()).isEqualTo(Status.SUBSCRIPTION_DELETED);

        Subscription loadedSubscription = database.load(Subscription.class).matching("benefitClubId").with(subscription.getBenefitClubId());
        ASSERT.that(loadedSubscription).isNull();

        User returnedUser = result.content(User.class);
        ASSERT.that(returnedUser.getSubscriptions()).hasSize(0);

        User storedUser = database.load(User.class).matching("_id").with(returnedUser.getId());
        ASSERT.that(storedUser).isEqualTo(returnedUser);
    }

    @Test
    public void shouldSendMandrillWelcomeMail() throws Exception {
        Application.security(credential -> true);
        String credential = converter.toJson(Factory.Credentials.thomas());
        client.setAuthorisationHeader(credential);

        Application.userFactory(someCredential -> Factory.Users.joseph());
        Application.timestamper("2016-01-12 20:00:00.000"::toString);

        UserSettings settings = Factory.Users.joseph().getSettings();
        database.save(new Configuration("1.0", settings.getOnlyOwnBenefits(), settings.getNumberOfLocations(), "some-search-url", "some-terms-and-conditions"));

        Result result = client.to(UserService.class).with("").post("/user/login");
        System.out.println(result.content());
        ASSERT.that(result.status()).isEqualTo(Status.NEW_USER_LOGGED_IN);
    }
}
